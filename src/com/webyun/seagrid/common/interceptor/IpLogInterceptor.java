package com.webyun.seagrid.common.interceptor;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.webyun.seagrid.common.listener.SystemInitListener;
import com.webyun.seagrid.common.model.IpLog;
import com.webyun.seagrid.common.util.SessionKeys;
import com.webyun.seagrid.common.util.ToolWeb;
import com.webyun.seagrid.core.system.service.IpLogService;



public class IpLogInterceptor extends HandlerInterceptorAdapter{ 
	
    private final Logger loger = LoggerFactory.getLogger(IpLogInterceptor.class);  
    
    /**
     * 进入controller前执行
     */
    @Override    
    public boolean preHandle(HttpServletRequest request,  HttpServletResponse response, Object handler) throws Exception {   
    	
        String ip = ToolWeb.getIpAddr(request);
        
    	String method = request.getMethod();
        
    	String url = request.getRequestURI();  
    	//.app是来自客户端的请求
    	if(url.indexOf(".") > 1 && !url.contains(".html") && !url.contains(".do") && !url.contains(".app")) {
    		return true;
    	}
        
        Map<String, String[]> ps = request.getParameterMap();
        Set<Entry<String, String[]>> entrySet = ps.entrySet();
        Map<String, String> params = new HashMap<>();
        for (Entry<String, String[]> entry : entrySet) {
        	String key = entry.getKey();
        	String value = Arrays.toString(entry.getValue());
        	params.put(key, value);
		}
        
        Object obj = request.getSession().getAttribute(SessionKeys.SESSION_USER_KEY);
        String userName = null;
        String userId = null;
        		
        
        IpLog log = new IpLog(UUID.randomUUID().toString(), ip, url, method, params.toString(), userName, userId, new Date());
        
        try {
        	IpLogService ipservice = SystemInitListener.webapp.getBean(IpLogService.class);
			ipservice.insert(log);
		} catch (Exception e) {
			loger.error(e.getMessage());
		} 
        return true;     
    }    
    
    /**
     * 进入controller后执行
     */
    @Override    
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {     
    	 
    }    
    
    /**
     * 最后执行
     */   
    @Override    
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  throws Exception {    
    	   
    }    
   
} 
