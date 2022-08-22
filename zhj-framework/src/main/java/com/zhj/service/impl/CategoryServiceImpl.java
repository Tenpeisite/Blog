package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.Result;
import com.zhj.domin.entity.Article;
import com.zhj.domin.entity.Category;
import com.zhj.domin.vo.CategoryVo;
import com.zhj.mapper.ArticleMapper;
import com.zhj.mapper.CategoryMapper;
import com.zhj.service.ArticleService;
import com.zhj.service.CategoryService;
import com.zhj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-08-19 12:36:26
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;

    @Override
    public Result getCategoryList() {
        //查询文章表 状态为已发布的文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream().map(item -> item.getCategoryId()).collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().filter(category -> category.getStatus().equals(SystemConstants.CATEGORY_STATUS_NORMAL)).collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return Result.okResult(categoryVos);
    }
}
