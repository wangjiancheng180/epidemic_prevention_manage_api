package com.wjc.controller.system;

import com.wjc.dto.system.SysRoleDto;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;

import com.wjc.enetity.system.Role;
import com.wjc.param.system.SysRoleCreateBean;
import com.wjc.param.system.SysRoleUpdateBean;
import com.wjc.service.system.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/12--9:49
 */
@RestController
@Api(tags = "角色管理")
@ApiOperation("角色管理")
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/queryRoleList")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryRole')")
    @ApiOperation("获取角色集合")
    public JsonResult<List<Role>> queryRoleList(){
        return JsonResult.success(roleService.queryRoleList());
    }

    @PostMapping("/createRole")
    @PreAuthorize("hasRole('super_admin')OR hasAuthority('createRole')")
    @ApiOperation("创建角色")
    public JsonResult<Long> createRole(@RequestBody SysRoleCreateBean bean){
        setCreate(bean);
        return JsonResult.success(roleService.createRole(bean));
    }

    @DeleteMapping("/deleteRole")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('deleteRole')")
    public JsonResult<Boolean> deleteRole(@RequestParam("id") Long id){
        boolean flag = roleService.deleteRole(id);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("该角色还有关联的用户无法删除！",false);
    }

    @PostMapping("/updateRole")
    @PreAuthorize("hasRole('super_admin')OR hasAuthority('updateRole')")
    @ApiOperation("更新角色")
    public JsonResult<Boolean> updateRole(@RequestBody  SysRoleUpdateBean bean){
        setUpdate(bean);
        return JsonResult.success(roleService.updateRole(bean));
    }

    @GetMapping("/queryRoleById")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryRole')")
    @ApiOperation("通过id查找角色")
    public JsonResult<SysRoleDto> queryRoleById(@RequestParam("id") Long id){
        return JsonResult.success(roleService.queryRoleById(id));

    }

}
