package com.zhj.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.entity.Category;
import com.zhj.domin.entity.Tag;
import com.zhj.domin.vo.CategoryVo;
import com.zhj.domin.vo.ExcelCategoryVo;
import com.zhj.domin.vo.TagVo;
import com.zhj.enums.AppHttpCodeEnum;
import com.zhj.service.CategoryService;
import com.zhj.service.TagService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/18 10:36
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<Category> list = categoryService.list();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    //@ps表示去容器中找到名字为ps的bean，调用其hasPermission方法来判断
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categories = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);
            //把数据写入Excel
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        }catch (Exception e){
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

}
