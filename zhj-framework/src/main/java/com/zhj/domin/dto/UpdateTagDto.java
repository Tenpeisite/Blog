package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/17 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTagDto {
    private Long id;
    private String name;
    private String remark;
}
