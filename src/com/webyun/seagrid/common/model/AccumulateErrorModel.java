package com.webyun.seagrid.common.model;

import java.math.BigDecimal;

/**
 * @Project: seaGrid
 * @Title: AccumulateErrorModel
 * @Description: 累加误差模型
 * @author: songwj
 * @date: 2017-8-25 上午10:56:03
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class AccumulateErrorModel {

	/**
	 * 平均误差
	 */
	private BigDecimal avgError;
	
	/**
	 * 平均绝对误差
	 */
	private BigDecimal avgAbsError;
	
	/**
	 * 偏小累加次数
	 */
	private int smallCount;
	
	/**
	 * 正确累加次数
	 */
	private int rightCount;
	
	/**
	 * 偏大累加次数
	 */
	private int largeCount;
	
	/**
	 * 累加次数
	 */
	private int count;

	public AccumulateErrorModel() {}

	public AccumulateErrorModel(BigDecimal avgError, BigDecimal avgAbsError, int smallCount, int rightCount, int largeCount, int count) {
		super();
		this.avgError = avgError;
		this.avgAbsError = avgAbsError;
		this.smallCount = smallCount;
		this.rightCount = rightCount;
		this.largeCount = largeCount;
		this.count = count;
	}

	public BigDecimal getAvgError() {
		return avgError;
	}

	public void setAvgError(BigDecimal avgError) {
		this.avgError = avgError;
	}

	public BigDecimal getAvgAbsError() {
		return avgAbsError;
	}

	public void setAvgAbsError(BigDecimal avgAbsError) {
		this.avgAbsError = avgAbsError;
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

	public int getLargeCount() {
		return largeCount;
	}

	public void setLargeCount(int largeCount) {
		this.largeCount = largeCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
