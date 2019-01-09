package com.webyun.seagrid.dataprocess.controller;

import java.util.Map;

import org.junit.Test;

/**
 * @Project: seaGrid
 * @Title: MeanWindControllerTest
 * @Description: GrADS10米平均风数据处理展示控制器方法测试
 * @Author: songwj
 * @Date: 2017-12-8 下午5:18:09
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
public class MeanWindControllerTest {

	/**
	 * @Description: 获取平均风误差检验数据测试
	 * @Author: songwj
	 * @Date: 2017-12-8 下午5:21:36
	 */
	@Test
	public void testGetMeanWindErrCheckSeriesData() {
		long start = System.currentTimeMillis();
		Map<String, Object> seriesData = new MeanWindController().getMeanWindErrCheckSeriesData("2017120808", "54558");
		System.out.println(seriesData);
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
}
