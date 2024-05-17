package com.yp.CXOJ.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * 公告表
 * @TableName announcements
 */
@TableName(value ="announcements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Announcements implements Serializable {
    /**
     * 公告ID
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 是否删除
     */
    @TableLogic
    private Integer is_deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}