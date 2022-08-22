package com.zhj.service;

import com.zhj.domin.Result;
import com.zhj.domin.entity.User;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/8/20 18:16
 */
public interface BlogLoginService {

    Result login(User user);

    Result logout();
}
