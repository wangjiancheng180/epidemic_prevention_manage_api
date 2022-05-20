package com.wjc.controller.university;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;

import com.wjc.dto.university.StudentDto;
import com.wjc.param.university.StudentCreateBean;
import com.wjc.param.university.StudentQueryBean;
import com.wjc.service.university.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
        setCreate(bean);
        boolean flag = studentService.createStudent(bean);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("学号重复！",false);

    }

    @PostMapping("/updateStudent")
    @ApiOperation("更新学生")
    public JsonResult<Boolean> updateStudent(@RequestBody StudentCreateBean bean){
        setUpdate(bean);
        boolean flag = studentService.updateStudent(bean);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("不存在该学生或学号重复！",false);
    }

    @DeleteMapping("/deleteStudent")
    @ApiOperation("删除学生")
    public JsonResult<Boolean> deleteStudent(@RequestParam("id") Long id){
        boolean flag = studentService.deleteStudent(id);
        if(flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("不存在该学生！",false);
    }

//    @DeleteMapping("/deleteStudentByIds")
//    @ApiOperation("批量删除学生")
//    public JsonResult<Boolean> deleteStudentByIds(@RequestBody Set<Long> ids){
//
//    }

}
