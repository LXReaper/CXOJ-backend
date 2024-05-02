package com.yp.CXOJ.judge.codesandbox.impl;

import com.yp.CXOJ.judge.codesandbox.CodeSandBox;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeRequest;
import com.yp.CXOJ.judge.codesandbox.model.ExecuteCodeResponse;
import com.yp.CXOJ.model.dto.questionsubmit.JudgeInfo;
import com.yp.CXOJ.model.enums.QuestionSubmitJudgeInfoEnum;
import com.yp.CXOJ.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（跑通业务流程）
 */
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(QuestionSubmitJudgeInfoEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        
        return executeCodeResponse;
    }
}
