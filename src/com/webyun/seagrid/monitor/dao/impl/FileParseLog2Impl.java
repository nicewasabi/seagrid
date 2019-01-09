package com.webyun.seagrid.monitor.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.webyun.seagrid.common.dao.impl.BaseDaoImpl;
import com.webyun.seagrid.common.model.FileParseLogEntity;

@Repository
public class FileParseLog2Impl extends BaseDaoImpl<FileParseLogEntity, Integer>  {
	
	public List<FileParseLogEntity> getPageList(String startDate, String endDate ,int startRow, int endRow){
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.monitorpath,t.isnormal,s.shelldescription,t.yyyymmdd,t.optime, rownum from FILE_PARSE_LOG t , shell_command s  where s.id=t.shellcode "
				+ " and  t.yyyymmdd between :startTime and :endTime ");
		map.put("startTime", startDate);
		map.put("endTime", endDate);
		sql.append(" and rownum between "+startRow+" and "+endRow);
		
		List<FileParseLogEntity> list = null;
		try {
			list = findList(sql.toString(),map);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return list==null?null:list;
	}


}