package com.wjc.controller.university;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.dto.university.StudentDto;
import com.wjc.param.university.StudentCreateBean;
import com.wjc.param.university.StudentQueryBean;
import com.wjc.service.university.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/28--11:00
 */

@RestController
@RequestMapping("/student")
@Api("学生信息")
@ApiOperation("学生信息")
public class StudentController extends BaseController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/queryStudentList")
    @ApiOperation("学生信息集合")
    public JsonResult<List<StudentDto>> queryStudentList(){
        return JsonResult.success(studentService.queryStudentList());
    }

    @PostMapping("/queryStudentPage")
    @ApiOperation("分页查询")
    public JsonResult<IPage<StudentDto>> queryStudentPage(@RequestBody StudentQueryBean bean){
        return JsonResult.success(studentService.queryStudentPage(bean));
    }


    @GetMapping("/queryStudentById")
    @ApiOperation("查询学生通过Id")
    public JsonResult<StudentDto> queryStudentById(@RequestParam("id") Long id){
        return JsonResult.success(studentService.queryStudentById(id));
    }

    @PostMapping("/createStudent")
    @ApiOperation("新建学生")
    public JsonResult<Boolean> createStudent(@RequestBody StudentCreateBean bean){
        UserInfoDto userInfo = getUserInfo();
        bean.setCreateUserId(userInfo.getId());
        bean.setCreateUserName(userInfo.getRealName());
        bean.setCreateTime(new Date(System.currentTimeMillis()));
        return JsonResult.success(studentService.createStudent(bean));
    }

    @PostMapping("/updateStudent")
    @ApiOperation("更新学生")
    public JsonResult<Boolean> updateStudent(@RequestBody StudentCreateBean bean){
        UserInfoDto userInfo = getUserInfo();
        bean.setUpdateUserId(userInfo.getId());
        bean.setUpdateUserName(userInfo.getRealName());
        bean.setUpdateTime(new Date(System.currentTimeMillis()));
        return JsonResult.success(studentService.updateStudent(bean));
    }

    @DeleteMapping("/deleteStudent")
    @ApiOperation("删除学生")
    public JsonResult<Boolean> deleteStudent(@RequestParam("id") Long id){
        return JsonResult.success(studentService.deleteStudent(id));
    }

//    @DeleteMapping("/deleteStudentByIds")
//    @ApiOperation("批量删除学生")
//    public JsonResult<Boolean> deleteStudentByIds(@RequestBody Set<Long> ids){
//
//    }
}
