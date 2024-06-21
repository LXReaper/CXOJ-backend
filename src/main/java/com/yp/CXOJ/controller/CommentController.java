package com.yp.CXOJ.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.CXOJ.common.BaseResponse;
import com.yp.CXOJ.common.ResultUtils;
import com.yp.CXOJ.model.dto.comments.CommentAddRequest;
import com.yp.CXOJ.model.dto.comments.CommentQueryRequest;
import com.yp.CXOJ.model.dto.comments.CommentUpdateRequest;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.Comments;
import com.yp.CXOJ.service.CommentsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 19:14:40
 */
@RestController
@RequestMapping("/comments")
@Slf4j
public class CommentController {
    @Resource
    private CommentsService commentsService;

    @PostMapping("/add")
    @ApiOperation("新增评论")
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest httpServletRequest) {
        return ResultUtils.success(commentsService.addComment(commentAddRequest));
    }

    @PostMapping("/delete")
    @ApiOperation("删除评论")
    public BaseResponse<Boolean> deleteComment(Long id, HttpServletRequest httpServletRequest) {
        return ResultUtils.success(commentsService.deleteComment(id));
    }

    @PostMapping("/update")
    @ApiOperation("更新评论")
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest, HttpServletRequest httpServletRequest) {
        return ResultUtils.success(commentsService.updateComment(commentUpdateRequest));
    }

    @PostMapping("/list")
    @ApiOperation("展示评论")
    public BaseResponse<Page<Comments>> listCommentsByPage(@RequestBody CommentQueryRequest commentQueryRequest,
                                                           HttpServletRequest httpServletRequest) {
        int current = commentQueryRequest.getCurrent();
        int pageSize = commentQueryRequest.getPageSize();

        //拿到请求中的所有信息
        QueryWrapper<Comments> queryWrapper = commentsService.getQueryWrapper(commentQueryRequest);
        List<Comments> list = commentsService.list(queryWrapper);

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
        return ResultUtils.success(commnetsPage);
    }
}
