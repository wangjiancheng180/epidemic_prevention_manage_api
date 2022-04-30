package com.wjc.service.university.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import com.wjc.dto.university.ClazzDto;
import com.wjc.dto.university.CollegeDto;
import com.wjc.param.university.ClazzCreateBean;
import com.wjc.param.university.ClazzQueryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wjc.mapper.university.ClazzMapper;
import com.wjc.enetity.university.Clazz;
import com.wjc.service.university.ClazzService;

import java.util.ArrayList;
import java.util.List;

/**
* @author 王建成
* @date 2022/4/27--8:48
*/     
@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper,Clazz> implements ClazzService{

    @Autowired
    private ClazzMapper clazzMapper;

//    @Autowired
//    private CollegeService collegeService;


    @Override
    public List<ClazzDto> queryClazzList() {
        List<ClazzDto> clazzDtos = clazzMapper.queryClazzList();

        return clazzDtos;
    }

    @Override
    public ClazzDto queryClazzById(Long id) {
        ClazzDto clazzDto = clazzMapper.queryClazzById(id);
//        List<Long> collegeIds = new ArrayList<>();
//        for (CollegeDto college:clazzDto.getCollegeDtos()
//             ) {
//            collegeIds.add(college.getId());
//        }
//        clazzDto.setCollegeIds(collegeIds);
        return clazzDto;
    }

    @Override
    public boolean createClazz(ClazzCreateBean bean) {
        Clazz clazz = new Clazz();
        clazz.setName(bean.getName());
        clazz.setSort(bean.getSort());
        clazz.setCreateUserId(bean.getCreateUserId());
        clazz.setCreateUserName(bean.getCreateUserName());
        clazz.setCreateTime(bean.getCreateTime());
        if(save(clazz)){
            clazzMapper.addRelationCollege(clazz.getId(),bean.getCollegeIds());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateClazz(ClazzCreateBean bean) {
        Clazz clazz = new Clazz();
        clazz.setId(bean.getId());
        clazz.setName(bean.getName());
        clazz.setSort(bean.getSort());
        clazz.setCreateUserId(bean.getCreateUserId());
        clazz.setCreateUserName(bean.getCreateUserName());
        clazz.setCreateTime(bean.getCreateTime());
        if (updateById(clazz)){
            //先删除旧的关联
            clazzMapper.deleteRelationCollege(clazz.getId());
            //再添加新的关联
            clazzMapper.addRelationCollege(clazz.getId(),bean.getCollegeIds());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteClazz(Long id) {
        //先查看当前班级下是否还有关联的学生
        int count =  clazzMapper.countStudent(id);
        if (count>0){
            //当班级还关联着学生时无法直接删除
            return false;
        }
        //先删除学院的关联
        clazzMapper.deleteRelationCollege(id);
        return removeById(id);
    }

    @Override
    public IPage<ClazzDto> queryClazzPage(ClazzQueryBean bean) {
        IPage<ClazzDto> pageInfo = new Page<>(bean.getPage(),bean.getSize());
        //这里进行曲线救国---单独对clazz进行分页查询避免一对多时出现分页不准确
        //这里不采用映射子查询解决是因为，条件中包括了对关联子表的条件过滤
        //所以先筛选出所有符合条件的班级的id，然后根据id约束去分页查询
        //TODO 这里最好能根据page和size初步做个limit限制，但是这样做就是手动分页了，麻烦
        List<ClazzDto> clazzDtos = clazzMapper.queryClazzPageF(bean);
        if (CollUtil.isEmpty(clazzDtos)){
            //如果筛选出来是空的直接返回
            return pageInfo;
        }
        IPage<ClazzDto> clazzDtoIPage = clazzMapper.queryClazzPage(clazzDtos, pageInfo);
        List<ClazzDto> records = clazzDtoIPage.getRecords();
        //这里需要将信息填充回去
        for (ClazzDto record:records
             ) {
            for (ClazzDto clazzDto:clazzDtos
                 ) {
                if (record.getId().equals(clazzDto.getId())){
                    record.setCollegeIds(clazzDto.getCollegeIds());
                    record.setCollegeDtos(clazzDto.getCollegeDtos());
                    break;
                }
            }
        }
        return clazzDtoIPage;
    }
}
