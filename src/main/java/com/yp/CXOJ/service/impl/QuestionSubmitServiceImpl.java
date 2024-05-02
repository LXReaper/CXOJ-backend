package com.yp.CXOJ.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.constant.CommonConstant;
import com.yp.CXOJ.exception.BusinessException;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.enums.QuestionSubmitLanguageEnum;
import com.yp.CXOJ.model.enums.QuestionSubmitStatusEnum;
import com.yp.CXOJ.model.vo.QuestionSubmitVO;
import com.yp.CXOJ.model.vo.UserVO;
import com.yp.CXOJ.service.QuestionService;
import com.yp.CXOJ.service.QuestionSubmitService;
import com.yp.CXOJ.mapper.QuestionSubmitMapper;
import com.yp.CXOJ.service.UserService;
import com.yp.CXOJ.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 余炎培
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-04-07 23:57:14
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;
    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 题目创建信息
     * @param loginUser 当前登录用户
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) throw new BusinessException(ErrorCode.PARAMS_ERROR , "编程语言错误");

        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置初始状态，初始状态为待判题
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        //判断是否成功保存到数据库,save为true则保存成功
        boolean save = this.save(questionSubmit);
        if (!save) throw new BusinessException(ErrorCode.SYSTEM_ERROR , "题目提交失败");

        // todo 执行判题服务
        return questionSubmit.getId();
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询 根据前端传来的请求对象， 得到Mybatis框架支持的查询类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);//可加可不加
        queryWrapper.eq("isDelete" , false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取单个信息的包装类
     * @param questionSubmit 未脱敏的信息
     * */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        //脱敏,仅仅本人和管理员能看自己提交的代码答案
        long userId = loginUser.getId();
        //处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)){
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }
    /**
     * 获取分页信息的包装类
     * */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }

        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            return getQuestionSubmitVO(questionSubmit, loginUser);
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




