package com.wjc.excel.converter.university;

import java.lang.annotation.*;

/**
 * @author 王建成
 * @date 2022/5/19--10:09
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CustomMerge {

    /**
     * 是否需要合并单元格
     */
    boolean needMerge() default false;

    /**
     * 是否是主键,即该字段相同的行合并
     */
    boolean isPk() default false;

}
