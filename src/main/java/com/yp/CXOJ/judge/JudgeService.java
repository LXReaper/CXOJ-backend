package com.yp.CXOJ.judge;

import com.yp.CXOJ.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId 题目提交ID
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
