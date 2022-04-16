package com.wjc.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.enetity.system.SysResource;
import org.apache.ibatis.annotations.Param;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
public interface SysResourceMapper extends BaseMapper<SysResource> {

    /**
     * 删除角色与资源的关联，通过资源id删除
     * @param resourceId
     */
    void removeRoleContactResource(@Param("resource_id") Long resourceId);
}