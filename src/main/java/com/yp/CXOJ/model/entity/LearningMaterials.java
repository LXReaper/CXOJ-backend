package com.yp.CXOJ.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学习中心表
 * @TableName learning_materials
 */
@TableName(value ="learning_materials")
@Data
public class LearningMaterials implements Serializable {
    /**
     * 资料ID
     */
    @TableId(type = IdType.AUTO)
    private Long material_id;

    /**
     * 视频
     */
    private String video_url;

    /**
     * 视频点赞数
     */
    private Integer thumb_Num;

    /**
     * 视频收藏数
     */
    private Integer favour_Num;

    /**
     * 视频转发数
     */
    private Integer share_Num;

    /**
     * 视频观看数
     */
    private Long watch_Num;

    /**
     * 文字内容
     */
    private String text_content;

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