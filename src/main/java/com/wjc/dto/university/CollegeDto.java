package com.wjc.dto.university;

import com.wjc.enetity.BaseEnetity;
import com.wjc.enetity.university.Clazz;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/27--9:55
 */
@ApiModel("学院Dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CollegeDto extends BaseEnetity {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("父辈Id集合")
    private List<Long> parentIds;

    /**
     * 父级学院
     */
    @ApiModelProperty("父级id")
    private Long parentId;

    /**
     * 学院名称
     */
    @ApiModelProperty("学院名称")
    private String name;

    /**
     * 学院地址
     */
    @ApiModelProperty("学院地址")
    private String address;

    /**
     * 联系号码
     */
    @ApiModelProperty("联系电话")
    private String telephone;

    /**
     * 学院层级（1级学院。二级学院）
     */
    @ApiModelProperty("学院层级（1级学院。二级学院）")
    private Integer level;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("班级")
    private List<Clazz> clazzes;

//    public CollegeDto(Long id, List<Long> parentIds, Long parentId, String name, String address, String telephone, Integer level, Integer sort, List<Clazz> clazzes,Long createUserId,  String createUserName,Date createTime, Long updateUserId,String updateUserName,  Date updateTime) {
//        super(createUserId, updateUserId, createUserName, updateUserName, createTime, updateTime);
//        this.id = id;
//        this.parentIds = parentIds;
//        this.parentId = parentId;
//        this.name = name;
//        this.address = address;
//        this.telephone = telephone;
//        this.level = level;
//        this.sort = sort;
//        this.clazzes = clazzes;
//    }
}
