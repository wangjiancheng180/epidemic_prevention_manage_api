package com.wjc.service.university;

import com.wjc.dto.university.CollegeDto;
import com.wjc.dto.university.CollegeTree;

import com.wjc.param.university.CollegeCreateBean;

import java.util.List;

/**
* @author 王建成
* @date 2022/4/27--8:49
*/     
public interface CollegeService{

    List<CollegeTree> queryCollegeTree();

    CollegeDto queryCollegeById(Long id);

    Long createCollege(CollegeCreateBean bean);

    boolean updateCollege(CollegeCreateBean bean);

    boolean deleteCollege(Long id);

    List<Long> findParentId(Long id);
}
