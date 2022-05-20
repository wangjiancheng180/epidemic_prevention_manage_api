package com.wjc.controller.system;

import com.wjc.dto.system.SysResourceDto;
import com.wjc.dto.system.SysResourceTree;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.param.system.SysResourceCreateBean;
import com.wjc.param.system.SysResourceUpdateBean;
import com.wjc.service.system.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/1--15:25
 */
@RestController
@Api(tags = "资源管理")
@ApiOperation("系统资源的管理")
@RequestMapping("/resource")
public class SysResourceController extends BaseController {

    @Autowired
    private SysResourceService  resourceService;


    @GetMapping("/queryResourceTree")
    @PreAuthorize("hasRole('super_admin')||hasAuthority('queryResource')")
    @ApiOperation("获取资源树")
    public JsonResult<List<SysResourceTree>> queryResourceTree(){

        return JsonResult.success(resourceService.queryResourceTree());
    }

    @PreAuthorize("hasRole('super_admin')||hasAuthority('createResource')")
    @PostMapping("/createResource")
    @ApiOperation("新建资源")
    public JsonResult<Long> createResource(@RequestBody SysResourceCreateBean bean){
        setCreate(bean);
        return JsonResult.success(resourceService.createResource(bean));
    }

    @PreAuthorize("hasRole('super_admin')||hasAuthority('deleteResource')")
    @DeleteMapping("/deleteResource")
    public JsonResult<Boolean> deleteResource(@RequestParam("id") Long id){
        boolean result = resourceService.deleteResource(id);
        if (result){
            return JsonResult.success(true);
        }
        return JsonResult.failure("请先删除此资源的子资源！",false);
    }

    @GetMapping("/queryResourceById")
    @PreAuthorize("hasRole('super_admin')|| hasAuthority('queryResource')" )
    @ApiOperation("查找资源")
    public JsonResult<SysResourceDto> queryResourceById(@RequestParam("id") Long id){
        return JsonResult.success(resourceService.queryResourceById(id));
    }

    @PostMapping("/updateResource")
    @PreAuthorize("hasRole('super_admin')||hasAuthority('updateResource')")
    @ApiOperation("更新资源")
    public JsonResult<Boolean> updateResource(@RequestBody SysResourceUpdateBean bean){
        setUpdate(bean);
        return JsonResult.success(resourceService.updateResource(bean));
    }
}
