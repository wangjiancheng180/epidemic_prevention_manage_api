package com.wjc.service.university;

import com.wjc.common.external.ChinaEpidemicData;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/6--23:27
 */
public interface StatisticsService {

    List<ChinaEpidemicData> queryChinaEpidemicData();

}
