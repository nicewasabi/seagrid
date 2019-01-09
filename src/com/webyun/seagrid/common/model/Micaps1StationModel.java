package com.webyun.seagrid.common.model;

import java.io.Serializable;

/**
 * @Project: ems
 * @Title: Micaps1StationModel
 * @Description: Micaps第1类站点模型
 * @author: songwj
 * @date: 2016-12-26 下午6:01:08
 * @company: webyun
 * @Copyright: Copyright (c) 2015-2016 Webyun. All Rights Reserved.
 * @version v1.0
 */
@SuppressWarnings("serial")
public class Micaps1StationModel implements Serializable {
	/**
	 * 站号编号
	 */
	private String stationCode;

	/**
	 * 经度
	 */
	private double lon;
	
	/**
	 * 纬度
	 */
	private double lat;
	
	/**
	 * 海拔高度
	 */
	private double alt;
	
	/**
	 * 站点级别
	 */
	private int siteLevel;
	
	/**
	 * 总云量
	 */
	private int totalCloudAmount;
	
	/**
	 * 风向
	 */
	private double windDirection;
	
	/**
	 * 风速
	 */
	private double windSpeed;
	
	/**
	 * 海平面气压
	 */
	private int seaLevelPressure;
	
	/**
	 * 3小时变压
	 */
	private int livePressure3h;
	
	/**
	 * 过去天气1
	 */
	private int pastWeather1;
	
	/**
	 * 过去天气2
	 */
	private int pastWeather2;
	
	/**
	 * 6小时降水
	 */
	private double rainfall6h;
	
	/**
	 * 低云状
	 */
	private int lowCloud;
	
	/**
	 * 低云量
	 */
	private int lowCloudAmount;
	
	/**
	 * 低云高
	 */
	private int lowCloudHigh;
	
	/**
	 * 露点
	 */
	private double dewPoint;
	
	/**
	 * 能见度
	 */
	private double vis;
	
	/**
	 * 现在天气
	 */
	private int currentWeather;
	
	/**
	 * 温度
	 */
	private double temperature;
	
	/**
	 * 中云状
	 */
	private int mediumCloud;
	
	/**
	 * 高云状
	 */
	private int highCloud;
	
	/**
	 * 标志1
	 */
	private int mark1;
	
	/**
	 * 标志2
	 */
	private int mark2;

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

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public int getSiteLevel() {
		return siteLevel;
	}

	public void setSiteLevel(int siteLevel) {
		this.siteLevel = siteLevel;
	}

	public int getTotalCloudAmount() {
		return totalCloudAmount;
	}

	public void setTotalCloudAmount(int totalCloudAmount) {
		this.totalCloudAmount = totalCloudAmount;
	}

	public double getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public int getSeaLevelPressure() {
		return seaLevelPressure;
	}

	public void setSeaLevelPressure(int seaLevelPressure) {
		this.seaLevelPressure = seaLevelPressure;
	}

	public int getLivePressure3h() {
		return livePressure3h;
	}

	public void setLivePressure3h(int livePressure3h) {
		this.livePressure3h = livePressure3h;
	}

	public int getPastWeather1() {
		return pastWeather1;
	}

	public void setPastWeather1(int pastWeather1) {
		this.pastWeather1 = pastWeather1;
	}

	public int getPastWeather2() {
		return pastWeather2;
	}

	public void setPastWeather2(int pastWeather2) {
		this.pastWeather2 = pastWeather2;
	}

	public double getRainfall6h() {
		return rainfall6h;
	}

	public void setRainfall6h(double rainfall6h) {
		this.rainfall6h = rainfall6h;
	}

	public int getLowCloud() {
		return lowCloud;
	}

	public void setLowCloud(int lowCloud) {
		this.lowCloud = lowCloud;
	}

	public int getLowCloudAmount() {
		return lowCloudAmount;
	}

	public void setLowCloudAmount(int lowCloudAmount) {
		this.lowCloudAmount = lowCloudAmount;
	}

	public int getLowCloudHigh() {
		return lowCloudHigh;
	}

	public void setLowCloudHigh(int lowCloudHigh) {
		this.lowCloudHigh = lowCloudHigh;
	}

	public double getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(double dewPoint) {
		this.dewPoint = dewPoint;
	}

	public double getVis() {
		return vis;
	}

	public void setVis(double vis) {
		this.vis = vis;
	}

	public int getCurrentWeather() {
		return currentWeather;
	}

	public void setCurrentWeather(int currentWeather) {
		this.currentWeather = currentWeather;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public int getMediumCloud() {
		return mediumCloud;
	}

	public void setMediumCloud(int mediumCloud) {
		this.mediumCloud = mediumCloud;
	}

	public int getHighCloud() {
		return highCloud;
	}

	public void setHighCloud(int highCloud) {
		this.highCloud = highCloud;
	}

	public int getMark1() {
		return mark1;
	}

	public void setMark1(int mark1) {
		this.mark1 = mark1;
	}

	public int getMark2() {
		return mark2;
	}

	public void setMark2(int mark2) {
		this.mark2 = mark2;
	}
}
