package com.wjc.mapper.university;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.dto.university.StudentDto;
import com.wjc.enetity.university.Student;
import com.wjc.param.university.StudentQueryBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 王建成
* @date 2022/4/24--23:01
*/     
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 查询所有学生信息
     * @return
     */
    List<StudentDto> queryStudentList();

    /**
     * 分页查询
     * @param studentDtos
     * @param pageInfo
     * @return
     */
    IPage<StudentDto> queryStudentPage(@Param("studentDtos") List<StudentDto> studentDtos, IPage<StudentDto> pageInfo);

    /**
     * 查询学生通过id
     * @param id
     * @return
     */
    StudentDto queryStudentById(@Param("id") Long id);

    /**
     * 添加学生和班级的联系
     * @param studentId
     * @param clazzId
     */
    void addRelationClazz(@Param("studentId") Long studentId, @Param("clazzId") Long clazzId);

    /**
     * 删除学生和班级的联系
     * @param id
     */
    void deleteRelationClazz(@Param("studentId") Long id);

    /**
     * 分页筛选请求
     * @param bean
     * @return
     */
    List<StudentDto> queryStudentPageF(@Param("bean") StudentQueryBean bean);

}