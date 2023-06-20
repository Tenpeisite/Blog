package com.zhj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.constants.SystemConstants;
import com.zhj.domin.ResponseResult;
import com.zhj.domin.entity.Menu;
import com.zhj.domin.entity.RoleMenu;
import com.zhj.domin.vo.AddMenuVo;
import com.zhj.domin.vo.MenuVo;
import com.zhj.enums.AppHttpCodeEnum;
import com.zhj.mapper.MenuMapper;
import com.zhj.service.MenuService;
import com.zhj.service.RoleMenuService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-15 21:37:29
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员(id为1)，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            //如果是 返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //否则 获得当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree(父子关系)
        //先找出第一层的菜单，然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public ResponseResult getMenuList(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //根据菜单状态查询 对菜单名模糊查询
        queryWrapper.eq(StringUtils.isNotBlank(status), Menu::getStatus, status)
                .like(StringUtils.isNotBlank(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        //List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult getMenuById(Integer id) {
        Menu menu = getById(id);
        //MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult editMenu(Menu menu) {
        if (menu.getParentId().equals(menu.getId())) {
            return ResponseResult.errorResult(500, "修改菜单" + menu.getMenuName() + "失败，上级菜单不能选择自己");
        } else {
            updateById(menu);
            return ResponseResult.okResult();
        }
    }

    @Override
    public ResponseResult deleteMenu(Integer menuId) {
        Menu menu = getById(menuId);
        List<Menu> menus = getBaseMapper().selectAllRouterMenu();
        //获得该菜单的子菜单
        List<Menu> children = getChildren(menu, menus);
        if (CollectionUtils.isEmpty(children)) {
            //如果该菜单没有子菜单
            removeById(menuId);
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(500, "存在子菜单不允许删除");
    }

    @Override
    public ResponseResult treeselect(Long id) {
        List<Menu> menus = selectRouterMenuTreeByUserId(id);
        List<AddMenuVo> addMenuVos = getAddMenuVos(menus);
        return ResponseResult.okResult(addMenuVos);
    }


    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        //获得所有菜单
        List<Menu> menus = getBaseMapper().selectAllRouterMenu();
        //构建具有父子关系的菜单
        List<Menu> menuList = builderMenuTree(menus, 0L);
        //构建vo
        List<AddMenuVo> menuVos = getAddMenuVos(menuList);

        //查询该角色对应的菜单id
        List<RoleMenu> list = roleMenuService.lambdaQuery()
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, id).list();
        List<Long> checkedKeys = list.stream().map(item -> item.getMenuId()).collect(Collectors.toList());
        Map map = new HashMap();
        map.put("menus", menuVos);
        map.put("checkedKeys", checkedKeys);
        return ResponseResult.okResult(map);
    }

    //@NotNull
    private List<AddMenuVo> getAddMenuVos(List<Menu> menus) {
        return menus.stream().map(item -> {
            AddMenuVo addMenuVo = BeanCopyUtils.copyBean(item, AddMenuVo.class);
            addMenuVo.setLabel(item.getMenuName());
            List<Menu> menuList = item.getChildren();
            List<AddMenuVo> collect = menuList.stream().map(menu -> {
                AddMenuVo bean = BeanCopyUtils.copyBean(menu, AddMenuVo.class);
                bean.setLabel(menu.getMenuName());
                return bean;
            }).collect(Collectors.toList());
            addMenuVo.setChildren(collect);
            return addMenuVo;
        }).collect(Collectors.toList());
    }


    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }


    /**
     * 获取传入参数的子menu集合
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                //二级子菜单
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三级子菜单
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
