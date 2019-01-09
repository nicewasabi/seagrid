package com.webyun.seagrid.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @Project: seaGrid
 * @Title: FTPUtil
 * @Description: FTP相关工具类
 * @author: zhongsb
 * @date: 2017年8月18日
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser：
 * @UpdateDescription：
 * @UpdateTime：上午10:43:54
 * @Version :V1.0
 */
public class FTPUtil {
	
	

	public static void main(String[] args) throws IOException {
		downAllFile("10.1.72.41", 21, "pub_nwp", "nafp8nmic", "/NAFP/ECMWF/HRES/20170910/12/",  "e:\\filepath");
		//向FTP站点发送东西
/*		File f =  new File("d:\\测试.txt"); 
        boolean flag = uploadFile("127.0.0.1", 21, "zx", "zx", "", "测试.txt", f); 
        System.out.println(flag); */
	   
/*		try { 
	        boolean flag = uploadDir("127.0.0.1", 21, "zx", "zx", "", "d:\\demo"); 
	        System.out.println(flag); 
	    } catch (FileNotFoundException e) { 
	        e.printStackTrace(); 
	    }*/
		//从FTP站点下载东西
//		int file = downFile("10.1.72.41", 21, "pub_nwp", "nafp8nmic", "/NAFP/ECMWF/HRES/20170817/12/", "W_NAFP_C_ECMF_20170817183430_P_C1D08171200082318001.bz2", "e:\\filepath");
//		System.out.println(file);
	}
	
	/**
	* Description: 向FTP服务器上传单个文件
	* @param url FTP服务器URL
	* @param port FTP服务器端口
	* @param username FTP登录账号
	* @param password FTP登录密码
	* @param ftpPath FTP服务器保存目录
	* @param filename 上传到FTP服务器上的文件名
	* @param input 输入流
	* @return 成功返回true，否则返回false
	*/ 
	public static boolean uploadFile(String url,int port,String username, String password, String ftpPath, String filename, File file) { 
	    FTPClient ftp = new FTPClient();
	    ByteArrayInputStream input = null;
	    try { 
	    	String ftpPathName = new String(ftpPath.getBytes());
	    	//连接FTP服务器 
	        ftp.connect(url, port);
	        //登录 
	        ftp.login(username, password);
	        //判断登录是否成功
	        int reply = ftp.getReplyCode(); 
	        if (!FTPReply.isPositiveCompletion(reply)) { 
	            ftp.disconnect(); 
	            return false; 
	        }
	        input = getInputStream(file);
	        //设置缓冲区大小为ftp默认大小
	        ftp.setBufferSize(ftp.getBufferSize());
	        boolean b = ftp.changeWorkingDirectory(ftpPathName); //文件保存地址
	        
	        if(!b) {//如果路径不存在则创建目录
	        	mkdir(ftp, ftpPathName);
	        	ftp.changeWorkingDirectory(ftpPathName); //文件保存地址
	        }
	        //设置文件类型（二进制）
	        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
	        //直接保存文件
	        ftp.storeFile(new String(filename.getBytes()), input);
	        //注销登录
	        ftp.logout(); 
	        return true; 
	    } catch (IOException e) { 
	        e.printStackTrace(); 
	        return false; 
	    } finally { 
	    	try {
	    		if(input != null)
	    			input.close();
				if (ftp.isConnected()) 
					ftp.disconnect(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
	    } 
	}
	
	/**
	 * 获得file的字节流，这个方法避免了FileInputStream 出现的末尾出现的中文异常
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static ByteArrayInputStream getInputStream(File file) throws IOException {
		ByteArrayInputStream input = null;
		byte[] allBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		input = new ByteArrayInputStream(allBytes);
		return input;
	}
	
	/**
	 * 上传一个一级目录的文件夹
	 * @param url		地址
	 * @param port		端口
	 * @param username	账号
	 * @param password	密码
	 * @param ftpPath	ftp站点的路径
	 * @param dirPath	文件在本地的路径
	 * @return
	 * @throws FileNotFoundException
	 */
	public static boolean uploadDir(String url,int port,String username, String password, String ftpPath, String dirPath) throws FileNotFoundException {
		File f = new File(dirPath);
		return uploadFiles(url, port, username, password, ftpPath + File.separator + f.getName(), dirPath);
	}
	
	/**
	 * 辅助uploadDir方法完成文件夹的上传
	 * @param url
	 * @param port
	 * @param username
	 * @param password
	 * @param ftpPath
	 * @param dirPath
	 * @return
	 * @throws FileNotFoundException
	 */
	private static boolean uploadFiles (String url,int port,String username, String password, String ftpPath, String dirPath) throws FileNotFoundException {
		File file = new File(dirPath);
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if(f.isDirectory()) {
					uploadFiles(url, port, username, password, ftpPath + File.separator + f.getName(), f.getAbsolutePath());
				} else {
					uploadFile(url, port, username, password, ftpPath, f.getName(), f);
				}
			}
			return true;
			
		} 
		return false; 
	}
	
	/**
	* @Description: 下载远端路径中的所有文件
	* @Author ：zhongsb
	* @param url
	* @param port
	* @param userName
	* @param password
	* @param remotePath
	* @param localPath
	* @return   int 0表示连不上；1表示正常；2表示没有找到文件；3表示获取异常
	*/
	public static String[] downAllFile(String url, int port, String userName, String password, String remotePath,String localPath){
		
		FTPClient ftp = new FTPClient();
		
		try {
			ftp.connect(url, port);
//			int cwd = ftp.cwd(remotePath);
			ftp.login(userName, password);//登录 
			boolean changeWorkingDirectory = ftp.changeWorkingDirectory(remotePath);
			System.out.println(changeWorkingDirectory);
			String[] listNames = ftp.listNames();
			for(int i=0;i<listNames.length;i++){
				System.out.println(listNames[i].toString());
			}
			return listNames;
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	* Description: 从FTP服务器下载文件
	* @param url FTP服务器URL
	* @param port FTP服务器端口
	* @param username FTP登录账号
	* @param password FTP登录密码
	* @param remotePath FTP服务器上的相对路径
	* @param fileName 要下载的文件名
	* @param localPath 下载后保存到本地的路径
	* @return int 0表示连不上；1表示正常；2表示没有找到文件；3表示获取异常
	*/ 
	public static int downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) { 
	    FTPClient ftp = new FTPClient(); 
	    try { 
	        ftp.connect(url, port); 
	        
	        //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器 
	        ftp.login(username, password);//登录 
	        int reply = ftp.getReplyCode(); 
	        if (!FTPReply.isPositiveCompletion(reply)) { 
	            ftp.disconnect(); 
	            return 0; 
	        } 
	        ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录 
	        FTPFile[] fs = ftp.listFiles(); 
	        boolean flag = false;
	        for(FTPFile ff:fs){ 
	            if(ff.getName().equals(fileName)){ 
	                File localFile = new File(localPath+File.separator+ff.getName()); 
	                OutputStream is = new FileOutputStream(localFile);  
	                ftp.retrieveFile(ff.getName(), is); 
	                is.close(); 
	                flag = true;
	            } 
	        } 
	        if(!flag){
	        	return 2;//没有找到文件
	        }
	        ftp.logout(); 
	        return 1; 
	    } catch (IOException e) { 
	        e.printStackTrace(); 
	        return 3; 
	    } finally { 
	        if (ftp.isConnected()) { 
	            try { 
	                ftp.disconnect(); 
	            } catch (IOException ioe) { 
	            	ioe.printStackTrace();
	            } 
	        } 
	    } 
	}
	
	/**
	 * 将FTP端文件从源位置移至到另一位置
	 * @param remoteFrom	原位置
	 * @param remoteTo		新位置
	 * @return
	 * @throws IOException
	 */
	public static boolean rename(FTPClient ftp, String remoteFrom, String remoteTo) throws IOException{
		boolean flag = ftp.rename(remoteFrom, remoteTo);
		return flag;
	}
	
	/**
	 * 在FTP Server上创建目录
	 * @param pathname	目录路径，从ftp目录开始
	 * @return
	 * @throws IOException
	 */
	public static boolean mkdir(FTPClient ftp, String pathname) throws IOException{
		boolean flag = ftp.makeDirectory(pathname);
		return flag;
	}
	
	/**
	 * 创建一个ftp客户端。
	 * @param url		地址
	 * @param port		端口
	 * @param username	账号
	 * @param password	密码
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static FTPClient getFTPClient (String url,int port,String username, String password) throws SocketException, IOException { 
	    FTPClient ftp = new FTPClient();
    	//连接FTP服务器 
        ftp.connect(url, port);
        //登录 
        ftp.login(username, password);
        //判断登录是否成功
        int reply = ftp.getReplyCode(); 
        if (!FTPReply.isPositiveCompletion(reply)) { 
            ftp.disconnect(); 
            return null;
        } 
	    return ftp;  
	}
}
