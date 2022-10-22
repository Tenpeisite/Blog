package com.zhj.domin.vo;

import com.zhj.domin.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/16 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {
    private List<Menu> menus;
}
