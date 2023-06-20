package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.dto.AddCategoryDto;
import com.zhj.domin.dto.EditCategoryDto;
import com.zhj.domin.dto.EditCategoryStatusDto;
import com.zhj.domin.entity.Article;
import com.zhj.domin.entity.Category;
import com.zhj.domin.vo.CategoryVo;
import com.zhj.domin.vo.PageVo;
import com.zhj.mapper.CategoryMapper;
import com.zhj.service.ArticleService;
import com.zhj.service.CategoryService;
import com.zhj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Category::getName,name)
                .eq(StringUtils.isNotBlank(status),Category::getStatus,status);
        page(page,queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryVo.class);
        return ResponseResult.okResult(new PageVo(categoryVos,page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategory(Long id) {
        Category category = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult editCategory(EditCategoryDto editCategoryDto) {
        Category category = BeanCopyUtils.copyBean(editCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(EditCategoryStatusDto editCategoryStatusDto) {
        Category category = getById(editCategoryStatusDto.getCategoryId());
        category.setStatus(editCategoryStatusDto.getStatus());
        updateById(category);
        return ResponseResult.okResult();
    }
}
