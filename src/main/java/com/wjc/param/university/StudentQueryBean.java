package com.wjc.param.university;

import com.wjc.common.PageRequestInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王建成
 * @date 2022/4/28--12:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("学生类的筛选条件")
public class StudentQueryBean extends PageRequestInfo {
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

    @ApiModelProperty("关联学院id")
    private  Long collegeId;

    @ApiModelProperty("关联班级Id")
    private Long clazzId;

    @ApiModelProperty("疫苗接种情况，0：未接种，1：已接种但为接种完成，2：已完成接种，3:完成加强针")
    private Integer vaccinationTimes;

}
