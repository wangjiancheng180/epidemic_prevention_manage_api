package com.wjc.controller.university;

import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.param.visualization.Spot;
import com.wjc.service.university.TrailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/4--20:38
 */
@RestController
@RequestMapping("/trail")
@Api("轨迹位置")
@ApiOperation("轨迹位置信息")
public class TrailController extends BaseController {

    @Autowired
    private TrailService trailService;



    @GetMapping("/queryPosition")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryStudent')")
    @ApiOperation("学生最新定位")
    public JsonResult<List<Object>> queryPosition(@RequestParam("studentId") Long studentId){
        return JsonResult.success(trailService.queryPosition(studentId));
    }

    @GetMapping("/queryLine")
    @PreAuthorize("hasRole('super_admin') OR hasAuthority('queryStudent')")
    @ApiOperation("48小时轨迹")
    public JsonResult<List<Object>>queryLine(@RequestParam("studentId") Long studentId){
        return JsonResult.success(trailService.queryLine(studentId));
    }

    @PostMapping("/simulation")
    public JsonResult<Boolean> simulation(@RequestBody Spot spot){
        return JsonResult.success(trailService.simulation(spot));
    }

    @GetMapping("/queryAllPoints")
    public JsonResult<List<Spot>> queryAllPoints(){
        return JsonResult.success(trailService.queryAllPoints());
    }
}
