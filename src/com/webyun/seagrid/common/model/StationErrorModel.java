package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: ErrorModel
 * @Description: 站点误差模型
 * @author: songwj
 * @date: 2017-8-21 下午5:16:10
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class StationErrorModel {

	/**
	 * 众数
	 */
	private double mode;
	
	/**
	 * 实况风速
	 */
	private double actualWindSpeed;
	
	/**
	 * 平均误差
	 */
	private double avgError;
	
	/**
	 * 平均绝对误差
	 */
	private double avgAbsError;

	public StationErrorModel() {}
	
	public StationErrorModel(double mode, double actualWindSpeed, double avgError, double avgAbsError) {
		super();
		this.mode = mode;
		this.actualWindSpeed = actualWindSpeed;
		this.avgError = avgError;
		this.avgAbsError = avgAbsError;
	}

	public double getMode() {
		return mode;
	}

	public void setMode(double mode) {
		this.mode = mode;
	}

	public double getActualWindSpeed() {
		return actualWindSpeed;
	}

	public void setActualWindSpeed(double actualWindSpeed) {
		this.actualWindSpeed = actualWindSpeed;
	}

	public double getAvgError() {
		return avgError;
	}

	public void setAvgError(double avgError) {
		this.avgError = avgError;
	}

	public double getAvgAbsError() {
		return avgAbsError;
	}

	public void setAvgAbsError(double avgAbsError) {
		this.avgAbsError = avgAbsError;
	}
	
}
