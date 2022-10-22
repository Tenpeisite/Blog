package com.zhj.service.impl;

import com.zhj.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/19 20:09
 */
@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     *
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission) {
        //如果是超级管理员则直接返回true
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        //否则判断当前用户是否具有权限
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
