package com.wjc.service.impl;

import com.wjc.common.login.LoginUser;
import com.wjc.enetity.UserInfo;
import com.wjc.service.UserInfoService;
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       //通过用户名查找用户
        UserInfo user = userInfoService.findByUserName(userName);
        if (user==null){
            //用户为空的话直接返回空值
            return null;
        }
        //将查找的用户封装到LoginUser类中
        return new LoginUser(user);
    }
}
