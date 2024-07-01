package com.yp.CXOJ.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章表
 * @TableName articles
 */
@TableName(value ="articles")
@Data
public class Articles implements Serializable {
    /**
     * 文章ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long article_id;

    /**
     * 文章标题
     */
    private String article_title;

    /**
     * 文章内容
     */
    private String article_content;

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 观看数
     */
    private Integer watch_count;

    /**
     * 点赞数
     */
    private Integer thumbs_count;

    /**
     * 收藏数
     */
    private Integer collect_count;

    /**
     * 转发数
     */
    private Integer forward_count;

    /**
     * 回复数量
     */
    private Integer reply_count;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 是否删除
     */
    private Integer is_deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}