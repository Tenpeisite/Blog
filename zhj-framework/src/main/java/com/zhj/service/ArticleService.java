package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddArticleDto;
import com.zhj.domin.dto.EditArticleDto;
import com.zhj.domin.dto.QueryArticleDto;
import com.zhj.domin.entity.Article;
import com.zhj.domin.Result;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/19 9:55
 */
public interface ArticleService extends IService<Article> {
    Result hotArticleList();

    Result articleList(Integer pageNum, Integer pageSize, Long categoryId);

    Result getArticleDetail(Long id);

    Result updateViewCount(Long id);

    ResponseResult addBlog(AddArticleDto addArticleDto);

    ResponseResult getArticles(QueryArticleDto queryArticleDto);

    ResponseResult getArticleInfo(Long id);

    ResponseResult editStatus(Long id);

    ResponseResult editBlog(EditArticleDto editArticleDto);

    ResponseResult deleteArticle(Long id);

    ResponseResult editTop(Long id);
}
