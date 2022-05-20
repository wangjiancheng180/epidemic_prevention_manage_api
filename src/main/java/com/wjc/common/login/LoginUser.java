package com.wjc.common.login;

import cn.hutool.system.UserInfo;
import com.wjc.dto.system.SysRoleDto;
import com.wjc.dto.system.AuthInfo;
import com.wjc.enetity.system.Role;
import com.wjc.enetity.system.SysResource;
import com.wjc.service.system.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王建成
 * @date 2022/3/17--10:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoginUser implements UserDetails {

    private AuthInfo userInfo;



    /**
     * 返回该账号下的所有权限信息
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (SysRoleDto role:userInfo.getRoleDtos()){
//            //每个权限标识前面要有ROLE_
//            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleKey()));
//        }

        return AuthorityUtils.commaSeparatedStringToAuthorityList(userInfo.getAuthorities());
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    /**
     * 账号没有过期(默认返回true）
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号没有被锁定(默认返回true）
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码没有过期(默认返回true）
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号可用(默认返回true）
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
