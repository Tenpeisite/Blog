package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/20 14:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryArticleDto {
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private String summary;
}
