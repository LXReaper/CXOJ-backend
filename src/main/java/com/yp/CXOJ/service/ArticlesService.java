package com.yp.CXOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.CXOJ.model.dto.articles.ArticleAddRequest;
import com.yp.CXOJ.model.dto.articles.ArticleQueryRequest;
import com.yp.CXOJ.model.dto.articles.ArticleUpdateRequest;
import com.yp.CXOJ.model.dto.comments.CommentAddRequest;
import com.yp.CXOJ.model.dto.comments.CommentUpdateRequest;
import com.yp.CXOJ.model.dto.user.UserQueryRequest;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.vo.ArticlesVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 余炎培
* @description 针对表【articles(文章表)】的数据库操作Service
* @createDate 2024-06-10 22:15:27
*/
public interface ArticlesService extends IService<Articles> {
    /**
     * 添加文章
     * @param articleAddRequest
     * @param request
     * @return
     */
    Long addArticle(ArticleAddRequest articleAddRequest, HttpServletRequest request);

    /**
     * 删除文章
     * @param id
     * @return
     */
    Boolean deleteArticle(Long id);

    /**
     * 更新文章
     * @param articleUpdateRequest
     * @return
     */
    Boolean updateArticle(ArticleUpdateRequest articleUpdateRequest);

    /**
     * 分页展示文章
     * @param articleQueryRequest
     * @return
     */
    Page<ArticlesVO> listArticlesByPage(ArticleQueryRequest articleQueryRequest);

    /**
     * 获取分页查询条件
     * @param articleQueryRequest
     * @return
     */
    QueryWrapper<Articles> getQueryWrapper(ArticleQueryRequest articleQueryRequest);
}
