package com.wjc.controller.system;

import com.wjc.Dto.system.SysRoleDto;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.enetity.system.Role;
import com.wjc.enetity.system.UserInfo;
import com.wjc.param.system.SysRoleCreateBean;
import com.wjc.param.system.SysRoleUpdateBean;
import com.wjc.service.system.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/12--9:49
 */
@RestController
@Api("角色管理")
@ApiOperation("角色管理")
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/queryRoleList")
    @ApiOperation("获取角色集合")
    public JsonResult<List<Role>> queryRoleList(){
        return JsonResult.success(roleService.queryRoleList());
    }

    @PostMapping("/createRole")
    @ApiOperation("创建角色")
    public JsonResult<Long> createRole(@RequestBody SysRoleCreateBean bean){
        UserInfo userInfo = getUserInfo();
        bean.setCreateUserId(userInfo.getId());
        bean.setCreateUserName(userInfo.getRealName());
        bean.setCreateTime(new Date(System.currentTimeMillis()));
        return JsonResult.success(roleService.createRole(bean));
    }

    @DeleteMapping("/deleteRole")
    public JsonResult<Boolean> deleteRole(@RequestParam("id") Long id){
        return JsonResult.success(roleService.deleteRole(id));
    }

    @PostMapping("/updateRole")
    @ApiOperation("更新角色")
    public JsonResult<Boolean> updateRole(@RequestBody  SysRoleUpdateBean bean){
        UserInfo userInfo = getUserInfo();
        bean.setUpdateUserId(userInfo.getId());
        bean.setUpdateUserName(userInfo.getRealName());
        bean.setUpdateTime(new Date(System.currentTimeMillis()));
        return JsonResult.success(roleService.updateRole(bean));
    }

    @GetMapping("/queryRoleById")
    @ApiOperation("通过id查找角色")
    public JsonResult<SysRoleDto> queryRoleById(@RequestParam("id") Long id){
        return JsonResult.success(roleService.queryRoleById(id));

    }

}
