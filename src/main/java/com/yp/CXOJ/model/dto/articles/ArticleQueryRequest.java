package com.yp.CXOJ.model.dto.articles;

import com.yp.CXOJ.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 22:43:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryRequest extends PageRequest implements Serializable {

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
     * 文章ID
     */
    private Long article_id;

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

    private static final long serialVersionUID = 1L;
}
