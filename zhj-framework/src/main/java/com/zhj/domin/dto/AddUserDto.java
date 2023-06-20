package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/23 12:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDto {
    private String userName;
    private String nickName;
    private String password;
    private String phonenumber;
    private String email;
    private String sex;
    private String status;
    private List<Long> roleIds;
}
