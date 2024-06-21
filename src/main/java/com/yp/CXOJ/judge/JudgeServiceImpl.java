package com.yp.CXOJ.judge;

import cn.hutool.json.JSONUtil;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.exception.BusinessException;
import com.yp.CXOJ.judge.codesandbox.CodeSandBox;
import com.yp.CXOJ.judge.codesandbox.CodeSandBoxFactory;
import com.yp.CXOJ.judge.codesandbox.CodeSandBoxProxy;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeResponse;
import com.yp.CXOJ.judge.strategy.JudgeContext;
import com.yp.CXOJ.model.dto.question.JudgeCase;
import com.yp.CXOJ.judge.codesandbox.model.JudgeInfo;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import com.yp.CXOJ.model.enums.QuestionSubmitStatusEnum;
import com.yp.CXOJ.service.QuestionService;
import com.yp.CXOJ.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Value("${codeSandBox.type:example}")
    private String type;

    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeManger judgeManger;

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //传入题目提交ID，获取对应的题目、提交信息（包含代码、编程语言）
        //调用沙箱,获取执行结果
        //根据沙箱的执行结果，设置题目的判题状态和信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR , "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question== null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR , "题目不存在");
        }
        /**
         * 这段代码相当于加了锁
         */
        //如果不为等待状态
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR , "题目正在判题中");
        }
        //更新当前题目提交的判题状态为“判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.JUDGING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update){//更新失败
            throw new BusinessException(ErrorCode.SYSTEM_ERROR , "题目状态更新错误");
        }

        /**
         * 调用代码沙箱,获取执行结果
         */
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);//代码沙箱执行返回的内容

        //获取代码沙箱执行代码后返回的结果
        List<String> outputList = executeCodeResponse.getOutputList();

        // todo .....这里要补充一个将代码沙箱的执行信息写入最终的返回值中，其实也可以不用，就是一些编译，还有代码执行的信息，
        //  的信息，可以直接就是在代码沙箱中将信息写入JudgeInfo中

        /**
         * 根据沙箱的执行结果，设置题目的判题状态和信息
         * 首先初始化判题信息状态为等待中
         * 接着以此比对代码沙箱的输出结果和实际正确代码
         */
        //上下文的作用
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());//这里面有代码实际执行的时间，空间，编译信息等数据
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        //使用代码沙箱返回的执行结果，来实现判题，并返回判题信息（如判题成功AC）
        JudgeInfo judgeInfo = judgeManger.doJudge(judgeContext);


        //到此结束判题，接着修改数据库的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update){//更新失败
            throw new BusinessException(ErrorCode.SYSTEM_ERROR , "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);//获取最新的题目提交信息
        return questionSubmitResult;
    }
}
