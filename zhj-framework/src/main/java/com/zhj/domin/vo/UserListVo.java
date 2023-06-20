package com.zhj.domin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/23 12:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListVo {
    private String avatar;
    private Date createTime;
    private String email;
    private Long id;
    private String nickName;
    private String phonenumber;
    private String sex;
    private String status;
    private Long updateBy;
    private Date updateTime;
    private String userName;

}
