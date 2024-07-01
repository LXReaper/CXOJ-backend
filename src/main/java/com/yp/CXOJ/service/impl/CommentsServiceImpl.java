package com.yp.CXOJ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.constant.CommonConstant;
import com.yp.CXOJ.constant.UserConstant;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.dto.comments.CommentAddRequest;
import com.yp.CXOJ.model.dto.comments.CommentQueryRequest;
import com.yp.CXOJ.model.dto.comments.CommentUpdateRequest;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.Comments;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.service.ArticlesService;
import com.yp.CXOJ.service.CommentsService;
import com.yp.CXOJ.mapper.CommentsMapper;
import com.yp.CXOJ.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 余炎培
* @description 针对表【comments(评论表)】的数据库操作Service实现
* @createDate 2024-06-09 20:35:11
*/
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService{

    @Resource
    private ArticlesService articlesService;

    /**
     * 新增评论
     * @param commentAddRequest
     * @return
     */
    @Override
    public Long addComment(CommentAddRequest commentAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(commentAddRequest == null, ErrorCode.PARAMS_ERROR,"请求参数为空");
        ThrowUtils.throwIf(commentAddRequest.getArticle_id() == null
                || commentAddRequest.getArticle_id() <= 0 ,ErrorCode.PARAMS_ERROR,"文章id不为空");
        ThrowUtils.throwIf(commentAddRequest.getCommented_user_id() == null
                || commentAddRequest.getCommented_user_id() <= 0 ,ErrorCode.PARAMS_ERROR,"被评论者id不为空");

        User loginUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(loginUser == null,ErrorCode.OPERATION_ERROR,"未登录");

        //判断文章是否存在
        QueryWrapper<Articles> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(commentAddRequest.getArticle_id() != null,"article_id", commentAddRequest.getArticle_id());
        Articles articles = articlesService.getOne(queryWrapper1);
        ThrowUtils.throwIf(articles == null,ErrorCode.NOT_FOUND_ERROR,"评论的文章不存在");

        //更新文章的回复数
        articles.setReply_count(articles.getReply_count() + 1);
        articlesService.updateById(articles);

        //新增评论
        Comments comments = new Comments();
        BeanUtils.copyProperties(commentAddRequest, comments);
        comments.setCommenter_id(loginUser.getId());
        boolean save = this.save(comments);
        ThrowUtils.throwIf(!save,ErrorCode.OPERATION_ERROR,"评论新增失败");

        //更新父评论的回复数
        if (commentAddRequest.getParent_comment_id() != null &&
        !commentAddRequest.getParent_comment_id().toString().isEmpty()
                && commentAddRequest.getParent_comment_id() > 0) {
            QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("comment_id",
                    commentAddRequest.getParent_comment_id());
            Comments parentComment = this.getOne(queryWrapper);

            ThrowUtils.throwIf(parentComment == null,ErrorCode.PARAMS_ERROR,"父评论不存在");

            parentComment.setReply_count(parentComment.getReply_count() + 1);
            boolean b = this.updateById(parentComment);
            ThrowUtils.throwIf(!b,ErrorCode.OPERATION_ERROR,"父评论更新失败");
        }
        return comments.getComment_id();
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @Override
    public Boolean deleteComment(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0,ErrorCode.PARAMS_ERROR,"请求删除评论id非法");

        QueryWrapper<Comments> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_id", id);
        Comments comment = this.getOne(wrapper);
        //判断是否选择删除的评论是否存在
        ThrowUtils.throwIf(comment == null,ErrorCode.NOT_FOUND_ERROR,"选择删除的评论不存在");

        //判断文章是否存在
        QueryWrapper<Articles> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(comment.getArticle_id() != null,"article_id", comment.getArticle_id());
        Articles articles = articlesService.getOne(queryWrapper1);
        ThrowUtils.throwIf(articles == null,ErrorCode.NOT_FOUND_ERROR,"评论的文章不存在");

        //更新文章的回复数
        articles.setReply_count(articles.getReply_count() - 1);
        articlesService.updateById(articles);

        //父评论减少回复数
        if (comment.getParent_comment_id() != null && !comment.getParent_comment_id().toString().isEmpty()
                && comment.getParent_comment_id() > 0){
            wrapper = new QueryWrapper<>();
            wrapper.eq("parent_comment_id", comment.getParent_comment_id());
            comment = this.getOne(wrapper);

            ThrowUtils.throwIf(comment == null,ErrorCode.OPERATION_ERROR,"父评论不存在");

            comment.setReply_count(comment.getReply_count() - 1);
            boolean b = this.updateById(comment);
            ThrowUtils.throwIf(!b,ErrorCode.OPERATION_ERROR,"父评论更新失败");
        }

        //删除该评论
        boolean b = this.removeById(id);

        //删除子评论
        QueryWrapper<Comments> childCommentsWrapper = new QueryWrapper<>();
        childCommentsWrapper.eq("parent_comment_id", id);
        List<Comments> commentsList = this.getBaseMapper().selectList(childCommentsWrapper);
        for (Comments childComment : commentsList) {
            b = this.removeById(childComment.getComment_id());
        }
        return b;
    }

    /**
     * 更新评论
     * @param commentUpdateRequest
     * @return
     */
    @Override
    public Boolean updateComment(CommentUpdateRequest commentUpdateRequest) {
        ThrowUtils.throwIf(commentUpdateRequest == null || commentUpdateRequest.getComment_id() == null,
                ErrorCode.PARAMS_ERROR,"评论更新请求为空");

        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id",commentUpdateRequest.getComment_id());
        Comments comment = this.getOne(queryWrapper);

        //判断请求更新的评论是否存在
        ThrowUtils.throwIf(comment == null,ErrorCode.NOT_FOUND_ERROR,"不存在该评论");

        comment.setComment_content(commentUpdateRequest.getComment_content());
        boolean b = this.updateById(comment);
        return b;
    }

    @Override
    public Page<Comments> listCommentsByPage(CommentQueryRequest commentQueryRequest) {
        int current = commentQueryRequest.getCurrent();
        int pageSize = commentQueryRequest.getPageSize();

        //拿到请求中的所有信息
        QueryWrapper<Comments> queryWrapper = this.getQueryWrapper(commentQueryRequest);
        List<Comments> list = this.list(queryWrapper);

        Page<Comments> commnetsPage = new Page<>(current, pageSize);
        if (StringUtils.isAnyBlank(commentQueryRequest.getParent_comment_id().toString())) {
            //拿到父评论的信息
            List<Comments> parentCommentsList = list.stream()
                    .filter(comment -> comment.getParent_comment_id() == null
                            || comment.getParent_comment_id().toString().isEmpty())
                    .limit(pageSize)
                    .collect(Collectors.toList());
            commnetsPage.setTotal(parentCommentsList.size());
            commnetsPage.setRecords(parentCommentsList);
        } else {
            //拿到子评论的信息
            List<Comments> childCommentsList = list.stream()
                    .filter(comment -> comment.getParent_comment_id() != null
                            && !comment.getParent_comment_id().toString().isEmpty())
                    .limit(pageSize)
                    .collect(Collectors.toList());
            commnetsPage.setTotal(childCommentsList.size());
            commnetsPage.setRecords(childCommentsList);
        }
        return commnetsPage;
    }

    // todo 这里得考虑考虑,因为有子评论，需要把子评论给用上
    /**
     * 获取分页条件
     * @param commentQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Comments> getQueryWrapper(CommentQueryRequest commentQueryRequest) {
        ThrowUtils.throwIf(commentQueryRequest == null ,ErrorCode.PARAMS_ERROR,"请求参数为空");

        Long comment_id = commentQueryRequest.getComment_id();
        String comment_content = commentQueryRequest.getComment_content();
        Long commenter_id = commentQueryRequest.getCommenter_id();
        Long articleId = commentQueryRequest.getArticle_id();
        Integer thumbs_count = commentQueryRequest.getThumbs_count();
        Integer forward_count = commentQueryRequest.getForward_count();
        Integer reply_count = commentQueryRequest.getReply_count();

        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();

        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(comment_id.toString()),"comment_id", comment_id);
        queryWrapper.like(StringUtils.isNotBlank(comment_content),"comment_content",comment_content);
        queryWrapper.eq(StringUtils.isNotBlank(commenter_id.toString()),"commenter_id", commenter_id);

        queryWrapper.eq(StringUtils.isNotBlank(articleId.toString()),"article_id", articleId);
        queryWrapper.eq(StringUtils.isNotBlank(thumbs_count.toString()),"thumbs_count", thumbs_count);
        queryWrapper.eq(StringUtils.isNotBlank(forward_count.toString()),"forward_count", forward_count);
        queryWrapper.eq(StringUtils.isNotBlank(reply_count.toString()),"reply_count", reply_count);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




