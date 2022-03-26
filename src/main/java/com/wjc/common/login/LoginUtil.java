package com.wjc.common.login;

/**
 * @author 王建成
 * @date 2022/3/18--21:06
 */
public class LoginUtil {
    //token的名称
   public static String AUTH = "Authorization";
    //token前缀
   public static String TOKEN_PREFIX = "Bearer ";

   //成功登录
   public static int SUCCESS_LOGIN_CODE = 1 ;

   //用户名不存在
   public static int USERNAME_NOT_FOUND_CODE = 2;

   //密码错误
   public static int PASSWORD_ERROR_CODE = 3;

   //token异常
   public static int TOKEN_ERROR_CODE= 4;



}
