package com.wjc.excel.data.university;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.wjc.excel.converter.university.CustomMerge;
import com.wjc.excel.converter.university.GenderConverter;
import com.wjc.excel.converter.university.VaccinationConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 王建成
 * @date 2022/5/19--9:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("学生excel信息")
public class StudentData {

    /**
     * 学号
     */
    @ApiModelProperty("学号")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "学号")
    @CustomMerge(needMerge = true,isPk = true)
    private String studentNo;

    @ApiModelProperty("姓名")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "姓名")
    @CustomMerge(needMerge = true)
    private String name;

    @ApiModelProperty("父学院")
    @ColumnWidth(value = 25)
    @ExcelProperty(value = {"学院","父学院"})
    private String collegeParent;

    @ApiModelProperty("子学院")
    @ColumnWidth(value = 25)
    @ExcelProperty(value = {"学院","子学院"})
    private String collegeChildren;

    @ApiModelProperty("班级编号")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "班级编号")
    @CustomMerge(needMerge = true)
    private Long clazzId;

    @ApiModelProperty("班级名称")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "班级")
    @CustomMerge(needMerge = true)
    private String clazzName;

    /**
     * 002：男，003:女
     */
    @ApiModelProperty("002：男，003:女")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "性别",converter = GenderConverter.class)
    @CustomMerge(needMerge = true)
    private String sex;


//    /**
//     * 健康码
//     */
//    @ApiModelProperty("健康码")
//    @CustomMerge(needMerge = true)
//    private String healthCode;
//
//    /**
//     * 疫苗接种记录
//     */
//    @ApiModelProperty("疫苗接种记录")
//    @CustomMerge(needMerge = true)
//    private String vaccinationRecord;
//
//    /**
//     * 行程码
//     */
//    @ApiModelProperty("行程码")
//    @CustomMerge(needMerge = true)
//    private String traveCard;

    /**
     * 疫苗接种情况，0：未接种，1：已接种但为接种完成，2：已完成接种，3:完成加强针
     */
    @ApiModelProperty("疫苗接种情况，0：未接种，1：已接种但为接种完成，2：已完成接种，3:完成加强针")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "疫苗接种情况",converter = VaccinationConverter.class)
    @CustomMerge(needMerge = true)
    private int vaccinationTimes;

    /**
     * 现居地址
     */
    @ApiModelProperty("现居地址")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "现居地址")
    @CustomMerge(needMerge = true)
    private String temporaryHome;

    /**
     * 家乡
     */
    @ApiModelProperty("家乡")
    @ColumnWidth(value = 15)
    @ExcelProperty(value = "家乡")
    @CustomMerge(needMerge = true)
    private String hometown;


    /**
     * 入学日期
     */
    @ApiModelProperty("入学日期")
    @ColumnWidth(value = 20)
    @ExcelProperty(value = "入学日期")
    @DateTimeFormat("yyyy-MM-dd")
    @CustomMerge(needMerge = true)
    private Date entrance;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    @ColumnWidth(value = 20)
    @ExcelProperty(value = "出生日期")
    @DateTimeFormat("yyyy-MM-dd")
    @CustomMerge(needMerge = true)
    private Date birthday;
}
