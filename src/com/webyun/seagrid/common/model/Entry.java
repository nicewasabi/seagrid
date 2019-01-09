package com.webyun.seagrid.common.model;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @Project: ems
 * @Title: Entry
 * @Description: 封装的每个调色板对象
 * @author: songwj
 * @date: 2016-1-21 下午4:06:24
 * @company: webyun
 * @Copyright: Copyright (c) 2015-2016
 * @version v1.0
 */
@SuppressWarnings("serial")
@XStreamAlias("entry")
public class Entry implements Serializable {
	/**
	 * 数值
	 */
	@XStreamAsAttribute
	private float value;
	
	/**
	 * 颜色分量，alpha表示透明度
	 */
	@XStreamAsAttribute
	private String rgba;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getRgba() {
		return rgba;
	}

	public void setRgba(String rgba) {
		this.rgba = rgba;
	}
}