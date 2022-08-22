package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.Result;
import com.zhj.domin.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-08-19 12:36:26
 */
public interface CategoryService extends IService<Category> {

    Result getCategoryList();
}
