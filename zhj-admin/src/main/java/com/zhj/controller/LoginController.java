package com.zhj.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.entity.LoginUser;
import com.zhj.domin.entity.Menu;
import com.zhj.domin.entity.User;
import com.zhj.domin.vo.AdminUserInfoVo;
import com.zhj.domin.vo.RoutersVo;
import com.zhj.domin.vo.UserInfoVo;
import com.zhj.enums.AppHttpCodeEnum;
import com.zhj.exception.SystemException;
import com.zhj.service.BlogLoginService;
import com.zhj.service.LoginService;
import com.zhj.service.MenuService;
import com.zhj.service.RoleService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.RedisCache;
import com.zhj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/20 18:12
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;


    @PostMapping("/user/login")
    public Result login(@RequestBody User user){
        if(StringUtils.isBlank(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms=menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList=roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus=menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
