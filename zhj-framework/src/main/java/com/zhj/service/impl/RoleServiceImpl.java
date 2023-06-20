package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.dto.AddRoleDto;
import com.zhj.domin.dto.EditRoleDto;
import com.zhj.domin.dto.EditRoleStatusDto;
import com.zhj.domin.entity.Menu;
import com.zhj.domin.entity.Role;
import com.zhj.domin.entity.RoleMenu;
import com.zhj.domin.vo.PageVo;
import com.zhj.domin.vo.RoleListVo;
import com.zhj.domin.vo.RoleVo;
import com.zhj.mapper.RoleMapper;
import com.zhj.service.MenuService;
import com.zhj.service.RoleMenuService;
import com.zhj.service.RoleService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-15 21:41:31
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是返回集合中只需要有admin
        if (id == 1L) {
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(roleName), Role::getRoleName, roleName)
                .eq(StringUtils.isNotBlank(status), Role::getStatus, status)
                .orderByAsc(Role::getRoleSort);
        page(page, queryWrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos, page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(EditRoleStatusDto editRoleStatusDto) {
        Role role = getById(editRoleStatusDto.getRoleId());
        role.setStatus(editRoleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        List<Long> menuIds = addRoleDto.getMenuIds();
        List<RoleMenu> roleMenus = menuIds.stream().map(menuId -> new RoleMenu(role.getId(), menuId)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRole(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    @Transactional
    public ResponseResult editRole(EditRoleDto editRoleDto) {
        Role role = BeanCopyUtils.copyBean(editRoleDto, Role.class);
        updateById(role);

        List<Long> menuIds = editRoleDto.getMenuIds();
        //先删除之前的menuIds
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuService.remove(queryWrapper);
        //再添加新的
        List<RoleMenu> roleMenus = menuIds.stream()
                .map(item -> new RoleMenu(role.getId(), item))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        removeById(id);
        //删除之前的menuIds
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, id);
        roleMenuService.remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> list = lambdaQuery()
                .eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL)
                .list();
        List<RoleListVo> roleListVos = BeanCopyUtils.copyBeanList(list, RoleListVo.class);
        return ResponseResult.okResult(roleListVos);
    }
}
