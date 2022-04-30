package com.wjc.controller.university;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.dto.university.ClazzDto;
import com.wjc.param.university.ClazzCreateBean;
import com.wjc.param.university.ClazzQueryBean;
import com.wjc.service.university.ClazzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/27--22:17
 */
@RestController
@RequestMapping("/clazz")
@Api("班级信息")
@ApiOperation("班级管理")
public class ClazzController extends BaseController {

    @Autowired
    private ClazzService clazzService;

    @GetMapping("/queryClazzList")
    @ApiOperation("班级集合")
    public JsonResult<List<ClazzDto>> queryClazzList(){
        return JsonResult.success(clazzService.queryClazzList());
    }


    @PostMapping("/queryClazzPage")
    @ApiOperation("分页查询")
    public JsonResult<IPage<ClazzDto>>queryClazzPage(@RequestBody ClazzQueryBean bean){
        return JsonResult.success(clazzService.queryClazzPage(bean));
    }


    @GetMapping("/queryClazzById")
    @ApiOperation("查询班级通过id")
    public JsonResult<ClazzDto> queryClazzById(@RequestParam("id") Long id){
        return JsonResult.success(clazzService.queryClazzById(id));
    }

    @PostMapping("/createClazz")
    @ApiOperation("创建班级")
    public JsonResult<Boolean> createClazz(@RequestBody ClazzCreateBean bean){
        UserInfoDto userInfo = getUserInfo();
        bean.setCreateUserId(userInfo.getId());
        bean.setCreateUserName(userInfo.getRealName());
        bean.setCreateTime(new Date(System.currentTimeMillis()));
        return JsonResult.success(clazzService.createClazz(bean));
    }

    @PostMapping("/updateClazz")
    @ApiOperation("更新班级")
    public JsonResult<Boolean> updateClazz(@RequestBody ClazzCreateBean bean){
        UserInfoDto userInfo = getUserInfo();
        bean.setUpdateUserId(userInfo.getId());
        bean.setUpdateUserName(userInfo.getRealName());
        bean.setUpdateTime(new Date(System.currentTimeMillis()));
        return JsonResult.success(clazzService.updateClazz(bean));
    }

    @DeleteMapping("/deleteClazz")
    @ApiOperation("删除班级")
    public JsonResult<Boolean> deleteClazz(@RequestParam("id") Long id){
        boolean flag = clazzService.deleteClazz(id);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("班级下还有关联的学生，无法直接删除！",false);
    }

}
