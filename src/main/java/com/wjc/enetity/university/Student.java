package com.wjc.enetity.university;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wjc.enetity.BaseEnetity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* @author 王建成
* @date 2022/4/24--23:01
*/     
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class Student extends BaseEnetity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 学号
    */
    private String studentNo;

    private String name;

    /**
    * 002：男，003:女
    */
    private String sex;

    private Integer age;

    /**
    * 健康码
    */
    private String healthCode;

    /**
    * 疫苗接种记录
    */
    private String vaccinationRecord;

    /**
    * 行程码
    */
    private String traveCard;

    /**
    * 疫苗接种情况，0：未接种，1：已接种但为接种完成，2：已完成接种，3:完成加强针
    */
    private int vaccinationTimes;

    /**
    * 现居地址
    */
    private String temporaryHome;

    /**
    * 家乡
    */
    private String hometown;

    /**
    * 学生端用来登录的账号（初步决定用小程序端学生上传自己的运动轨迹）
    */
    private String account;

    /**
    * 登录用的密码
    */
    private String password;

    /**
     * 入学日期
     */
    private Date entrance;

    /**
     * 出生日期
     */
    private Date birthday;
}