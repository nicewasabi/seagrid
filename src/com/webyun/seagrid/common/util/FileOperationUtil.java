package com.webyun.seagrid.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Project: seaGrid
 * @Title: FileOperationUtil
 * @Description: 文件处理工具
 * @author: songwj
 * @date: 2017-8-15 下午4:05:12
 * @company: webyun
 * @Copyright: Copyright (c) 2017 Webyun. All Rights Reserved.
 * @version v1.0
 */
public class FileOperationUtil {
	/**
	 * 创建文件
	 * @param fileName
	 * @return
	 */
	public static boolean createFile(File fileName) throws Exception {
		boolean flag = false;

		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
				flag = true;
			} else {
				String name = fileName.getName();
				File newFile = new File(fileName.getParentFile() + "/" + name.substring(0, name.lastIndexOf(".")) + "(1)" + name.substring(name.lastIndexOf(".")));
				createFile(newFile);
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 删除文件
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(File fileName) throws Exception {
		try {
			if (fileName.exists()) {
				fileName.delete();
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * @Title: 级联删除文件夹及子孙文件
	 * @author: gaos
	 * @date: 2016年11月22日 下午5:31:26
	 * @param destPath 传入目录路径时，该目录及目录内的所有内容会被删除；传入文件路径则删除该文件。
	 * @return
	 */
	public static boolean deleteFileCascade(String destPath) {
		File destFile = new File(destPath);
		if(! destFile.exists()) {
			return false;
		}
		if(destFile.isDirectory()) {
			File [] files = destFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFileCascade(files[i].getAbsolutePath());
			}
		}
		return destFile.delete();
	}

	/**
	 * 读文件内容
	 * @param fileName
	 * @return
	 */
	public static String readFile(File fileName) throws Exception {
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
					result = result + read + "\r\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
		return result;
	}

	/**
	 * 向文件中写入内容
	 * @param content 文本内容
	 * @param fileName 文件名称
	 * @return
	 * @throws Exception
	 */
	public static boolean writeContent2File(String content, File fileName)
			throws Exception {
		boolean flag = false;
		FileOutputStream o = null;

		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("GBK"));
			o.close();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (o != null)
				o.close();
		}
		return flag;
	}

	/**
	 * 追加文本内容
	 * @param filePath
	 * @param content
	 */
	public static void content2TxtFile(String filePath, String content) {
		String str = new String(); // 原有txt内容
		String s1 = new String();// 内容更新

		try {
			File f = new File(filePath);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			BufferedReader input = new BufferedReader(new FileReader(f));
			while ((str = input.readLine()) != null) {
				s1 += str + "\n";
			}
			System.out.println(s1);
			input.close();
			s1 += content;
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件读写
	 * @param in 读取文件
	 * @param out 输出文件
	 * @param length 缓冲区的字节数
	 */
	public static void writeFile(InputStream in, OutputStream out, int length) {
		try {
			byte[] buf = new byte[length];
			int len;
			while (true) {
				len = in.read(buf, 0, buf.length);
				if (len == -1) {
					break;
				}
				out.write(buf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据指定后缀名筛选文件
	 * @param dir 磁盘指定目录
	 * @param suffixName 文件后缀名
	 * @return
	 */
	public static String[] getFileBySuffixName(File dir, final String suffixName) {
		String[] filenames = dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(suffixName)) {
					return true;
				}
				return false;
			}
		});

		return filenames;
	}

	/**
	 * 判断目标路径的file是否存在并且是文件，而不是目录
	 * @param filePath		指定的路径
	 * @return
	 */
	public static boolean fileExists(String filePath){
		File f = new File(filePath);
		if(f.exists() && f.isFile())
			return true;
		return false;
	}
	
	/**
	 * 创建文件夹
	 * @param parentFolderPath 父类包路径 如:D:/ems
	 * @param newFolderName 新的包名称 如:aqiData
	 * @return 是否创建成功
	 */
	public static boolean makeDirs(String parentFolderPath, String newFolderName) {
		File file = new File(parentFolderPath + "/" + newFolderName);
		return file.mkdirs();
	}

	/**
	 * 判断指定路径的目录是否存在，如果不存在则创建
	 * @param path
	 */
	public static void mkdirs(String path){
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	/**
	 * 去除空行
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public static String readLine(BufferedReader br) throws IOException {
		String row = br.readLine();
		
		if (row == null) {
			return null;
		}
		
		while ("".equals(row)) {
			row = br.readLine();
		}
		
		return row;
	}

	/**
	 * 根据指定条件筛选指定磁盘目录下的文件
	 * @param dir 磁盘目录
	 * @param condition 筛选条件
	 * @return
	 */
	public static String[] getFilesByCondition(File dir, final String condition) {
		String[] filenames = dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.contains(condition)) {
					return true;
				}
				return false;
			}
		});

		return filenames;
	}
	
	/**
	 * @Title: 复制文件-支持新文件名、自动创建目标文件夹路径
	 * @author: gaos
	 * @date: 2016年11月17日 下午5:29:08
	 * @param sourceFilePath 源文件路径
	 * @param desDirPath 目标文件夹路径，末尾需加/
	 * @param newFileName 新文件名，设置为空则沿用原文件名
	 * @param isAutoDesDir 是否自动创建目标文件夹路径，设置为true，当目标文件夹路径不存在时则自动创建
	 */
	public static boolean copyFile(String sourceFilePath, String desDirPath, String newFileName, boolean isAutoDesDir) {
		File desDir = new File(desDirPath);
		
		if(! desDir.exists() && isAutoDesDir) {
			//自动创建目标文件夹路径
			desDir.mkdirs();
		}
		
		if(! desDir.exists()) {
			return false;
		}
		
		File sourceFile = new File(sourceFilePath);
		if(! sourceFile.exists() || ! sourceFile.isFile()) {
			return false;
		}
		
		//重命名
		newFileName = StringUtils.isNotEmpty(newFileName) ? newFileName : sourceFile.getName();
		
		File desfile = new File(desDirPath + newFileName);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(desfile);
			int len = 0;
			byte[] buf = new byte[10000];
			//文件复制
			while((len = fis.read(buf)) != -1)
				fos.write(buf,0,len);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Title: 复制文件,目标文件夹路径不存在时会复制失败
	 * @author: gaos
	 * @date: 2016年11月17日 下午5:29:08
	 * @param sourceFilePath 源文件路径
	 * @param desDirPath 目标文件夹路径，末尾需加/
	 */
	public static boolean copyFile(String sourceFilePath, String desDirPath) {
		return copyFile(sourceFilePath, desDirPath, null, false);
	}
	
}