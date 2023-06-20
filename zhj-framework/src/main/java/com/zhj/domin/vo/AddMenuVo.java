package com.zhj.domin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/22 16:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMenuVo {
    private Long id;
    private String label;
    private Long parentId;
    private List<AddMenuVo> children;
}
