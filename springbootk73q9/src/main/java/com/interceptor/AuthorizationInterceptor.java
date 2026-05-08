package com.interceptor;

import java.io.PrintWriter;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;

import com.annotation.IgnoreAuth;
import com.entity.EIException;
import com.entity.TokenEntity;
import com.service.TokenService;
import com.utils.R;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String LOGIN_TOKEN_KEY = "Token";

    @Autowired
    private TokenService tokenService;
    
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

	// 预检请求直接放行，跨域由全局 CORS 配置处理
	if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
        	response.setStatus(HttpStatus.OK.value());
            return true;
        }
        
        IgnoreAuth annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }

        //从header中获取token
        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            String authorization = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(authorization) && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
            }
        }
        
        /**
         * 不需要验证权限的方法直接放过
         */
        if(annotation!=null) {
        	return true;
        }
        
        TokenEntity tokenEntity = null;
        if(StringUtils.isNotBlank(token)) {
        	tokenEntity = tokenService.getTokenEntity(token);
        }
        
        if(tokenEntity != null) {
        	request.getSession().setAttribute("userId", tokenEntity.getUserid());
        	request.getSession().setAttribute("role", tokenEntity.getRole());
        	request.getSession().setAttribute("tableName", tokenEntity.getTablename());
        	request.getSession().setAttribute("username", tokenEntity.getUsername());
        	return true;
        }
        
		PrintWriter writer = null;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
		    writer = response.getWriter();
		    writer.print(JSONObject.toJSONString(R.error(401, "请先登录")));
		} finally {
		    if(writer != null){
		        writer.close();
		    }
		}
//				throw new EIException("请先登录", 401);
		return false;
    }
}
