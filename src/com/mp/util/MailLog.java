package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: MailLog.java
 * Version 		: 1.0
 * 작성일 		: 2002/10/15
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: 메일 전송 로그 생성
 *
 * 프로젝트		: 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre> 
 */
public class MailLog extends Util {

	/** 모듈명 설정 */
	private String MNAME = "MailLog";

	/**	로그 화일 위치.	*/
	private static String  MAILLOGPOS = null;	//로그 파일 위치
	/**	에러 로그 화일 위치.	*/
	private static String  ERMAILLOGPOS = null;	//로그 파일 위치
	/**	전송 로그 화일 위치.	*/
	private static String  TRMAILLOGPOS = null;	//로그 파일 위치
	/**	전송 로그 스트림	*/
	private static PrintWriter tr_log_out = null;		//전송 로그 스트림
	/**	에러 로그 스트림.	*/
	private static PrintWriter er_log_out = null;		//에러 로그 스트림
	/**	전송로그 파일명.	*/
	private static String tr_log_file_name = null;		//전송 로그 파일명
	/**	에러 로그 파일명.	*/
	private static String er_log_file_name = null;		//에러 로그 파일명

	/** 에러파일 생성일자 */
	private static String er_log_date = null;
	/** 전송파일 생성일자 */
	private static String tr_log_date = null;

	/** 경로 구분자 설정 */
	//private static String PATH_GUBUN = null;

	/**
	 * 생성자 
	 */
	public MailLog() {
        //if(getPlatform() == 0) PATH_GUBUN = "/";
        //else PATH_GUBUN = "\\";
		LoadEnv();
	}

	/**
   	 *
   	 * 환경설정
	 *
   	 **/
	public void LoadEnv() {
		try {
			if((MAILLOGPOS = getEnv("MAILLOGPOS")) == null) {
				MAILLOGPOS = "";
			}
			ERMAILLOGPOS = MAILLOGPOS + PATH_GUBUN + "err";	
			TRMAILLOGPOS = MAILLOGPOS + PATH_GUBUN + "trans";	
			setLogStream();
		} catch (Err err) {
			log(err.getEXStr());
		}
	}
    /**
     *
     * 로그 디렉토리를 설정한다.
     * @param   String      로그명의 헤더
     *          String      로그디렉토리
     *
     */
    public void setLogStream() {

        //전송,에러 로그스트림의 파일명을 생성한다.
        try {
			if(er_log_out == null) {	//에러 로그가 null이라면
				er_log_date = getDate(Code.TM_YMD);
				er_log_file_name = ERMAILLOGPOS + PATH_GUBUN + "err_" + er_log_date;
				er_log_out = new PrintWriter(new FileWriter(er_log_file_name,true));	
			} else {
                if(!(er_log_date = getDate(Code.TM_YMD)).equals(er_log_date)) {
                    er_log_out.close();
                    er_log_file_name = ERMAILLOGPOS + PATH_GUBUN + "err_" + er_log_date;
                    er_log_out = new PrintWriter(new FileWriter(er_log_file_name,true));
                }
            }
			if(tr_log_out == null) {	//전송 로그가 null이라면
				tr_log_date = getDate(Code.TM_YMD);
				tr_log_file_name = TRMAILLOGPOS + PATH_GUBUN + "tr_" + tr_log_date;
				tr_log_out = new PrintWriter(new FileWriter(tr_log_file_name,true));	
			} else {
                if(!(tr_log_date = getDate(Code.TM_YMD)).equals(tr_log_date)) {
                    tr_log_out.close();
                    tr_log_file_name = TRMAILLOGPOS + PATH_GUBUN + "tr_" + tr_log_date;
                    tr_log_out = new PrintWriter(new FileWriter(tr_log_file_name,true));
                }
            }

        } catch (Exception e) {
            log("메일 로그 스트림 설정을 실패했읍니다.\n"+e.toString());
        }
    }

	/**
	 *
	 * 전송로그를 출력한다.
	 * @param	code 	코드
	 * 			to		수신자	
	 * 			type	유형	
	 *
	 **/
	public void trlog(String code, String to, String type) {
		String tempmsg = "[시각 : " +getDate(Code.TM_YMDHMS)+"] " + "[코드 : "+code+"] [메세지 : [to : "+to+"] [type : "+type+"]]";
		System.out.println(DELIMETER);
		System.out.println(tempmsg);
		System.out.println(DELIMETER);
		trlogwriter(tempmsg);
	}

	/**
	 *
	 * 에러로그를 출력한다.
	 * @param	code 	코드
	 * 			to		수신자	
	 * 			type	유형	
	 *
	 **/
	public void erlog(String code, String to, String type) {

		String tempmsg = "[시각 : " +getDate(Code.TM_YMDHMS)+"] " + "[코드 : "+code+"] [메세지 : [to : "+to+"] [type : "+type+"]]";

		System.out.println(DELIMETER);
		System.out.println(tempmsg);
		System.out.println(DELIMETER);
		erlogwriter(tempmsg);
	}

	/**
	 *
	 * 에러로그를 출력한다.
	 * @param	code 	코드
	 * 			to		수신자	
	 * 			type	유형	
	 * 			msg		수신서버메시지 
	 *
	 **/
	public void erlog(String code, String to, String type, String msg) {

		String tempmsg = "[시각 : " +getDate(Code.TM_YMDHMS)+"] " + "[코드 : "+code+"] [메세지 : [to : "+to+"] [type : "+type+"] [메시지 :  " + msg + "]]";

		System.out.println(DELIMETER);
		System.out.println(tempmsg);
		System.out.println(DELIMETER);
		erlogwriter(tempmsg);
	}

	/**
	 *
	 * 에러로그를 출력한다.
	 * @param	code 	코드
	 * 			to		수신자	
	 * 			type	유형	
	 * 			msg		수신서버메시지 
	 * 			level	처리단계
	 *
	 **/
	public void erlog(String code, String to, String type, String msg, String level) {

		String tempmsg = "[시각 : " +getDate(Code.TM_YMDHMS)+"] " + "[CODE : "+code+"] [메세지 : [to : "+to+"] [type : "+type+"] [LEVEL: " + level + "] [메시지 :  " + msg + "]]";

		System.out.println(DELIMETER);
		System.out.println(tempmsg);
		System.out.println(DELIMETER);
		erlogwriter(tempmsg);
	}

    /**
     *
     * 로그를 로그 파일에 기록한다.
     * @param   int			로그유형
     *			String      로그 메세지
     *
     */
    public void logwriter(int flag, String msg) {
		if(flag == Code.MAIL_TR_LOG) trlogwriter(msg);
		else if(flag == Code.MAIL_ER_LOG) erlogwriter(msg);
    }

    /**
     *
     * 에러 로그를 로그 파일에 기록한다.
     * @param   msg     로그 메세지
     *
     */
    public void erlogwriter(String msg) {

        String temp_file_name = null;

        try {
            temp_file_name = ERMAILLOGPOS + PATH_GUBUN + "err_" + getDate(Code.TM_YMD);
            if(er_log_out != null) {
                if(!er_log_file_name.equals(temp_file_name)) { //로그화일의 날짜가 지났다면 ...
                    er_log_out.close();
                    er_log_file_name = temp_file_name;
                    er_log_out = new PrintWriter(new FileWriter(er_log_file_name,true));
                }
            } else {
                er_log_file_name = temp_file_name;
                er_log_out = new PrintWriter(new FileWriter(er_log_file_name,true));
            }

            er_log_out.println(msg);
            er_log_out.flush();
        } catch (Exception e) {
            System.out.println("메일 에러 로그 기록에 에러가 발생했습니다.\n" + e.toString());
        }
    }

    /**
     *
     * 전송 로그를 로그 파일에 기록한다.
     * @param   msg     로그 메세지
     *
     */
    public void trlogwriter(String msg) {

        String temp_file_name = null;

        try {
            temp_file_name = TRMAILLOGPOS + PATH_GUBUN + "tr_" + getDate(Code.TM_YMD);
            if(tr_log_out != null) {
                if(!tr_log_file_name.equals(temp_file_name)) { //로그화일의 날짜가 지났다면 ...
                    tr_log_out.close();
                    tr_log_file_name = temp_file_name;
                    tr_log_out = new PrintWriter(new FileWriter(tr_log_file_name,true));
                }
            } else {
                tr_log_file_name = temp_file_name;
                tr_log_out = new PrintWriter(new FileWriter(tr_log_file_name,true));
            }

            tr_log_out.println(msg);
            tr_log_out.flush();
        } catch (Exception e) {
            System.out.println("메일 전송 로그 기록에 에러가 발생했습니다.\n" + e.toString());
        }
    }

    /**
     *
     * 로그화일을 닫는다.
     *
     */
    public void log_close() {
        try {
            tr_log_out.close();
			er_log_out.close();
        } catch (Exception e) {}
    }

}


