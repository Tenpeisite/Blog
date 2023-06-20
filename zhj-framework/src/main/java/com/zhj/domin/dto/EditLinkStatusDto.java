package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/22 16:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditLinkStatusDto {
    private Long id;
    private String status;
}
