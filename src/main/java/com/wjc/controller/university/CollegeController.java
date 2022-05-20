package com.wjc.controller.university;

import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;

import com.wjc.dto.university.CollegeDto;
import com.wjc.dto.university.CollegeTree;
import com.wjc.param.university.CollegeCreateBean;
import com.wjc.service.university.CollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/27--8:54
 */
@RestController
@RequestMapping("/college")
@Api("学院信息")
@ApiOperation("学院管理")
public class CollegeController extends BaseController {

    @Autowired
    private CollegeService collegeService;

    @GetMapping("/queryCollegeTree")
    @ApiOperation("获取学院树")
    public JsonResult<List<CollegeTree>> queryCollegeTree(){
        return JsonResult.success(collegeService.queryCollegeTree());
    }

    @GetMapping("/queryCollegeById")
    @ApiOperation("查询学院通过Id")
    public JsonResult<CollegeDto> queryCollegeById(@RequestParam("id") Long id){
        return JsonResult.success(collegeService.queryCollegeById(id));
    }

    @PostMapping("/createCollege")
    @ApiOperation("新建学院")
    public JsonResult<Long> createCollege(@RequestBody CollegeCreateBean bean){
        setCreate(bean);
        return JsonResult.success(collegeService.createCollege(bean));
    }

    @PostMapping("/updateCollege")
    @ApiOperation("修改学院")
    public JsonResult<Boolean> updateCollege(@RequestBody CollegeCreateBean  bean){
        setUpdate(bean);
        return JsonResult.success(collegeService.updateCollege(bean));
    }

    @DeleteMapping("/deleteCollege")
    @ApiOperation("删除学院")
    public JsonResult<Boolean> deleteCollege(@RequestParam("id") Long id){
        boolean flag = collegeService.deleteCollege(id);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("不存在该学院或学院下还有关联班级或者分院无法直接删除！",false);
    }





}
