package com.yp.CXOJ.judge.strategy;

import com.yp.CXOJ.model.dto.question.JudgeCase;
import com.yp.CXOJ.judge.codesandbox.model.JudgeInfo;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;
    private QuestionSubmit questionSubmit;
}
