package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.Result;
import com.zhj.domin.dto.AddUserDto;
import com.zhj.domin.dto.EditUserDto;
import com.zhj.domin.dto.EditUserStatusDto;
import com.zhj.domin.entity.User;
import com.zhj.domin.entity.UserRole;
import com.zhj.domin.vo.*;
import com.zhj.enums.AppHttpCodeEnum;
import com.zhj.exception.SystemException;
import com.zhj.mapper.UserMapper;
import com.zhj.service.RoleService;
import com.zhj.service.UserRoleService;
import com.zhj.service.UserService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-08-20 19:21:00
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装称vo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return Result.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        redisTemplate.opsForSet().add(SystemConstants.Article_PIC_DB_RESOURCES,user.getAvatar());
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        checkData(user);
        //对密码加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    private void checkData(User user) {
        //对数据进行校验
        if (StringUtils.isBlank(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (StringUtils.isBlank(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (StringUtils.isBlank(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (StringUtils.isBlank(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据进行是否存在判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(userName), User::getUserName, userName)
                .like(StringUtils.isNotBlank(phonenumber), User::getPhonenumber, phonenumber)
                .eq(StringUtils.isNotBlank(status), User::getStatus, status);
        page(page, queryWrapper);
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);
        return ResponseResult.okResult(new PageVo(userListVos, page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        //检验手机号
        if (StringUtils.isBlank(user.getPhonenumber())) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //对数据进行是否存在判断
        if (phoneNumberExist(user.getPhonenumber())) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        //检验其他数据
        checkData(user);
        user.setPassword(passwordEncoder.encode(addUserDto.getPassword()));
        //添加用户
        save(user);

        //添加用户对应的角色
        List<Long> roleIds = addUserDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteUser(Long id) {
        //删除用户
        removeById(id);
        //删除用户和角色关系
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        userRoleService.remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUser(Long id) {
        //获得用户信息
        User user = getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //获得所有角色列表
        List<RoleListVo> roleListVos = (List<RoleListVo>) roleService.listAllRole().getData();

        //获得用户关联的角色信息
        List<UserRole> list = userRoleService.lambdaQuery().eq(UserRole::getUserId, id).select(UserRole::getRoleId).list();
        List<Long> roleIds = list.stream().map(item -> item.getRoleId()).collect(Collectors.toList());

        //封装vo
        UserAndRoleVo userAndRoleVo = new UserAndRoleVo(roleIds, roleListVos, userInfoVo);
        return ResponseResult.okResult(userAndRoleVo);
    }

    @Override
    public ResponseResult editUser(EditUserDto editUserDto) {
        User user = BeanCopyUtils.copyBean(editUserDto, User.class);
        updateById(user);

        //删除原来的关系
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(queryWrapper);

        //添加新的关系
        List<Long> roleIds = editUserDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream().map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(EditUserStatusDto editUserStatusDto) {
        User user = getById(editUserStatusDto.getUserId());
        user.setStatus(editUserStatusDto.getStatus());
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean emailExist(String email) {
        return lambdaQuery().eq(User::getEmail, email).count().intValue() > 0 ? true : false;
    }

    private boolean nickNameExist(String nickName) {
        return lambdaQuery().eq(User::getNickName, nickName).count().intValue() > 0 ? true : false;
    }

    private boolean userNameExist(String userName) {
        int count = lambdaQuery().eq(User::getUserName, userName).count();
        return count > 0 ? true : false;
    }

    private boolean phoneNumberExist(String phone) {
        return lambdaQuery().eq(User::getPhonenumber, phone).count().intValue() > 0;
    }
}
