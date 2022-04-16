package com.wjc.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.Dto.system.SysRoleDto;
import com.wjc.enetity.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 王建成
* @date 2022/3/17--11:25
*/     
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 添加资源和角色关联
     * @param roleId
     * @param resourceIds
     * @return
     */
    int contactResourceIds(@Param("roleId") Long roleId, @Param("resourceIds")List<Long> resourceIds);

    /**
     * 删除所有和角色关联的资源
     * @param roleId
     */
    void removeRoleContactResource(@Param("roleId") Long roleId);

    /**
     * 通过角色id查找角色详细信息
     * @param roleId
     * @return
     */
    SysRoleDto queryRoleById(@Param("roleId") Long roleId);
}