package com.webyun.seagrid.common.util;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @Project: ems
 * @Title: XmlConverter
 * @Description: XML文件与实体对象之间的转换器
 * @author: songwj
 * @date: 2016-2-29 下午3:24:42
 * @company: webyun
 * @Copyright: Copyright (c) 2015-2016 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class XMLConverter {
	/**
	 * 将XML文件转为实体对象
	 * @param entity 实体对象
	 * @param clz 实体类对象
	 * @param relativePath XML文件相对路径
	 * @return
	 */
	public static <T> T xmlToObj(T entity, Class<?> clz, String relativePath) {
		InputStream in = null;
		
		try {
			in = XMLConverter.class.getResourceAsStream(relativePath);
			XStream xs = new XStream(new DomDriver("utf-8"));
			xs.processAnnotations(clz);
			xs.fromXML(in, entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entity;
	}
}
