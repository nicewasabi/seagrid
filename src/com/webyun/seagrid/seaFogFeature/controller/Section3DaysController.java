/**  

 * Copyright © 2019webyun. All rights reserved.

 *

 * @Title: SeaFogFeatureController.java

 * @Prject: seaGrid

 * @Package: com.webyun.seagrid.seaFogFeature.controller

 * @Description: TODO

 * @author: wasabi  

 * @date: 2019年1月10日 下午3:46:39

 * @version: V1.0  

 */
package com.webyun.seagrid.seaFogFeature.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webyun.seagrid.objectiveMethodProduct.controller.HuananSeaFogController;

/**

 * @ClassName: SeaFogFeatureController

 * @Description: TODO

 * @author: wasabi

 * @date: 2019年1月10日 下午3:46:39


 */
@Controller
@RequestMapping("/seaFogFeature/section3days")
public class Section3DaysController {
	private Logger log = Logger.getLogger(Section3DaysController.class);
	
	@RequestMapping("/main.do")
	public String toMain() {
		return "/seaFogFeature/section3days";
	}
	


}
