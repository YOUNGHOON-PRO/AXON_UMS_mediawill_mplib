
package com.mp.util;

import java.io.*;
import java.util.*;
import java.lang.*;
import com.mp.exception.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : InitApp.java
 * Version      : 1.0
 * 작성일       : 2002/12/11
 * 작성자       : 오범석
 * 수정일       : 2003/12/02
 * 수정자       : 오범석
 * 수정설명		: WINDOW RESIN의 경우 java의 -E 옵션 사용할 수 없음
 *				  그래서 System.property에 있는 resin.home을 읽어 
 *				  resin.home 의 config밑에 env화일을 읽어들인다.
 * 수정일		: 2003/12/15 
 * 수정자		: 오범석
 * 수정설명 	: 환경값에 있는 LOGYN과 LOGFILE의 값을 읽어 
 *				  로그 출력을 설정한다.
 * 수정일		: 2003/12/17
 * 수정자		: 오범석
 * 수정설명		: 어플리케이션 시작 시간을 환경값에 설정한다.
 *
 * 설명         : 초기화 메소드를 구현한다.
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class InitApp4HTXTS extends WebUtil {

	/*=============================================*/
	// Global Zone 
	/*=============================================*/

	/** 생성자를 호출한다. */
	public void InitApp() {

		try {

			String envfile = null;

			//시스템 PLATFORM을 읽어온다.
			//if(getPlatform() == Code.WINDOW) {
			//어플리케이션 서버명을 읽어온다.
			//	String app_srv_home = System.getProperty("resin.home");
			//	if(app_srv_home != null) {
			//		envfile = app_srv_home + "\\conf\\mp.env";
			//		System.setProperty("envfile",envfile);
			//	}
			//}

			if(envfile == null) {
				//환경 화일 위치를 읽어온다.
				envfile = System.getProperty("envfile");
			}

			//초기화를 실시한다.
			Hashtable envhash = getEnvironment(envfile);
		
			setEnv("ENVfile",envfile);

			//플랫폼을 설정한다.
			getPlatformPath();

			String ip = null;
			try {
				ip = getEnv("ENVip");
				if(ip.equals("")) {
					ip = getServerIP();
					setEnv("ENVip",ip);
				}
			} catch (Err err) {
				System.out.println("[ENVip]환경값이 존재 안하여 시스템 IP로 [ENVip]로 설정함");
				ip = getServerIP();
				setEnv("ENVip",ip);
			}			
			String port = null;
			try {
				port = getEnv("ENVport");
				if(port.equals("")) {
					port = "DEFAULT";
					setEnv("ENVport",port);
				}
			} catch (Err err) {
				System.out.println("[ENVport]환경값이 존재 안하여 'DEFAULT'로 [ENVport]로 설정함");
				port = "DEFAULT";
				setEnv("ENVport",port);
			}			
			
			setLogStream(ip+"_"+port,getEnv("LOGPOS"),true);
			try {	
				String logverbose = getEnv("LOGVERBOSE");
				String logyn = getEnv("LOGYN");
				String logfile = getEnv("LOGFILE");

				//로그 출력 여부
				if(logverbose.equals("Y")) setLOGVerbose(true);
				else setLOGVerbose(false);

				//STADARD OUT 로그 여부
				if(logyn.equals("Y")) setLOGYN(true);
				else setLOGYN(false);

				//FILE 로그 여부
				if(logfile.equals("Y")) {
					setLOGFile(true);		
				} else setLOGFile(false);
			} catch (Err err) {
				System.out.println("기본 로그 출력 정보로 설정합니다.");	
				setEnv("LOGVERBOSE","Y");
				setEnv("LOGYN","Y");
				setEnv("LOGFILE","Y");
			}

			String UILang = getEnv("UILang",false);
			if(UILang != null && UILang.equals("Y")) {
				//UI 언어권 정보를 로딩[초기화]한다.
				loadUILang(getEnv("UI_LANG_ENV_POS"));
			}
/* TS 중복 실행 방지 - only 국세청용 07.03.17
			String TIME_THREAD = getEnv("TIME_THREAD",false);
			if(TIME_THREAD != null && TIME_THREAD.equals("Y")) {

				//타입 쓰레드를 수행한다.
				new TimeThread().start();	
			}
*/

		} catch (Err err) {
			System.out.println("===================================================");
			System.out.println("\n\n\n\n");
			System.out.println(" 어플리케이션 초기화를 실패했습니다. ");
			System.out.println(" 원인 : 환경값 \"LOGHEAD\",\"LOGPOS\" 를 읽지 못했거나. ");
			System.out.println(" 	    로그파일 설정을 실패했습니다. ");
			System.out.println(" 조치 : 환경화일을 확인하시고, 환경값이 제대로 설정돼어 있는지 확인하십시요. ");
			System.out.println(" 		다음으로 Env.java와 Log.java를 확인하십시요.");
			System.out.println("\n\n\n\n");
			System.out.println("===================================================");
		}

        String initStr = "\n\n\n\n";
		initStr += " InitApp Called !!!!!!!!!!! \n";
		initStr += " Welcome to MPLIB Platform !!!!!!!!!!! \n";
		initStr += "\n\n";
		initStr += " 어플리케이션 초기화를 성공했습니다. \n";
		initStr += "\n\n\n\n";
		log("InitApp",initStr);

		String app_startdt = getDate(Code.TM_YMDHMS);
		setEnv("ENVapp_startdt",app_startdt);
		log("InitApp","서버가 실행되었습니다.\n" +
				"실행시간 : " + getFDate(app_startdt,Code.DT_KOR));

	}
}
