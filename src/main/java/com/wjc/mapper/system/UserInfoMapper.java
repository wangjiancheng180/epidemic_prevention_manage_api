package com.wjc.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.enetity.system.UserInfo;
import org.apache.ibatis.annotations.Param;


/**
* @author 王建成
* @date 2022/3/16--22:37
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo findByUserName(@Param("userName") String userName);
}