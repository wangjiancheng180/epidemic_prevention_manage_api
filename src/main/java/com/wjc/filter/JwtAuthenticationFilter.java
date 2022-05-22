package com.wjc.filter;

import cn.hutool.core.util.StrUtil;
import com.wjc.common.login.LoginUtil;
import com.wjc.common.login.RedisKey;
import com.wjc.dto.system.AuthInfo;
import com.wjc.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 王建成
 * @date 2022/3/19--17:39
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter{

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //配置的自定义标识 Authorization
        String token = request.getHeader(LoginUtil.AUTH);
        //Bearer （有个空格）标识
        if (StrUtil.isNotBlank(token) && token.startsWith(LoginUtil.TOKEN_PREFIX)) {
            //生成的token中带有Bearer 标识，去掉标识后就剩纯粹的token了。
            String substring = token.substring(LoginUtil.TOKEN_PREFIX.length());
            try {
                //解析token拿到我们生成token的时候存进去的username
                Claims claims = tokenUtils.parseToken(substring);
                String userName = (String) claims.get("username");
                //从redis中获取用户资源权限
                AuthInfo authInfo = (AuthInfo) redisTemplate.opsForHash().get(RedisKey.USER_INFO,userName);
                if (StrUtil.isNotBlank(userName)) {
                    //将查询到的用户信息取其账号（登录凭证）以及密码去生成一个Authentication对象
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authInfo.getAuthorities()));
                    //将Authentication对象放进springsecurity上下文中（进行认证操作）
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }catch (Exception e){
                //这里在解析错误token或token过期时会报异常
                log.error("解析token异常"+e.getMessage());
            }
        }
        //走下一条过滤器
        chain.doFilter(request,response);
    }
}
