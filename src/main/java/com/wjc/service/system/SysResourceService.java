package com.wjc.service.system;

import com.wjc.Dto.system.SysResourceDto;
import com.wjc.Dto.system.SysResourceTree;
import com.wjc.enetity.system.SysResource;
import com.wjc.param.system.SysResourceCreateBean;
import com.wjc.param.system.SysResourceUpdateBean;

import java.util.List;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
public interface SysResourceService{


    List<SysResourceTree> queryResourceTree();

    Long createResource(SysResourceCreateBean bean);

    SysResourceDto queryResourceById(Long id);

    boolean updateResource(SysResourceUpdateBean bean);

    void findParentIds(Long id, List<SysResource> list, List<Long> parentIds);

    List<SysResource> resourceList();

    SysResourceTree toTree(SysResourceDto sysResourceDto);

    SysResourceTree toTree(SysResource resource);

    void combinationTree(List<SysResourceTree> resourceTrees, List<SysResourceDto> resourceDtos);

    boolean deleteResource(Long id);
}
