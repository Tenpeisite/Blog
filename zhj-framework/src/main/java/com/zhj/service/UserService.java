package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.dto.AddUserDto;
import com.zhj.domin.dto.EditUserDto;
import com.zhj.domin.dto.EditUserStatusDto;
import com.zhj.domin.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-08-20 19:21:00
 */
public interface UserService extends IService<User> {

    Result userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUser(Long id);

    ResponseResult getUser(Long id);

    ResponseResult editUser(EditUserDto editUserDto);

    ResponseResult changeStatus(EditUserStatusDto editUserStatusDto);
}
