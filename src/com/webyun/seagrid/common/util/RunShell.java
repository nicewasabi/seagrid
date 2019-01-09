package com.webyun.seagrid.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @Project: seaGrid
 * @Title: RunShell
 * @Description: execute shell util class
 * @author: zhongsb
 * @date: 
 * @company: webyun
 * @Copyright: Copyright (c) 2014
 * @UpdateUser:
 * @UpdateDescription:
 * @UpdateTime 10:43:06
 * @Version :V1.0
 */
public class RunShell {
	private static Logger log = Logger.getLogger(RunShell.class);
	protected String CMD;
	private String show;
	private String[] sCmds = null;

	public String run() throws Exception {
		BufferedReader bufferedReader = null;
		String sRet = "";
		try {
			Runtime rt = Runtime.getRuntime();

			Process m_process = rt.exec(this.sCmds);
			System.out.println(new Date().toString() + "start run:" + this.CMD);
			bufferedReader = new BufferedReader(new InputStreamReader(
					m_process.getInputStream()));
			String ls_1 = "";
			while ((ls_1 = bufferedReader.readLine()) != null) {
				sRet = sRet + ls_1 + "\n";
			}

			setShow(sRet);
			m_process.waitFor();
		} catch (IOException ee) {
			log.error("RunScript have a IO error :" + ee.getMessage());
			throw ee;
		} catch (InterruptedException ie) {
			log.error("RunScript have a interrupte error:"
					+ ie.getMessage());
			throw ie;
		} catch (Exception ex) {
			log.error("RunScript have a error :" + ex.getMessage());
			throw ex;
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return sRet;
	}

	/**
	* @Description: 执行有参数的shell
	* @Author ：zhongsb
	* @param str
	* @return
	* @throws Exception   
	*/
	public String run(String[] str) throws Exception {
		String sRet = "";
		Process m_process = null;
		BufferedReader bufferedReader = null;
		String s = null;
		try {
			log.info("##############################################");
			Runtime rt = Runtime.getRuntime();
			m_process = rt.exec(str);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(m_process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(m_process.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
            	sRet = sRet + s + "\n";
            }
            while ((s = stdError.readLine()) != null) {
            	log.error(s);
            }
			/*bufferedReader = new BufferedReader(new InputStreamReader(
					m_process.getInputStream()));
			String ls_1 = "";
			while ((ls_1 = bufferedReader.readLine()) != null) {
				sRet = sRet + ls_1 + "\n";
			}
			setShow(sRet);*/
            setShow(sRet);
			m_process.waitFor();
			log.info("-----------------------------------------------");
		} catch (IOException ee) {
			log.error("RunScript have a IO error :" + ee.getMessage());
			throw ee;
		} catch (InterruptedException ie) {
			log.error("RunScript have a interrupte error:"
					+ ie.getMessage());
			throw ie;
		} catch (Exception ex) {
			log.error("RunScript have a error :" + ex.getMessage());
			throw ex;
		} finally {
			if (m_process != null)
				m_process.destroy();
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return sRet;
	}

	public String runBakWWW(String[] strs) throws Exception {
		String sRet = run(strs);
		return sRet;
	}

	public void setCMD(String newCMD) {
		this.CMD = newCMD;
	}

	public String getCMD() {
		return this.CMD;
	}

	public void setShow(String newShow) {
		this.show = newShow;
	}

	public String getShow() {
		return this.show;
	}

	public static void main(String[] args) {
		for(int i=0;i<args.length;i++){
			System.out.println("args["+i+"]="+args[i]);
		}
/*		a[0] = "F:/workspace-zsb/sea/resources/import.bat";
		a[1] = "web_sea/web_sea123@cmawebdb";
		a[2] = "F:/workspace-zsb/sea/WEB-INF/classes/survey_data.ctl";
		a[3] = "F:/workspace-zsb/sea/product/temp/20101123194633414299884.000";
		a[4] = "2";
		a[5] = "F:/workspace-zsb/sea/product/temp/";*/
/*		a[0] = "/home/zhongsb/ftptest/bin/ftp.sh";
		a[1] = "/NAFP/NCEP/CFS/20170817/18/";
		a[2] = "/home/zhongsb/ftptest/";
		a[3] = "pgbf.04.2017081700.201710.avrg.grib.18Z.grb2";*/

		RunShell runScript = new RunShell();
		try {
			String run = runScript.run(args);
			File file = new File("/home/zhongsb/ftptest/bin/log.txt");
			OutputStream  out = new FileOutputStream(file);
			byte[] b = run.getBytes();
			out.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
