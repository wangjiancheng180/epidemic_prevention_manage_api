package com.wjc.common.external;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/7--21:45
 */
@ApiModel("全国疫情数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChinaEpidemicData {

    @ApiModelProperty("无症状")
    private Integer asymptomatic;

    @ApiModelProperty("城市，例如宁波")
    private String city;

    @ApiModelProperty("累计确诊")
    private Integer confirm;

    @ApiModelProperty("当前确诊")
    private Integer curConfirm;

    @ApiModelProperty("死亡人数")
    private Integer died;

    @ApiModelProperty("治愈人数")
    private Integer heal;

    private String confirmInter;

    @ApiModelProperty("时间戳")
    private String relativeTime;

    @ApiModelProperty("区域，例如浙江")
    private String xArea;

    @ApiModelProperty("区域下的城市数据")
    private List<ChinaEpidemicData> subList;
}
