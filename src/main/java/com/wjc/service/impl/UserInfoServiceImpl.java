package com.wjc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.enetity.UserInfo;
import com.wjc.mapper.UserInfoMapper;
import com.wjc.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 王建成
 * @date 2022/3/17--13:29
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo findByUserName(String userName) {
        return userInfoMapper.findByUserName(userName);
    }
}