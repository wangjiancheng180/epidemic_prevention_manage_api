package com.wjc.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wjc.mapper.system.RoleMapper;
import com.wjc.service.system.RoleService;
/**
* @author 王建成
* @date 2022/3/17--11:25
*/     
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;



}
