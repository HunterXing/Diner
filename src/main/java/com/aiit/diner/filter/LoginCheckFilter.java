package com.aiit.diner.filter;

import com.aiit.diner.common.R;
import com.aiit.diner.utils.JwtUtils;
import com.aiit.diner.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * 登录检查过滤器
 * 拦截所有接口
 * @author xingheng
 */
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("LoginCheckFilter 执行了，拦截到请求 {}", request.getRequestURI()) ;

        // 1、获取本次请求的URL
        String requestURI = request.getRequestURI();
        // 不需要拦截的接口，白名单
        String[] whiteUrls = new String[]{
            "/diner/employee/login",
            "/diner/employee/logout",
        };

        // 2、判断本次请求是否需要处理
        boolean hasPermission = checkPermission(requestURI, whiteUrls);
        // 3、如果不需要处理，则直接放行
        if(hasPermission) {
            filterChain.doFilter(request, response);
            return;
        }
        // 4、判断登录状态，如果已登陆，则直接放行
        // 获取请求头中的token
        String token = request.getHeader("token");
        log.info("header中的token ---> {}", token);
        // 通过token获取用户id
        Object id = JwtUtils.getUserIdByToken(token);
        log.info("id ---> {}", id) ;
        // 从redis中获取 TOKEN${id}键的值，和token对比
        Object redisToken = RedisUtils.get("TOKEN"+id);
        log.info("redisToken ---> {}", redisToken);

        // 如果RedisToken和token一致，则放行
        if(redisToken != null && redisToken.equals(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 5、如果未登录，则返回未登录结果
        response.setCharacterEncoding("utf-8");
        response.getWriter()
                .write(JSON.toJSONString(R.error("暂未登录", 401)));
        log.info("用户未登录");
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要处理
     * @param requestURI
     * @param urls
     * @return boolean
     */
    public boolean checkPermission(String requestURI, String[] urls) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}