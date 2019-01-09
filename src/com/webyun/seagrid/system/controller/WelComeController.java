package com.webyun.seagrid.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webyun.seagrid.common.util.SessionKeys;

/**
 * @Project: dataShare
 * @Title: WelComeController
 * @Description: 系统入口
 * @author: zhangx
 * @date: 2017年6月28日 下午8:15:32
 * @company: webyun
 * @Copyright: Copyright (c) 2017
 * @version v1.0
 */
@Controller
public class WelComeController {
	
	/**
	 * @Title: 系统入口
	 * @param req
	 * @return
	 * @author: zhangx
	 * @date: 2017年7月5日 上午10:37:23
	 * @version v1.0
	 */
	@RequestMapping("/welcome.do")
	public String welcome(HttpServletRequest req){
		Object obj = req.getSession().getAttribute(SessionKeys.SESSION_USER_KEY);
		if(obj == null) {
			return "login";//没有登录的，跳转到登录页
		} else {//已经登录了
			return "product/customization";//登录的，跳转到用户操作页面
		}
	}
	
	
}
