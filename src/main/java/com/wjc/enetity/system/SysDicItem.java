package com.wjc.enetity.system;

import com.wjc.enetity.BaseEnetity;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Date;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/3/31--10:58
*/     
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class SysDicItem extends BaseEnetity {
    private Long id;

    private String name;

    private String dicItemKey;

    private String dicItemValue;

    private Integer sort;

    private String remark;
}