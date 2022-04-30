package com.wjc.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.enetity.system.UserInfo;
import com.wjc.param.system.UserInfoQueryBean;
import com.wjc.service.system.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JsonResult<IPage<UserInfo>> pageList(@RequestBody UserInfoQueryBean bean){
        return JsonResult.success(userInfoService.pageList(bean));
    }

    @GetMapping("/queryByUsername")
    public JsonResult<UserInfoDto> queryByUsername(@RequestParam String username){
        return JsonResult.success(userInfoService.queryByUsername(username));
    }
}
