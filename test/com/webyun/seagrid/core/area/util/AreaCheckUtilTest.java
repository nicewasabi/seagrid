package com.webyun.seagrid.core.area.util;

import java.util.Map;

import org.junit.Test;

/**
 * @Project: seaGrid
 * @Title: AreaCheckUtilTest
 * @Description: 区域检验工具测试类
 * @Author: songwj
 * @Date: 2017-12-6 下午12:52:45
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
public class AreaCheckUtilTest {

	/**
	 * @Description: 获取指定预报时间指定区域的检验数据
	 * @Author: songwj
	 * @Date: 2017-12-6 下午12:53:42
	 */
	@Test
	public void testGetAreaCheckSingleDatas() {
		long start = System.currentTimeMillis();
		String datetime = "2017120508";
		String model = "ec";
		double lonStart = 96.08;
		double latStart = 44.08;
		double lonEnd = 141.06;
		double latEnd = -9.03;
		String prodType = "uv10";
		Map<String, Object> datas = AreaCheckUtil.getAreaCheckSingleDatas(model, datetime, lonStart, latStart, lonEnd, latEnd, prodType);
		System.out.println(datas.get("xLabs"));
		System.out.println(datas.get("yDatas"));
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
}
