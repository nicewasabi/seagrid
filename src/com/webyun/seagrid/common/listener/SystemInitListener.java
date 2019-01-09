package com.webyun.seagrid.common.listener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @Project: dataShare
 * @Title: SystemInitListener
 * @Description: 项目初始化
 * @author: zhangx
 * @date: 2017年2月9日 下午4:53:19
 * @company: webyun
 * @Copyright: Copyright (c) 2017
 * @version v1.0
 */
@Service
public class SystemInitListener  implements ServletContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemInitListener.class);
	
	public static WebApplicationContext webapp;
	
	@Override
	public void setServletContext(ServletContext app) {
		webapp= WebApplicationContextUtils.getWebApplicationContext(app);
		
	}


	

	
	
	
}
