package com.zhj.controller;

import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddUserDto;
import com.zhj.domin.dto.EditUserDto;
import com.zhj.domin.dto.EditUserStatusDto;
import com.zhj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/23 12:15
 */
@RequestMapping("/system/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult getUserList(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.getUserList(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PutMapping
    public ResponseResult editUser(@RequestBody EditUserDto editUserDto){
        return userService.editUser(editUserDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody EditUserStatusDto editUserStatusDto){
        return userService.changeStatus(editUserStatusDto);
    }
}
