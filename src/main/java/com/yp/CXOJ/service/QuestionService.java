package com.yp.CXOJ.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.CXOJ.model.dto.question.QuestionQueryRequest;
import com.yp.CXOJ.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.CXOJ.model.vo.QuestionVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author 余炎培
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-04-07 23:56:11
*/
public interface QuestionService extends IService<Question> {
    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 使用爬虫获取的md数据添加题目
     * @param request
     * @return
     */
    Boolean addQuestionsFromMd(HttpServletRequest request);

    /**
     * 将文件中的数据作为题目添加
     * @param files
     * @param request
     * @return
     */
    Boolean addQuestionsFromFiles(MultipartFile[] files,HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
