package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: StationPoint
 * @Description: 站点模型
 * @author: songwj
 * @date: 2017-9-22 上午10:00:49
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class StationPoint extends Point {
	/**
	 * 站点编号
	 */
	private String stationCode;

	public StationPoint() {}
	
	public StationPoint(String stationCode, double lon, double lat) {
		super(lon, lat);
		this.stationCode = stationCode;
	}
	
	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	
}
