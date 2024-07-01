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
        return ResultUtils.success(commentsService.addComment(commentAddRequest,httpServletRequest));
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
        return ResultUtils.success(commentsService.listCommentsByPage(commentQueryRequest));
    }
}
