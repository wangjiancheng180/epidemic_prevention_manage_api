package com.wjc.config;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wjc.common.JsonResult;
import com.wjc.common.login.LoginUtil;
import com.wjc.common.login.RedisKey;
import com.wjc.dto.system.AuthInfo;
import com.wjc.exception.CaptchaException;
import com.wjc.filter.CaptchaFilter;
import com.wjc.filter.CustomAuthenticationFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ?????????
 * @date 2022/3/17--13:19
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//?????????????????? ???????????????????????? @PreAuthorize("hasRole('admin')") @PreAuthorize("hasAuthority('system:user:save')")
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    //?????????UserDetailsService??????
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private CaptchaFilter captchaFilter;





    private static final String[] URL_WHITELISTS ={
//            "/login/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/doc.html",
            "/captcha/**",
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
        //springsecurity??????userDetailsService???loadUserByUsername??????
        //????????????????????????????????????
//        auth.userDetailsService(userDetailsService)
//                //????????????????????????????????????BCryptPasswordEncoder?????????springsecurity???????????????????????????
//                //???????????????
//                .passwordEncoder(passwordEncoder());
            auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                //???????????????
                .antMatchers(URL_WHITELISTS).permitAll()
                //??????????????????????????????
                .anyRequest().authenticated()
                //??????
                .and()
                //??????session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //????????????form??????
                .formLogin().disable();
                //?????????????????????url
//                .loginProcessingUrl("/login")
//                //??????????????????
//                .successHandler(successHandler())
//                //??????????????????
//                .failureHandler(failureHandler())

//                .and()
                http
                //???????????????????????????????????????????????????????????????????????????
                .exceptionHandling()
                //?????????????????????
                .authenticationEntryPoint(AuthenticationEntryPoint())
                //???????????????
                .accessDeniedHandler(accessDeniedHandler())
                //?????????????????????token?????????????????????????????????????????????
                .and()
                .addFilterAt(customAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthenticationFilter());

           //???????????????httpBasic()???????????????
        //springsecurity????????????yml??????????????????
//        http.httpBasic().disable();

    }


    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter () throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        //???????????????????????????
        filter.setAuthenticationSuccessHandler(successHandler());
        //???????????????????????????
        filter.setAuthenticationFailureHandler(failureHandler());

        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }



    /**
     *?????????????????????
     * @return
     */
    public AuthenticationSuccessHandler successHandler(){
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                //?????????????????????????????????????????????
                String sessionId = httpServletRequest.getRequestedSessionId();
                if (StrUtil.isNotEmpty(sessionId)){
                    redisTemplate.opsForHash().delete(RedisKey.CAPTCHA_RESULT,sessionId);
                }
                //??????????????????????????????
                String username = authentication.getName();
                AuthInfo authInfo = (AuthInfo) redisTemplate.opsForHash().get(RedisKey.USER_INFO, username);
                if (authInfo == null){
                    authInfo = userInfoService.queryByUsername(username);
                }
                //???????????????????????????
//                authInfo.setPassword(null);
                Map<String,Object> claims = new HashMap<>();
                claims.put("username",username );
                //??????token
                String token = tokenUtils.createToken(claims);
                httpServletResponse.setHeader(LoginUtil.AUTH,token);
                PrintWriter writer = httpServletResponse.getWriter();
                //???token???????????????????????????????????????
                writer.println(JSONUtil.toJsonStr(JsonResult.success(LoginUtil.SUCCESS_LOGIN_CODE,authInfo)));
                //????????????????????????
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     *?????????????????????
     * @return
     */
    @Bean
    public AuthenticationFailureHandler failureHandler(){
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                if(e.getClass().equals(CaptchaException.class)){
                    writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.CAPTCHA_ERROR_CODE,e.getMessage())));
                }
                else if(e.getClass().equals(UsernameNotFoundException.class)){
                    //????????????????????????????????????
                    writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.USERNAME_NOT_FOUND_CODE,"?????????????????????")));
                }else {
                    //??????????????????????????????
                    writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.PASSWORD_ERROR_CODE,"???????????????")));
                }
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     *?????????????????????
     * @return
     */
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.PERMISSONS_ERROR_CODE,"????????????")));
                writer.flush();
                writer.close();
            }
        };
    }

    /**
     *???????????????
     * @return
     */
    public AuthenticationEntryPoint AuthenticationEntryPoint(){
        return new AuthenticationEntryPoint(){
            @Override
            public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
               //????????????????????? /login ?????????????????????json?????????
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println(JSONUtil.toJsonStr(JsonResult.failure(LoginUtil.TOKEN_ERROR_CODE,"?????????????????????????????????????????????")));
                writer.flush();
                writer.close();
            }
        };
    }

}
