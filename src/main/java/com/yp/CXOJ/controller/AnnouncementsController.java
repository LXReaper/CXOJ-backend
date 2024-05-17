package com.yp.CXOJ.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.CXOJ.annotation.AuthCheck;
import com.yp.CXOJ.common.BaseResponse;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.common.ResultUtils;
import com.yp.CXOJ.constant.UserConstant;
import com.yp.CXOJ.exception.BusinessException;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsAddRequest;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsEditRequest;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsQueryRequest;
import com.yp.CXOJ.model.dto.question.QuestionEditRequest;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yp.CXOJ.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yp.CXOJ.model.entity.Announcements;
import com.yp.CXOJ.model.entity.QuestionSubmit;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.enums.AnnouncementTypeStatusEnum;
import com.yp.CXOJ.model.vo.QuestionSubmitVO;
import com.yp.CXOJ.service.AnnouncementsService;
import com.yp.CXOJ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 公告接口
 */
@RestController
@RequestMapping("/Announcements")
@Slf4j
public class AnnouncementsController {

    @Resource
    private AnnouncementsService announcementsService;

    @Resource
    private UserService userService;

    /**
     * 添加公告(管理员可以使用)
     *
     * @param announcementsAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addAnnouncement(@RequestBody AnnouncementsAddRequest announcementsAddRequest,
                                              HttpServletRequest request) {
        //判断请求是否为空，以及请求的用户id是否为空或者小于0
        ThrowUtils.throwIf(announcementsAddRequest == null ||
                            announcementsAddRequest.getUserId() == null ||
                            announcementsAddRequest.getUserId() <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(announcementsService.addAnnouncements(announcementsAddRequest));
    }

    /**
     * 分页获取公告信息
     *
     * @param announcementsQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Announcements>> listAnnouncementsByPage(@RequestBody AnnouncementsQueryRequest announcementsQueryRequest,
                                                                      HttpServletRequest request) {
        long current = announcementsQueryRequest.getCurrent();
        long size = announcementsQueryRequest.getPageSize();
        // 从数据库中查询到原始的题目提交分页信息
        Page<Announcements> announcementsPage = announcementsService.page(new Page<>(current, size),
                announcementsService.getQueryWrapper(announcementsQueryRequest));

        // 返回脱敏信息,如题目提交代码
        return ResultUtils.success(announcementsPage);
    }

    /**
     * 编辑公告信息
     *
     * @param announcementsEditRequest
     * @return
     */
    @PostMapping("/edit")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> editAnnouncements(@RequestBody AnnouncementsEditRequest announcementsEditRequest, HttpServletRequest request) {
        return ResultUtils.success(announcementsService.editAnnouncements(announcementsEditRequest,request));
    }

    /**
     * 删除公告信息
     *
     * @param announcement_id
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteAnnouncements(Long announcement_id, HttpServletRequest request) {
        ThrowUtils.throwIf(announcement_id == null || announcement_id <= 0, ErrorCode.PARAMS_ERROR);
        boolean b = announcementsService.removeById(announcement_id);
        return ResultUtils.success(b);
    }
}
