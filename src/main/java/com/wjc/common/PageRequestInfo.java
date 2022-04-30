package com.wjc.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 王建成
 * @date 2022/4/17--22:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestInfo implements Serializable {

    @ApiModelProperty(value = "页数", required = true)
    @NotNull(message = "页码不能为空")
    private Integer page;

    @ApiModelProperty(value = "每页显示条数", required = true)
    @NotNull(message = "每页显示条数不能为空")
    private Integer size;
}
