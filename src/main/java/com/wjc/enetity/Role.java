package com.wjc.enetity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/3/17--11:25
*/     
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Integer id;

    /**
    * 角色名称 普通用户，超级管理员，运营人员
    */
    private String name;

    /**
    * 角色key role_user role_admin 

    */
    private String roleKey;

    /**
    * 状态 1：开启 0：禁用
    */
    private Integer status;

    /**
    * 描述
    */
    private String description;

    private Date createTime;

    private Date updateTime;
}