package com.yp.CXOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsAddRequest;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsEditRequest;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsQueryRequest;
import com.yp.CXOJ.model.entity.Announcements;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 余炎培
 * @description 针对表【announcements(公告表)】的数据库操作Service
 * @createDate 2024-05-15 22:58:17
 */
public interface AnnouncementsService extends IService<Announcements> {


    /**
     * 添加公告
     *
     * @param announcementsAddRequest
     * @param multipartFile
     * @param request
     * @return
     */
    Long addAnnouncements(AnnouncementsAddRequest announcementsAddRequest, MultipartFile multipartFile, HttpServletRequest request);

    /**
     * 编辑公告信息
     *
     * @param multipartFile
     * @param announcementsEditRequest
     * @return
     */
    Boolean editAnnouncements(AnnouncementsEditRequest announcementsEditRequest, MultipartFile multipartFile, HttpServletRequest request);

    /**
     * 获取查询包装类（用户根据哪些字段查询 根据前端传来的请求对象， 得到Mybatis框架支持的查询类）
     *
     * @param announcementsQueryRequest
     * @return
     */
    QueryWrapper<Announcements> getQueryWrapper(AnnouncementsQueryRequest announcementsQueryRequest);
}
