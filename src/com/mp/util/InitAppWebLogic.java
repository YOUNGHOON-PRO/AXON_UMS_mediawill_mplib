
package com.mp.util;

import java.io.*;
import java.util.*;
import java.lang.*;
import weblogic.common.*;
import com.mp.exception.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : startup class(java)
 * 프로그램명   : InitAppWebLogic.java
 * Version      : 1.0
 * 작성일       : 2002/04/09
 * 작성자       : 오범석
 * 수정일		: 2003/12/15 
 * 수정자		: 오범석
 * 수정설명 	: 환경값에 있는 LOGYN과 LOGFILE의 값을 읽어 
 *				  로그 출력을 설정한다.
 * 수정일		: 2003/12/17
 * 수정자		: 오범석
 * 수정설명		: 어플리케이션 시작 시간을 환경값에 설정한다.
 *
 * 설명         : 어플리케이션 설정을 초기화한다.
 *
 * 프로젝트명   : 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class InitAppWebLogic extends WebUtil implements T3StartupDef {

	/** 모듈명 설정 */
	private String MNAME = "InitAppWebLogic";

	/**	 서비스 선언	*/
	private T3ServicesDef services;

	/**
	 * 생성자
	 */
	public InitAppWebLogic() {
	}

	/**
	 * 서비스를 설정한다.
	 * @param		services		서비스
	 */
	public void setServices(T3ServicesDef services) {
		this.services = services;
	}

	/**
	 * startup 메소드를 정의한다(실제 호출되는 메소드).
	 * @param		name		서비스명
	 *				args		실행 인자
	 * @return		String		실행 결과
	 */
	public String startup(String name, Hashtable args) throws Exception {
		String envfile = null;
		String ip = null;
		String port = null;
		if(args.containsKey("envfile") && args.containsKey("ip") && args.containsKey("port")) {
			envfile = (String)args.get("envfile");
			ip = (String)args.get("ip");
			port = (String)args.get("port");
			Hashtable envhash = getEnvironment(envfile);	
			setEnv("ENVip",ip);
			setEnv("ENVport",port);
			setEnv("ENVfile",envfile);
			try {
				getPlatformPath();
				setLogStream(ip+"_"+port,getEnv("LOGPOS"));
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

            	String TIME_THREAD = getEnv("TIME_THREAD",false);
            	if(TIME_THREAD != null && TIME_THREAD.equals("Y")) {
                	//타입 쓰레드를 수행한다.
                	new TimeThread().start();
            	}

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
			log(MNAME,"서버가 실행되었습니다.\n" +
					"실행시간 : " + getFDate(app_startdt,Code.DT_KOR));
		} else {
			System.out.println("===================================================");
			System.out.println("\n\n\n\n");
			System.out.println(" 환경화일 위치나 ip, port가 없습니다. ");
			System.out.println(" 웹로직 startup을 확인하세요. ");
			System.out.println("\n\n\n\n");
			System.out.println("===================================================");
		}

		String app_startdt = getDate(Code.TM_YMDHMS);
		setEnv("ENVapp_startdt",app_startdt);

		return "InitApp startup Call Success !!!!!!";

	}
}
				
