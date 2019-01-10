/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: Section7DaysController.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.seaFogFeature.controller

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月10日 下午3:54:13

 * @version: V1.0  

 */
package com.webyun.seagrid.seaFogFeature.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**

 * @ClassName: Section7DaysController

 * @Description: TODO

 * @author: wasabi

 * @date: 2019年1月10日 下午3:54:13


 */
@Controller
@RequestMapping("/seaFogFeature/section7days")
public class Section7DaysController {
	
	private Logger log = Logger.getLogger(Section7DaysController.class);
	
	
	
	@RequestMapping("/main.do")
	public String toMain() {
		return "/seaFogFeature/section7days";
	}
	


}
