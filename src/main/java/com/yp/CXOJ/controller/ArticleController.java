package com.yp.CXOJ.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.CXOJ.common.BaseResponse;
import com.yp.CXOJ.common.ResultUtils;
import com.yp.CXOJ.model.dto.articles.ArticleAddRequest;
import com.yp.CXOJ.model.dto.articles.ArticleQueryRequest;
import com.yp.CXOJ.model.dto.articles.ArticleUpdateRequest;
import com.yp.CXOJ.model.dto.comments.CommentAddRequest;
import com.yp.CXOJ.model.dto.comments.CommentUpdateRequest;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.Comments;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.vo.ArticlesVO;
import com.yp.CXOJ.service.ArticlesService;
import com.yp.CXOJ.service.CommentsService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 余炎培
 * @since 2024-06-10 星期一 22:40:30
 */
@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {
    @Resource
    private ArticlesService articlesService;

    @PostMapping("/add")
    @ApiOperation("新增文章")
    public BaseResponse<Long> addArticle(@RequestBody ArticleAddRequest articleAddRequest , HttpServletRequest httpServletRequest) {
        return ResultUtils.success(articlesService.addArticle(articleAddRequest,httpServletRequest));
    }

    @PostMapping("/delete")
    @ApiOperation("删除文章")
    public BaseResponse<Boolean> deleteArticle(Long id , HttpServletRequest httpServletRequest) {
        return ResultUtils.success(articlesService.deleteArticle(id));
    }

    @PostMapping("/update")
    @ApiOperation("更新文章")
    public BaseResponse<Boolean> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest , HttpServletRequest httpServletRequest) {
        return ResultUtils.success(articlesService.updateArticle(articleUpdateRequest));
    }

    @PostMapping("/list/page")
    @ApiOperation("分页展示文章")
    public BaseResponse<Page<ArticlesVO>> listArticlesByPage(@RequestBody ArticleQueryRequest articleQueryRequest,
                                                             HttpServletRequest httpServletRequest){
        return ResultUtils.success(articlesService.listArticlesByPage(articleQueryRequest));
    }
}
