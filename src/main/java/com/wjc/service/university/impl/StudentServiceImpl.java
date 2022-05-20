package com.wjc.service.university.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.common.login.RedisKey;
import com.wjc.dto.system.AuthInfo;
import com.wjc.dto.university.StudentDto;
import com.wjc.excel.data.university.StudentData;
import com.wjc.mapper.university.ClazzMapper;
import com.wjc.param.university.StudentCreateBean;
import com.wjc.param.university.StudentQueryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.wjc.enetity.university.Student;
import com.wjc.mapper.university.StudentMapper;
import com.wjc.service.university.StudentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author 王建成
* @date 2022/4/24--23:01
*/     
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper,Student> implements StudentService{

    @Resource
    private StudentMapper studentMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

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
        Student student1 = getOne(new LambdaQueryWrapper<Student>().eq(Student::getStudentNo, bean.getStudentNo()));
        if (student1!=null){
            return false;
        }
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
        if (getById(bean.getId())==null){
            return false;
        }
        Student student1 = getOne(new LambdaQueryWrapper<Student>().eq(Student::getStudentNo, bean.getStudentNo()).ne(Student::getId,bean.getId()));
        if (student1!=null){
            return false;
        }
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

    @Override
    public List<StudentData> getStudentData(StudentQueryBean bean) {
        List<StudentDto> studentDtos = studentMapper.queryStudentPageF(bean);
        List<StudentData> studentDatas = new ArrayList<>();
        for (StudentDto studentDto: studentDtos
             ) {
            studentDatas.add(
                    new StudentData(
                            studentDto.getStudentNo(),
                            studentDto.getName(),
                            studentDto.getCollegeDtos().get(0).getName(),
                            studentDto.getCollegeDtos().get(studentDto.getCollegeDtos().size()-1).getName(),
                            studentDto.getClazzDto().getId(),
                            studentDto.getClazzDto().getName(),
                            studentDto.getSex(),
                            studentDto.getVaccinationTimes(),
                            studentDto.getTemporaryHome(),
                            studentDto.getHometown(),
                            studentDto.getEntrance(),
                            studentDto.getBirthday()));
        }
        return studentDatas;
    }

    @Override
    public void saveDataBatch(List<StudentData> cachedDataList) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AuthInfo authInfo = (AuthInfo) redisTemplate.opsForHash().get(RedisKey.USER_INFO, principal);
        Date date = new Date();
        List<Student> students = new ArrayList<>();
        //班级id
        List<Long> clazzIds = new ArrayList<>();
        cachedDataList.forEach(data->{
            if(data.getClazzId()!=null){
                clazzIds.add(data.getClazzId());
                students.add(new
                        Student(
                        data.getStudentNo().trim(),
                        data.getName().trim(),
                        data.getSex().trim(),
                        data.getVaccinationTimes(),
                        data.getTemporaryHome().trim(),
                        data.getHometown().trim(),
                        data.getEntrance(),
                        data.getBirthday(),
                        authInfo.getId(),
                        authInfo.getRealName(),
                        date
                ));
            }
        });
        //增加学生
        saveBatch(students);
        //添加学生和班级的关联
        studentMapper.saveStudentRelationClazzBatch(students,clazzIds);
    }


}
