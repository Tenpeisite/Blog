package com.zhj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhj.domin.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-15 21:41:30
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}
