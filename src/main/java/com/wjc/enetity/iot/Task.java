package com.wjc.enetity.iot;

import java.util.Date;

import com.wjc.enetity.BaseEnetity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/12/21--13:11
*/     
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task extends BaseEnetity {
    private Long id;

    private String deviceName;

    private String name;

    private String address;

    private String lng;

    private String lat;

//    private Long createUserId;
//
//    private String createUserName;
//
//    private Date createTime;
//
//    private Long updateUserId;
//
//    private String updateUserName;
//
//    private Date updateTime;
}