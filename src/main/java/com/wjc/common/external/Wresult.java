package com.wjc.common.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/7--21:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wresult {

    private Integer codeid;

    private String message;

    private List<ChinaEpidemicData> retdata;

    private String curtime;
}
