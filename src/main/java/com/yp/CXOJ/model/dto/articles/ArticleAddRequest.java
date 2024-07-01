package com.yp.CXOJ.model.dto.articles;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 22:24:03
 */
@Data
public class ArticleAddRequest implements Serializable {
    /**
     * 文章标题
     */
    private String article_title;

    /**
     * 文章内容
     */
    private String article_content;

//    /**
//     * 用户ID
//     */
//    private Long user_id;

    private static final long serialVersionUID = 1L;
}
