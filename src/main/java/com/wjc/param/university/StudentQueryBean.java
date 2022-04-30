package com.wjc.param.university;

import com.wjc.common.PageRequestInfo;
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



}
