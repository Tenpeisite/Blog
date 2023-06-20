package com.zhj.domin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 朱焕杰
 * @version 1.0
 * @date 2022/10/22 18:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditRoleDto {
    private Long id;
    private String remark;
    private String roleKey;
    private String roleName;
    private Integer roleSort;
    private String status;
    private List<Long> menuIds;
}
