package com.wjc.controller.system;

import cn.hutool.core.date.DateUtil;
import com.wjc.Dto.system.SysResourceTree;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.enetity.system.SysResource;
import com.wjc.enetity.system.UserInfo;
import com.wjc.param.system.SysResourceCreateBean;
import com.wjc.service.system.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
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
    @ApiOperation("获取资源树")
    public JsonResult<List<SysResourceTree>> queryResourceTree(){

        return JsonResult.success(resourceService.queryResourceTree());
    }

    @PostMapping("/createResource")
    @ApiOperation("新建资源")
    public JsonResult<Long> createResource(@RequestBody SysResourceCreateBean bean){
        UserInfo userInfo = getUserInfo();
        bean.setCreateUserId(userInfo.getId());
        bean.setCreateUserName(userInfo.getRealName());
        bean.setCreateTime(new Date(System.currentTimeMillis()));
        return JsonResult.success(resourceService.createResource(bean));
    }
}
