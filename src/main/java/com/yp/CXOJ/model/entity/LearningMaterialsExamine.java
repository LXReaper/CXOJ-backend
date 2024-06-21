package com.yp.CXOJ.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学习内容上传审核表
 * @TableName learning_materials_examine
 */
@TableName(value ="learning_materials_examine")
@Data
public class LearningMaterialsExamine implements Serializable {
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
     * 文字内容
     */
    private String text_content;

    /**
     * 上传用户ID
     */
    private Long user_id;

    /**
     * 上传日期
     */
    private Date upload_date;

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