package com.wjc.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.dto.system.AuthInfo;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.enetity.system.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* @author 王建成
* @date 2022/3/16--22:37
*/
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo findByUserName(@Param("userName") String userName);

    AuthInfo queryByUsername(@Param("userName") String username);

    List<UserInfoDto> queryUserInfo();

    void createUserRelationRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    void removeUserRelationRole(@Param("userId") Long userId);

    UserInfoDto queryUserById(@Param("id") Long id);

    List<AuthInfo> updteByRoleId(@Param("roleId") Long roleId);
}