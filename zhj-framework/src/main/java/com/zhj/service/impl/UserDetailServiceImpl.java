package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.entity.LoginUser;
import com.zhj.domin.entity.User;
import com.zhj.mapper.MenuMapper;
import com.zhj.mapper.UserMapper;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/20 18:27
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = (User) userMapper.selectOne(queryWrapper);
        //判断是否查到用户，如果没有则抛异常
        if(Objects.isNull(user)){
            throw  new RuntimeException("用户不存在");
        }
        //返回用户信息
        //TODO 查询权限信息封装
        //todo 后台用户才需要查询权限封装
        if(SystemConstants.ADMIN.equals(user.getType())){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        return new LoginUser(user);
    }
}
