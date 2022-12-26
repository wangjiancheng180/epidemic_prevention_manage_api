package com.wjc.param.iot;

import com.wjc.common.PageRequestInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 王建成
 * @date 2022/12/19--12:01
 */
@Data
@ApiModel("设备查询条件")
public class DeviceQueryBean extends PageRequestInfo {

    private String carNumber;
    private String deviceName;
    private Integer disabled;
    private Integer accessNetwork;

}
