package com.wjc.enetity.iot;

import lombok.Data;

import java.util.Date;

/**
 * @author 王建成
 * @date 2022/12/19--11:55
 */
@Data
public class DeviceInfo {
    private String iotId;
    private Date gmtCreate;
    private Date utcCreate;
    private String deviceId;
    private String deviceSecret;
    private Date gmtModified;
    private String deviceStatus;
    private Date utcModified;
    private String productKey;
    private String deviceName;
    private String nickname;
}
