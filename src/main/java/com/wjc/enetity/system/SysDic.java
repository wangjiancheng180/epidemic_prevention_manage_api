package com.wjc.enetity.system;

import java.util.Date;

import com.wjc.enetity.BaseEnetity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/3/31--10:17
*/     
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDic extends BaseEnetity {
    private Long id;

    /**
    * 字典名称
    */
    private String name;

    /**
    * 字典key
    */
    private String dicKey;

    /**
    * 备注
    */
    private String remark;



}