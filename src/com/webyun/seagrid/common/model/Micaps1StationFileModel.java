package com.webyun.seagrid.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * @Project: ems
 * @Title: Micaps1StationFileModel
 * @Description: Micaps第1类站点文件解析模型
 * @author: songwj
 * @date: 2016-12-26 下午5:53:09
 * @company: webyun
 * @Copyright: Copyright (c) 2015-2016 Webyun. All Rights Reserved.
 * @version v1.0
 */
@SuppressWarnings("serial")
public class Micaps1StationFileModel implements Serializable {
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 年
	 */
	private String year;
	
	/**
	 * 月
	 */
	private String month;
	
	/**
	 * 日
	 */
	private String day;
	
	/**
	 * 时次
	 */
	private String timeLevel;
	
	/**
	 * 总站点数
	 */
	private int stationNum;
	
	/**
	 * 站点信息集合
	 */
	private List<Micaps1StationModel> stationModels;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTimeLevel() {
		return timeLevel;
	}

	public void setTimeLevel(String timeLevel) {
		this.timeLevel = timeLevel;
	}

	public int getStationNum() {
		return stationNum;
	}

	public void setStationNum(int stationNum) {
		this.stationNum = stationNum;
	}

	public List<Micaps1StationModel> getStationModels() {
		return stationModels;
	}

	public void setStationModels(List<Micaps1StationModel> stationModels) {
		this.stationModels = stationModels;
	}
}
