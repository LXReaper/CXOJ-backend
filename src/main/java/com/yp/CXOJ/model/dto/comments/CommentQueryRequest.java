package com.yp.CXOJ.model.dto.comments;

import com.yp.CXOJ.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 20:29:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {
    /**
     * 评论ID
     */
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

    private static final long serialVersionUID = 1L;
}
