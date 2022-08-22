package com.zhj.controller;

import com.zhj.domin.Result;
import com.zhj.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/19 15:30
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    public Result getCategoryList(){
        return categoryService.getCategoryList();
    }
}
