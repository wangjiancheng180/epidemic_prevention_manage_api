package com.wjc.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.dto.system.SysResourceDto;
import com.wjc.dto.system.SysResourceTree;
import com.wjc.dto.system.SysRoleDto;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.enetity.system.UserInfo;
import com.wjc.mapper.system.UserInfoMapper;
import com.wjc.param.system.UserInfoQueryBean;
import com.wjc.service.system.RoleService;
import com.wjc.service.system.SysResourceService;
import com.wjc.service.system.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 王建成
 * @date 2022/3/17--13:29
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SysResourceService resourceService;

    @Override
    public UserInfo findByUserName(String userName) {
        return userInfoMapper.findByUserName(userName);
    }

    @Override
    public IPage<UserInfo> pageList(UserInfoQueryBean bean) {
        IPage<UserInfo> pageInfo = new Page<>(bean.getPage(),bean.getSize());
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(UserInfo::getUsername,bean.getUsername());
        queryWrapper.like(UserInfo::getRealName,bean.getRealName());
        queryWrapper.like(UserInfo::getPhone,bean.getPhone());
        queryWrapper.eq(UserInfo::getSex,bean.getSex());
        queryWrapper.eq(UserInfo::getStatus,bean.getStatus());
        return page(pageInfo,queryWrapper);
    }

    @Override
    public UserInfoDto queryByUsername(String username) {

        UserInfoDto userInfoDto = userInfoMapper.queryByUsername(username);
        Set<Long> roleIds = new HashSet<>();
        for (SysRoleDto  role:userInfoDto.getRoleDtos()
             ) {
            roleIds.add(role.getId());
        }
        //这里需要获取所有角色的资源，去重
        Set<SysResourceDto> sysResourceDtos = roleService.getResourceDtos(roleIds);
        //将这些资源组成一颗树
        List<SysResourceTree> sysResourceTrees = resourceService.combinationTree(sysResourceDtos);
        userInfoDto.setResourceTrees(sysResourceTrees);
        return userInfoDto;
    }
}
