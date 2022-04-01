package com.wjc.service.system;

import com.wjc.enetity.system.SysDicItem;
    /**
* @author 王建成
* @date 2022/3/31--10:58
*/     
public interface SysDicItemService{


    int deleteByPrimaryKey(Long id);

    int insert(SysDicItem record);

    int insertSelective(SysDicItem record);

    SysDicItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDicItem record);

    int updateByPrimaryKey(SysDicItem record);

}
