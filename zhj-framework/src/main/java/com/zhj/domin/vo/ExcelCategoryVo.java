package com.zhj.domin.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/19 14:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCategoryVo {
    @ExcelIgnore
    private Long id;

    @ExcelProperty("分类名")
    private String name;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("状态0：正常，1禁用")
    private String status;
}
