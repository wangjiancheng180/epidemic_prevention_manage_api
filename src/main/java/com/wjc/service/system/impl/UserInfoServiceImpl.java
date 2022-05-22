package com.wjc.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.wjc.common.login.RedisKey;
import com.wjc.dto.system.*;
import com.wjc.enetity.system.UserInfo;
import com.wjc.mapper.system.UserInfoMapper;
import com.wjc.param.system.UserInfoCreateBean;
import com.wjc.param.system.UserInfoQueryBean;
import com.wjc.service.system.RoleService;
import com.wjc.service.system.SysResourceService;
import com.wjc.service.system.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

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
    public AuthInfo queryByUsername(String username) {

        AuthInfo authInfo = userInfoMapper.queryByUsername(username);
        if (authInfo==null){
            return null;
        }
        Set<Long> roleIds = new HashSet<>();
        for (SysRoleDto  role:authInfo.getRoleDtos()
             ) {
            roleIds.add(role.getId());
        }
        //这里需要获取所有角色的资源，去重
        Set<SysResourceDto> sysResourceDtos = roleService.getResourceDtos(roleIds);

        //权限信息
        authInfo.setResourceDtos(sysResourceDtos);
        authInfo.setAuthorities(getAuthorityList(authInfo));
        //将这些资源组成一颗树
        List<SysResourceTree> sysResourceTrees = resourceService.combinationTree(sysResourceDtos);
        authInfo.setResourceTrees(sysResourceTrees);
        return authInfo;
    }

    @Override
    public String getAuthorityList(AuthInfo userInfo) {
        String authority = "";
        if (CollUtil.isNotEmpty(userInfo.getRoleDtos())){
            String roleKeys = userInfo.getRoleDtos().stream().map(r -> "ROLE_"+r.getRoleKey()).collect(Collectors.joining(","));
            authority = roleKeys;
        }
        if (CollUtil.isNotEmpty(userInfo.getResourceDtos())){
            String resourceKeys = userInfo.getResourceDtos().stream().map(resource -> resource.getSourceKey()).collect(Collectors.joining(","));
            authority=authority.concat(",").concat(resourceKeys);
        }
        return authority;
    }

    @Override
    public List<UserInfoDto> queryUserInfo() {

        return userInfoMapper.queryUserInfo();
    }

    @Override
    public boolean createUserInfo(UserInfoCreateBean bean) {
        if (findByUserName(bean.getUsername())!=null){
            return false;
        }
        UserInfo userInfo1 = baseMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getPhone, bean.getPhone()));
        if (userInfo1!=null){
            return false;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(bean.getUsername());
        userInfo.setNickName(bean.getNickName());
        userInfo.setRealName(bean.getRealName());
        userInfo.setPassword(passwordEncoder.encode(bean.getPassword()));
        userInfo.setPhone(bean.getPhone());
        userInfo.setSex(bean.getSex());
        userInfo.setStatus(bean.getStatus());
        userInfo.setCreateUserId(bean.getCreateUserId());
        userInfo.setCreateUserName(bean.getCreateUserName());
        userInfo.setCreateTime(bean.getCreateTime());
        if (save(userInfo)){
            userInfoMapper.createUserRelationRole(userInfo.getId(),bean.getRoleIds());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserInfo(UserInfoCreateBean bean) {
        if (getById(bean.getId())==null){
            return false;
        }
        UserInfo userInfo1 = getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, bean.getUsername()).ne(UserInfo::getId, bean.getId()));
        if (userInfo1!=null){
            return false;
        }
        userInfo1 = baseMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getPhone, bean.getPhone()).ne(UserInfo::getId, bean.getId()));
        if (userInfo1!=null){
            return false;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(bean.getId());
        userInfo.setUsername(bean.getUsername());
        userInfo.setNickName(bean.getNickName());
        userInfo.setRealName(bean.getRealName());
        userInfo.setPassword(passwordEncoder.encode(bean.getPassword()));
        userInfo.setPhone(bean.getPhone());
        userInfo.setSex(bean.getSex());
        userInfo.setStatus(bean.getStatus());
        userInfo.setUpdateUserId(bean.getUpdateUserId());
        userInfo.setUpdateUserName(bean.getUpdateUserName());
        userInfo.setUpdateTime(bean.getUpdateTime());
        if (updateById(userInfo)){
            userInfoMapper.removeUserRelationRole(bean.getId());
            userInfoMapper.createUserRelationRole(bean.getId(),bean.getRoleIds());
            return true;
        }
        AuthInfo authInfo = queryByUsername(bean.getUsername());
        //这里需要更新缓存在redis中的用户信息
        redisTemplate.opsForHash().put(RedisKey.USER_INFO,authInfo.getUsername(),authInfo);
        return false;
    }

    @Override
    public boolean deleteUserInfo(Long id) {
        UserInfo user = getById(id);
        if (user==null){
            return false;
        }
        userInfoMapper.removeUserRelationRole(id);
        //删除redis中的缓存
        redisTemplate.opsForHash().delete(RedisKey.USER_INFO,user);
        return removeById(id);
    }

    @Override
    public UserInfoDto queryUserById(Long id) {
        return userInfoMapper.queryUserById(id);
    }

    /**
     *角色改变时更新缓存信息
     * @param roleId
     */
    @Override
    public void updteByRoleId(Long roleId) {
        List<AuthInfo> authInfos = userInfoMapper.updteByRoleId(roleId);

        for (AuthInfo authInfo: authInfos
             ) {
            Set<Long> roleIds = new HashSet<>();
            for (SysRoleDto  role:authInfo.getRoleDtos()
            ) {
                roleIds.add(role.getId());
            }
            //这里需要获取所有角色的资源，去重
            Set<SysResourceDto> sysResourceDtos = roleService.getResourceDtos(roleIds);

            //权限信息
            authInfo.setResourceDtos(sysResourceDtos);
            authInfo.setAuthorities(getAuthorityList(authInfo));

            //将这些资源组成一颗树
            List<SysResourceTree> sysResourceTrees = resourceService.combinationTree(sysResourceDtos);
            authInfo.setResourceTrees(sysResourceTrees);

            redisTemplate.opsForHash().put(RedisKey.USER_INFO,authInfo.getUsername(),authInfo);
        }

    }
}
