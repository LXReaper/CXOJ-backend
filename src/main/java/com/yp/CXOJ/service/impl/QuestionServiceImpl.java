package com.yp.CXOJ.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.constant.CommonConstant;
import com.yp.CXOJ.exception.BusinessException;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.dto.question.JudgeCase;
import com.yp.CXOJ.model.dto.question.JudgeConfig;
import com.yp.CXOJ.model.dto.question.QuestionQueryRequest;
import com.yp.CXOJ.model.entity.*;
import com.yp.CXOJ.model.vo.QuestionVO;
import com.yp.CXOJ.model.vo.UserVO;
import com.yp.CXOJ.service.QuestionService;
import com.yp.CXOJ.mapper.QuestionMapper;
import com.yp.CXOJ.service.UserService;
import com.yp.CXOJ.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 余炎培
 * @description 针对表【question(题目)】的数据库操作Service实现
 * @createDate 2024-04-07 23:56:11
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {

    /**
     * 定义全局的上传的题目路径
     */
    private static final String GLOBAL_QUESTION_DIR = "uploadQuestions";


    @Resource
    private UserService userService;

    /*
     *
     * 校验题目是否合法
     * */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 使用爬虫获取的md数据添加题目
     *
     * @return
     */
    @Override
    public Boolean addQuestionsFromMd(HttpServletRequest request) {
        File dir = new File("src/main/resources/题库");
        File[] files = dir.listFiles();
        ThrowUtils.throwIf(files == null, ErrorCode.NOT_FOUND_ERROR, "题库文件夹内无数据");
        for (File file : files) {
            addQuestionFromFile(file,request);
        }
        return true;
    }

    @Override
    public Boolean addQuestionsFromFiles(MultipartFile[] files,HttpServletRequest request) {
        ThrowUtils.throwIf(files == null, ErrorCode.PARAMS_ERROR, "文件数据为空");

        /*1、生成统一存放前端上传的题目文件的文件夹*/
        String userDir = System.getProperty("user.dir");//获取当前项目的根目录
        String globalQuestionPathName = userDir + File.separator + "src"
                + File.separator + "main" + File.separator
                + File.separator + "resources"
                + File.separator + GLOBAL_QUESTION_DIR;//这里使用File.separator是为了确保在不同的系统中可以使用符合系统的路径分隔符
        //判断全局存放上传题目的文件夹是否存在，如不存在则新建一个新文件夹
        if (!FileUtil.exist(globalQuestionPathName)) {
            FileUtil.mkdir(globalQuestionPathName);
        }

        /*2、拿到不同用户的文件夹目录,UUID.randomUUID()来隔离文件*/
        String userQuestionsPath = globalQuestionPathName + File.separator + UUID.randomUUID();//某个用户上传题目目录
        //判断存放当前用户上传题目的文件夹是否存在，如不存在则新建一个新文件夹
        if (!FileUtil.exist(userQuestionsPath)) {
            FileUtil.mkdir(userQuestionsPath);
        }

        //3、开始读取，并做持久化操作
        for (MultipartFile multipartFile : files) {
            //当前实际的某个题目文件
            String realQuestionPath = userQuestionsPath + File.separator + Objects.requireNonNull(multipartFile.getOriginalFilename());
            File file = new File(realQuestionPath);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            } catch (IOException e) {
                System.out.println(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                ThrowUtils.throwIf(true, ErrorCode.SYSTEM_ERROR, e.getMessage());
            }
            addQuestionFromFile(file,request);
        }
        if (FileUtil.exist(userQuestionsPath))FileUtil.del(userQuestionsPath);//删除该用户文件夹
        return true;
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询 根据前端传来的请求对象， 得到Mybatis框架支持的查询类）
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }

        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();//待排序的字段

        // 拼接查询条件,like和eq方法都是判断字段是否相同（即where语句）,然后该字段的查询条件为空则跳过,不判断
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    //读取题目文件内的数据添加到数据库中
    private void addQuestionFromFile(File file, HttpServletRequest request) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            //读取当前行
            String Line;

            boolean flag = false;//判断当前读取的是题目标题还是题目内容（false是题目标题）

            //标签tags
            List<String> tags = Arrays.asList("洛谷");
            //判题用例
            List<JudgeCase> judgeCaseList = new ArrayList<>();

            //判题配置judgeConfig
            JudgeConfig judgeConfig = JudgeConfig.builder()
                    .timeLimit(1000L)
                    .memoryLimit(64L)
                    .stackLimit(64L)
                    .build();

            //标题title
            String title = "";
            //内容content
            String content = "";
            StringBuilder sb = new StringBuilder();
            while ((Line = bufferedReader.readLine()) != null) {//读取题目标题后读取题目内容
                if (StringUtils.isAnyBlank(Line) && !flag) continue;
                if (!flag) {
                    if (Line.contains("[") && Line.contains("]")) {
                        tags = Arrays.asList(Line
                                .substring(Line.indexOf("[") + 1, Line.indexOf("]"))
                                .split(" "));
                    }
                    //题目标题
                    flag = true;
                    title = Line;
                } else {
                    //题目内容
                    sb.append(Line).append('\n');
                }
            }
            content = sb.toString();
//            //处理输入输出样例
//            String inOutCase = content.substring(content.lastIndexOf("输入输出样例") + 6);
//            List<String> inputList = Arrays.stream(inOutCase.split("输入样例 #")).map(s -> s.substring(0, s.indexOf("输出样例 #"))).collect(Collectors.toList());
//            List<String> outputList = Arrays.stream(inOutCase.split("输出样例 #")).map(s -> s.substring(s.indexOf("输入样例 #") + 6)).collect(Collectors.toList());
//            System.out.println("长度"+inOutCase
//                    .split("(输入样例 #|输出样例 #)").length);
            bufferedReader.close();
            file.delete();//删除该文件

            //当前登录的用户
            User loginUser = userService.getLoginUser(request);

            Question question = Question.builder()
                    .title(title)
                    .content(content)
                    .tags(JSONUtil.toJsonStr(tags))
                    .answer("无")
                    .judgeConfig(JSONUtil.toJsonStr(judgeConfig))
                    .userId(loginUser.getId())
                    .build();

            //持久层
//            System.out.println(question.toString());
            boolean result = this.save(question);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR,"题目添加失败");
        } catch (IOException e) {
            file.delete();
            ThrowUtils.throwIf(true, ErrorCode.PARAMS_ERROR, e.getMessage());
        }
    }
}




