package com.wjc.config;


import cn.hutool.json.JSONUtil;
import com.wjc.common.JsonResult;
import com.wjc.common.login.LoginUtil;
import com.wjc.common.login.RedisKey;
import com.wjc.dto.system.UserInfoDto;
import com.wjc.filter.JwtAuthenticationFilter;
import com.wjc.service.system.UserInfoService;
import com.wjc.service.system.impl.UserDetailsServiceImpl;
import com.wjc.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王建成
 * @date 2022/3/17--13:19
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解标注 哪些方法需要鉴权 @PreAuthorize("hasRole('admin')") @PreAuthorize("hasAuthority('system:user:save')")
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    //实现了UserDetailsService的类
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;



    private static final String[] URL_WHITELISTS ={
            "/login/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/doc.html",
            "/trail/simulation"

    };

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(){

            @Override
            public String encode(CharSequence rawPassword) {
                return super.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {

                return super.matches(rawPassword, encodedPassword);
            }
        };
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }


        @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //springsecurity通过userDetailsService的loadUserByUsername方法
        //去数据库里查询用户并认证
//        auth.userDetailsService(userDetailsService)
//                //设置密码加密方式，默认为BCryptPasswordEncoder，也是springsecurity默认的密码加密方式
//                //这个必须要
//                .passwordEncoder(passwordEncoder());
            auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                //白名单放行
                .antMatchers(URL_WHITELISTS).permitAll()
                //其他所有请求都要认证
                .anyRequest().authenticated()
                //连接
                .and()
                //关闭session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //登录
                .formLogin()
                //处理登录请求的url
                .loginProcessingUrl("/login")
                //登录成功处理
                .successHandler(successHandler())
                //登录失败处理
                .failureHandler(failureHandler())

                .and()
                //设置访问被拒绝后的事件（用来处理权限不足时的返回）
                .exceptionHandling()
                //未登录访问资源
                .authenticationEntryPoint(AuthenticationEntryPoint())
                //权限不足时
                .accessDeniedHandler(accessDeniedHandler())
                //将我们自定义的token过滤器按照一定顺序加入过滤器链
                .and()
                .addFilter(jwtAuthenticationFilter());

           //关闭默认的httpBasic()的认证方式
        //springsecurity账号密码yml就不起作用了
//        http.httpBasic().disable();

    }




    /**
     *登录成功处理器
     * @return
     */
    public AuthenticationSuccessHandler successHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                //取出此时登录的用户名
                String username = authentication.getName();
                UserInfoDto userInfoDto = (UserInfoDto) redisTemplate.opsForHash().get(RedisKey.USER_INFO, username);
                if (userInfoDto == null){
                    userInfoDto = userInfoService.queryByUsername(username);
                }
                //这里需要将密码置空
                userInfoDto.setPassword(null);
                Map<String,Object> claims = new HashMap<>();
                claims.put("username",username );
                //生成token
                String token = tokenUtils.createToken(claims);
                httpServletResponse.setHeader(LoginUtil.AUTH,token);
                PrintWriter writer = httpServletResponse.getWriter();
                //将token包装到同一的返回结果类返回
                writer.println(JSONUtil.toJsonStr(JsonResult.success(LoginUtil.SUCCESS_LOGIN_CODE,userInfoDto)));
                //刷新确保成功响应
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     *登录失败处理器
     * @return
     */
    public AuthenticationFailureHandler failureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                if(e.getClass().equals(UsernameNotFoundException.class)){
                    //此时异常属于用户名不存在
                    writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.USERNAME_NOT_FOUND_CODE,"用户名不存在！")));
                }else {
                    //此时异常属于密码错误
                    writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.PASSWORD_ERROR_CODE,"密码错误！")));
                }
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     *鉴权失败处理器
     * @return
     */
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println(JsonResult.failure("权限不足"));
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     *认证处理器
     * @return
     */
    public AuthenticationEntryPoint AuthenticationEntryPoint(){
        return new AuthenticationEntryPoint(){
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
               //前后端分离项目 /login 可以是返回一个json字符串
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.TOKEN_ERROR_CODE,"未登录！或登录失效请重新登录！")));
                writer.flush();
                writer.close();
            }
        };
    }

}
