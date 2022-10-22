package com.zhj.service.impl;

import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.entity.LoginUser;
import com.zhj.domin.entity.User;
import com.zhj.domin.vo.BlogUserLoginVo;
import com.zhj.domin.vo.UserInfoVo;
import com.zhj.service.BlogLoginService;
import com.zhj.service.LoginService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.JwtUtil;
import com.zhj.utils.RedisCache;
import com.zhj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/20 18:17
 */
@Service
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public Result login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //这里调用userDetailService进行认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(userId.toString());
        //把用户信息存入redis
        redisCache.setCacheObject(SystemConstants.REDIS_BACKSTAGELOGINUSER_PREFIX + userId, loginUser);

        //把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return Result.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(SystemConstants.REDIS_BACKSTAGELOGINUSER_PREFIX+userId);
        return ResponseResult.okResult();
    }


}
