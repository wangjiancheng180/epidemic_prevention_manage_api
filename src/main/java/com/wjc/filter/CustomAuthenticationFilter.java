package com.wjc.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author 王建成
 * @date 2022/5/22--15:32
 * 重写用户名密码拦截器使之支持json
 */

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            //如果是一个json请求
            try {
                //拿到输入流
                InputStream is = request.getInputStream();
                String json = IoUtil.read(is, Charset.forName("utf-8"));
                JSONObject jsonObject = JSONUtil.parseObj(json);
                Map map = jsonObject.toBean(Map.class);
                String username = (String) map.get("username");
                String password = (String) map.get("password");
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                this.setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (Exception e) {
                //这里需要捕获全局异常，因为登录要是没有进行图片滑动验证，就会抛出异常，这时候request的输入流会关闭，导致hutool宝IORuntimeException异常
                return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken("",""));
            }
        }
        return super.attemptAuthentication(request,response);
    }
}
