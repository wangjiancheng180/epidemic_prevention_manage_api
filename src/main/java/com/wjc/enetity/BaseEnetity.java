package com.wjc.enetity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 王建成
 * @date 2022/3/31--10:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEnetity {
    private Long createUserId;

    private Long updateUserId;

    private String createUserName;

    private String updateUserName;
    private Date createTime;

    private Date updateTime;

}
