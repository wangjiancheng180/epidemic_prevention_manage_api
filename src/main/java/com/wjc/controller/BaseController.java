package com.wjc.controller;

import com.wjc.common.login.RedisKey;
import com.wjc.dto.system.UserInfoDto;
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

   public UserInfoDto getUserInfo(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       UserInfoDto userinfo = (UserInfoDto) redisTemplate.opsForHash().get(RedisKey.USER_INFO, principal);
       return userinfo;
   }
}
