package com.zhj.domin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/23 13:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndRoleVo {
    private List<Long> roleIds;
    private List<RoleListVo> roles;
    private UserInfoVo user;
}
