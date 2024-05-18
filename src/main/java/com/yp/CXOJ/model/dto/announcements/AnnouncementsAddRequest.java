package com.yp.CXOJ.model.dto.announcements;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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

    private static final long serialVersionUID = 1L;
}
