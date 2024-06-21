package com.yp.CXOJ.model.dto.comments;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 20:00:11
 */
@Data
public class CommentUpdateRequest implements Serializable {
    /**
     * 评论ID
     */
    private Long comment_id;

    /**
     * 评论内容
     */
    private String comment_content;

    private static final long serialVersionUID = 1L;
}
