package com.wjc.service.university;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.dto.university.ClazzDto;
import com.wjc.param.university.ClazzCreateBean;
import com.wjc.param.university.ClazzQueryBean;

import java.util.List;

/**
* @author 王建成
* @date 2022/4/27--8:48
*/     
public interface ClazzService{


    List<ClazzDto> queryClazzList();

    ClazzDto queryClazzById(Long id);

    boolean createClazz(ClazzCreateBean bean);

    boolean updateClazz(ClazzCreateBean bean);

    boolean deleteClazz(Long id);

    IPage<ClazzDto> queryClazzPage(ClazzQueryBean bean);
}
