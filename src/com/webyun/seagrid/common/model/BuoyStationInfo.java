package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: BuoyStationInfo
 * @Description: 浮标站点信息
 * @Author: songwj
 * @Date: 2017-11-15 下午5:09:45
 * @Company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @Version v1.0
 */
public class BuoyStationInfo {
	
	/**
	 * 站点编号
	 */
	public String stationCode;
	
	/**
	 * 经度
	 */
	public double lon;
	
	/**
	 * 纬度
	 */
	public double lat;
	
	/**
	 * 浮标名称
	 */
	public String buoyName;
	
	/**
	 * 浮标类型
	 */
	public String buoyType;
	
	/**
	 * 浮标等级
	 */
	public int level;
	
	public BuoyStationInfo() {}
	
	public BuoyStationInfo(String stationCode, double lon, double lat, String buoyName, String buoyType, int level) {
		super();
		this.stationCode = stationCode;
		this.lon = lon;
		this.lat = lat;
		this.buoyName = buoyName;
		this.buoyType = buoyType;
		this.level = level;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getBuoyName() {
		return buoyName;
	}

	public void setBuoyName(String buoyName) {
		this.buoyName = buoyName;
	}

	public String getBuoyType() {
		return buoyType;
	}

	public void setBuoyType(String buoyType) {
		this.buoyType = buoyType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
