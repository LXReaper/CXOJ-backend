package com.yp.CXOJ.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.CXOJ.annotation.AuthCheck;
import com.yp.CXOJ.common.BaseResponse;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.common.ResultUtils;
import com.yp.CXOJ.constant.UserConstant;
import com.yp.CXOJ.exception.BusinessException;
import com.yp.CXOJ.model.dto.question.QuestionQueryRequest;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.vo.QuestionSubmitVO;
import com.yp.CXOJ.service.QuestionSubmitService;
import com.yp.CXOJ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交代码
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询到原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));

        User loginUser = userService.getLoginUser(request);

        // 返回脱敏信息,如题目提交代码
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

    /**
     * 获取某个用户的提交信息(所有用户可见)
     *
     * @param id
     * @return
     */
    @PostMapping("/list/QuestionSubmitVO")
    public BaseResponse<List<QuestionSubmitVO>> listQuestionSubmitVoByUserId(Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitService.getQuestionSubmitVOListByUserId(id, request);

        // 返回脱敏信息,如题目提交代码
        return ResultUtils.success(questionSubmitVOList);
    }

    /**
     * 获取某个用户的提交信息(本人和管理员可见)
     *
     * @param id
     * @return
     */
    @PostMapping("/list/QuestionSubmit")
    public BaseResponse<List<QuestionSubmit>> listQuestionSubmitByUserId(Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        //只有本人和管理员可见
        if (!loginUser.getId().equals(id) && !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<QuestionSubmit> questionSubmitList = questionSubmitService.getQuestionSubmitListByUserId(id, request);

        // 返回脱敏信息,如题目提交代码
        return ResultUtils.success(questionSubmitList);
    }
}
