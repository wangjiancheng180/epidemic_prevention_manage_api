package com.wjc.service.iot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wjc.enetity.iot.Car;
import com.wjc.enetity.iot.DeviceInfo;
import com.wjc.enetity.iot.Task;
import com.wjc.param.iot.DeviceQueryBean;
import com.wjc.param.iot.DevicesQueryBean;

/**
 * @author 王建成
 * @date 2022/12/18--17:15
 */
public interface DeviceService extends IService<Car> {
/**
 *第一步肯定要添加设备吧
 */
    Car creatCar(Car car);

    IPage<DeviceInfo> queryDevice(DeviceQueryBean bean);

    Boolean issueTask(Task task);

    IPage<Car> queryCar(DeviceQueryBean bean);

    Car queryById(Long id);

    Boolean updateCar(Car car);

    Boolean deleteDevice(Long id);

    String queryDeviceList(DevicesQueryBean bean);
}
