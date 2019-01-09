package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: WindSpdAndDirModel
 * @Description: 风模型
 * @author: songwj
 * @date: 2017-8-18 下午3:30:25
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class WindSpdAndDirModel {
	
	/**
	 * 风速
	 */
	private double windSpeed;
	
	/**
	 * 风向
	 */
	private double windDirection;
	
	public WindSpdAndDirModel() {}

	public WindSpdAndDirModel(double windSpeed, double windDirection) {
		super();
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(double windDirection) {
		this.windDirection = windDirection;
	}
	
}
