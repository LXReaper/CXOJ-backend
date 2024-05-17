package com.yp.CXOJ.model.dto.announcements;

import com.yp.CXOJ.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AnnouncementsQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long announcement_id;

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
     * 发布用户ID
     */
    private Long user_id;

    /**
     * 发布日期
     */
    private Date publish_date;

    /**
     * 更新用户ID
     */
    private Long updated_user_id;

    /**
     * 更新日期
     */
    private Date update_date;


    private static final long serialVersionUID = 1L;
}