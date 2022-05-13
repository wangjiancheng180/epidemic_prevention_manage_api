package com.wjc.controller.visualization;

import com.wjc.common.JsonResult;
import com.wjc.common.external.ChinaEpidemicData;
import com.wjc.service.university.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/6--23:20
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/queryChinaEpidemicData")
    public JsonResult<List<ChinaEpidemicData>> queryChinaEpidemicData(){
        return JsonResult.success(statisticsService.queryChinaEpidemicData());
    }


}
