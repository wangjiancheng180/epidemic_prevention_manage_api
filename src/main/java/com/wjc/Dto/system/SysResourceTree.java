package com.wjc.Dto.system;

import com.wjc.enetity.BaseEnetity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/1--15:34
 */
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResourceTree extends BaseEnetity {
    private Long id;

    private Integer level;

    private String name;

    private Integer sort;

    private String sourceKey;

    private String sourceUrl;

    private List<SysResourceTree> children;

    public SysResourceTree( Long id, Integer level, String name, Integer sort, String sourceKey, String sourceUrl, List<SysResourceTree> children,Long createUserId, Long updateUserId, String createUserName, String updateUserName, Date createTime, Date updateTime) {
        super(createUserId, updateUserId, createUserName, updateUserName, createTime, updateTime);
        this.id = id;
        this.level = level;
        this.name = name;
        this.sort = sort;
        this.sourceKey = sourceKey;
        this.sourceUrl = sourceUrl;
        this.children = children;
    }
}
