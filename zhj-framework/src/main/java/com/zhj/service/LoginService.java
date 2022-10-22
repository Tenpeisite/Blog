package com.zhj.service;

import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.entity.User;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/13 16:41
 */
public interface LoginService {
    Result login(User user);

    ResponseResult logout();
}
