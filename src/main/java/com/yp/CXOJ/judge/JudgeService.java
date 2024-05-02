package com.yp.CXOJ.judge;

import com.yp.CXOJ.model.vo.QuestionSubmitVO;

/**
 * 判题服务
 */
public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId 题目提交ID
     * @return
     */
    QuestionSubmitVO doJudge(long questionSubmitId);
}
