package com.zhj.controller;

import com.zhj.domin.ResponseResult;
import com.zhj.domin.entity.Menu;
import com.zhj.domin.vo.AddMenuVo;
import com.zhj.service.MenuService;
import com.zhj.utils.BeanCopyUtils;
import com.zhj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/20 17:19
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult list(String status, String menuName) {
        return menuService.getMenuList(status, menuName);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Integer id) {
        return menuService.getMenuById(id);
    }

    @PutMapping
    public ResponseResult editMenu(@RequestBody Menu menu) {
        return menuService.editMenu(menu);
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Integer menuId) {
        return menuService.deleteMenu(menuId);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        return menuService.treeselect(SecurityUtils.getUserId());
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id){
        return menuService.roleMenuTreeselect(id);
    }
}
