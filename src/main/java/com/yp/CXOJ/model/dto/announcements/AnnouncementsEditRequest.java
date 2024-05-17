package com.yp.CXOJ.model.dto.announcements;

import com.yp.CXOJ.model.dto.question.JudgeCase;
import com.yp.CXOJ.model.dto.question.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑请求
 *
 */
@Data
public class AnnouncementsEditRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}