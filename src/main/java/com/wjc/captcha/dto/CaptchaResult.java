package com.wjc.captcha.dto;

import lombok.Data;

/**
 * @author 王建成
 * @date 2022/5/21--16:37
 * 验证结果
 */
@Data
public class CaptchaResult {

    private Boolean success;

    private String message;

}
