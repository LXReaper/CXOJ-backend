package com.yp.CXOJ.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.Comments;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  articlesVO
 */
@Data
public class ArticlesVO implements Serializable {
    /**
     * 文章ID
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

    /**
     * 用户ID
     */
    private Long user_id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

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

    /**
     * 回复内容
     */
    private List<CommentsVO> commentsList;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    private static final long serialVersionUID = 1L;

    public static ArticlesVO objToVO(Articles articles) {
        ArticlesVO articlesVO = new ArticlesVO();
        BeanUtils.copyProperties(articles, articlesVO);
        return articlesVO;
    }
}