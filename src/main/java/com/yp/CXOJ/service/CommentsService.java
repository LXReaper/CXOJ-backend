package com.yp.CXOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.CXOJ.model.dto.articles.ArticleQueryRequest;
import com.yp.CXOJ.model.dto.comments.CommentAddRequest;
import com.yp.CXOJ.model.dto.comments.CommentQueryRequest;
import com.yp.CXOJ.model.dto.comments.CommentUpdateRequest;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.Comments;

/**
* @author 余炎培
* @description 针对表【comments(评论表)】的数据库操作Service
* @createDate 2024-06-09 20:35:11
*/
public interface CommentsService extends IService<Comments> {
    /**
     * 添加评论
     * @param commentAddRequest
     * @return
     */
    Long addComment(CommentAddRequest commentAddRequest);

    /**
     * 删除评论
     * @param id
     * @return
     */
    Boolean deleteComment(Long id);

    /**
     * 更新评论
     * @param commentUpdateRequest
     * @return
     */
    Boolean updateComment(CommentUpdateRequest commentUpdateRequest);

    /**
     * 获取分页查询条件
     */
    QueryWrapper<Comments> getQueryWrapper(CommentQueryRequest commentQueryRequest);
}
