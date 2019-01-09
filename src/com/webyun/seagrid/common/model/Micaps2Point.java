package com.webyun.seagrid.common.model;

/**
 * @Project: ems
 * @Title: Micaps2Point
 * @Description: Micap第2类格点文件的点模型
 * @author: songwj
 * @date: 2016-12-28 下午3:47:58
 * @company: webyun
 * @Copyright: Copyright (c) 2015-2016 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class Micaps2Point extends Point {
	/**
	 * 温度
	 */
	private double temperature;
	
	/**
	 * 温度露点差
	 */
	private double dewPointDepression;
	
	/**
	 * 风向
	 */
	private double windDirection;
	
	/**
	 * 风速
	 */
	private double windSpeed;

	public Micaps2Point() {}
	
	public Micaps2Point(double x, double y, double temperature, double dewPointDepression, double windDirection, double windSpeed) {
		super(x, y, windSpeed);// 将风速值赋给value
		this.temperature = temperature;
		this.dewPointDepression = dewPointDepression;
		this.windDirection = windDirection;
		this.windSpeed = windSpeed;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getDewPointDepression() {
		return dewPointDepression;
	}

	public void setDewPointDepression(double dewPointDepression) {
		this.dewPointDepression = dewPointDepression;
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
}
