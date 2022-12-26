package com.wjc.mapper.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.enetity.iot.Task;

/**
* @author 王建成
* @date 2022/12/21--13:11
*/     
public interface TaskMapper extends BaseMapper<Task> {
    int deleteByPrimaryKey(Long id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);
}