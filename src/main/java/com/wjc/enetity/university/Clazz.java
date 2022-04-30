package com.wjc.enetity.university;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wjc.enetity.BaseEnetity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* @author 王建成
* @date 2022/4/27--8:48
*/     
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class Clazz extends BaseEnetity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 班级名称
    */
    private String name;

    /**
    * 排序
    */
    private int sort;

}