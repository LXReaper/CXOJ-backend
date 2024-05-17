package com.yp.CXOJ.judge;

import com.yp.CXOJ.judge.strategy.DefaultStrategyImpl;
import com.yp.CXOJ.judge.strategy.JavaLanguageStrategyImpl;
import com.yp.CXOJ.judge.strategy.JudgeContext;
import com.yp.CXOJ.judge.strategy.JudgeStrategy;
import com.yp.CXOJ.judge.codesandbox.model.JudgeInfo;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @author 余炎培
 * @since 2024-05-03 星期五 11:29:02
 */
@Service
public class JudgeManger {
    /**
     * 执行判断不同编程语言，可以方便JudgeService中根据不同编程语言来判断时空限制
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultStrategyImpl();
        if (language.equals("java")) judgeStrategy = new JavaLanguageStrategyImpl();

        return judgeStrategy.doJudge(judgeContext);
    }
}
