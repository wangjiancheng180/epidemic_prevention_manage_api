package com.wjc.param.university;

import com.wjc.common.PageRequestInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 王建成
 * @date 2022/4/29--8:36
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("班级查询条件")
public class ClazzQueryBean extends PageRequestInfo {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("联系学院id")
    private Long collegeId;
}
