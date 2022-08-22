package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
