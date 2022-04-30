package com.wjc.service.university;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.dto.university.StudentDto;
import com.wjc.param.university.StudentCreateBean;
import com.wjc.param.university.StudentQueryBean;


import java.util.List;

/**
* @author 王建成
* @date 2022/4/24--23:01
*/     
public interface StudentService{


    List<StudentDto> queryStudentList();

    IPage<StudentDto> queryStudentPage(StudentQueryBean bean);

    StudentDto queryStudentById(Long id);

    boolean createStudent(StudentCreateBean bean);

    boolean updateStudent(StudentCreateBean bean);

    boolean deleteStudent(Long id);
}
