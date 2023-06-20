package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.dto.AddCategoryDto;
import com.zhj.domin.dto.EditCategoryDto;
import com.zhj.domin.dto.EditCategoryStatusDto;
import com.zhj.domin.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-08-19 12:36:26
 */
public interface CategoryService extends IService<Category> {

    Result getCategoryList();

    ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult getCategory(Long id);

    ResponseResult editCategory(EditCategoryDto editCategoryDto);

    ResponseResult changeStatus(EditCategoryStatusDto editCategoryStatusDto);
}
