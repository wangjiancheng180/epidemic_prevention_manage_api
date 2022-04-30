package com.wjc.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.enetity.system.UserInfo;
import com.wjc.param.system.UserInfoQueryBean;

/**
 * @author 王建成
 * @date 2022/3/17--13:28
 */
public interface UserInfoService extends IService<UserInfo> {
    UserInfo findByUserName(String userName);

    IPage<UserInfo> pageList(UserInfoQueryBean bean);

    UserInfoDto queryByUsername(String username);
}
