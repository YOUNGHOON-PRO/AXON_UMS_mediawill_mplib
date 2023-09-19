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
 * 프로그램명	: RespProcess.java
 * Version 		: 1.0
 * 작성일 		: 2005/02/26
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: 수신 로그를 DB에 저장한다.
 *
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class RespProcess implements TmRun {

	/** 모듈명 설정 */
	private String MNAME = "RespProcess";

  private WebUtil util = null;
  private String workname = null;
  private String workclass = null;

  //생성자
  public RespProcess() {}

  //설정정보
  public void setWork(Hashtable setinfohash) {
    this.util = (WebUtil)setinfohash.get("UTIL");
    this.workname = (String)setinfohash.get("WORKNAME");
    this.workclass = (String)setinfohash.get("WORKCLASS");
  }

  //작업내용
  public void doWork() {
    String pos = MNAME+".doWork()";

  	/** 서버IP, 서버PORT, RESPONSE URL 설정 */
 		String ip = null;
  	String port = null;
  	String url = null;

    try {
      ip = util.getEnv("ENVip");
      port = util.getEnv("ENVport");
      String tempUrl = util.getEnv("RESPONSE_URL");
      tempUrl = tempUrl.substring(tempUrl.indexOf("//")+2);
      url = tempUrl.substring(tempUrl.indexOf("/"));
      String call_url = "http://"+ip+":"+port+url+"?TimeThread";
      util.log(pos,"수신결과 처리 : " + util.getUrlData(call_url));

    } catch (Err err) {
      util.log(pos,err.getEXStr());
    } catch (Exception e) {
      util.log(pos,e.toString());
    }

  }


}
