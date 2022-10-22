package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-10-15 21:41:31
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
