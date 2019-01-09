package com.webyun.seagrid.common.util;

import java.io.FileOutputStream;

import org.junit.Test;

import com.webyun.seagrid.common.model.GridModel;
import com.webyun.seagrid.common.model.Micaps11GridModel;
import com.webyun.seagrid.common.model.WindSpdAndDirModel;

/**
 * @Project: seaGrid
 * @Title: GridFileUtilTest
 * @Description: 格点工具单元测试类
 * @author: songwj
 * @date: 2017-8-15 下午4:23:41
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class GridFileUtilTest {
	
	/**
	 * @Description: 解析Micaps第2类格点文件测试
	 * @author: songwj
	 * @date: 2017-9-14 下午5:05:29
	 */
	@Test
	public void testMicaps2ToGridModel() {
		long start = System.currentTimeMillis();
		String micaps11FilePath = "D:/seaGrid/meanWindProductOut/micaps2/2017102800/2017102800.003";
		GridModel gridModel = GridFileUtil.micaps2ToGridModel(micaps11FilePath, "UTF-8", true);
		System.out.println(gridModel);
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 将MICAPS第11类文件解析为格点模型测试
	 * @author: songwj
	 * @date: 2017-9-10 下午1:00:08
	 */
	@Test
	public void testMicaps11ToGridModel() {
		long start = System.currentTimeMillis();
		String micaps11FilePath = "D:/seaGrid/meanWindProductOut/members/c00/2017090312.003";
		Micaps11GridModel gridModel = GridFileUtil.micaps11ToGridModel(micaps11FilePath, "UTF-8");
		System.out.println(gridModel);
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 将10米风GrADS二进制格点文件转为格点模型测试
	 * @author: songwj
	 * @date: 2017-8-23 上午9:40:43
	 */
	@Test
	public void testGradsToGridModel() {
		long start = System.currentTimeMillis();
		String gradsFilePath = "D:/seaGrid/10mwind/2016030100/ecmf_short3h_10m_uv-component_of_wind.dat";
		GridFileUtil.gradsWindToGridModel(gradsFilePath, "UTF-8");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 10米风数据处理测试
	 * @author: songwj
	 * @date: 2017-8-15 下午4:24:35
	 */
	@Test
	public void testGrads10mWindProcess() {
		long start = System.currentTimeMillis();
		String gradsFilePath = "D:/seaGrid/10mwind/2016030100/ecmf_short3h_10m_uv-component_of_wind.dat";
		GridFileUtil.grads10mWindProcess(gradsFilePath, "UTF-8");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 处理指定时间段的10m风数据测试
	 * @author: songwj
	 * @date: 2017-8-23 下午3:42:11
	 */
	@Test
	public void testGrads10mWindProcessByTimeRange() {
		long start = System.currentTimeMillis();
		GridFileUtil.grads10mWindProcessByTimeRange("2014112000", "2017033112");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 累加统计指定时间段的10m风平均误差、平均绝对误差、偏小概率、正确概率、偏大概率、偏大、正确和偏小区间测试
	 * @author: songwj
	 * @date: 2017-8-23 下午3:42:11
	 */
	@Test
	public void testGet10mMeanWindAccumulateResult() {
		long start = System.currentTimeMillis();
		GridFileUtil.get10mMeanWindAccumulateResult("2014112000", "2017033112");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 获取指定时间段内的10m风平均偏大、偏小误差测试
	 * @author: songwj
	 * @date: 2017-8-29 下午5:26:51
	 */
	@Test
	public void testGet10mMeanWindAvgLargeAndSmallError() {
		long start = System.currentTimeMillis();
		GridFileUtil.get10mMeanWindAvgLargeAndSmallError("2014112000", "2017033112");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 将10m风数据文件先插值为0.1公里场再求众数最后处理为MICAPS文件测试
	 * @author: songwj
	 * @date: 2017-9-4 下午7:39:05
	 */
	@Test
	public void testGrads10mWindToMicapsProcess() {
		long start = System.currentTimeMillis();
		String gradsFilePath = "D:/seaGrid/products/daily/grads/2017102800/ecmf_short3h_10m_uv-component_of_wind.dat";
		String avgDeviationFilePath = "D:/seaGrid/meanWindProductOut/2014112000_2017033112_avg_deviation.txt";
		String rangeFilePath = "D:/seaGrid/meanWindProductOut/seaGrid/2014112000_2017033112_range.txt";
		GridFileUtil.grads10mWindToMicapsProcess(gradsFilePath, avgDeviationFilePath, rangeFilePath, "UTF-8");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 将MICAPS格点文件处理为图像输出测试
	 * @author: songwj
	 * @date: 2017-9-11 下午8:28:18
	 * @throws Exception
	 */
	@Test
	public void testMicapsToImage() throws Exception {
		String micaps4FilePath = "D:/seaGrid/meanWindProductOut/micaps2/2017090312/2017090312.003";
		GridFileUtil.micapsToImage(micaps4FilePath, "utf-8", 2, "wind", new FileOutputStream("D:/seaGrid/meanWindProductOut/micaps2/2017090312/2017090312.003.png"));
	}
	
	/**
	 * @Description: 根据水平和垂直风向值计算叠加后的风速和风向测试
	 * @author: songwj
	 * @date: 2017-9-17 下午8:29:38
	 */
	@Test
	public void testGetWindSpeedAndDirection() {
		double u = 3.7903443;
		double v = -2.6936575;
		WindSpdAndDirModel model = GridFileUtil.getWindSpeedAndDirection(u, v);
		System.out.println("风向：" + model.getWindDirection() + "，风速：" + model.getWindSpeed());
	}
	
	/**
	 * @Description: 10米阵风数据处理测试
	 * @author: songwj
	 * @date: 2017-8-15 下午4:24:35
	 */
	@Test
	public void testGrads10mMaxWindProcess() {
		long start = System.currentTimeMillis();
		String gradsFilePath = "D:/seaGrid/10mwind/2016030100/ecmf_short_10m_wind_gust_in_last_6h.dat";
		GridFileUtil.grads10mMaxWindProcess(gradsFilePath, "UTF-8");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 10米阵风时段数据处理测试
	 * @Author: songwj
	 * @Date: 2017-11-30 上午11:25:40
	 */
	@Test
	public void testGrads10mMaxWindProcessByTimeRange() {
		long start = System.currentTimeMillis();
		GridFileUtil.grads10mMaxWindProcessByTimeRange("2015032400", "2017063012");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 累加统计指定时间段的10m阵风平均误差、平均绝对误差、偏小概率、正确概率、偏大概率、偏大、正确和偏小区间测试
	 * @author: songwj
	 * @date: 2017-8-23 下午3:42:11
	 */
	@Test
	public void testGet10mMaxWindAccumulateResult() {
		long start = System.currentTimeMillis();
		GridFileUtil.get10mMaxWindAccumulateResult("2015032400", "2017063012");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 获取指定时间段内的10m阵风平均偏大、偏小误差测试
	 * @author: songwj
	 * @date: 2017-8-29 下午5:26:51
	 */
	@Test
	public void testGet10mMaxWindAvgLargeAndSmallError() {
		long start = System.currentTimeMillis();
		GridFileUtil.get10mMaxWindAvgLargeAndSmallError("2015032400", "2017063012");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 将10m阵风数据文件先插值为0.1公里场再求众数订正最后处理为MICAPS文件测试
	 * @Author: songwj
	 * @Date: 2017-12-1 下午6:50:02
	 */
	@Test
	public void testGrads10mMaxWindToMicapsProcess() {
		long start = System.currentTimeMillis();
		String gradsFilePath = "D:/seaGrid/products/daily/grads/2017102800/ecmf_short_10m_wind_gust_in_last_6h.dat";
		String avgDeviationFilePath = "D:/seaGrid/maxWindProductOut/2015032400_2017063012_avg_deviation.txt";
		String rangeFilePath = "D:/seaGrid/maxWindProductOut/2015032400_2017063012_range.txt";
		GridFileUtil.grads10mMaxWindToMicapsProcess(gradsFilePath, avgDeviationFilePath, rangeFilePath, "UTF-8");
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
	/**
	 * @Description: 区域提取测试
	 * @Author: songwj
	 * @Date: 2017-12-20 下午6:41:45
	 */
	@Test
	public void testRegionExtract() {
		long start = System.currentTimeMillis();
		String micaps11FilePath = "D:/seaGrid/areaCheck/ecData/uv10/17113008.120";
		Micaps11GridModel gridModel = GridFileUtil.micaps11ToGridModel(micaps11FilePath, "UTF-8");
		double lonStart = 95.18;
		double latStart = 44.96;
		double lonEnd = 141.08;
		double latEnd = -9.06;
		GridModel regionExtractModel = GridFileUtil.regionExtract(gridModel, lonStart, latStart, lonEnd, latEnd);
		System.out.println(regionExtractModel);
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + (end - start)/1000.0 + "秒");
	}
	
}
