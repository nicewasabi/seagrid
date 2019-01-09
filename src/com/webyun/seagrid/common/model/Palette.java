package com.webyun.seagrid.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @Project: ems
 * @Title: Palette
 * @Description: 调色板
 * @author: songwj
 * @date: 2016-1-21 下午4:06:38
 * @company: webyun
 * @Copyright: Copyright (c) 2015-2016
 * @version v1.0
 */
@SuppressWarnings("serial")
@XStreamAlias("palette")
public class Palette implements Serializable {
	/**
	 * 调色板集合
	 */
	@XStreamImplicit
	private List<Entry> entries = new ArrayList<Entry>();

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
}
