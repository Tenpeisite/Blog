package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/25 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditLinkDto {

    private Long id;
    private String name;
    private String logo;
    private String description;
    private String address;
    private String status;

}
