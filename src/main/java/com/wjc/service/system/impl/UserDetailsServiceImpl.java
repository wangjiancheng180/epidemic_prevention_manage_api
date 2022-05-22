package com.wjc.service.system.impl;

import com.wjc.common.login.LoginUser;
import com.wjc.common.login.RedisKey;
import com.wjc.dto.system.AuthInfo;

import com.wjc.service.system.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String userName){
        AuthInfo authInfo = (AuthInfo) redisTemplate.opsForHash().get(RedisKey.USER_INFO, userName);
        if (authInfo==null){
            //通过用户名查找用户
            authInfo = userInfoService.queryByUsername(userName);
            if (authInfo!=null){
                //将用户信息放入redis
                redisTemplate.opsForHash().put(RedisKey.USER_INFO,authInfo.getUsername(),authInfo);
            }
        }

        LoginUser loginUser = new LoginUser();
        if (authInfo==null){
            //用户为为空直接抛出异常
            throw new UsernameNotFoundException("用户名未找到");

        }else {

            //将查找的用户封装到LoginUser类中
            loginUser.setUserInfo(authInfo);
        }
        return loginUser;
    }
}
