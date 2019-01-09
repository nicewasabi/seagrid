package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: GridModel
 * @Description: 格点模型
 * @author: songwj
 * @date: 2017-8-15 下午3:53:29
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class GridModel {
	
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
	private String batch;
	
	/**
	 * 时效
	 */
	private String timelimit;
	
	/**
	 * 层次
	 */
	private String layer;
	
	/**
	 * 格点文件所对应的要素名
	 */
	private String parameter;
	
	/**
	 * 起始经度
	 */
	private double lonStart;
	
	/**
	 * 起始纬度
	 */
	private double latStart;
	
	/**
	 * 终止经度
	 */
	private double lonEnd;
	
	/**
	 * 终止纬度
	 */
	private double latEnd;
	
	/**
	 * 经度格距
	 */
	private double lonGap;
	
	/**
	 * 纬度格距
	 */
	private double latGap;
	
	/**
	 * 纬向格点数
	 */
	private int xCount;
	
	/**
	 * 经向格点数
	 */
	private int yCount;
	
	/**
	 * 存放格点文件数据值
	 */
	private Point[][] points;

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

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(String timelimit) {
		this.timelimit = timelimit;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public double getLonStart() {
		return lonStart;
	}

	public void setLonStart(double lonStart) {
		this.lonStart = lonStart;
	}

	public double getLatStart() {
		return latStart;
	}

	public void setLatStart(double latStart) {
		this.latStart = latStart;
	}

	public double getLonEnd() {
		return lonEnd;
	}

	public void setLonEnd(double lonEnd) {
		this.lonEnd = lonEnd;
	}

	public double getLatEnd() {
		return latEnd;
	}

	public void setLatEnd(double latEnd) {
		this.latEnd = latEnd;
	}

	public double getLonGap() {
		return lonGap;
	}

	public void setLonGap(double lonGap) {
		this.lonGap = lonGap;
	}

	public double getLatGap() {
		return latGap;
	}

	public void setLatGap(double latGap) {
		this.latGap = latGap;
	}

	public int getxCount() {
		return xCount;
	}

	public void setxCount(int xCount) {
		this.xCount = xCount;
	}

	public int getyCount() {
		return yCount;
	}

	public void setyCount(int yCount) {
		this.yCount = yCount;
	}

	public Point[][] getPoints() {
		return points;
	}

	public void setPoints(Point[][] points) {
		this.points = points;
	}
	
}
