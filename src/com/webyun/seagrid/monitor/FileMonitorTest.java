package com.webyun.seagrid.monitor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.webyun.seagrid.monitor.service.impl.MonitorServiceImpl;

/**
 * 文件监控测试
 * 
 * 在Apache的Commons-IO中有关于文件的监控功能的代码. 文件监控的原理如下：
 * 由文件监控类FileAlterationMonitor中的线程不停的扫描文件观察器FileAlterationObserver，
 * 如果有文件的变化，则根据相关的文件比较器，判断文件时新增，还是删除，还是更改。（默认为1000毫秒执行一次扫描）
 * 
 * @author wy
 * 
 */
@Component("fileMonitorTest")
public class FileMonitorTest {
	
	@Autowired
	private MonitorServiceImpl service;
	
	
	public void testMonitor(){
    	// 监控目录
		String rootDir = "e:\\filepath";
		// 轮询间隔 5 秒
		long interval = TimeUnit.SECONDS.toMillis(5);
		
	      // Create a FileFilter
	      IOFileFilter directories = FileFilterUtils.and(
	                                      FileFilterUtils.directoryFileFilter(),
	                                      HiddenFileFilter.VISIBLE);
	      IOFileFilter files       = FileFilterUtils.and(
	                                      FileFilterUtils.fileFileFilter(),
	                                      FileFilterUtils.suffixFileFilter(".java"));
	      IOFileFilter filter = FileFilterUtils.or(directories, files);

	      // Create the File system observer and register File Listeners
	      FileAlterationObserver observer2 = new FileAlterationObserver(new File("e:\\filepath"), filter);

	      
		// 创建一个文件观察器用于处理文件的格式
		FileAlterationObserver _observer = new FileAlterationObserver(rootDir,
				FileFilterUtils.and(FileFilterUtils.fileFileFilter(),
						FileFilterUtils.suffixFileFilter(".txt")), null);
		//只过滤目录
		FileAlterationObserver observer = new FileAlterationObserver(rootDir);// 过滤文件格式

		observer.addListener(new FileListener()); // 设置文件变化监听器
		// 创建文件变化监听器
		FileAlterationMonitor monitor = new FileAlterationMonitor(interval,observer);
		// 开始监控
		try {
			monitor.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
/*		 Timer timer = new Timer();  
	        timer.schedule(new TimerTask() {  
	            public void run() {  
	                System.out.println("-------设定要指定任务--------");  
	            }  
	        }, 2000);*/
/*		System.out.println("monitor==============================================");
    	ApplicationContext  context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    	FileMonitorTest monitor = context.getBean(FileMonitorTest.class);
    	CacheManage manager = new CacheManage();
		Cache cache = manager.getCache();
		Element element = cache.get("allTask");
		List<String> keys = null;
        System.out.println("-------设定要指定任务--------"); 
        keys = cache.getKeys();
        
        if(keys.size() <= 0){//未命中
			//把task任务数据放入缓存
			System.out.println("缓存中没有任务数据");
			List<FileMonitorTask> allTask = (List<FileMonitorTask>)monitor.service.getAllTask();
			for(int i=0;i<allTask.size();i++){
				FileMonitorTask fileMonitorTask = allTask.get(i);
				manager.put(fileMonitorTask);
			}
		}
        keys = cache.getKeys();
        
		for(String key:keys){
			JSONObject fromObject = JSONObject.fromObject(cache.get(key).getObjectValue().toString());
    		String endTime = (String) fromObject.get("endTime");
    		String deadLine = (String) fromObject.get("deadLine");
    		System.out.println(endTime);
    		System.out.println(deadLine);
		}
        */
		
    	ApplicationContext  context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    	FileMonitorTest monitor2 = context.getBean(FileMonitorTest.class);
    	FileListener fileListener = context.getBean(FileListener.class);
    	
    	// 监控目录
		String rootDir = "e:\\filepath";
		// 轮询间隔 5 秒
		long interval = TimeUnit.SECONDS.toMillis(5);
		
	      // Create a FileFilter
	      IOFileFilter directories = FileFilterUtils.and(
	                                      FileFilterUtils.directoryFileFilter(),
	                                      HiddenFileFilter.VISIBLE);
	      IOFileFilter files       = FileFilterUtils.and(
	                                      FileFilterUtils.fileFileFilter(),
	                                      FileFilterUtils.suffixFileFilter(".java"));
	      IOFileFilter filter = FileFilterUtils.or(directories, files);

	      // Create the File system observer and register File Listeners
	      FileAlterationObserver observer2 = new FileAlterationObserver(new File("e:\\filepath"), filter);

	      
		// 创建一个文件观察器用于处理文件的格式
		FileAlterationObserver _observer = new FileAlterationObserver(rootDir,
				FileFilterUtils.and(FileFilterUtils.fileFileFilter(),
						FileFilterUtils.suffixFileFilter(".txt")), null);
		//只过滤目录
		FileAlterationObserver observer = new FileAlterationObserver(rootDir);// 过滤文件格式

		observer.addListener(new FileListener()); // 设置文件变化监听器
		// 创建文件变化监听器
		FileAlterationMonitor monitor = new FileAlterationMonitor(interval,
				observer);
		// 开始监控
		monitor.start();
	}

}