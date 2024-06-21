package com.yp.CXOJ.model.dto.articles;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 22:25:04
 */
@Data
public class ArticleUpdateRequest implements Serializable {
    /**
     * 文章id
     */
    private Long article_id;

    /**
     * 文章标题
     */
    private String article_title;

    /**
     * 文章内容
     */
    private String article_content;

    private static final long serialVersionUID = 1L;
}
