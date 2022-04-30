package com.wjc.param.university;

import com.wjc.dto.university.CollegeDto;
import com.wjc.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/28--9:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("班级创建类")
public class ClazzCreateBean extends BaseEnetity {
    @ApiModelProperty("id")
    private Long id;

    /**
     * 班级名称
     */
    @ApiModelProperty("班级名称")
    private String name;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private int sort;

    @ApiModelProperty("绑定的院校Id包括父级院校")
    private List<Long> collegeIds;
}
