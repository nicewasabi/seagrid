package com.webyun.seagrid.monitor;

import java.io.File;
import java.util.List;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webyun.seagrid.common.model.FileMonitorTask;
import com.webyun.seagrid.monitor.service.IMonitorService;
import com.webyun.seagrid.monitor.util.MonitorUtil;
  
@Component
public class FileListener extends FileAlterationListenerAdaptor {  
	
    private Logger log = Logger.getLogger(FileListener.class); 
    
    @Autowired
    private IMonitorService service;
  
    /** 
     * 文件创建执行 
     */  
    @Override  
    public void onFileCreate(File file) {  
        log.info("[新建]:" + file.getAbsolutePath());  
        log.info("文件名："+file.getName());
/*        long lastModified = file.lastModified();
        Date date = new Date(lastModified);
        String string = date.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        log.info(date);*/
        List<FileMonitorTask> taskList = service.getByFile(file);//应该只有一个
        //获取迟到时间
        String endTime2 = taskList.get(0).getEndTime();//十分
        String endTime = MonitorUtil.getEndTime(endTime2, file);
        //获取失效时间
        String deadTime2 = taskList.get(0).getDeadline();//十分
        String deadTime = MonitorUtil.getDeadTime(deadTime2,taskList.get(0).getIsNextDay(), file);
        //比较文件时间和迟到时间，确定文件状态：正常、迟到、失效
        String fileTime = MonitorUtil.getFileTime(file);
        int status = MonitorUtil.getStatus(fileTime, endTime, deadTime);
        
        //修改log表中的对应任务记录的状态为status
        
        
        
        
        
    }  
  
    /** 
     * 文件创建修改 
     */  
    @Override  
    public void onFileChange(File file) {  
        log.info("[修改]:" + file.getAbsolutePath());  
    }  
  
    /** 
     * 文件删除 
     */  
    @Override  
    public void onFileDelete(File file) {  
        log.info("[删除]:" + file.getAbsolutePath());  
    }  
  
    /** 
     * 目录创建 
     */  
    @Override  
    public void onDirectoryCreate(File directory) {  
        log.info("[新建]:" + directory.getAbsolutePath());  
    }  
  
    /** 
     * 目录修改 
     */  
    @Override  
    public void onDirectoryChange(File directory) {  
        log.info("[修改]:" + directory.getAbsolutePath());  
    }  
  
    /** 
     * 目录删除 
     */  
    @Override  
    public void onDirectoryDelete(File directory) {  
        log.info("[删除]:" + directory.getAbsolutePath());  
    }  
  
    @Override  
    public void onStart(FileAlterationObserver observer) {  
    	//启动对配置表中迟到时间、失效时间的监控
        super.onStart(observer);
       /* Timer timer = new Timer();  
        timer.schedule(new TimerTask() {  
            public void run() {  
                System.out.println("-------设定要指定任务--------");  
            }  
        }, 2000);//设定指定的时间time,此处为2000毫秒  
*/    }  
  
    @Override  
    public void onStop(FileAlterationObserver observer) {  
        // TODO Auto-generated method stub  
        super.onStop(observer);  
    }  
  
} 