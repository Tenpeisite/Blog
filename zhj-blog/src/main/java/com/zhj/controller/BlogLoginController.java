package com.zhj.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhj.domin.Result;
import com.zhj.domin.entity.User;
import com.zhj.enums.AppHttpCodeEnum;
import com.zhj.exception.SystemException;
import com.zhj.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/20 18:12
 */
@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        if(StringUtils.isBlank(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public Result logout(){
        return blogLoginService.logout();
    }
}
