package com.yp.CXOJ.judge;

import cn.hutool.json.JSONUtil;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.exception.BusinessException;
import com.yp.CXOJ.judge.codesandbox.CodeSandBox;
import com.yp.CXOJ.judge.codesandbox.CodeSandBoxFactory;
import com.yp.CXOJ.judge.codesandbox.CodeSandBoxProxy;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeResponse;
import com.yp.CXOJ.model.dto.question.JudgeCase;
import com.yp.CXOJ.model.dto.questionsubmit.JudgeInfo;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import com.yp.CXOJ.model.enums.QuestionSubmitJudgeInfoEnum;
import com.yp.CXOJ.model.enums.QuestionSubmitLanguageEnum;
import com.yp.CXOJ.model.enums.QuestionSubmitStatusEnum;
import com.yp.CXOJ.model.vo.QuestionSubmitVO;
import com.yp.CXOJ.service.QuestionService;
import com.yp.CXOJ.service.QuestionSubmitService;
import org.elasticsearch.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Value("${codeSandBox.type:example}")
    private String type;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Override
    public QuestionSubmitVO doJudge(long questionSubmitId) {
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
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);


        //获取代码沙箱执行代码后返回的结果
        List<String> outputList = executeCodeResponse.getOutputList();
        /**
         * 根据沙箱的执行结果，设置题目的判题状态和信息
         * 首先初始化判题信息状态为等待中
         */
        QuestionSubmitJudgeInfoEnum questionSubmitJudgeInfoEnum = QuestionSubmitJudgeInfoEnum.WAITING;
        if (outputList.size() != inputList.size()){//代码沙箱返回的输出结果数量跟原先输入用例的数量不一致，即有些输入用例没有结果
            questionSubmitJudgeInfoEnum = QuestionSubmitJudgeInfoEnum.WRONG_ANSWER;
        }
        Long id = questionSubmit.getId();
        String judgeInfo = questionSubmit.getJudgeInfo();
        Integer status = questionSubmit.getStatus();
        Long userId = questionSubmit.getUserId();
        Date createTime = questionSubmit.getCreateTime();
        Date updateTime = questionSubmit.getUpdateTime();
        Integer isDelete = questionSubmit.getIsDelete();


        return null;
    }
}
