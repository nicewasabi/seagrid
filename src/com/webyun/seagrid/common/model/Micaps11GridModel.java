package com.webyun.seagrid.common.model;

/**
 * @Project: seaGrid
 * @Title: Micaps11GridModel
 * @Description: Micaps第11类格点模型（矢量数据）
 * @author: songwj
 * @date: 2017-9-10 下午12:28:51
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class Micaps11GridModel extends GridModel {
	
	/**
	 * 存放文件V（垂直）方向数据值
	 */
	private Point[][] vPoints;

	public Point[][] getvPoints() {
		return vPoints;
	}

	public void setvPoints(Point[][] vPoints) {
		this.vPoints = vPoints;
	}
	
}
