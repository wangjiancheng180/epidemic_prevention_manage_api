package com.wjc.service.system;

import com.wjc.Dto.system.SysRoleDto;
import com.wjc.enetity.system.Role;
import com.wjc.param.system.SysRoleCreateBean;
import com.wjc.param.system.SysRoleUpdateBean;

import java.util.List;

/**
* @author 王建成
* @date 2022/3/17--11:25
*/     
public interface RoleService {

    List<Role> queryRoleList();

    Long createRole(SysRoleCreateBean bean);

    boolean updateRole(SysRoleUpdateBean bean);

    SysRoleDto queryRoleById(Long id);

    boolean deleteRole(Long id);
}
