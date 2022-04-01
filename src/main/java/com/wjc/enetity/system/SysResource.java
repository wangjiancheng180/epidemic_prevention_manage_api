package com.wjc.enetity.system;

import com.wjc.enetity.BaseEnetity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/4/1--15:32
*/     
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class SysResource extends BaseEnetity {
    private Long id;

    private Long parentId;

    /**
    * 层级，1：代表最高级，2代表1级的下级，依次类推
    */
    private Integer level;

    private String name;

    private Integer sort;

    private String sourceKey;

    private String sourceUrl;
}