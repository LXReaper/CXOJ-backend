package com.yp.CXOJ.model.dto.comments;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 19:17:42
 */
@Data
public class CommentAddRequest implements Serializable {
    /**
     * 评论内容
     */
    private String comment_content;

//    /**
//     * 评论者ID
//     */
//    private Long commenter_id;

    /**
     * 被评论者ID（比如@某人）
     */
    private Long commented_user_id;

    /**
     * 父评论ID
     */
    private Long parent_comment_id;

    /**
     * 文章ID
     */
    private Long article_id;

    private static final long serialVersionUID = 1L;
}
