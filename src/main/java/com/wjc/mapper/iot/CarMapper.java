package com.wjc.mapper.iot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjc.enetity.iot.Car;
import org.apache.ibatis.annotations.Param;

/**
* @author 王建成
* @date 2022/12/19--9:09
*/     
public interface CarMapper extends BaseMapper<Car> {
    int deleteByPrimaryKey(Long id);

    int create(@Param("car") Car car);

    int insertSelective(Car record);

    Car selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Car record);

    int updateByPrimaryKey(Car record);
}