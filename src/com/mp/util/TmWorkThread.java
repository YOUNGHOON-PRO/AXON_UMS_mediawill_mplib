package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.sql.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: TmWorkThread.java
 * Version 		: 1.0
 * 작성일 		: 2005/02/26
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			  : TIME THREAD에 대한 작업을 수행한다.
 *
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class TmWorkThread extends Thread {

	/** 모듈명 설정 */
	private String MNAME = "TmWorkThread";

	private WebUtil util = null;

	//작업 클래스명
	private String workclass = null;

	//작업명
	private String workname = null;

	/**	
	 * 생성자.
 	 */
	public TmWorkThread(WebUtil util, String workname, String workclass) {
			this.util = util;
			this.workclass = workclass;
			this.workname = workname;
	}

	/**
	 *	처리 쓰레드 
	 **/
	public void run() {
		String pos = MNAME+".run()";
		try {
			util.log(pos,"TIME THREAD 작업 수행 [작업명:"+workname+"][작업클래스:"+workclass+"]");
			//작업수행 클래스를 수행 시킨다.
			TmRun tmrun = (TmRun)Class.forName(workclass).newInstance();				
			Hashtable setinfohash = new Hashtable();
			setinfohash.put("UTIL",util);
			setinfohash.put("WORKNAME",workname);
			setinfohash.put("WORKCLASS",workclass);
			tmrun.setWork(setinfohash);
			//tmrun.doWork();
		} catch (Exception e) {
			util.log(pos,"TIME THREAD 작업 수행 에러 [작업명:"+workname+"][작업클래스:"+workclass+"]\n"+e.toString());	
		}
	}

}
