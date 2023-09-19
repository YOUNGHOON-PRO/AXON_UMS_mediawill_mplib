package com.mp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import com.mp.exception.Err;
//import java.util.*;

/**
 * <pre>
 * 프로그램명	: CampLogProcess.java
 * Version 		: 1.0
 * 작성일 		: 2005/03/03
 * 작성자 		: 서창훈
 * 수정일 		:
 * 수정자 		:
 * 설명			: 캠페인 로그 적재 프로시져를 실행
 *
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class CampLogProcess_프로시져사용 implements TmRun {

	/** 모듈명 설정 */
	private String MNAME = "CampLogProcess";

	private WebUtil util = null;
	private String workname = null;
	private String workclass = null;
	Connection resp_conn = null;
	private ResultSet result = null;
	
	
	String pos = MNAME+".doWork()";
	
	//생성자
	public CampLogProcess_프로시져사용() {}
	/** 데이터 베이스 UTIL */
	private DBUtil dbutil = null;	
	//설정정보
	public void setWork(Hashtable setinfohash) {
		this.util = (WebUtil)setinfohash.get("UTIL");
		this.workname = (String)setinfohash.get("WORKNAME");
		this.workclass = (String)setinfohash.get("WORKCLASS");
	}

	//작업내용
	public void doWork() {
		


		String pos = MNAME+".doWork()";
		String SQL = "{call SP_AG_CAMP_LOG (?)}";
		ParamType param = new ParamType();
		param.put(1,"",Code.DB_VARCHAR);
		Hashtable inputhash = new Hashtable();
		inputhash.put("SQL",SQL);
		inputhash.put("PARAM",param);
				Hashtable rethash = null;
				try {
						rethash = util.callSData(inputhash);
			
						System.out.println("SQL = > " + SQL);
			util.log(pos,"캠페인 로그 적재 프로시져 실행 성공 !!" +SQL);
		
		} catch (Err err) {
		  util.log(pos,err.getEXStr());
		} catch (Exception e) {
		  util.log(pos,e.toString());
		}
		
	}
	

		
	
		
		
}
