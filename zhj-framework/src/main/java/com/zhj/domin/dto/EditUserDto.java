package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/23 13:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditUserDto {
    private List<Long> roleIds;
    private Long id;
    private String nickName;
    private String email;
    private String sex;
    private String status;
    private String userName;

}
