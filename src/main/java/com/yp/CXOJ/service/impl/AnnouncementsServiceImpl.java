package com.yp.CXOJ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.constant.CommonConstant;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsAddRequest;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsEditRequest;
import com.yp.CXOJ.model.dto.announcements.AnnouncementsQueryRequest;
import com.yp.CXOJ.model.entity.Announcements;
import com.yp.CXOJ.model.entity.Question;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.enums.AnnouncementTypeStatusEnum;
import com.yp.CXOJ.service.AnnouncementsService;
import com.yp.CXOJ.mapper.AnnouncementsMapper;
import com.yp.CXOJ.service.UserService;
import com.yp.CXOJ.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 余炎培
 * @description 针对表【announcements(公告表)】的数据库操作Service实现
 * @createDate 2024-05-15 22:58:17
 */
@Service
public class AnnouncementsServiceImpl extends ServiceImpl<AnnouncementsMapper, Announcements>
        implements AnnouncementsService {

    @Resource
    private UserService userService;

    /**
     * 添加公告
     *
     * @param announcementsAddRequest
     * @return
     */
    @Override
    public Long addAnnouncements(AnnouncementsAddRequest announcementsAddRequest) {
        String announcement_type = announcementsAddRequest.getAnnouncement_type();
        String title = announcementsAddRequest.getTitle();
        String content = announcementsAddRequest.getContent();
        String image_url = announcementsAddRequest.getImage_url();
        Long userId = announcementsAddRequest.getUserId();

        ThrowUtils.throwIf(StringUtils.isAnyBlank(announcement_type), ErrorCode.PARAMS_ERROR, "公告类型不能为空");
        ThrowUtils.throwIf(AnnouncementTypeStatusEnum.getEnumByValue(announcement_type) == null, ErrorCode.PARAMS_ERROR, "公告类型不正确");
        ThrowUtils.throwIf(StringUtils.isAnyBlank(title), ErrorCode.PARAMS_ERROR, "公告标题不能为空");
        ThrowUtils.throwIf(StringUtils.isAnyBlank(content), ErrorCode.PARAMS_ERROR, "公告内容不能为空");


        Date curDate = new Date(System.currentTimeMillis());
        Announcements announcements = Announcements.builder()
                .announcement_type(announcement_type)
                .title(title)
                .content(content)
                .image_url(image_url)
                .user_id(userId)
                .publish_date(curDate)
                .updated_user_id(userId)
                .update_date(curDate)
                .build();
        boolean save = this.save(announcements);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "公告发布失败");

        long newAnnouncementId = announcements.getAnnouncement_id();
        return newAnnouncementId;
    }

    @Override
    public Boolean editAnnouncements(AnnouncementsEditRequest announcementsEditRequest,HttpServletRequest request) {
        //如果不存在请求编辑的公告
        ThrowUtils.throwIf(announcementsEditRequest == null ||
                announcementsEditRequest.getAnnouncement_id() <= 0, ErrorCode.PARAMS_ERROR);

        //拿到需要编辑的数据
        Long announcement_id = announcementsEditRequest.getAnnouncement_id();
        String announcement_type = announcementsEditRequest.getAnnouncement_type();
        String title = announcementsEditRequest.getTitle();
        String content = announcementsEditRequest.getContent();
        String image_url = announcementsEditRequest.getImage_url();
        //拿到数据库中需要编辑的公告数据
        Announcements announcements = this.getById(announcement_id);

        //设置公告类型
        if (announcement_type != null && !announcement_type.equals("")
                && AnnouncementTypeStatusEnum.getEnumByValue(announcement_type) != null) {
            announcements.setAnnouncement_type(announcement_type);
        }
        //设置公告标题
        if (title != null && !title.equals("")) {
            announcements.setTitle(title);
        }
        //设置公告内容
        if (content != null && !content.equals("")) {
            announcements.setContent(content);
        }
        //设置公告图片路径
        if (image_url != null && !image_url.equals("")) {
            announcements.setImage_url(image_url);
        }

        //拿到登录用户信息
        User loginUser = userService.getLoginUser(request);
        announcements.setUpdated_user_id(loginUser.getId());
        announcements.setUpdate_date(new Date(System.currentTimeMillis()));

        boolean save = this.updateById(announcements);
        return save;
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询 根据前端传来的请求对象， 得到Mybatis框架支持的查询类）
     *
     * @param announcementsQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Announcements> getQueryWrapper(AnnouncementsQueryRequest announcementsQueryRequest) {
        QueryWrapper<Announcements> queryWrapper = new QueryWrapper<>();
        if (announcementsQueryRequest == null) {//查询请求为空则直接返回查询条件
            return queryWrapper;
        }
        Long announcement_id = announcementsQueryRequest.getAnnouncement_id();
        String announcement_type = announcementsQueryRequest.getAnnouncement_type();
        String title = announcementsQueryRequest.getTitle();
        String content = announcementsQueryRequest.getContent();
        String image_url = announcementsQueryRequest.getImage_url();
        Long user_id = announcementsQueryRequest.getUser_id();
        Date publish_date = announcementsQueryRequest.getPublish_date();
        Long updated_user_id = announcementsQueryRequest.getUpdated_user_id();
        Date update_date = announcementsQueryRequest.getUpdate_date();
        String sortField = announcementsQueryRequest.getSortField();
        String sortOrder = announcementsQueryRequest.getSortOrder();

        //先查询id的数据
        queryWrapper.eq(ObjectUtils.isNotEmpty(announcement_id), "announcement_id", announcement_id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(user_id), "user_id", user_id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(updated_user_id), "updated_user_id", updated_user_id);

        //查询的内容
        queryWrapper.like(StringUtils.isNotBlank(announcement_type), "announcement_type", announcement_type);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.eq(StringUtils.isNotBlank(image_url), "image_url", image_url);
        queryWrapper.eq(publish_date != null && StringUtils.isNotBlank(publish_date.toString()), "publish_date", publish_date);
        queryWrapper.eq(update_date != null && StringUtils.isNotBlank(update_date.toString()), "update_date", update_date);

        //排序
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return queryWrapper;
    }
}




