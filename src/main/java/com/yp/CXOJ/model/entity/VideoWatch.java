package com.yp.CXOJ.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 视频观看表
 * @TableName video_watch
 */
@TableName(value ="video_watch")
@Data
public class VideoWatch implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long watch_id;

    /**
     * 视频id
     */
    private Long material_id;

    /**
     * 观看用户ID
     */
    private Long user_id;

    /**
     * 开始观看日期
     */
    private Date watch_date;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer is_deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}