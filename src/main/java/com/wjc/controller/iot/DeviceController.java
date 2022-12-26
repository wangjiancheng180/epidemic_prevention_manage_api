package com.wjc.controller.iot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.common.JsonResult;
import com.wjc.controller.BaseController;
import com.wjc.enetity.iot.Car;
import com.wjc.enetity.iot.DeviceInfo;
import com.wjc.enetity.iot.Task;
import com.wjc.param.iot.DeviceQueryBean;
import com.wjc.param.iot.DevicesQueryBean;
import com.wjc.service.iot.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/12/19--10:42
 */
@RestController
@RequestMapping("/device")
@Api("设备信息")
@ApiOperation("设备管理")
public class DeviceController extends BaseController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/createCar")
    @ApiOperation("新建车辆")
    public JsonResult<Car> createCar(@RequestBody Car car){
        setCreate(car);
        return JsonResult.success(deviceService.creatCar(car));
    }

    @PostMapping("/queryDevice")
    @ApiOperation("查询设备信息")
    public JsonResult<IPage<DeviceInfo>> queryDevice(@RequestBody DeviceQueryBean bean){
        return JsonResult.success(deviceService.queryDevice(bean));
    }

    @PostMapping("/issueTask")
    @ApiOperation("下发任务")
    public JsonResult<Boolean> issueTask(@RequestBody Task task){
        return JsonResult.success(deviceService.issueTask(task));

    }

    @PostMapping("/queryCarPage")
    @ApiOperation("分页查询设备信息")
    public JsonResult<IPage<Car>> queryCarPage(@RequestBody DeviceQueryBean bean){
        return JsonResult.success(deviceService.queryCar(bean));
    }

    @GetMapping("/queryById")
    @ApiOperation("根据Id查找")
    public JsonResult<Car> queryById(@RequestParam("id") Long id){
        return JsonResult.success(deviceService.queryById(id));
    }

    @PostMapping("/updateCar")
    @ApiOperation("更新设备")
    public JsonResult<Boolean> updateCar(@RequestBody Car car){
        setUpdate(car);
        return JsonResult.success(deviceService.updateCar(car));
    }

    @DeleteMapping("/deleteDevice")
    public JsonResult<Boolean> deleteDevice(@RequestParam("id") Long id){
        return JsonResult.success(deviceService.deleteDevice(id));
    }

    @PostMapping("/queryDeviceList")
    public JsonResult<String> queryDeviceList(@RequestBody DevicesQueryBean bean){
        return JsonResult.success(deviceService.queryDeviceList(bean));
    }

    @GetMapping("/show")
    public void  show(HttpServletResponse response) {
        try {
            String fileName = "D:\\BaiduNetdiskDownload\\snpg.mp4";
            FileInputStream fis = new FileInputStream(fileName); // 以byte流的方式打开文件
            int i = fis.available(); //得到文件大小
            byte data[] = new byte[i];
            fis.read(data);  //读数据
            response.setContentType("mp4/*"); //设置返回的文件类型
            OutputStream toClient = response.getOutputStream(); //得到向客户端输出二进制数据的对象
            toClient.write(data);  //输出数据
            toClient.flush();
            toClient.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件不存在");
        }
    }
}
