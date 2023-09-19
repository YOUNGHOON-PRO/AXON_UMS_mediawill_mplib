package com.mp.util;

import java.io.*;
import java.util.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램유형 : 공통유틸리티(java)
 * 프로그램명   : RespLog.java
 * Version      : 1.0
 * 작성일       : 2002/03/18
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 *
 * 설명         : 수신 로그를 구현한다.
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class RespLog extends Env {

	/** 수신 로그 모듈명	*/
	private String MNAME = "RespLog";

	/*=============================================*/
	// Global Zone
	/*=============================================*/
	/**	수신 로그출력여부	*/
	private static boolean RESPLOGVERBOSE = true;

	/**	수신로그파일 위치 */
	private static String RESPLOGPOS = null;

	/**	수신로그처리 서버 IP */
	private static String RESPIP = null;

	/**	수신로그처리 서버 PORT */
	private static String RESPPORT = null;

	/**	수신파일생성일자	*/
	private static String resp_log_date = null;

	/**	수신로그파일명	*/
	private static String resp_log_file_name = null;

	/**	수신로그스트림	*/
	private static PrintWriter resp_log_out = null;

	/** 로그 파일 출력 주기    */
	private static int log_term;

	/** 생성자선언	*/
	public RespLog() {
		try {
			//환경값 설정
			RESPLOGPOS = getEnv("RESP_LOG_POS");
			RESPIP = getEnv("ENVip");
			RESPPORT = getEnv("ENVport");
			log_term = Integer.parseInt(getEnv("RESP_LOG_TERM"));

			//로그스트림을 생성한다.
			String new_resp_log_date = null;
			switch (log_term) {
			case Code.RESP_LOG_YEAR:
				new_resp_log_date = getDate(Code.TM_Y);
				break;
			case Code.RESP_LOG_MONTH:
				new_resp_log_date = getDate(Code.TM_YM);
				break;
			case Code.RESP_LOG_DAY:
				new_resp_log_date = getDate(Code.TM_YMD);
				break;
			case Code.RESP_LOG_HOUR:
				new_resp_log_date = getDate(Code.TM_YMDH);
				break;
			default:
				new_resp_log_date = getDate(Code.TM_YMD);
			}
			
			if (resp_log_out == null) { //에러로그가 널이라면 생성
				
				resp_log_date = new_resp_log_date;
				resp_log_file_name = RESPLOGPOS + PATH_GUBUN + "RESP_" + RESPIP + "_" + RESPPORT + "_" + resp_log_date;
				resp_log_out = new PrintWriter(new FileWriter(resp_log_file_name, true));
				
			} else {

				if (!new_resp_log_date.equals(resp_log_date)) {

					resp_log_out.close();
					resp_log_date = new_resp_log_date;
					resp_log_file_name = RESPLOGPOS + PATH_GUBUN + "RESP_" + RESPIP + "_" + RESPPORT + "_"+ resp_log_date;
					resp_log_out = new PrintWriter(new FileWriter(resp_log_file_name, true));
				}
			}
		} catch (Err err) {
			log(MNAME, "수신 로그 스트림 설정을 실패했읍니다.\n" + err.getEXStr());
		} catch (IOException e) {
			log(MNAME, "수신 로그 스트림 설정을 실패했읍니다.\n" + e.toString());
		}
	}

	/**
	 *
	 * 수신 로그를 로그 파일에 기록한다.
	 * @param   msg		로그 메세지
	 *
	 */
	public void resp_logwriter(String msg) {
		String temp_file_name = null;

		try {
			if (RESPLOGPOS == null || PATH_GUBUN == null)
				return;
			String new_resp_log_date = null;
			switch (log_term) {
			case Code.RESP_LOG_YEAR:
				new_resp_log_date = getDate(Code.TM_Y);
				break;
			case Code.RESP_LOG_MONTH:
				new_resp_log_date = getDate(Code.TM_YM);
				break;
			case Code.RESP_LOG_DAY:
				new_resp_log_date = getDate(Code.TM_YMD);
				break;
			case Code.RESP_LOG_HOUR:
				new_resp_log_date = getDate(Code.TM_YMDH);
				break;
			default:
				new_resp_log_date = getDate(Code.TM_YMD);
			}
			
			if (resp_log_out != null) {
				if (!resp_log_date.equals(new_resp_log_date)) { //로그화일의 날짜가 지났다면 ...
					resp_log_out.close();
					
					//해당 로그 파일 DB 로딩 쓰레드를 수행한다.
					new RespThread(resp_log_file_name, RESPLOGPOS, PATH_GUBUN, resp_log_date, log_term, this).start();
					resp_log_file_name = RESPLOGPOS + PATH_GUBUN + "RESP_" + RESPIP + "_" + RESPPORT + "_"
							+ new_resp_log_date;
					resp_log_out = new PrintWriter(new FileWriter(resp_log_file_name, true));
					resp_log_date = new_resp_log_date; //로그 일자 변경
				}
			} else {
				resp_log_file_name = RESPLOGPOS + PATH_GUBUN + "RESP_" + RESPIP + "_" + RESPPORT + "_"
						+ new_resp_log_date;
				resp_log_out = new PrintWriter(new FileWriter(resp_log_file_name, true));
			}

			resp_log_out.println(msg);
			resp_log_out.flush();

		} catch (Exception e) {
			log(MNAME, "수신 로그 기록에 에러가 발생했습니다.\n" + e.toString());
		}finally{
		      loadDB(resp_log_file_name, RESPLOGPOS, resp_log_date);
		}
	}

	/**
	 * 로그 파일을 close 한다.
	 */
	public void resp_log_close() {
		try {
			resp_log_out.close();
		} catch (Exception e) {
			resp_log_out = null;
		}
	}

	/**
	 *
	 * 로그 출력 여부를 설정한다.
	 * @param	flag		로그출력여부
	 *
	 */
	public void setRESPLOGVerbose(boolean flag) {
		RESPLOGVERBOSE = flag;
	}

	/**
	 *
	 * 현재 파일의 특정 일자 로그 파일을 임의로 DB 로딩 한다.
	 * @param	resp_log_file		처리 로그 파일
	 * @param	RESPLOGPOS			로그 저장 위치(디렉토리)
	 * @param	resp_log_date		로그 일자
	 *
	 */
	public void loadDB(String resp_log_file, String RESPLOGPOS, String resp_log_date) {
		//해당 로그 파일 DB 로딩 쓰레드를 수행한다.
		new RespThread(resp_log_file, RESPLOGPOS, PATH_GUBUN, resp_log_date, log_term, this).start();

	}

}
