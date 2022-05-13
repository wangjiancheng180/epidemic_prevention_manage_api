package com.wjc.service.university;

import com.wjc.param.visualization.Spot;

import java.util.List;

/**
* @author 王建成
* @date 2022/5/4--20:35
*/     
public interface TrailService{


    List<Object> queryPosition(Long studentId);

    List<Object> queryLine(Long studentId);

    boolean simulation(Spot spot);

    List<Spot> queryAllPoints();
}
