package com.wjc.mapper.university;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import com.wjc.dto.university.ClazzDto;
import com.wjc.enetity.university.Clazz;
import com.wjc.param.university.ClazzQueryBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 王建成
* @date 2022/4/27--8:48
*/     
public interface ClazzMapper extends BaseMapper<Clazz> {

    List<ClazzDto> queryClazzList();

    /**
     * 通过id查询班级
     * @param id
     * @return
     */
    ClazzDto queryClazzById(@Param("id") Long id);

    /**
     * 添加与学院的依赖
     * @param clazzId
     * @param collegeIds
     */
    void addRelationCollege(@Param("clazzId") Long clazzId, @Param("collegeIds") List<Long> collegeIds);

    /**
     * 删除班级与学院的联系
     * @param clazzId
     */
    void deleteRelationCollege(@Param("clazzId") Long clazzId);

    /**
     * 统计学生数量
     * @param clazzId
     * @return
     */
    int countStudent(@Param("clazzId") Long clazzId);

    /**
     * 条件分页查询
     * @param clazzDtos
     * @param pageInfo
     * @return
     */
    IPage<ClazzDto> queryClazzPage(@Param("clazzDtos") List<ClazzDto>  clazzDtos, IPage<ClazzDto> pageInfo);

    /**
     * 初步过滤分页条件中符合要求的班级id
     * @param bean
     * @return
     */
    List<ClazzDto> queryClazzPageF(@Param("bean") ClazzQueryBean bean);
}