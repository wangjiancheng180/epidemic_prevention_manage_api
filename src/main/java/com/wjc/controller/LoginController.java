package com.wjc.controller;

import com.wjc.common.JsonResult;
import com.wjc.enetity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 王建成
 * @date 2022/3/17--9:07
 */
@RestController
@Api(tags = "用户登录")
@ApiOperation("所有用户的登录接口")
public class LoginController {

    @PostMapping("login")
    @ApiOperation("登录接口")
    public JsonResult<UserInfo> Login(@ApiParam("用户名")@RequestParam("username") String username, @ApiParam("密码") @RequestParam("password")String password){
        Date date =new Date();
        return JsonResult.success( 200, new UserInfo(1,"ceshi","测试","测试","1213","1213",1,date,date,null));

    }

    @GetMapping("test")
    @ApiOperation("测试接口")
    public JsonResult test(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return JsonResult.success(bCryptPasswordEncoder.encode("123456"));

    }

}
