package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: ErrorModel
 * @Description: 误差模型
 * @author: songwj
 * @date: 2017-8-22 下午5:17:10
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class ErrorModel {

	/**
	 * 平均误差
	 */
	private double avgError;
	
	/**
	 * 平均绝对误差
	 */
	private double avgAbsError;
	
	/**
	 * 偏小次数
	 */
	private int smallCount;
	
	/**
	 * 正确次数
	 */
	private int rightCount;
	
	/**
	 * 偏大次数
	 */
	private int largeCount;
	
	/**
	 * 累加次数
	 */
	private int count;

	public ErrorModel() {}
	
	public ErrorModel(double avgError, double avgAbsError, int smallCount, int rightCount, int largeCount, int count) {
		super();
		this.avgError = avgError;
		this.avgAbsError = avgAbsError;
		this.smallCount = smallCount;
		this.rightCount = rightCount;
		this.largeCount = largeCount;
		this.count = count;
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

	public int getLargeCount() {
		return largeCount;
	}

	public void setLargeCount(int largeCount) {
		this.largeCount = largeCount;
	}

	public int getSmallCount() {
		return smallCount;
	}

	public void setSmallCount(int smallCount) {
		this.smallCount = smallCount;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
