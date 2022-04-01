package com.wjc.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjc.enetity.system.UserInfo;

/**
 * @author 王建成
 * @date 2022/3/17--13:28
 */
public interface UserInfoService extends IService<UserInfo> {
    UserInfo findByUserName(String userName);
}
