package com.wjc.dto.university;

import com.wjc.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 王建成
 * @date 2022/4/28--11:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("学生信息")
public class StudentDto extends BaseEnetity {

    @ApiModelProperty("id")
    private Long id;

    /**
     * 学号
     */
    @ApiModelProperty("学号")
    private String studentNo;

    @ApiModelProperty("姓名")
    private String name;

    /**
     * 002：男，003:女
     */
    @ApiModelProperty("002：男，003:女")
    private String sex;

    @ApiModelProperty("年龄")
    private Integer age;

    /**
     * 健康码
     */
    @ApiModelProperty("健康码")
    private String healthCode;

    /**
     * 疫苗接种记录
     */
    @ApiModelProperty("疫苗接种记录")
    private String vaccinationRecord;

    /**
     * 行程码
     */
    @ApiModelProperty("行程码")
    private String traveCard;

    /**
     * 疫苗接种情况，0：未接种，1：已接种但为接种完成，2：已完成接种，3:完成加强针
     */
    @ApiModelProperty("疫苗接种情况，0：未接种，1：已接种但为接种完成，2：已完成接种，3:完成加强针")
    private int vaccinationTimes;

    /**
     * 现居地址
     */
    @ApiModelProperty("现居地址")
    private String temporaryHome;

    /**
     * 家乡
     */
    @ApiModelProperty("家乡")
    private String hometown;

    /**
     * 学生端用来登录的账号（初步决定用小程序端学生上传自己的运动轨迹）
     */
    @ApiModelProperty("学生端用来登录的账号")
    private String account;

    /**
     * 登录用的密码
     */
    @ApiModelProperty("登录用的密码")
    private String password;

    /**
     * 入学日期
     */
    @ApiModelProperty("入学日期")
    private Date entrance;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    private Date birthday;

    @ApiModelProperty("班级信息")
    private ClazzDto clazzDto;
}
