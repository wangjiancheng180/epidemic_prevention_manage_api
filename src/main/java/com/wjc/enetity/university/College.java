package com.wjc.enetity.university;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wjc.enetity.BaseEnetity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/4/27--8:49
*/     
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class College extends BaseEnetity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 父级学院
    */
    private Long parentId;

    /**
    * 学院名称
    */
    private String name;

    /**
    * 学院地址
    */
    private String address;

    /**
    * 联系号码
    */
    private String telephone;

    /**
    * 学院层级（1级学院。二级学院）
    */
    private Integer level;

    /**
    * 排序
    */
    private Integer sort;
}