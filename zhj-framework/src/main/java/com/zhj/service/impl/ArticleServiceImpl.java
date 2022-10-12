package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.entity.Article;
import com.zhj.domin.Result;
import com.zhj.domin.entity.Category;
import com.zhj.domin.vo.ArticleDetailVo;
import com.zhj.domin.vo.ArticleVo;
import com.zhj.domin.vo.HotArticleVo;
import com.zhj.domin.vo.PageVo;
import com.zhj.mapper.ArticleMapper;
import com.zhj.service.ArticleService;
import com.zhj.service.CategoryService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.RedisCache;
import org.assertj.core.util.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/19 10:16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;


    @Override
    public Result hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //1.必须是正式发布的
        //2.按照浏览量排序
        //3.最多查询前10条
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL).orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Article> records = page.getRecords();
        //List<HotArticleVo> list = records.stream().map(item->{
        //    HotArticleVo hotArticleVo = new HotArticleVo();
        //    BeanUtils.copyProperties(item,hotArticleVo);
        //    return hotArticleVo;
        //}).collect(Collectors.toList());
        List<HotArticleVo> list = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        list = list.stream().map(item -> {
            Integer viewCount = (Integer) redisCache.getCacheMap(SystemConstants.REDIS_VIEWCOUNT_PREFIX).get(item.getId().toString());
            item.setViewCount(Long.valueOf(viewCount));
            return item;
        }).collect(Collectors.toList());
        return Result.okResult(list);
    }

    @Override
    public Result articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //1.查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null && categoryId > 0, Article::getCategoryId, categoryId)
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getIsTop);
        //2.分页查询
        Page<Article> pageInfo = new Page<>(pageNum, pageSize);
        page(pageInfo, queryWrapper);
        Page<ArticleVo> articleVoPage = new Page<>();
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, articleVoPage, "records");
        List<ArticleVo> list = pageInfo.getRecords().stream().map(item -> {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(item, articleVo, "categoryId");
            Category category = categoryService.getById(item.getCategoryId());
            articleVo.setCategoryName(category.getName());
            Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEWCOUNT_PREFIX, item.getId().toString());
            articleVo.setViewCount(viewCount.longValue());
            return articleVo;
        }).collect(Collectors.toList());
        PageVo pageVo = new PageVo(list, articleVoPage.getTotal());
        return Result.okResult(pageVo);
    }

    @Override
    public Result getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEWCOUNT_PREFIX, id.toString());
        article.setViewCount(viewCount.longValue());
        //封装vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        return Result.okResult(articleDetailVo);
    }

    @Override
    public Result updateViewCount(Long id) {
        //Article article = getById(id);
        //article.setViewCount(article.getViewCount()+1);
        //updateById(article);
        //return Result.okResult();

        //更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.REDIS_VIEWCOUNT_PREFIX, id.toString(), 1);
        return Result.okResult();
    }
}
