package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: Point
 * @Description: 点实体
 * @author: songwj
 * @date: 2017-8-15 下午3:51:12
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class Point {
	
	/**
	 * 经度值
	 */
	private double x;
	
	/**
	 * 纬度值
	 */
	private double y;
	
	/**
	 * 海拔高度
	 */
	private double altitude;
	
	/**
	 * 点值，默认为缺报值9999.00
	 */
	private double value = 9999.00;
	
	public Point() {}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Point(double x, double y, double value) {
		super();
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public Point(double x, double y, double altitude, double value) {
		super();
		this.x = x;
		this.y = y;
		this.altitude = altitude;
		this.value = value;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getAltitude() {
		return altitude;
	}
	
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
}