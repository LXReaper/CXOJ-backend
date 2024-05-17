package com.yp.CXOJ.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.yp.CXOJ.model.dto.question.JudgeCase;
import com.yp.CXOJ.model.dto.question.JudgeConfig;
import com.yp.CXOJ.judge.codesandbox.model.JudgeInfo;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.enums.QuestionSubmitJudgeInfoEnum;

import java.util.List;

/**
 * Java语言的判题策略
 */
public class JavaLanguageStrategyImpl implements JudgeStrategy {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        //实际执行的时间和空间大小
        Long time = judgeInfo.getTime();
        Long memory = judgeInfo.getMemory();

        List<String> inputList = judgeContext.getInputList();//题目的输入用例
        List<String> outputList = judgeContext.getOutputList();//代码沙箱的输出结果
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();//代码沙箱的执行时间和空间
        Question question = judgeContext.getQuestion();

        //最终返回的判题信息
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);

        /**
         * 根据沙箱的执行结果，设置题目的判题状态和信息
         * 首先初始化判题信息状态为等待中
         * 接着以此比对代码沙箱的输出结果和实际正确代码
         */
        if (outputList.size() != inputList.size()){//代码沙箱返回的输出结果数量跟原先输入用例的数量不一致，即有些输入用例没有结果
            judgeInfoResponse.setMessage(QuestionSubmitJudgeInfoEnum.WRONG_ANSWER.getValue());
            return judgeInfoResponse;
        }
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))){//判断预期输出的答案和实际输出的答案是否相等
                judgeInfoResponse.setMessage(QuestionSubmitJudgeInfoEnum.WRONG_ANSWER.getValue());
                return judgeInfoResponse;
            }
        }

        /**
         * 判断题目限制
         */
        //获取题目限制信息,限制最大的时间和空间大小
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if (memory > memoryLimit){
            judgeInfoResponse.setMessage(QuestionSubmitJudgeInfoEnum.MEMORY_LIMIT_EXCEEDED.getValue());
            return judgeInfoResponse;
        }
        if (time > timeLimit){
            judgeInfoResponse.setMessage(QuestionSubmitJudgeInfoEnum.TIME_LIMIT_EXCEEDED.getValue());
            return judgeInfoResponse;
        }

        //题目AC了
        judgeInfoResponse.setMessage(QuestionSubmitJudgeInfoEnum.ACCEPTED.getValue());

        return judgeInfoResponse;
    }
}
