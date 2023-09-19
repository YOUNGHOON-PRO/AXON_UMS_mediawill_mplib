package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: FileDistLog.java
 * Version 		: 1.0
 * 작성일 		: 2003/08/20
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: 파일 분배 로그
 *
 * 프로젝트		: 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre> 
 */
public class FileDistLog extends Util {

	/**	로그 화일 위치.	*/
	private static String  FILEDISTLOGPOS = null;	//로그 파일 위치
	/**	로그 스트림	*/
	private static PrintWriter dist_log_out = null;		//전송 로그 스트림

	/** 로그 파일 명 */
	private static String dist_log_file_name = null;

	/** 경로 구분자 설정 */
	//private static String PATH_GUBUN = null;

	/**
	 * 생성자 
	 */
	public FileDistLog() {
		LoadEnv();
	}

	/**
   	 *
   	 * 환경설정
	 *
   	 **/
	public void LoadEnv() {
		try {
			if((FILEDISTLOGPOS = getEnv("FILEDISTLOGPOS")) == null) {
				FILEDISTLOGPOS = "";
			}
			setLogStream();
		} catch (Err err) {
			System.out.println("파일 분배 로그 파일 생성 실패\n"+err.getEXStr());	
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
			if(dist_log_out == null) {	//전송 로그가 null이라면
				dist_log_file_name = FILEDISTLOGPOS + PATH_GUBUN + "dist_log";
				dist_log_out = new PrintWriter(new FileWriter(dist_log_file_name,true));
            }

        } catch (Exception e) {
			System.out.println("파일 분배 로그 파일 생성 실패");	
		}
	
    }

    /**
     *
     * 전송 로그를 로그 파일에 기록한다.
     * @param   msg     로그 메세지
     *
     */
    public void distlogwriter(String msg) {

        String temp_file_name = null;

        try {
            if(dist_log_out == null) {
				dist_log_file_name = FILEDISTLOGPOS + PATH_GUBUN + "dist_log";
                dist_log_out = new PrintWriter(new FileWriter(dist_log_file_name,true));
            }
            dist_log_out.println(msg);
            dist_log_out.flush();
        } catch (Exception e) {
            System.out.println("파일 분산 로그 기록에 에러가 발생했습니다.\n" + e.toString());
        }
    }

    /**
     *
     * 로그화일을 닫는다.
     *
     */
    public void log_close() {
        try {
            dist_log_out.close();
        } catch (Exception e) {}
    }

}

			

