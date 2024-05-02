package com.yp.CXOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.CXOJ.model.dto.question.QuestionQueryRequest;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.vo.QuestionSubmitVO;
import com.yp.CXOJ.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 余炎培
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-04-07 23:57:15
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目创建信息
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser 登录用户
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser 登录用户
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
