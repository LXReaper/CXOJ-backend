package com.yp.CXOJ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.constant.CommonConstant;
import com.yp.CXOJ.constant.UserConstant;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.dto.articles.ArticleAddRequest;
import com.yp.CXOJ.model.dto.articles.ArticleQueryRequest;
import com.yp.CXOJ.model.dto.articles.ArticleUpdateRequest;
import com.yp.CXOJ.model.entity.Articles;
import com.yp.CXOJ.model.entity.Comments;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.vo.ArticlesVO;
import com.yp.CXOJ.model.vo.CommentsVO;
import com.yp.CXOJ.service.ArticlesService;
import com.yp.CXOJ.mapper.ArticlesMapper;
import com.yp.CXOJ.service.CommentsService;
import com.yp.CXOJ.service.UserService;
import com.yp.CXOJ.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 余炎培
 * @description 针对表【articles(文章表)】的数据库操作Service实现
 * @createDate 2024-06-10 22:15:27
 */
@Service
public class ArticlesServiceImpl extends ServiceImpl<ArticlesMapper, Articles>
        implements ArticlesService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private CommentsService commentsService;

    /**
     * 添加公告
     *
     * @param articleAddRequest
     * @return
     */
    @Override
    public Long addArticle(ArticleAddRequest articleAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(articleAddRequest == null, ErrorCode.PARAMS_ERROR, "请求参数为空");
        ThrowUtils.throwIf(StringUtils.isAnyBlank(articleAddRequest.getArticle_title()), ErrorCode.PARAMS_ERROR, "文章标题不能为空");
        ThrowUtils.throwIf(StringUtils.isAnyBlank(articleAddRequest.getArticle_content()), ErrorCode.PARAMS_ERROR, "文章内容不能为空");

        //判断用户是否登录
        User loginUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.OPERATION_ERROR, "未登录");

        Articles articles = new Articles();
        BeanUtils.copyProperties(articleAddRequest, articles);
        articles.setUser_id(loginUser.getId());
        boolean save = this.save(articles);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "文章新增失败");

        return articles.getArticle_id();
    }

    /**
     * 删除公告
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteArticle(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "请求删除文章id非法");

        QueryWrapper<Articles> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", id);
        Articles articles = this.getOne(wrapper);

        //判断是否选择删除的文章是否存在
        ThrowUtils.throwIf(articles == null, ErrorCode.NOT_FOUND_ERROR, "选择删除的文章不存在");
        boolean b = this.removeById(id);
        return b;
    }

    /**
     * 更新公告
     *
     * @param articleUpdateRequest
     * @return
     */
    @Override
    public Boolean updateArticle(ArticleUpdateRequest articleUpdateRequest) {
        ThrowUtils.throwIf(articleUpdateRequest == null || articleUpdateRequest.getArticle_id() == null,
                ErrorCode.PARAMS_ERROR, "文章更新请求为空");

        QueryWrapper<Articles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleUpdateRequest.getArticle_id());
        Articles articles = this.getOne(queryWrapper);

        //判断请求更新的文章是否存在
        ThrowUtils.throwIf(articles == null, ErrorCode.NOT_FOUND_ERROR, "不存在该文章");

        articles.setArticle_title(articleUpdateRequest.getArticle_title());
        articles.setArticle_content(articleUpdateRequest.getArticle_content());
        boolean b = this.updateById(articles);
        return b;
    }

    @Override
    public Page<ArticlesVO> listArticlesByPage(ArticleQueryRequest articleQueryRequest) {
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        //查到满足条件的articlesVO
        QueryWrapper<Articles> queryWrapper = getQueryWrapper(articleQueryRequest);
        List<Articles> articlesList = this.list(queryWrapper);
        List<ArticlesVO> articlesVOList = articlesList.stream()
                .limit(size)
                .map(ArticlesVO::objToVO)
                .sorted((o1, o2) -> {
                    if (o1.getUser_id() == null || o2.getUser_id() == null)
                        return o1.getArticle_id().compareTo(o2.getArticle_id());
                    return o1.getUser_id() > o2.getUser_id() ? -1 : 1;
                })
                .collect(Collectors.toList());

        Page<ArticlesVO> articlesVOPage = new Page<>(current, size);

        for (int i = 0; i < articlesVOList.size(); ) {
            QueryWrapper<User> queryWrapperUser = new QueryWrapper<>();
            queryWrapperUser.eq(articlesVOList.get(i).getUser_id() != null, "id", articlesVOList.get(i).getUser_id());
            User user = userService.getOne(queryWrapperUser);
            //这只是为了减少重复查找数据库，提高性能
            int j = i;
            while (j < articlesVOList.size()
                    && articlesVOList.get(j).getUser_id().equals(user.getId())) {
                articlesVOList.get(j).setUserAvatar(user.getUserAvatar());
                articlesVOList.get(j).setUserName(user.getUserName());

                //拿到文章的所有回复信息
                QueryWrapper<Comments> commentsQueryWrapper = new QueryWrapper<>();
                commentsQueryWrapper.eq(articlesVOList.get(j).getArticle_id() != null, "article_id"
                        , articlesVOList.get(j).getArticle_id());
                List<CommentsVO> commentsList = commentsService.list(commentsQueryWrapper)
                        .stream()
                        .map(comments -> {
                            CommentsVO commentsVO = CommentsVO.objToVO(comments);
                            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                            userQueryWrapper.eq(commentsVO.getCommenter_id() != null, "id", commentsVO.getCommenter_id());
                            User user1 = userService.getOne(userQueryWrapper);
                            if (user1 != null) {
                                commentsVO.setCommenterName(user1.getUserName());
                                commentsVO.setCommenterAvatar(user1.getUserAvatar());
                            }

                            userQueryWrapper = new QueryWrapper<>();
                            userQueryWrapper.eq(comments.getCommented_user_id() != null, "id", comments.getCommented_user_id());
                            user1 = userService.getOne(userQueryWrapper);
                            if (user1 != null) {
                                commentsVO.setCommented_userName(user1.getUserName());
                                commentsVO.setCommented_userAvatar(user1.getUserAvatar());
                            }
                            return commentsVO;
                        })
                        .sorted((o1,o2)-> o2.getUpdate_time().compareTo(o1.getUpdate_time()))//按更新时间倒序
                        .collect(Collectors.toList());

                articlesVOList.get(j).setCommentsList(commentsList);
                ++j;
            }
            i = j;
        }

        //默认优先按更新时间倒序，后按观看数倒序
        articlesVOList.sort((o1, o2) -> {
            if (StringUtils.isAnyBlank(articleQueryRequest.getSortOrder())
                    && articleQueryRequest.getSortOrder().equals(CommonConstant.SORT_ORDER_ASC))
                return o1.getUpdate_time().compareTo(o2.getUpdate_time());
            if (o1.getUpdate_time().compareTo(o2.getUpdate_time()) == 0)//时间相同
                return o2.getWatch_count() - o1.getWatch_count();
            return o2.getUpdate_time().compareTo(o1.getUpdate_time());
        });

        articlesVOPage.setRecords(articlesVOList);
        articlesVOPage.setTotal(articlesList.size());
        return articlesVOPage;
    }

    /**
     * 获取分页条件
     *
     * @param articleQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Articles> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        ThrowUtils.throwIf(articleQueryRequest == null, ErrorCode.PARAMS_ERROR, "请求参数为空");

        String article_title = articleQueryRequest.getArticle_title();
        String article_content = articleQueryRequest.getArticle_content();
        Long user_id = articleQueryRequest.getUser_id();
        Integer watch_count = articleQueryRequest.getWatch_count();
        Integer thumbs_count = articleQueryRequest.getThumbs_count();
        Integer collect_count = articleQueryRequest.getCollect_count();
        Integer forward_count = articleQueryRequest.getForward_count();
        Integer reply_count = articleQueryRequest.getReply_count();
        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();

        QueryWrapper<Articles> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(article_title), "article_title", article_title);
        queryWrapper.like(StringUtils.isNotBlank(article_content), "article_content", article_content);
        queryWrapper.eq(user_id != null, "user_id", user_id);
        queryWrapper.eq(watch_count != null, "watch_count", watch_count);
        queryWrapper.eq(thumbs_count != null, "thumbs_count", thumbs_count);
        queryWrapper.eq(collect_count != null, "collect_count", collect_count);
        queryWrapper.eq(forward_count != null, "forward_count", forward_count);
        queryWrapper.eq(reply_count != null, "reply_count", reply_count);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




