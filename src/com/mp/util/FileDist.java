package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import sun.misc.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: FileDist.java
 * Version 		: 1.0
 * 작성일 		: 2003/08/20
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: 파일을 해당 서버로 전송한다.
 *				  
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class FileDist extends Thread {

	/** LOG 클래스	*/
	private FileDistLog log = null;
	private String filepath = null;
	private String rfilepath = null;

	/**	
	 * 생성자.
	 * @param  log			로그 클래스
	 * 		   filepath		로컬 파일 패스
 	 */
	public FileDist(FileDistLog log, String filepath) {
		this.log = log;
		this.filepath = filepath;
	}

	/**	
	 * 생성자.
	 * @param  log			로그 클래스
	 * 		   filepath		로컬 파일 패스
	 * 		   rfilepath	리모트 파일 패스
 	 */
	public FileDist(FileDistLog log, String filepath, String rfilepath) {
		this.log = log;
		this.filepath = filepath;
		this.rfilepath = rfilepath;
	}

	/**
	 *	파일 COPY
	 **/
	public void run() {

		FileDistFtp filedistftp = new FileDistFtp();
		String wasstr = null;
		String wasip = null;
		String wasport = null;
		String wasuser = null;
		String waspass = null;
		String prefixpath = null;
		String remotepath = null;
		try {
			wasstr = log.getEnv("WASLIST");
			if(rfilepath == null) {  //리모트 파일 패스가 없다면...
				//prefixpath = log.getEnv("WASPREFIXPATH");
				//remotepath = log.repStr(filepath,prefixpath,"");
				//remotepath = remotepath.substring(0,remotepath.indexOf(log.getPlatformPath())+1);
				remotepath = filepath.substring(0,remotepath.indexOf(log.getPlatformPath())+1);
			} else {
				remotepath = rfilepath;	
			}
			StringTokenizer token = new StringTokenizer(wasstr,"|");
			while(token.hasMoreTokens()) {	
				String srv = token.nextToken();
				wasip = log.getEnv(srv+"IP");
				wasport = log.getEnv(srv+"PORT");
				wasuser = log.getEnv(srv+"USER");
				waspass = log.getEnv(srv+"PASS");

				Hashtable inputhash = new Hashtable();
				inputhash.put("SVRIP",wasip);
				inputhash.put("SVRPORT",wasport);
				inputhash.put("USER",wasuser);
				inputhash.put("PASSWORD",waspass);		
				inputhash.put("SVRPATH",remotepath);	//수신지 파일 패스
				inputhash.put("LOCALPATH",filepath);	//전송할 파일 패스
				try {
					filedistftp.setFile(inputhash);	
					System.out.println("FileDist : " + inputhash);
				} catch (Err err) {
					//로그 화일 기록
					log.distlogwriter(wasip+"|"+filepath+"->"+rfilepath);	
					System.out.println("서버 전송 실패\n"+err.getEXStr());
				}	
			}
		} catch (Err err) {
			//로그 화일 기록
			//log.distlogwriter(filepath);	
			System.out.println("서버 전송 실패\n"+err.getEXStr());
		} catch (Exception e) {
			//로그 화일 기록
			//log.distlogwriter(filepath);	
			System.out.println("서버 전송 실패\n"+e.toString());
		}
	}
}
	
		
	

		
