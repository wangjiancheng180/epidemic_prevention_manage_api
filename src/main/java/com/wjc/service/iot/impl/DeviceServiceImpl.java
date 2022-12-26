package com.wjc.service.iot.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConverter;
import cn.hutool.json.JSONUtil;
import com.aliyun.sdk.service.iot20180120.AsyncClient;
import com.aliyun.sdk.service.iot20180120.models.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.wjc.enetity.iot.Car;
import com.wjc.enetity.iot.DeviceInfo;
import com.wjc.enetity.iot.Task;
import com.wjc.mapper.iot.CarMapper;
import com.wjc.param.iot.DeviceQueryBean;
import com.wjc.param.iot.DevicesQueryBean;
import com.wjc.service.iot.DeviceService;
import com.wjc.service.iot.TaskService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author 王建成
 * @date 2022/12/19--9:11
 */
@Service
@Slf4j
public class DeviceServiceImpl extends ServiceImpl<CarMapper, Car> implements DeviceService {

    @Resource
    private AsyncClient alIotClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CarMapper carMapper;
    /**
     * 添加设备
     * @param car
     * @return
     */
    @Override
    public Car creatCar(Car car) {
        /**
         * 首先将调用阿里云的方法将设备添加到云平台上去
         * 将得到产品的secret反馈回来再继续存储到数据库,
         * 后续需要现场人员要用secert将产品激活
         */
        RegisterDeviceRequest registerDeviceRequest = RegisterDeviceRequest.builder()
                .productKey(car.getProductKey())
                .deviceName(car.getDeviceName())
                .nickname(car.getCarNumber())
                .iotInstanceId("iot-06z00bk0x41tw1q")
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();
        CompletableFuture<RegisterDeviceResponse> response = alIotClient.registerDevice(registerDeviceRequest);
        try{
            RegisterDeviceResponse resp = response.get();
            //这里需要判断一下是否创建时就禁用了设备
            if(car.getDisabled()==2){
                disableDevice(car);
            }
            Map res = JSONUtil.toBean(new Gson().toJson(resp), Map.class);
            Map body = (Map) res.get("body");
            Map data = (Map) body.get("data");
            log.info((String) data.get("deviceSecret"));
            car.setDeviceSecret((String) data.get("deviceSecret"));
            car.setAccessNetwork(2);
            if (baseMapper.create(car)!=0){
                return car;
            }else {
                return null;
            }

        }catch (Exception e){
            log.error(StrUtil.format("注册设备出错！错误信息：{}",e.getMessage()));
        }
        // Finally, close the client
//        alIotClient.close();
        return null;
    }

    @Override
    public IPage<DeviceInfo> queryDevice(DeviceQueryBean bean) {
        /**
         * 要是时间来不及直接返回结果查询前端去处理分析
         */
        QueryDeviceRequest queryDeviceRequest = QueryDeviceRequest.builder()
                .productKey("i2u8RbzQHjt")
                .iotInstanceId("iot-06z00bk0x41tw1q")
                .pageSize(bean.getSize())
                .currentPage(bean.getPage())
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<QueryDeviceResponse> response = alIotClient.queryDevice(queryDeviceRequest);
        // Synchronously get the return value of the API request
        try{
            QueryDeviceResponse resp = response.get();
            Map body = JSONUtil.toBean(new Gson().toJson(resp.getBody()),Map.class);
            Map data = (Map) body.get("data");
            JSONArray deviceInfo = (JSONArray) data.get("deviceInfo");
            List<DeviceInfo> deviceInfos = JSONUtil.toList(deviceInfo, DeviceInfo.class);
            IPage<DeviceInfo> page = new Page<>();
            page.setTotal((Integer)body.get("total"));
            page.setSize((Integer)body.get("pageSize"));
            page.setPages((Integer)body.get("pageCount"));
            page.setRecords(deviceInfos);
            page.setCurrent((Integer)body.get("page"));
            return page;

        }catch (Exception e){
            log.error(StrUtil.format("查询设备出错！错误信息：{}",e.getMessage()));
        }
        return null;
    }

    @Override
    public Boolean issueTask(Task task) {

        //下发任务
        // Parameter settings for API request
        PubRequest pubRequest = PubRequest.builder()
                .iotInstanceId("iot-06z00bk0x41tw1q")
                .productKey("i2u8RbzQHjt")
                .topicFullName(StrUtil.format("/i2u8RbzQHjt/{}/user/get",task.getDeviceName()))
                .messageContent(Base64.encode(JSONUtil.toJsonStr(task)))
                .build();


        CompletableFuture<PubResponse> response = alIotClient.pub(pubRequest);
        try {
            PubResponse resp = response.get();
            log.info(JSONUtil.toJsonStr(resp.getBody()));
            //将任务存储
            return taskService.save(task);
        }catch (Exception e){
            log.error("下发任务失败");
        }

        return false;
    }

    @Override
    public IPage<Car> queryCar(DeviceQueryBean bean) {
        IPage<Car> ipage = new Page<>(bean.getPage(),bean.getSize());
        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Car::getCarNumber,bean.getCarNumber());
        queryWrapper.like(Car::getDeviceName,bean.getDeviceName());
        queryWrapper.orderByDesc(Car::getCreateTime);
        if(bean.getAccessNetwork()!=null){
            queryWrapper.eq(Car::getAccessNetwork,bean.getAccessNetwork());
        }
       if (bean.getDisabled()!=null){
           queryWrapper.eq(Car::getDisabled,bean.getDisabled());
       }

        IPage<Car> carIPage = page(ipage,queryWrapper);
        return carIPage;
    }

    @Override
    public Car queryById(Long id) {
        return getById(id);
    }

    @Override
    public Boolean updateCar(Car car) {
        Car car1 = getById(car.getId());
        if (car1.getDisabled()!=car.getDisabled()){
           if(car.getDisabled()==2){
               disableDevice(car);
           }else {
               enableDevice(car);
           }
        }
        return updateById(car);
    }

    @Override
    public Boolean deleteDevice(Long id) {
        //先要查找出该设备信息
        Car car = getById(id);
        if (car==null){
            return false;
        }
        //删除阿里云端设备
        DeleteDeviceRequest deleteDeviceRequest = DeleteDeviceRequest.builder()
                .iotInstanceId("iot-06z00bk0x41tw1q")
                .productKey(car.getProductKey())
                .deviceName(car.getDeviceName())
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<DeleteDeviceResponse> response = alIotClient.deleteDevice(deleteDeviceRequest);
        // Synchronously get the return value of the API request
        try{
            DeleteDeviceResponse resp = response.get();
//            System.out.println(new Gson().toJson(resp));
            log.info(JSONUtil.toJsonStr(resp.getBody()));
            if ((resp.getBody().getSuccess()==true)) {
                //数据库同属删除
                return removeById(id);
            }

        }catch (Exception e){
            log.error(StrUtil.format("删除设备失败：{}",e.getMessage()));
        }
        return false;
    }

    @Override
    public String queryDeviceList(DevicesQueryBean bean) {
        QueryDeviceBySQLRequest queryDeviceBySQLRequest = QueryDeviceBySQLRequest.builder()
                .iotInstanceId("iot-06z00bk0x41tw1q")
                .SQL(StrUtil.format("SELECT * FROM device WHERE status=\'{}\'",bean.getStatus()))
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<QueryDeviceBySQLResponse> response = alIotClient.queryDeviceBySQL(queryDeviceBySQLRequest);
        // Synchronously get the return value of the API request
        try{
            QueryDeviceBySQLResponse resp = response.get();
            return JSONUtil.toJsonStr(resp.getBody());
        }catch (Exception e){
            log.error(StrUtil.format("按sql查询出错！错误信息：{}",e.getMessage()));
        }
        return null;
    }

    /**
     * 启用设备
     * @param car
     * @return
     */
    private boolean enableDevice(Car car){
        // Parameter settings for API request
        EnableThingRequest enableThingRequest = EnableThingRequest.builder()
                .iotInstanceId("iot-06z00bk0x41tw1q")
                .productKey(car.getProductKey())
                .deviceName(car.getDeviceName())
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<EnableThingResponse> response = alIotClient.enableThing(enableThingRequest);
        // Synchronously get the return value of the API request
        try{
            EnableThingResponse resp = response.get();
            return resp.getBody().getSuccess();
        }catch (Exception e){
            log.error("启用设备出错");
        }
       return false;
    }

    /**
     * 禁用设备
     */
    private boolean disableDevice(Car car){
        DisableThingRequest disableThingRequest = DisableThingRequest.builder()
                .iotInstanceId("iot-06z00bk0x41tw1q")
                .productKey(car.getProductKey())
                .deviceName(car.getDeviceName())
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<DisableThingResponse> response = alIotClient.disableThing(disableThingRequest);
        // Synchronously get the return value of the API request
        try{
            DisableThingResponse resp = response.get();
            return resp.getBody().getSuccess();
        }catch (Exception e){
            log.error("启用设备出错");
        }
        return false;
    }
}
