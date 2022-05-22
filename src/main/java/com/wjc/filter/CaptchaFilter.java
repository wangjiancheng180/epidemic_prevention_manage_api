package com.wjc.filter;

import cn.hutool.core.util.StrUtil;
import com.wjc.captcha.dto.CaptchaResult;
import com.wjc.common.login.RedisKey;
import com.wjc.exception.CaptchaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 王建成
 * @date 2022/5/22--0:02
 * 图片验证过滤器
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         //这里先判断一下请求是否是访问登录的
        String uri = request.getRequestURI();
        if (uri.equals("/api/login")&&request.getMethod().equals("POST")){
            try{
                verify(request);
            }catch (CaptchaException e){
                //没有进行图片验证直接交给失败处理器处理
                failureHandler.onAuthenticationFailure(request,response,e);
            }
        }
        //进入下一个过滤器
        filterChain.doFilter(request,response);

    }

    private void verify(HttpServletRequest request) {
        //先获取sessionId
        String sessionId = request.getRequestedSessionId();
        //判断一下是否是第一次访问
        if(StrUtil.isEmpty(sessionId)){
            throw new CaptchaException("请先进行图片验证！");
        }
        //从redis中获取图片验证的结果
        CaptchaResult captchaResult = (CaptchaResult) redisTemplate.opsForHash().get(RedisKey.CAPTCHA_RESULT,sessionId);
        //如果验证为空或者验证结果失败都抛出验证失败异常
        if (captchaResult==null||!captchaResult.getSuccess()){
            throw new CaptchaException("图片验证失败！");
        }

//        redisTemplate.opsForHash().delete(RedisKey.CAPTCHA_RESULT,sessionId);
    }
}
