package com.wjc.enetity.iot;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wjc.enetity.BaseEnetity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/12/19--9:09
*/     
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("car")
public class Car extends BaseEnetity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 车牌号
    */
    @TableField
    private String carNumber;

    /**
    * 设备名称
    */
    @TableField
    private String deviceName;

    /**
    * 设备所属的产品key
    */
    @TableField
    private String productKey;

    /**
    * 设备密钥
    */
    @TableField
    private String deviceSecret;

    /**
    * 车辆型号
    */
    @TableField
    private String type;

    /**
    *是否入网
     */
    @TableField
    private Integer accessNetwork;

    /**
     * 是否禁用
     */
    @TableField
    private Integer disabled;

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