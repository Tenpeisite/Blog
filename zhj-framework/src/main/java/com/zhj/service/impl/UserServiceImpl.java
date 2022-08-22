package com.zhj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.domin.entity.User;
import com.zhj.mapper.UserMapper;
import com.zhj.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-08-20 19:21:00
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
