package com.wjc.service.university.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.dto.university.StudentDto;
import com.wjc.param.university.StudentCreateBean;
import com.wjc.param.university.StudentQueryBean;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.wjc.enetity.university.Student;
import com.wjc.mapper.university.StudentMapper;
import com.wjc.service.university.StudentService;

import java.util.ArrayList;
import java.util.List;

/**
* @author 王建成
* @date 2022/4/24--23:01
*/     
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper,Student> implements StudentService{

    @Resource
    private StudentMapper studentMapper;


    @Override
    public List<StudentDto> queryStudentList() {

        List<StudentDto> studentDtos = studentMapper.queryStudentList();

        return studentDtos;
    }

    @Override
    public IPage<StudentDto> queryStudentPage(StudentQueryBean bean) {
        IPage<StudentDto> pageInfo = new Page<>(bean.getPage(),bean.getSize());
        //TODO:继续曲线救国
        List<StudentDto> studentDtos = studentMapper.queryStudentPageF(bean);
        if (CollUtil.isEmpty(studentDtos)){
            //筛选出来是空值的话直接返回
            return pageInfo;
        }
        IPage<StudentDto> studentPage = studentMapper.queryStudentPage(studentDtos, pageInfo);

        for (StudentDto record:studentPage.getRecords()
             ) {
            for (StudentDto studentDto: studentDtos
                 ) {
                if(record.getId().equals(studentDto.getId())){
                    record.setClazzDto(studentDto.getClazzDto());
                    record.setCollegeDtos(studentDto.getCollegeDtos());
                    break;
                }
            }
        }
        return studentPage;
    }

    @Override
    public StudentDto queryStudentById(Long id) {
        return studentMapper.queryStudentById(id);
    }

    @Override
    public boolean createStudent(StudentCreateBean bean) {
        Student student = new Student();
        student.setStudentNo(bean.getStudentNo());
        student.setName(bean.getName());
        student.setSex(bean.getSex());
        student.setAge(bean.getAge());
        student.setVaccinationTimes(bean.getVaccinationTimes());
        student.setTemporaryHome(bean.getTemporaryHome());
        student.setHometown(bean.getHometown());
        student.setAccount(bean.getAccount());
        student.setPassword(bean.getPassword());
        student.setEntrance(bean.getEntrance());
        student.setBirthday(bean.getBirthday());
        student.setCreateUserId(bean.getCreateUserId());
        student.setCreateUserName(bean.getCreateUserName());
        student.setCreateTime(bean.getCreateTime());
        if(save(student)){
            //添加学生与班级的联系
            studentMapper.addRelationClazz(student.getId(),bean.getClazzId());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateStudent(StudentCreateBean bean) {
        Student student = new Student();
        student.setId(bean.getId());
        student.setStudentNo(bean.getStudentNo());
        student.setName(bean.getName());
        student.setSex(bean.getSex());
        student.setAge(bean.getAge());
        student.setVaccinationTimes(bean.getVaccinationTimes());
        student.setTemporaryHome(bean.getTemporaryHome());
        student.setHometown(bean.getHometown());
        student.setAccount(bean.getAccount());
        student.setPassword(bean.getPassword());
        student.setEntrance(bean.getEntrance());
        student.setBirthday(bean.getBirthday());
        student.setUpdateUserId(bean.getUpdateUserId());
        student.setUpdateUserName(bean.getUpdateUserName());
        student.setUpdateTime(bean.getUpdateTime());
        if (updateById(student)){
            //先删除所关联的班级
            studentMapper.deleteRelationClazz(student.getId());
            //再添加新的关联班级
            studentMapper.addRelationClazz(student.getId(),bean.getClazzId());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteStudent(Long id) {
        StudentDto studentDto = queryStudentById(id);
        //先要判断是否存在改学生
        if(studentDto==null){
            return false;
        }
        //先删除学生与班级的联系
        studentMapper.deleteRelationClazz(id);
        return removeById(id);

    }
}
