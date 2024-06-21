package com.yp.CXOJ.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论表
 * @TableName comments
 */
@TableName(value ="comments")
@Data
public class Comments implements Serializable {
    /**
     * 评论ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long comment_id;

    /**
     * 评论内容
     */
    private String comment_content;

    /**
     * 评论者ID
     */
    private Long commenter_id;

    /**
     * 被评论者ID
     */
    private Long commented_user_id;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 父评论ID
     */
    private Long parent_comment_id;

    /**
     * 文章ID
     */
    private Long article_id;

    /**
     * 点赞数
     */
    private Integer thumbs_count;

    /**
     * 转发数
     */
    private Integer forward_count;

    /**
     * 回复数量
     */
    private Integer reply_count;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer is_deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}