package com.wjc.controller;

import com.wjc.enetity.system.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 王建成
 * @date 2022/4/6--11:09
 */

public class BaseController {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

   public UserInfo getUserInfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       UserInfo userinfo = (UserInfo) redisTemplate.opsForHash().get("userinfo", principal);
       return userinfo;
   }
}
