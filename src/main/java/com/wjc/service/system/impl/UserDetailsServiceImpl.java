package com.wjc.service.system.impl;

import com.wjc.common.login.LoginUser;
import com.wjc.enetity.system.UserInfo;

import com.wjc.service.system.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 王建成
 * @date 2022/3/17--13:22
 * 用用于sercurity框架认证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserInfoService userInfoService;


    @Override
    public UserDetails loadUserByUsername(String userName){
       //通过用户名查找用户
        UserInfo user = userInfoService.findByUserName(userName);
        LoginUser loginUser = new LoginUser();
        if (user==null){
            //用户为为空直接抛出异常
            throw new UsernameNotFoundException("用户名未找到");

        }else {
            //将查找的用户封装到LoginUser类中
            loginUser.setUserInfo(user);
        }
        return loginUser;
    }
}
