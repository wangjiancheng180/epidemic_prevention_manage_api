package com.wjc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wjc.mapper.RoleMapper;
import com.wjc.service.RoleService;
/**
* @author 王建成
* @date 2022/3/17--11:25
*/     
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;



}
