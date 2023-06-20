package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/25 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLinkDto {
    private String name;
    private String description;
    private String address;
    private String logo;
    private String status;

}
