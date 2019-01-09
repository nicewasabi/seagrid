
package com.webyun.seagrid.common.util;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Who;

import com.sun.management.OperatingSystemMXBean;

/**
 * @Project: xywl
 * @Title: ToolOS
 * @Description: 系统环境相关
 * @author: zhangx
 * @date: 2017年3月24日 上午8:55:50
 * @company: webyun
 * @Copyright: Copyright (c) 2017
 * @version v1.0
 */
public class ToolOS {

	private static Logger log = Logger.getLogger(ToolOS.class);

	public static final String java_version = "java.version"; // Java的运行环境版本

	public static final String java_vendo = "java.vendor"; // Java的运行环境供应商

	public static final String java_vendo_url = "java.vendor.url"; // Java供应商的URL

	public static final String java_home = "java.home"; // Java的安装路径

	public static final String java_vm_specification_version = "java.vm.specification.version"; // Java的虚拟机规范版本

	public static final String java_vm_specification_vendor = "java.vm.specification.vendor"; // Java的虚拟机规范供应商

	public static final String java_vm_specification_name = "java.vm.specification.name"; // Java的虚拟机规范名称

	public static final String java_vm_version = "java.vm.version"; // Java的虚拟机实现版本

	public static final String java_vm_vendor = "java.vm.vendor"; // Java的虚拟机实现供应商

	public static final String java_vm_name = "java.vm.name"; // Java的虚拟机实现名称

	public static final String java_specification_version = "java.specification.version"; // Java运行时环境规范版本

	public static final String java_specification_vender = "java.specification.vender"; // Java运行时环境规范供应商

	public static final String java_specification_name = "java.specification.name"; // Java运行时环境规范名称

	public static final String java_class_version = "java.class.version"; // Java的类格式版本号

	public static final String java_class_path = "java.class.path"; // Java的类路径

	public static final String java_library_path = "java.library.path"; // 加载库时搜索的路径列表

	public static final String java_io_tmpdir = "java.io.tmpdir"; // 默认的临时文件路径

	public static final String java_ext_dirs = "java.ext.dirs"; // 一个或多个扩展目录的路径

	public static final String os_name = "os.name"; // 操作系统的名称

	public static final String os_arch = "os.arch"; // 操作系统的构架

	public static final String os_version = "os.version"; // 操作系统的版本

	public static final String file_separator = "file.separator"; // 文件分隔符

	public static final String path_separator = "path.separator"; // 路径分隔符

	public static final String line_separator = "line.separator"; // 行分隔符

	public static final String user_name = "user.name"; // 用户的账户名称

	public static final String user_home = "user.home"; // 用户的主目录

	public static final String user_dir = "user.dir"; // 用户的当前工作目录

	// 系统bean
	private static final OperatingSystemMXBean osmxb;

	private static final List<GarbageCollectorMXBean> list;

	//
	private static final long K2M = 1024l * 1024l;

	// K转换M
	private static final long K1M = 1024l;

	static {
		osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		list = ManagementFactory.getGarbageCollectorMXBeans();
	}

	/**
	 * 获取java系统环境变量
	 * 
	 * @author 董华健 2012-9-7 下午2:18:07
	 * @param key
	 * @return
	 */
	public static String getOsSystemProperty(String key) {
		return System.getProperty(key);
	}

	/**
	 * 获取本机IP
	 * 
	 * @return
	 */
	public static String getOsLocalHostIp() {
		InetAddress addr;
		String ip = null;
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();// 获得本机IP
		} catch (UnknownHostException e) {
			ip = "未知";
		}
		return ip;
	}

	/**
	 * 获取本机名称
	 * 
	 * @return
	 */
	public static String getOsLocalHostName() {
		InetAddress addr;
		String name = null;
		try {
			addr = InetAddress.getLocalHost();
			name = addr.getHostName();// 获得本机名称
		} catch (UnknownHostException e) {
			name = "未知";
		}
		return name;
	}

	/**
	 * 获取操作系统路径类型
	 * 
	 * @author 董华健 2012-9-7 下午2:17:34
	 * @return
	 */
	public static String getOsPathType() {
		String osPathType = System.getProperty("file.separator");
		if (osPathType.equals("\\")) {
			return "\\\\";
		}
		if (osPathType.equals("/")) {
			return "/";
		}
		return null;
	}

	/**
	 * 获取操作系统类型名称
	 * 
	 * @author 董华健 2012-9-7 下午2:17:42
	 * @return
	 */
	public static String getOsName() {
		return osmxb.getName();// System.getProperty("os.name");
	}

	/**
	 * 操作系统的体系结构 如:x86
	 * 
	 * @author 董华健 2012-9-7 下午2:17:59
	 * @return
	 */
	public static String getOsArch() {
		return osmxb.getArch();// System.getProperty("os.arch");
	}

	/**
	 * 获取CPU数量
	 * 
	 * @author 董华健 2012-9-7 下午2:18:18
	 * @return
	 */
	public static int getOsCpuNumber() {
		return osmxb.getAvailableProcessors();// Runtime.getRuntime().availableProcessors();//
												// 获取当前电脑CPU数量
	}

	/**
	 * cpu使用率
	 * 
	 * @return
	 */
	public static double getOscpuRatio() {
		try {
			Sigar sigar = new Sigar();
			return sigar.getCpuPerc().getCombined();
		} catch (SigarException e) {
			log.error("cpu使用率获取出错");
			return 0;
		}

	}

	/**
	 * 物理内存，总的可使用的，单位：M
	 * 
	 * @return
	 */
	public static long getOsPhysicalMemory() {
		long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / K2M; // M
		return totalMemorySize;
	}

	/**
	 * 物理内存，剩余，单位：M
	 * 
	 * @return
	 */
	public static long getOsPhysicalFreeMemory() {
		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / K2M; // M
		return freePhysicalMemorySize;
	}

	public static long getOsDiskMemory() {
		try {
			Sigar sigar = new Sigar();
			sigar.getFileSystemUsage("C:\\");
			FileSystem fslist[] = sigar.getFileSystemList();
			FileSystem fs = fslist[0];
			FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
			return usage.getTotal() / K1M;
		} catch (SigarException e) {
			return 0;
		}
	}

	public static long getOsDiskFreeMemory() {
		try {
			Sigar sigar = new Sigar();
			sigar.getFileSystemUsage("C:\\");
			FileSystem fslist[] = sigar.getFileSystemList();
			FileSystem fs = fslist[0];
			FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
			return usage.getFree() / K1M;
		} catch (SigarException e) {
			return 0;
		}
	}

	/**
	 * JVM内存，内存总量，单位：M
	 * 
	 * @author 董华健 2012-10-9 上午11:24:02
	 * @return
	 */
	public static long getJvmTotalMemory() {
		return Runtime.getRuntime().totalMemory() / K2M;
	}

	/**
	 * JVM内存，空闲内存量，单位：M
	 * 
	 * @author 董华健 2012-10-9 上午11:24:35
	 * @return
	 */
	public static long getJvmFreeMemory() {
		return Runtime.getRuntime().freeMemory() / K2M;
	}

	/**
	 * JVM内存，最大内存量，单位：M
	 * 
	 * @author 董华健 2012-10-9 上午11:24:50
	 * @return
	 */
	public static long getJvmMaxMemory() {
		return Runtime.getRuntime().maxMemory() / K2M;
	}

	/**
	 * 获取JVM GC次数
	 * 
	 * @return
	 */
	public static long getJvmGcCount() {
		long count = 0;
		for (final GarbageCollectorMXBean garbageCollectorMXBean : list) {
			count += garbageCollectorMXBean.getCollectionCount();
		}
		return count;
	}
	
	/**
	 * 系统线程列表
	 * 
	 * @author 董华健 2012-10-9 上午11:26:39
	 * @return
	 */
	public static List<Thread> getJvmThreads() {
		int activeCount = Thread.activeCount();
		Thread[] threads = new Thread[activeCount];
		Thread.enumerate(threads);
		return java.util.Arrays.asList(threads);
	}

	/**
	 * @Title:获得某个用户的运行时间 
	 * @return
	 * @author: zhangx
	 * @date: 2017年7月18日 下午1:15:50
	 * @version v1.0
	 */
	public static long getRunTime(String userName){
		Sigar sigar = new Sigar();
		try {
			Who[] who = sigar.getWhoList();
			if (who != null && who.length > 0) {  
				for (int i = 0; i < who.length; i++) {  
					Who _who = who[i];  
					if(userName.equals(_who.getUser())) {
						return _who.getTime(); //小时 
					}
				}  
			}  
		} catch (SigarException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	/**
	 * @Title:获得计算内核类型 
	 * @return
	 * @author: zhangx
	 * @date: 2017年7月18日 下午1:19:08
	 * @version v1.0
	 */
	public static String getDataModel(){
		OperatingSystem os = OperatingSystem.getInstance();
		return os.getArch() + "架构 "+os.getDataModel() + "位";//32位 
	}
	
	public static void main(String[] args) {
		System.out.println(getOscpuRatio());
	}
}
