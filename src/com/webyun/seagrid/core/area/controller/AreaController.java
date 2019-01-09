package com.webyun.seagrid.core.area.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webyun.seagrid.common.model.Point;
import com.webyun.seagrid.common.util.FileConfigUtil;
import com.webyun.seagrid.common.util.GridFileUtil;
import com.webyun.seagrid.core.area.util.AreaCheckUtil;
import com.webyun.seagrid.monitor.task.ShellTask;


/**
 * @Project: seaGrid
 * @Title: AreaController
 * @Description: 一致性检验controller
 * @author: zhongsb
 * @date: 2017年9月19日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：下午5:35:35
 * @Version :V1.0
 */
@Controller
@RequestMapping("/area")
public class AreaController {
	
	@Autowired
	private ShellTask task;
	
	/**
	* @Description: 主页面入口
	* @Author ：zhongsb
	* @return   
	*/
	@RequestMapping("/index.do")
	public String areaIndex(){
		return "area/area";
	}
	
	/**
	* @Description: 区域提取，执行shell脚本
	* @Author ：zhongsb
	* @param northLatitude 北纬
	* @param southLatitude 南纬
	* @param westLongitude 西经
	* @param eastLongitude 东经
	* @return   
	*/
	@RequestMapping("/getArea.do")
	@ResponseBody
	public String getArea(@RequestParam("northLatitude") String northLatitude, @RequestParam("southLatitude") 
		String southLatitude,@RequestParam("westLongitude") String westLongitude,@RequestParam("eastLongitude")
		String eastLongitude){
		String shellPath = FileConfigUtil.getProperty("area.shell.path");
		String logPath = FileConfigUtil.getProperty("area.shell.log");
		task.runShell(shellPath, logPath);
		return "123";
	}
	
	/**
	* @Description: 根据日期+时效，经纬度计算最后的结果
	* @Author ：zhongsb
	* @param searchDate 搜索日期
	* @param timeAge 时效
	* @param northLatitude 北纬
	* @param southLatitude 南纬
	* @param westLongitude 西经
	* @param eastLongitude 东经
	* @return   
	*/
	@RequestMapping("/getLastResult.do")
	@ResponseBody
	public Map<String, Object> getLastResult(@RequestParam("productCode") String productCode, @RequestParam("searchDate") String searchDate, 
			@RequestParam("timeAge") String timeAge,
			@RequestParam("northLatitude") String northLatitude, @RequestParam("southLatitude") String southLatitude, 
			@RequestParam("westLongitude") String westLongitude, @RequestParam("eastLongitude") String eastLongitude){
		
		
		System.out.println("searchDate="+searchDate);
		System.out.println("timeAge="+timeAge);
		StringBuffer yyyymmddhh = new StringBuffer();
		yyyymmddhh.append(searchDate).append(timeAge);
		Map<String, Object> datas = AreaCheckUtil.getAreaCheckSingleDatas("ec", yyyymmddhh.toString(), Double.parseDouble(westLongitude), 
				Double.parseDouble(southLatitude), 
				Double.parseDouble(eastLongitude), Double.parseDouble(northLatitude), productCode);
		
		Map<String, Object> datas2 = AreaCheckUtil.getAreaCheckSingleDatas("ncep", yyyymmddhh.toString(), Double.parseDouble(westLongitude), 
				Double.parseDouble(southLatitude), 
				Double.parseDouble(eastLongitude), Double.parseDouble(northLatitude), productCode);
		datas.put("ncepYdata", datas2.get("yDatas"));
		return datas;
	}
	

	/**
	* @Description: 获取前10个时效的文件名
	* @Author ：zhongsb
	* @param yyyymmddhh
	* @param num 个数
	* @param 逐span个小时
	* @return  文件名数组
	*/
	private static List<String> getFileNameList(String yyyymmddhh, int num, int span) {
		List<String> fileNameList = new ArrayList<String>();
		for(int i = 0; i <= num; i++){
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(Integer.parseInt(yyyymmddhh.substring(0, 4)), Integer.parseInt(yyyymmddhh.substring(4, 6))-1, 
					Integer.parseInt(yyyymmddhh.substring(6, 8)), Integer.parseInt(yyyymmddhh.substring(8, 10)), 0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
			calendar.add(Calendar.HOUR_OF_DAY, -span * i);
			Date date = calendar.getTime();
			sdf.format(date);
			String fileName = null;
			if(span*i < 10){
				fileName = sdf.format(date)+".00"+span*i;
			}else if(span*i < 100){
				fileName = sdf.format(date)+".0"+span*i;
			}else if(span*i < 1000) {
				fileName = sdf.format(date)+"."+span*i;
			}
			fileNameList.add(fileName.substring(2));
			
		}
		
		return fileNameList;
	}
	
	public static void main(String[] args) {
		List<String> fileNameList = getFileNameList("2017120608", 8, 1);
		fileNameList:for (String string : fileNameList) {
			System.out.println(string);
		}
	}
	


}
