package com.wjc.param.visualization;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * @author 王建成
 * @date 2022/5/11--22:27
 */
@Data
@Measurement(name = "trail")
public class Spot {

    @Column(name = "studentId",tag = true)
    private String studentId;

    @Column(name = "lng")
   private double lng;

    @Column(name="lat")
   private double lat;

    @Column(name="time")
    private String time;
}
