package com.wjc.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.dto.system.AuthInfo;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.enetity.system.UserInfo;
import com.wjc.param.system.UserInfoCreateBean;
import com.wjc.param.system.UserInfoQueryBean;
import com.wjc.service.system.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/17--22:36
 */
@Api(tags = "用户管理")
@ApiOperation("用户管理")
@RequestMapping("/user")
@RestController
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;


    @GetMapping("/page")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryUser')")
    public JsonResult<IPage<UserInfo>> pageList(@RequestBody UserInfoQueryBean bean){
        return JsonResult.success(userInfoService.pageList(bean));
    }

    @GetMapping("/queryByUsername")
    @ApiOperation("根据用户名查找")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryUser')")
    public JsonResult<AuthInfo> queryByUsername(@RequestParam String username){
        return JsonResult.success(userInfoService.queryByUsername(username));
    }

    @GetMapping("/queryUserInfo")
    @ApiOperation("用户列表")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryUser')")
    public JsonResult<List<UserInfoDto>> queryUserInfo(){
        return JsonResult.success(userInfoService.queryUserInfo());
    }

    @PostMapping("/createUserInfo")
    @ApiOperation("创建用户")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('createUser')")
    public JsonResult<Boolean> createUserInfo(@RequestBody UserInfoCreateBean bean){
        setCreate(bean);
        boolean flag = userInfoService.createUserInfo(bean);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("用户名或手机号重复！",false);
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation("更新用户")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('updateUser')")
    public JsonResult<Boolean> updateUserInfo(@RequestBody UserInfoCreateBean bean){
        boolean flag = userInfoService.updateUserInfo(bean);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("不存在该用户或用户名手机号重复！",false);
    }

    @DeleteMapping("/deleteUserInfo")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('deleteUser')")
    @ApiOperation("删除用户")
    public JsonResult<Boolean> deleteUserInfo(@RequestParam("id") Long id){
        boolean flag = userInfoService.deleteUserInfo(id);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("不存在该用户！",false);
    }

    @GetMapping("/queryUserById")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryUser')")
    @ApiOperation("通过id查询用户")
    public JsonResult<UserInfoDto> querUserById(@RequestParam("id") Long id){
        return JsonResult.success(userInfoService.queryUserById(id));
    }

}
