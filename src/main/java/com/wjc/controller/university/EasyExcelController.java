package com.wjc.controller.university;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.wjc.controller.BaseController;
import com.wjc.excel.converter.university.CustomMergeStrategy;
import com.wjc.excel.data.university.StudentData;
import com.wjc.excel.listener.StudentDataListener;
import com.wjc.param.university.StudentQueryBean;
import com.wjc.service.university.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/19--11:03
 */
@RequestMapping("/easyExcel")
@Api("excel")
@ApiOperation("excel表导入导出")
@Controller
@Slf4j
public class EasyExcelController extends BaseController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/exportStudentList")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('exportStudent')")
    @ApiOperation("学生列表导出")
    @SneakyThrows
    public void exportStudentList(HttpServletResponse response, @RequestBody StudentQueryBean bean){
        List<StudentData> studentDataList = studentService.getStudentData(bean);
        setExcelRespProp(response,"学生列表");
        EasyExcel.write(response.getOutputStream())
                .head(StudentData.class)
                .registerWriteHandler(new CustomMergeStrategy(StudentData.class))
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("学生列表")
                .doWrite(studentDataList);
    }

    @PostMapping("importStudentexcel")
    @ApiOperation("学生列表导入")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('importStudent')")
    @ResponseBody
    public boolean importStudentexcel(@RequestPart("file") MultipartFile file){
        try{
            EasyExcel.read(file.getInputStream(),StudentData.class,new StudentDataListener(studentService)).sheet().doRead();
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return false;
    }
}
