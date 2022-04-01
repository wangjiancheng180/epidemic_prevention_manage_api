package com.wjc.service.system;

import com.wjc.Dto.system.SysResourceTree;
import com.wjc.enetity.system.SysResource;

import java.util.List;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
public interface SysResourceService{


    List<SysResourceTree> queryResourceTree();
    }
