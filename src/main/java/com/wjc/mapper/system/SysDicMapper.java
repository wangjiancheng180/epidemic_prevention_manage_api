package com.wjc.mapper.system;

import com.wjc.enetity.system.SysDic;

/**
* @author 王建成
* @date 2022/3/31--10:17
*/     
public interface SysDicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysDic record);

    int insertSelective(SysDic record);

    SysDic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDic record);

    int updateByPrimaryKey(SysDic record);
}