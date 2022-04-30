package com.wjc.param.university;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wjc.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 王建成
 * @date 2022/4/27--21:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("新建学院")
public class CollegeCreateBean extends BaseEnetity {


    @ApiModelProperty("id")
    private Long id;

    /**
     * 父级学院
     */
    @ApiModelProperty("父级id")
    private Long parentId;

    /**
     * 学院名称
     */
    @ApiModelProperty("学院名称")
    private String name;

    /**
     * 学院地址
     */
    @ApiModelProperty("学院地址")
    private String address;

    /**
     * 联系号码
     */
    @ApiModelProperty("联系电话")
    private String telephone;

    /**
     * 学院层级（1级学院。二级学院）
     */
    @ApiModelProperty("学院层级（1级学院。二级学院）")
    private Integer level;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

}
