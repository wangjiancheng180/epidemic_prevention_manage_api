package com.wjc.param.system;

import com.wjc.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 王建成
 * @date 2022/4/8--22:40
 */
@ApiModel("资源更新类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysResourceUpdateBean extends BaseEnetity {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父id")
    private Long parentId;

    /**
     * 层级，1：代表最高级，2代表1级的下级，依次类推
     */
    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("资源key")
    private String sourceKey;

    @ApiModelProperty("资源路径")
    private String sourceUrl;
}
