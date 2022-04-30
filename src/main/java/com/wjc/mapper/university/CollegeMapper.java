package com.wjc.mapper.university;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.dto.university.CollegeDto;
import com.wjc.enetity.university.College;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 王建成
* @date 2022/4/27--8:49
*/     
public interface CollegeMapper extends BaseMapper<College> {

    List<CollegeDto> listAll();

    /**
     * 通过id查询学院
     * @param id
     * @return
     */
    CollegeDto queryCollegeById(@Param("id") Long id);

    /**
     * 统计分院信息
     * @param id
     * @return
     */
    int countChildren(@Param("college_id") Long id);
}