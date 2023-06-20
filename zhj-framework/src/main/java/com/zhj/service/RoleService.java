package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddRoleDto;
import com.zhj.domin.dto.EditRoleDto;
import com.zhj.domin.dto.EditRoleStatusDto;
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

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(EditRoleStatusDto editRoleStatusDto);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRole(Long id);

    ResponseResult editRole(EditRoleDto editRoleDto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();

}
