package com.yp.CXOJ.model.dto.announcements;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-05-15 星期三 23:06:52
 * 公告发布请求
 */
@Data
public class AnnouncementsAddRequest implements Serializable {

    /**
     * 公告类型
     */
    private String announcement_type;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告文字
     */
    private String content;

    /**
     * 公告图片
     */
    private String image_url;

    /**
     * 发布的用户信息
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
