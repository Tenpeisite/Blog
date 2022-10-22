package com.zhj.domin.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/16 18:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "标签描述")
public class TagListDto {
    private String name;
    private String remark;
}
