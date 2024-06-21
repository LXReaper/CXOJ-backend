package com.yp.CXOJ.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.yp.CXOJ.service.QiNiuService;
import com.yp.CXOJ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private QiNiuService qiNiuService;

    //我的七牛云空间文件的访问路径
    @Value("${oss.url}")
    private String ossUrl;

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
    public BaseResponse<Long> addAnnouncement(@RequestParam("file") MultipartFile multipartFile,
                                              AnnouncementsAddRequest announcementsAddRequest,
                                              HttpServletRequest request) {
        //判断请求是否为空
        ThrowUtils.throwIf(announcementsAddRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(announcementsService.addAnnouncements(announcementsAddRequest, multipartFile, request));
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

        // 返回脱敏信息,如题目提交代码announcements
        return ResultUtils.success(announcementsPage);
    }

    /**
     * 编辑公告信息
     *
     * @param multipartFile
     * @param announcementsEditRequest
     * @return
     */
    @PostMapping("/edit")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> editAnnouncements(@RequestParam("file") MultipartFile multipartFile,
                                                   AnnouncementsEditRequest announcementsEditRequest, HttpServletRequest request) {
        return ResultUtils.success(announcementsService.editAnnouncements(announcementsEditRequest, multipartFile, request));
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
        //查询该公告
        QueryWrapper<Announcements> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("announcement_id", announcement_id);
        Announcements announcements = announcementsService.getOne(queryWrapper);
        if (StringUtils.isNotBlank(announcements.getImage_url())) {//有公告图片才删除
            //新图片路径获取成功后才删除原来的图片文件(删除前先拿到存放在七牛云服务器中的文件名,通过文件路径拿到)
            qiNiuService.deleteFileOnQiNiu(announcements.getImage_url().substring(ossUrl.length()));
        }
        boolean b = announcementsService.removeById(announcement_id);
        return ResultUtils.success(b);
    }
}
