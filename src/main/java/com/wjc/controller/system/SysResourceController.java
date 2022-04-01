package com.wjc.controller.system;

import com.wjc.Dto.system.SysResourceTree;
import com.wjc.common.JsonResult;
import com.wjc.enetity.system.SysResource;
import com.wjc.service.system.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/1--15:25
 */
@RestController
@Api(tags = "资源管理")
@ApiOperation("系统资源的管理")
@RequestMapping("/resource")
public class SysResourceController {

    @Autowired
    private SysResourceService  resourceService;

    @GetMapping("/queryResourceTree")
    @ApiOperation("获取资源树")
    public JsonResult<List<SysResourceTree>> queryResourceTree(){

        return JsonResult.success(resourceService.queryResourceTree());
    }
}
