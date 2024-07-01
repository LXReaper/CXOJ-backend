package com.yp.CXOJ.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.Comments;
import com.yp.CXOJ.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论VO
 */
@Data
public class CommentsVO implements Serializable {
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
     * 评论者昵称
     */
    private String commenterName;

    /**
     * 评论者头像
     */
    private String commenterAvatar;

    /**
     * 被评论者ID
     */
    private Long commented_user_id;

    /**
     * 被评论者昵称
     */
    private String commented_userName;

    /**
     * 被评论者头像
     */
    private String commented_userAvatar;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

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

    public static CommentsVO objToVO(Comments comments) {
        CommentsVO commentsVO = new CommentsVO();
        BeanUtils.copyProperties(comments, commentsVO);
        return commentsVO;
    }
}