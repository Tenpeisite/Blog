package com.zhj.controller;

import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddRoleDto;
import com.zhj.domin.dto.EditRoleDto;
import com.zhj.domin.dto.EditRoleStatusDto;
import com.zhj.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/22 15:38
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status) {
        return roleService.getRoleList(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody EditRoleStatusDto editRoleStatusDto){
        return roleService.changeStatus(editRoleStatusDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable Long id){
        return roleService.getRole(id);
    }

    @PutMapping
    public ResponseResult editRole(@RequestBody EditRoleDto editRoleDto){
        return roleService.editRole(editRoleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }

}
