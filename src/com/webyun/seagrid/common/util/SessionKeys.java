package com.webyun.seagrid.common.util;
/**
 * @Project: dataShare
 * @Title: ApplicationKeys
 * @Description: applicationContext中保存数据的key
 * @author: zhangx
 * @date: 2017年6月28日 下午8:18:28
 * @company: webyun
 * @Copyright: Copyright (c) 2017
 * @version v1.0
 */
public interface SessionKeys {
	/**
	 * 用户登录验证码
	 */
	String VERIFICATION_KEY = "verification";
	
	/**
	 * 保存用户信息
	 */
	String SESSION_USER_KEY = "user";
	
	/**
	 * 保存用户所有的权限
	 */
	String USER_PERMISSION_KEY = "user_permission";
	
	/**
	 * 保存用户所有的角色
	 */
	String USER_ROLE_KEY = "user_role";
	
	
}
