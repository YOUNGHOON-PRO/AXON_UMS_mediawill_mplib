package com.mp.util;

import java.util.Hashtable;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.math.BigDecimal;
import java.io.*;
import java.util.*;
import java.util.Vector;
import java.lang.*;
import java.net.*;
import java.text.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.mp.exception.*;
import com.mp.util.*;
import sun.misc.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : Util.java
 * Version      : 1.0
 * 작성일       : 2002/03/18
 * 작성자       : 오범석
 * 수정일       : 2002/10/20
 * 수정자       : 오범석
 * 수정일       : 2003/12/06
 * 수정자       : 오범석
 * 수정설명		: 날짜 관련 메소드 추가(윤달관련, 요일 관련..)
 * 수정일       : 2003/12/19
 * 수정자		: 오범석
 * 수정설명		: 데이터베이스 처리를 위한 처리값 변경 메소드추가
 * 			  예를들어 검색파라미터 설정을 할경우 검색조건을 생성하여 
 * 			  사용하는데 이때 문자에 ' 이 들어간 경우나 특수문자가 들어간 
 * 수정일       : 2004/01/01
 * 수정자		: 오범석
 * 수정설명		: 통계정보 메소드 추가
 * 수정일       : 2004/06/30
 * 수정자		: 오범석
 * 수정설명		: 영문 요일 호출 메소드 추가 (getEnDayNM(int day))
 * 설명         : 각종 Utility 메소드를 구현한다.
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class Util extends UILang {

	/* ============================================= */
	// Global Zone
	/* ============================================= */
	/** JDBC 환경값(poolname) */
	// private static String jdbcenv = null;
	/** 모듈명 설정 */
	private String MNAME = "Util";

	/** 생성자를 호출한다. */
	public Util() {
	}

	/**
	 * 초기화한다.(타 외부 어플리케이션이 사용할 시 수행)
	 * 
	 * @param fname
	 *            환경화일위치 daylog_flag 로그 출력 구분(일자별 로그, 일자구분없는 로그)
	 */
	public void initCall(String fname, boolean daylog_flag) {
		String pos = MNAME + ".initCall()";
		Hashtable envhash = getEnvironment(fname);
		// 로그 스트림을 설정한다.
		try {
			setLogStream(getEnv("LOGHEAD"), getEnv("LOGPOS"), daylog_flag);
		} catch (Err err) {
			log(pos, "로그 설정을 실패했습니다.\n" + err.getEXStr());
		}
	}

	/**
	 * 초기화한다.(타 외부 어플리케이션이 사용할 시 수행)
	 * 
	 * @param fname
	 *            환경화일위치 loghead 로그 위치 daylog_flag 로그 출력 구분(일자별 로그, 일자구분없는 로그)
	 */
	public void initCall(String fname, String loghead, boolean daylog_flag) {
		String pos = MNAME + ".initCall()";
		Hashtable envhash = getEnvironment(fname);
		// 로그 스트림을 설정한다.
		try {
			setLogStream(loghead, getEnv("LOGPOS"), daylog_flag);
		} catch (Err err) {
			log(pos, "로그 설정을 실패했습니다." + err.getEXStr());
		}
	}

	/**
	 * 초기화한다.(타 외부 어플리케이션이 사용할 시 수행)
	 * 
	 * @param fname
	 *            환경화일위치 loghead 로그 위치 logpos 로그 PREFIX daylog_flag 로그 출력 구분(일자별
	 *            로그, 일자구분없는 로그)
	 */
	public void initCall(String fname, String loghead, String logpos,
			boolean daylog_flag) {
		Hashtable envhash = getEnvironment(fname);
		// 로그 스트림을 설정한다.
		setLogStream(loghead, logpos, daylog_flag);
	}

	/* ============================================= */
	// JDBC Zone
	/* ============================================= */
	/**
	 * JDBC 환경을 설정한다. 환경값을 읽어들여 JDBC환경을 설정한다.
	 */
	// public void setJDBC() {
	// try {
	// jdbcenv = getEnv("ENVpoolname");
	// } catch (Err err) {
	// log(MNAME,"JDBC 환경 설정을 실패했습니다.");
	// }
	// }
	/**
	 * JDBC 환경을 리턴한다.
	 * 
	 * @param String
	 *            Pool 대상명
	 * @return String JDBC 환경값
	 */
	public String getPool(String name) {
		String pos = MNAME + ".getPool()";
		String jdbcenv = null;
		try {
			jdbcenv = getEnv(name + "poolname");
		} catch (Err err) {
			log(pos, "JDBC 풀명 읽기를 실패했습니다.");
		}
		return jdbcenv;
	}

	/**
	 * Default DB Vendor 코드를 읽어온다.
	 * 
	 * @param dbname
	 *            DB 명
	 * @return String Vendor 코드
	 */
	public String getDBVendor() {
		String pos = MNAME + ".getDBVendor()";
		String retval = "";
		try {
			retval = getEnv("DefaultDBVendor");
		} catch (Err err) {
			log(pos, "DB Vendor 읽기를 실패했습니다.");
		}
		return retval;
	}

	/**
	 * DB Vendor 코드를 읽어온다.
	 * 
	 * @param dbname
	 *            DB 명
	 * @return String Vendor 코드
	 */
	public String getDBVendor(String dbname) {
		String pos = MNAME + ".getDBVendor()";
		String retval = "";
		try {
			retval = getEnv(dbname + "DBVendor");
		} catch (Err err) {
			log(pos, "DB Vendor 읽기를 실패했습니다.");
		}
		return retval;
	}

	/* ============================================= */
	// Character Zone
	/* ============================================= */

	/**
	 * Character Set를 EUC-KR로 컨버전
	 * 
	 * @param str
	 *            변환할 문자
	 * @return String 변환문자
	 */
	public String convEUC(String str) {
		String pos = MNAME + ".convEUC()";
		String tmp = new String("");
		if (str == null || str.length() == 0)
			return "";
		try {
			tmp = new String(str.getBytes("ISO-8859-1"), "EUC-KR");
		} catch (UnsupportedEncodingException uee) {
			log(pos, "Character Set변환을 실패했습니다.\n" + uee.toString());
		} catch (Exception e) {
			log(pos, "에러가 발생했습니다.\n" + e.toString());
		}
		return tmp;
	}

	/**
	 * Character Set를 ISO-8859-1로 컨버전
	 * 
	 * @param str
	 *            변환할 문자
	 * @return String 변환문자
	 */
	public String convISO(String str) {
		String pos = MNAME + ".convISO()";
		String tmp = new String("");
		if (str == null || str.length() == 0)
			return "";
		try {
			tmp = new String(str.getBytes("EUC-KR"), "ISO-8859-1");
		} catch (UnsupportedEncodingException uee) {
			log(pos, "Character Set변환을 실패했습니다.\n" + uee.toString());
		} catch (Exception e) {
			log(pos, "에러가 발생했습니다.\n" + e.toString());
		}
		return tmp;
	}

	/* ============================================= */
	// Money Zone
	/* ============================================= */

	/**
	 * 돈이 '0' 혹은 '0.0'이면 ' '으로 리턴
	 * 
	 * @param str
	 *            입력문자
	 * @return String 변환문자
	 */
	public String ZeroMoney(String str) {
		if (str.equals("0") || str.equals("0.0")) {
			str = "";
			return str;
		} else
			return str;
	}

	/**
	 * 한국 화폐단위의 표시기로 표현(EX. 10000 -> 10,000)
	 * 
	 * @param inp
	 *            입력숫자
	 * @return String 변환문자
	 */
	public String HanMoney(int inp) {
		String str = "";
		try {
			DecimalFormat df = new DecimalFormat("###,###");
			str = df.format(inp);
		} catch (Exception e) {
		}
		return str;
	}

	/**
	 * 한국 화폐단위의 표시기로 표현(EX. 10000 -> 10,000)
	 * 
	 * @param inp
	 *            입력숫자
	 * @return String 변환문자
	 */
	public String HanMoney(long inp) {
		String str = "";
		try {
			DecimalFormat df = new DecimalFormat("###,###");
			str = df.format(inp);
		} catch (Exception e) {
		}
		return str;
	}

	/**
	 * 한국 화폐단위의 표시기로 표현(EX. 10000 -> 10,000)
	 * 
	 * @param str
	 *            입력문자
	 * @return String 변환문자
	 */
	public String HanMoney(String str) {
		if (str == null || str.equals("") || !isNumber(str))
			return str;
		String suffix = null;
		String prefix = null;
		int pos = 0;
		if ((pos = str.indexOf(".")) != -1) {
			prefix = str.substring(0, pos + 1);
			suffix = str.substring(pos - 1);
		} else {
			prefix = str;
			suffix = "";
		}
		try {
			DecimalFormat df = new DecimalFormat("###,###");
			prefix = df.format(parseLong(prefix));
		} catch (Exception e) {
		}
		return prefix + suffix;
	}

	/**
	 * 한국 화폐단위의 표시기로 표현(EX. 10000 -> 10,000) 0이거나 0.0 일때 "" 로 리턴
	 * 
	 * @param str
	 *            입력문자
	 * @return String 변환문자
	 */
	public String ZHanMoney(String str) {
		if (str == null || str.equals("") || !isNumber(str))
			return str;
		String suffix = null;
		String prefix = null;
		int pos = 0;
		if ((pos = str.indexOf(".")) != -1) {
			prefix = str.substring(0, pos + 1);
			suffix = str.substring(pos - 1);
		} else {
			prefix = str;
			suffix = "";
		}
		try {
			DecimalFormat df = new DecimalFormat("###,###");
			prefix = df.format(parseLong(prefix));
		} catch (Exception e) {
		}
		return ZeroMoney(prefix + suffix);
	}

	/**
	 * 한국 화폐단위의 표시기로 표현(EX. 10000 -> 10,000) 0이거나 0.0 일때 "" 로 리턴
	 * 
	 * @param inp
	 *            입력숫자
	 * @return String 변환문자
	 */
	public String ZHanMoney(int inp) {
		String str = "";
		try {
			DecimalFormat df = new DecimalFormat("###,###");
			str = df.format(inp);
		} catch (Exception e) {
		}
		return ZeroMoney(str);
	}

	/* ============================================= */
	// Data Conversion Zone
	/* ============================================= */

	/**
	 * 해쉬테이블의 데이터를 읽는다. <br>
	 * 테이블의 필드명에 해당하는 데이터를 리턴한다. (키값 대소문자를 구분하지 않는다.)
	 * 
	 * @param dataname
	 *            데이터컬럼의 명 dataHash 컬럼명의 해쉬 테이블
	 * @return String 데이터
	 */
	public String getData(String dataname, Hashtable dataHash) {
		String datanamevar = dataname.toUpperCase();
		String retStr = "";
		String key = null;
		Enumeration e = dataHash.keys();
		while (e.hasMoreElements()) {
			key = (String) e.nextElement();
			if (datanamevar.equals(key.toUpperCase())) {
				retStr = (String) dataHash.get(key);
				break;
			}
		}
		if (!retStr.equals("")) {
			retStr = retStr.trim();
		}
		return retStr;
	}

	/**
	 * 해쉬테이블의 데이터를 읽는다. <br>
	 * Trim() 없이 읽어온다. (키값 대소문자를 구분하지 않는다.)
	 * 
	 * @param dataname
	 *            데이터컬럼의 명 dataHash 컬럼명의 해쉬 테이블
	 * @return String 데이터
	 */
	public String getNoTrimData(String dataname, Hashtable dataHash) {
		String datanamevar = dataname.toUpperCase();
		String retStr = "";
		String key = null;
		Enumeration e = dataHash.keys();
		while (e.hasMoreElements()) {
			key = (String) e.nextElement();
			if (datanamevar.equals(key.toUpperCase())) {
				retStr = (String) dataHash.get(key);
				break;
			}
		}
		return retStr;
	}

	/**
	 * 해쉬테이블의 데이터를 읽는다. (키값 대소문자를 구분하지 않는다.)
	 * 
	 * @param dataname
	 *            키값 dataHash 해쉬 데이터
	 * @return Object 데이터
	 */
	public Object getObjData(String dataname, Hashtable dataHash) {
		String datanamevar = dataname.toUpperCase();
		Object retobj = null;
		String key = null;
		Enumeration e = dataHash.keys();
		while (e.hasMoreElements()) {
			key = (String) e.nextElement();
			if (datanamevar.equals(key.toUpperCase())) {
				retobj = dataHash.get(key);
				break;
			}
		}
		return retobj;
	}

	/**
	 * 해쉬테이블의 데이터 존재 여부를 확인한다. (키값 대소문자를 구분하지 않는다.)
	 * 
	 * @param dataname
	 *            키값 dataHash 해쉬 데이터
	 * @return boolean 존재 여부
	 */
	public boolean containsData(String dataname, Hashtable dataHash) {
		String datanamevar = dataname.toUpperCase();
		String key = null;
		Enumeration e = dataHash.keys();
		while (e.hasMoreElements()) {
			key = (String) e.nextElement();
			if (datanamevar.equals(key.toUpperCase()))
				return true;
		}
		return false;
	}

	/* ============================================= */
	// Context Zone
	/* ============================================= */

	/**
	 * Context정보를 얻어온다. JNDI룩업을 위한 콘텍스트를 리턴한다.
	 * 
	 * @param fac
	 *            JNDI룩업 팩토리
	 * @param url
	 *            JNDI서버의 위치 URL
	 * @return InitialContext JNDI 컨텍스트
	 */
	public static InitialContext getInitialContext(String fac, String url)
			throws NamingException {
		InitialContext context = null;
		// Hashtable ht = new Hashtable();
		Properties ht = new Properties();
		ht.put(Context.INITIAL_CONTEXT_FACTORY, fac);
		ht.put(Context.PROVIDER_URL, url);
		context = new InitialContext(ht);
		return context;
	}

	/**
	 * Context정보를 얻어온다. 로컬 JNDI 콘텍스트를 리턴한다.
	 * 
	 * @return InitialContext JNDI 컨텍스트
	 */
	public static InitialContext getInitialContext() throws NamingException {
		InitialContext context = null;
		context = new InitialContext();
		return context;
	}

	/* ============================================= */
	// JNDI LOOKUP Zone
	/* ============================================= */

	/**
	 * JNDI등록 객체를 호출한다.
	 * 
	 * @param jndi_name
	 *            룩업할 객체명
	 * @return Object 찾은 객체
	 * @exception MallErr
	 *                <br>
	 *                에러코드 : 15G <br>
	 *                메세지 : 시스템 에러 발생
	 */
	public Object lookup(String jndi_name) throws Err {

		String pos = MNAME + ".lookup()";
		
		setSTATLOOKUPTRY(); // 통계정보설정
		setSTATLOOKUPKIND(jndi_name, Code.STAT_TRY); // 통계정보설정
		

		Object obj = new Object();
		try {
			InitialContext initCon = getInitialContext();
			obj = initCon.lookup(jndi_name);
		} catch (NamingException ne) {
			setSTATLOOKUPFAIL(); // 통계정보설정
			setSTATLOOKUPKIND(jndi_name, Code.STAT_FAIL); // 통계정보설정
			log(pos, "JNDI 등록 객체호출을 실패했습니다.\n" + ne.toString());
			throw new Err("1", "5", "G", "시스템 에러 발생.", pos);
		}
		setSTATLOOKUPSUCCESS(); // 통계정보설정
		setSTATLOOKUPKIND(jndi_name, Code.STAT_SUCCESS); // 통계정보설정
		return obj;
	}

	/**
	 * JNDI등록 객체를 호출한다.
	 * 
	 * @param jndi_name
	 *            룩업할 객체명
	 * @param fac
	 *            룩업할 팩토리명
	 * @param url
	 *            JNDI 서버명
	 * @return Object 찾은 객체
	 * @exception Err
	 *                <br>
	 *                에러코드 : 15G <br>
	 *                메세지 : 시스템 에러 발생
	 */
	public Object lookup(String jndi_name, String fac, String url) throws Err {
		String pos = MNAME + ".lookup()";

		setSTATLOOKUPTRY(); // 통계정보설정
		setSTATLOOKUPKIND(jndi_name, Code.STAT_TRY); // 통계정보설정

		Object obj = new Object();
		try {
			InitialContext initCon = getInitialContext(fac, url);
			obj = initCon.lookup(jndi_name);
		} catch (NamingException ne) {
			setSTATLOOKUPFAIL(); // 통계정보설정
			setSTATLOOKUPKIND(jndi_name, Code.STAT_FAIL); // 통계정보설정
			log(pos, "JNDI 등록 객체호출을 실패했습니다.\n" + ne.toString());
			throw new Err("1", "5", "G", "시스템 에러 발생.", pos);
		}
		setSTATLOOKUPSUCCESS(); // 통계정보설정
		setSTATLOOKUPKIND(jndi_name, Code.STAT_SUCCESS); // 통계정보설정
		return obj;
	}

	/* ============================================= */
	// BASE64 인코딩/디코딩 zone
	/* ============================================= */
	/**
	 * Base64 인코딩
	 * 
	 * @param input
	 *            인코딩 문자
	 * @return String 인코딩된 문자
	 */
	public String getBASE64E(String input) {
		String base64E = null;
		try {
			byte[] inputBytes = input.getBytes();
			BASE64Encoder encoder = new BASE64Encoder();
			base64E = encoder.encode(inputBytes);
		} catch (Exception e) {
		}
		return base64E;
	}

	/**
	 * Base64 디코딩
	 * 
	 * @param input
	 *            디코딩 문자
	 * @return String 디코딩된 문자
	 */
	public String getBASE64D(String input) {
		String base64D = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			base64D = new String(decoder.decodeBuffer(input));
		} catch (Exception e) {
		}
		return base64D;
	}

	/***************************************************************************
	 * =================================================== 날짜포맷 Zone
	 * ===================================================
	 */

	/** 윤년의 달의 일수 */
	private final int[] DOMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
			31 };

	/** 평년의 달의 일수 */
	private final int[] lDOMonth = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31,
			30, 31 };

	/** 요일 표시 */
	private final String[] ko_dayName = { "일", "월", "화", "수", "목", "금", "토" };

	private final String[] en_dayName = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };

	/**
	 * 
	 * 조회 년월의 일수
	 * 
	 * @param year
	 *            년도 month 월
	 * @return int 일수
	 * 
	 */
	public int getDaysOfMonth(String year, String month) {

		int iyear = parseInt(year);
		int imonth = parseInt(month);

		if ((iyear % 4) == 0) {
			if ((iyear % 100) == 0 && (iyear % 400) != 0)
				return DOMonth[imonth - 1];
			return lDOMonth[imonth - 1];
		}
		return DOMonth[imonth - 1];
	}

	/**
	 * 
	 * 그달의 첫번째 요일 숫자값
	 * 
	 * @param year
	 *            년도 month 월
	 * @return int 일수
	 * 
	 */
	public int getFirstDay(String year, String month) {
		int iyear = parseInt(year);
		int imonth = parseInt(month);

		Calendar cal = Calendar.getInstance();
		cal.set(iyear, imonth - 1, 1);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

		return dayofweek;
	}

	/**
	 * 
	 * 그달의 마지막 요일 숫자값
	 * 
	 * @param year
	 *            년도 month 월
	 * @return int 일수
	 * 
	 */
	public int getLastDay(String year, String month) {
		int iyear = parseInt(year);
		int imonth = parseInt(month);

		Calendar cal = Calendar.getInstance();
		cal.set(iyear, imonth - 1, 1);
		cal.set(iyear, imonth - 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

		return dayofweek;
	}

	/**
	 * 
	 * 요일 숫자값 구하기
	 * 
	 * @param year
	 *            년도 month 월 day 일
	 * @return String 일수
	 * 
	 */
	public int getDay(int year, int month, int day) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

		return dayofweek;
	}

	/**
	 * 
	 * 요일 숫자값 구하기
	 * 
	 * @param year
	 *            년도 month 월 day 일
	 * @return String 일수
	 * 
	 */
	public int getDay(String year, String month, String day) {
		int iyear = parseInt(year);
		int imonth = parseInt(month);
		int iday = parseInt(day);

		return getDay(iyear, imonth, iday);
	}

	/**
	 * 
	 * 요일 숫자값 구하기
	 * 
	 * @param idate
	 *            년월일
	 * @return String 일수
	 * 
	 */
	public int getDay(String idate) {
		String year = extData(idate, 0, 4);
		String month = extData(idate, 4, 6);
		String day = extData(idate, 6, 8);

		return getDay(year, month, day);
	}

	/**
	 * 
	 * 요일명을 리턴한다.(한글 요일명) 0:일요일, 1:월요일, 2:화요일, 3:수요일, 4:목요일, 5:금요일, 6:토요일
	 * 
	 * @param day
	 *            요일 숫자값
	 * @return String 요일
	 * 
	 */
	public String getDayNM(int day) {
		return getKoDayNM(day);
	}

	/**
	 * 
	 * 요일명을 리턴한다.(한글 요일명) 0:일요일, 1:월요일, 2:화요일, 3:수요일, 4:목요일, 5:금요일, 6:토요일
	 * 
	 * @param day
	 *            요일 숫자값
	 * @return String 요일
	 * 
	 */
	public String getKoDayNM(int day) {
		if (ko_dayName.length <= day)
			return "";
		else
			return ko_dayName[day];
	}

	/**
	 * 
	 * 요일명을 리턴한다.(영문 요일명) 0:Sunday, 1:Monday, 2:Tuesday, 3:Wednesday,
	 * 4:Thursday, 5:Freday, 6:Saturday
	 * 
	 * @param day
	 *            요일 숫자값
	 * @return String 요일
	 * 
	 */
	public String getEnDayNM(int day) {
		if (en_dayName.length <= day)
			return "";
		else
			return en_dayName[day];
	}

	/** 데이타 */
	private final String[] MonthData = {
	/* 1881 */"1212122322121", "1212121221220", "1121121222120",
			"2112132122122", "2112112121220", "2121211212120", "2212321121212",
			"2122121121210", "2122121212120", "1232122121212",

			/* 1891 */"1212121221220", "1121123221222", "1121121212220",
			"1212112121220", "2121231212121", "2221211212120", "1221212121210",
			"2123221212121", "2121212212120", "1211212232212",

			/* 1901 */"1211212122210", "2121121212220", "1212132112212",
			"2212112112210", "2212211212120", "1221412121212", "1212122121210",
			"2112212122120", "1231212122212", "1211212122210",

			/* 1911 */"2121123122122", "2121121122120", "2212112112120",
			"2212231212112", "2122121212120", "1212122121210", "2132122122121",
			"2112121222120", "1211212322122", "1211211221220",

			/* 1921 */"2121121121220", "2122132112122", "1221212121120",
			"2121221212110", "2122321221212", "1121212212210", "2112121221220",
			"1231211221222", "1211211212220", "1221123121221",

			/* 1931 */"2221121121210", "2221212112120", "1221241212112",
			"1212212212120", "1121212212210", "2114121212221", "2112112122210",
			"2211211412212", "2211211212120", "2212121121210",

			/* 1941 */"2212214112121", "2122122121120", "1212122122120",
			"1121412122122", "1121121222120", "2112112122120", "2231211212122",
			"2121211212120", "2212121321212", "2122121121210",

			/* 1951 */"2122121212120", "1212142121212", "1211221221220",
			"1121121221220", "2114112121222", "1212112121220", "2121211232122",
			"1221211212120", "1221212121210", "2121223212121",

			/* 1961 */"2121212212120", "1211212212210", "2121321212221",
			"2121121212220", "1212112112210", "2223211211221", "2212211212120",
			"1221212321212", "1212122121210", "2112212122120",

			/* 1971 */"1211232122212", "1211212122210", "2121121122210",
			"2212312112212", "2212112112120", "2212121232112", "2122121212110",
			"2212122121210", "2112124122121", "2112121221220",

			/* 1981 */"1211211221220", "2121321122122", "2121121121220",
			"2122112112322", "1221212112120", "1221221212110", "2122123221212",
			"1121212212210", "2112121221220", "1211231212222",

			/* 1991 */"1211211212220", "1221121121220", "1223212112121",
			"2221212112120", "1221221232112", "1212212122120", "1121212212210",
			"2112132212221", "2112112122210", "2211211212210",

			/* 2001 */"2221321121212", "2212121121210", "2212212112120",
			"1232212122112", "1212122122120", "1121212322122", "1121121222120",
			"2112112122120", "2211231212122", "2121211212120",

			/* 2011 */"2122121121210", "2124212112121", "2122121212120",
			"1212121223212", "1211212221220", "1121121221220", "2112132121222",
			"1212112121220", "2121211212120", "2122321121212",

			/* 2021 */"1221212121210", "2121221212120", "1232121221212",
			"1211212212210", "2121123212221", "2121121212220", "1212112112220",
			"1221231211221", "2212211211220", "1212212121210",

			/* 2031 */"2123212212121", "2112122122120", "1211212322212",
			"1211212122210", "2121121122120", "2212114112122", "2212112112120",
			"2212121211210", "2212232121211", "2122122121210",

			/* 2041 */"2112122122120", "1231212122212", "1211211221220",
			"2121121321222", "2121121121220", "2122112112120", "2122141211212",
			"1221221212110", "2121221221210", "2114121221221" };

	/**
	 * 
	 * 양력 날짜를 음력날짜로 변환.
	 * 
	 * @param date
	 *            년월일
	 * @return String 월일
	 * 
	 */
	public String getSolToLun(String date) {
		String iyear = extData(date, 0, 4);
		String imonth = extData(date, 4, 6);
		String iday = extData(date, 6, 8);
		int year = parseInt(iyear);
		int month = parseInt(imonth);
		int day = parseInt(iday);

		int[] currentDate = new int[162];
		int[] MonthAll = { 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		int dateAll = 0;
		int currentDateAll = 0;
		int currentYear = 0;

		int tempDate = 0;
		int tempDate0 = 0;
		int m1 = 0;
		int m2 = 0;
		int JCOUNT = 0;

		for (int i = 0; i < 162; i++) {
			currentDate[i] = 0;
			for (int j = 0; j <= 12; j++) {
				if (MonthData[i].substring(j, j + 1).equals("1")
						|| MonthData[i].substring(j, j + 1).equals("3"))
					currentDate[i] = currentDate[i] + 29;
				else if (MonthData[i].substring(j, j + 1).equals("2")
						|| MonthData[i].substring(j, j + 1).equals("4"))
					currentDate[i] = currentDate[i] + 30;
			}
		}
		// 1880년 1월30일 까지의 총일수
		dateAll = 1880 * 365 + 1880 / 4 - 1880 / 100 + 1880 / 400 + 30;
		currentYear = year - 1;
		// 입력된 년도 1년전까지의 총일수
		currentDateAll = currentYear * 365 + currentYear / 4 - currentYear
				/ 100 + currentYear / 400;

		if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
			MonthAll[1] = 29;
		else
			MonthAll[1] = 28;

		for (int i = 0; i < month - 1; i++) {
			currentDateAll = currentDateAll + MonthAll[i];
		}
		currentDateAll = currentDateAll + day;

		tempDate = currentDateAll - dateAll + 1;

		tempDate0 = currentDate[0];
		int thisdate = 0;
		while (tempDate > tempDate0) {
			tempDate0 = tempDate0 + currentDate[thisdate + 1];
			thisdate++;
		}
		year = thisdate + 1881;

		tempDate0 = tempDate0 - currentDate[thisdate];
		tempDate = tempDate - tempDate0;

		if (MonthData[thisdate].substring(12).equals("0"))
			JCOUNT = 11;
		else
			JCOUNT = 12;
		m2 = 0;
		for (int j = 0; j <= JCOUNT; j++) {
			if (Integer.parseInt(MonthData[thisdate].substring(j, j + 1)) <= 2) {
				m2 = m2 + 1;
				m1 = Integer.parseInt(MonthData[thisdate].substring(j, j + 1)) + 28;
			} else
				m1 = Integer.parseInt(MonthData[thisdate].substring(j, j + 1)) + 26;
			if (tempDate <= m1) {
				break;
			}
			tempDate = tempDate - m1;
		}
		month = m2;
		day = tempDate;

		return addZero(Integer.toString(month))
				+ addZero(Integer.toString(day));

	}// SolToLun end

	/**
	 * 
	 * 날짜포맷으로 변환한다. <br>
	 * 20010830 --> 2001/08/30
	 * 
	 * @param datestr
	 *            날짜데이터
	 * @return String 날짜 출력 형식 변환 데이터
	 * 
	 */
	public String getFDate(String datestr) {
		int length = datestr.length();
		String fdate = null;
		switch (length) {
		case 8:
			fdate = datestr.substring(0, 4);
			fdate += "/";
			fdate += datestr.substring(4, 6);
			fdate += "/";
			fdate += datestr.substring(6, 8);
			break;
		case 12:
			fdate = datestr.substring(0, 4);
			fdate += "/";
			fdate += datestr.substring(4, 6);
			fdate += "/";
			fdate += datestr.substring(6, 8);
			fdate += " ";
			fdate += datestr.substring(8, 10);
			fdate += ":";
			fdate += datestr.substring(10, 12);
			break;
		default:
			fdate = "";
		}
		return fdate;
	}

	/**
	 * 
	 * 날짜포맷으로 변환한다. <br>
	 * 20010830 --> 2001/08/30 또는 2001-08-30 또는 2001년 08월 30일 로 변환한다.
	 * 
	 * @param datestr
	 *            날짜데이터
	 * @param type
	 *            출력 포맷<br>
	 *            (DT_FMT1(1) --> xxxx/xx/xx, DT_FMT2(2) --> xxxx-xx-xx,
	 *            DT_KOR(3) --> xxxx년 xx월 xx일)
	 * @return String 날짜 출력 형식 변환 데이터
	 * 
	 */
	public String getFDate(String datestr, int type) {
		int length = datestr.length();
		String fdate = null;
		switch (type) {
		case Code.DT_FMT1:
			switch (length) {
			case 8:
				fdate = datestr.substring(0, 4);
				fdate += "/";
				fdate += datestr.substring(4, 6);
				fdate += "/";
				fdate += datestr.substring(6, 8);
				break;
			case 12:
				fdate = datestr.substring(0, 4);
				fdate += "/";
				fdate += datestr.substring(4, 6);
				fdate += "/";
				fdate += datestr.substring(6, 8);
				fdate += " ";
				fdate += datestr.substring(8, 10);
				fdate += ":";
				fdate += datestr.substring(10, 12);
				break;
			case 14:
				fdate = datestr.substring(0, 4);
				fdate += "/";
				fdate += datestr.substring(4, 6);
				fdate += "/";
				fdate += datestr.substring(6, 8);
				fdate += " ";
				fdate += datestr.substring(8, 10);
				fdate += ":";
				fdate += datestr.substring(10, 12);
				fdate += " ";
				fdate += datestr.substring(12, 14);
				break;
			default:
				fdate = "";
			}
			break;
		case Code.DT_FMT2:
			switch (length) {
			case 8:
				fdate = datestr.substring(0, 4);
				fdate += "-";
				fdate += datestr.substring(4, 6);
				fdate += "-";
				fdate += datestr.substring(6, 8);
				break;
			case 12:
				fdate = datestr.substring(0, 4);
				fdate += "-";
				fdate += datestr.substring(4, 6);
				fdate += "-";
				fdate += datestr.substring(6, 8);
				fdate += " ";
				fdate += datestr.substring(8, 10);
				fdate += ":";
				fdate += datestr.substring(10, 12);
				break;
			case 14:
				fdate = datestr.substring(0, 4);
				fdate += "-";
				fdate += datestr.substring(4, 6);
				fdate += "-";
				fdate += datestr.substring(6, 8);
				fdate += " ";
				fdate += datestr.substring(8, 10);
				fdate += ":";
				fdate += datestr.substring(10, 12);
				fdate += " ";
				fdate += datestr.substring(12, 14);
				break;
			default:
				fdate = "";
			}
			break;
		case Code.DT_KOR:
			switch (length) {
			case 8:
				fdate = datestr.substring(0, 4);
				fdate += "년 ";
				fdate += datestr.substring(4, 6);
				fdate += "월 ";
				fdate += datestr.substring(6, 8);
				fdate += "일";
				break;
			case 12:
				fdate = datestr.substring(0, 4);
				fdate += "년 ";
				fdate += datestr.substring(4, 6);
				fdate += "월 ";
				fdate += datestr.substring(6, 8);
				fdate += "일 ";
				fdate += datestr.substring(8, 10);
				fdate += "시 ";
				fdate += datestr.substring(10, 12);
				fdate += "분";
				break;
			case 14:
				fdate = datestr.substring(0, 4);
				fdate += "년 ";
				fdate += datestr.substring(4, 6);
				fdate += "월 ";
				fdate += datestr.substring(6, 8);
				fdate += "일 ";
				fdate += datestr.substring(8, 10);
				fdate += "시 ";
				fdate += datestr.substring(10, 12);
				fdate += "분 ";
				fdate += datestr.substring(12, 14);
				fdate += "초";
				break;
			default:
				fdate = "";
			}
			break;
		default:
			fdate = "";
		}
		return fdate;
	}

	/**
	 * 
	 * 현재날짜를 날짜포맷으로 변환한다. <br>
	 * 20010830 --> 2001/08/30 또는 2001-08-30 또는 2001년 08월 30일 로 변환한다. 현재날짜문자를 받지
	 * 않고 바로 생성하여 포맷을 생성한다.
	 * 
	 * @param dttype
	 *            현재 날짜 생성 포맷 (TM_YMDHMS(1) --> 년월일시분초 TM_MDHMS(2) --> 월일시분초
	 *            TM_DHMS(3) --> 일시분초 TM_HMS(4) --> 시분초 TM_MS(5) --> 분초 TM_S(6)
	 *            --> 초 TM_YMD(7) --> 년월일 TM_YMDHM(8) --> 년월일시분
	 * @param type
	 *            출력 포맷<br>
	 *            (DT_FMT1(1) --> xxxx/xx/xx, DT_FMT2(2) --> xxxx-xx-xx,
	 *            DT_KOR(3) --> xxxx년 xx월 xx일)
	 * @return String 날짜 출력 형식 변환 데이터
	 * 
	 */
	public String getFDate(int dttype, int type) {
		String dtstr = getDate(dttype);
		return getFDate(dtstr, type);
	}

	/**
	 * 
	 * 두날짜의 차이를 계산한다. <br>
	 * 예를 들어 20030210 과 20030411 의 일수차이는 얼마인가 ?
	 * 
	 * @param bdate
	 *            이전 날짜 adate 이후 날짜
	 * @return int 날짜 일수(-1 이면 입력에러)
	 * 
	 */
	public int getIntervalDay(String bdate, String adate) {

		String pos = MNAME + ".getIntervalDay()";

		if (bdate.length() != 8)
			return -1;

		int year, month, day;

		try {
			year = Integer.parseInt(bdate.substring(0, 4));
			month = Integer.parseInt(bdate.substring(4, 6)) - 1;
			day = Integer.parseInt(bdate.substring(6, 8));
		} catch (Exception e) {
			log(pos, "날짜 입력(포맷) 에러");
			return -1;
		}

		// 이전 날짜 설정
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, day);

		// 이전 날짜의 밀리세컨 총수
		long bdate_long = (cal.getTime()).getTime();

		try {
			year = Integer.parseInt(adate.substring(0, 4));
			month = Integer.parseInt(adate.substring(4, 6)) - 1;
			day = Integer.parseInt(adate.substring(6, 8));
		} catch (Exception e) {
			log(pos, "날짜 입력(포맷) 에러");
			return -1;
		}

		cal.clear();
		cal.set(year, month, day);
		// 이후 날짜의 밀리세컨 총수
		long fdate_long = (cal.getTime()).getTime();

		// 두 날짜의 차이 일수 계산
		long temp_long = fdate_long - bdate_long;

		temp_long = Math.abs(temp_long);

		int interval = (int) (temp_long / (24 * 60 * 60 * 1000));

		return interval;

	}

	/**
	 * 
	 * 특정날짜의 특정 일 이전이나 이후의 날짜를 구한다. <br>
	 * 예를 들어 20030210의 30일 후 날짜는 언제인가 ?
	 * 
	 * @param stand_date
	 *            기준 일자 interval 간격
	 * @return String 날짜(space면 포맷상의 문제)
	 * 
	 */
	public String getIntervalDate(String stand_date, long interval) {

		String pos = MNAME + ".getIntervalDate()";

		if (stand_date.length() != 8)
			return "";

		int year, month, day;

		try {
			year = Integer.parseInt(stand_date.substring(0, 4));
			month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
			day = Integer.parseInt(stand_date.substring(6, 8));
		} catch (Exception e) {
			log(pos, "날짜 입력(포맷) 에러");
			return "";
		}

		// 기준 날짜 설정
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, day);

		// 기준 날짜의 밀리세컨 총수
		long stand_date_long = (cal.getTime()).getTime();

		// 간격에 대한 밀리세컨 총수
		long interval_long = interval * (24 * 60 * 60 * 1000);

		long temp_long = stand_date_long + interval_long;

		cal.clear();
		cal.setTime(new Date(temp_long));

		String retval = Integer.toString(cal.get(cal.YEAR))
				+ lPad(Integer.toString(cal.get(cal.MONTH) + 1), 2, "0")
				+ lPad(Integer.toString(cal.get(cal.DAY_OF_MONTH)), 2, "0");

		return retval;

	}

	/**
	 * 
	 * 특정날짜의 특정 시간 이전이나 이후의 날짜를 구한다. <br>
	 * 예를 들어 20030210의 7시간 후 날짜는 언제인가 ?
	 * 
	 * @param stand_date
	 *            기준 일자
	 * @param interval
	 *            간격(시간, +시간, -시간)
	 * @return String 날짜(space면 포맷상의 문제)
	 * 
	 */
	public String getIntervalTime(String stand_date, String interval) {
		return getIntervalTime(stand_date, parseLong(interval));
	}

	/**
	 * 
	 * 특정날짜의 특정 시간 이전이나 이후의 날짜를 구한다. <br>
	 * 예를 들어 20030210의 7시간 후 날짜는 언제인가 ?
	 * 
	 * @param stand_date
	 *            기준 일자
	 * @param interval
	 *            간격(시간, +시간, -시간)
	 * @return String 날짜(space면 포맷상의 문제)
	 * 
	 */
	public String getIntervalTime(String stand_date, long interval) {

		String pos = MNAME + ".getIntervalTime()";

		if (stand_date == null) {
			log(pos, "입력날짜  NULL");
			return "";
		}

		int length = stand_date.length();

		int year, month, day, hour, minute, second, milli;

		String retval = "";

		Calendar cal = null;

		long stand_date_long, interval_long, temp_long;

		try {

			switch (length) {

			case 8:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(cal.YEAR))
						+ lPad(Integer.toString(cal.get(cal.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.DAY_OF_MONTH)), 2,
								"0");
				break;

			case 10:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = 0;

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(cal.YEAR))
						+ lPad(Integer.toString(cal.get(cal.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.DAY_OF_MONTH)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(cal.HOUR_OF_DAY)), 2,
								"0");
				break;

			case 12:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = Integer.parseInt(stand_date.substring(10, 12));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(cal.YEAR))
						+ lPad(Integer.toString(cal.get(cal.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.DAY_OF_MONTH)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(cal.HOUR_OF_DAY)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(cal.MINUTE)), 2, "0");
				break;

			case 14:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = Integer.parseInt(stand_date.substring(10, 12));
				second = Integer.parseInt(stand_date.substring(12, 14));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute, second);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(cal.YEAR))
						+ lPad(Integer.toString(cal.get(cal.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.DAY_OF_MONTH)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(cal.HOUR_OF_DAY)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(cal.MINUTE)), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.SECOND)), 2, "0");
				break;

			case 18:
				year = Integer.parseInt(stand_date.substring(0, 4));
				month = Integer.parseInt(stand_date.substring(4, 6)) - 1;
				day = Integer.parseInt(stand_date.substring(6, 8));
				hour = Integer.parseInt(stand_date.substring(8, 10));
				minute = Integer.parseInt(stand_date.substring(10, 12));
				second = Integer.parseInt(stand_date.substring(12, 14));

				// 기준 날짜 설정
				cal = Calendar.getInstance();
				cal.clear();
				cal.set(year, month, day, hour, minute, second);

				// 기준 날짜의 밀리세컨 총수
				stand_date_long = (cal.getTime()).getTime();

				// 간격에 대한 밀리세컨 총수
				interval_long = interval * (60 * 1000);

				temp_long = stand_date_long + interval_long;

				cal.clear();
				cal.setTime(new Date(temp_long));

				retval = Integer.toString(cal.get(cal.YEAR))
						+ lPad(Integer.toString(cal.get(cal.MONTH) + 1), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.DAY_OF_MONTH)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(cal.HOUR_OF_DAY)), 2,
								"0")
						+ lPad(Integer.toString(cal.get(cal.MINUTE)), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.SECOND)), 2, "0")
						+ lPad(Integer.toString(cal.get(cal.MILLISECOND)), 4,
								"0");
				break;

			default:
				retval = "";

			}
		} catch (Exception e) {
			log(pos, "날짜 입력(포맷) 에러\n" + e.toString());
			return "";
		}

		return retval;

	}

	/**
	 * 
	 * 특정일자의 이전 이후 달 일자를 읽어온다. <br>
	 * 예를 들어 20030210의 3달 후 날짜는 언제인가 ?
	 * 
	 * @param stand_date
	 *            기준 일자 interval 간격
	 * @return String 날짜(space면 포맷상의 문제)
	 * 
	 */
	public String getIntervalMonth(String stand_date, long interval) {

		String pos = MNAME + ".getIntervalMonth()";

		if (stand_date.length() != 8)
			return "";

		int year, month, day;

		try {
			year = Integer.parseInt(stand_date.substring(0, 4));
			month = Integer.parseInt(stand_date.substring(4, 6));
			day = Integer.parseInt(stand_date.substring(6, 8));
		} catch (Exception e) {
			log(pos, "날짜 입력(포맷) 에러");
			return "";
		}

		for (int i = 0; i < interval; i++) {
			++month;
			if (month > 12) {
				++year;
				month = 1;
			}
		}

		String retval = Integer.toString(year)
				+ lPad(Integer.toString(month), 2, "0")
				+ lPad(Integer.toString(day), 2, "0");

		return retval;

	}

	/***************************************************************************
	 * =================================================== 데이터 포맷 변환
	 * ===================================================
	 */

	/**
	 * 
	 * 문자의 포맷주기
	 * 
	 * @param as_Data
	 *            데이터 as_String KeyType (R --> 1234567777777 --> 123456-7777777)
	 * @return String 데이터출력 형식 변환 데이터
	 */
	public String getFormatData(String s, int keyType) {
		String value = s;
		if (s == null)
			return "";
		if (keyType == Code.FORMAT_A) {
			if (s.length() != 14 || !isNumber(s))
				return s;
			value = s.substring(0, 3) + "-" + s.substring(3, 9) + "-"
					+ s.substring(9, 14);
		} else if (keyType == Code.FORMAT_M) {
			value = MaskAmount(s);
		} else if (keyType == Code.FORMAT_D) {
			value = MaskDate(s, "/");
		} else if (keyType == Code.FORMAT_T) {
			if (s.length() != 6 || !isNumber(s))
				return s;
			value = s.substring(0, 2) + ":" + s.substring(2, 4) + ":"
					+ s.substring(4, 6);
		} else if (keyType == Code.FORMAT_R) {
			if (s.length() != 13 || !isNumber(s))
				return s;
			value = s.substring(0, 6) + "-" + s.substring(6, 13);
		} else if (keyType == Code.FORMAT_P) {
			if (s.length() != 12 || !isNumber(s))
				return s;
			value = s.substring(0, 4) + "-" + s.substring(4, 9) + "-"
					+ s.substring(8, 12);
		} else if (keyType == Code.FORMAT_K) {
			if (s.length() != 16 || !isNumber(s))
				return s;
			value = s.substring(0, 4) + "-" + s.substring(4, 8) + "-"
					+ s.substring(8, 12) + "-" + s.substring(12, 16);
		} else if (keyType == Code.FORMAT_Z) {
			if (s.length() != 6 || !isNumber(s))
				return s;
			value = s.substring(0, 3) + "-" + s.substring(3, 6);
		} else if (keyType == Code.FORMAT_O) {
			if (s.length() != 10 || !isNumber(s))
				return s;
			value = s.substring(0, 3) + "-" + s.substring(3, 5) + "-"
					+ s.substring(5, 10);
		} else if (keyType == Code.FORMAT_C) {
			if (s.length() != 13 || !isNumber(s))
				return s;
			value = s.substring(0, 6) + "-" + s.substring(6, 13);
		}

		return value;

	}

	/**
	 * 
	 * 숫자 여부 체크
	 * 
	 * @param sVal
	 *            체크데이터
	 * @return boolean 숫자여부
	 */
	public boolean isNumber(String sVal) {
		String num = "0123456789";
		for (int i = 0; i < sVal.length(); i++) {
			if (-1 == num.indexOf(sVal.substring(i, i + 1)))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * 문자의 포맷 Clear
	 * 
	 * @param str
	 *            데이터 (100-200 --> 100200)
	 * @return String 데이터출력 형식 변환 데이터
	 */
	public static final String MaskClear(String str) {

		if (str == null || str.equals(""))
			return str;

		boolean Eumsu = false;
		boolean bbb = false;
		StringBuffer strBuffer = new StringBuffer("");
		String tempStr = new String("");
		if (str == null)
			return "";

		if (str.trim().equals(""))
			return "";

		if (str.substring(0, 1).equals("-")) {
			if (str.length() == 1)
				return "";
			str = str.substring(1, str.length());
			Eumsu = true;
		}

		for (int b = 0; b < str.length(); b++) {
			char c = str.charAt(b);
			if (Character.isDigit(c)) {
				strBuffer.append(c);
			}
		}

		tempStr = strBuffer.toString();

		if (Eumsu)
			tempStr = "-" + strBuffer.toString();
		else if (tempStr.trim().equals(""))
			tempStr = "";

		return tempStr;

	}

	/**
	 * 
	 * 날짜형으로 리턴 입력 mask 로 날짜형을 변환한다.
	 * 
	 * @param str
	 *            데이터 (mask : [-] , 20031010 --> 2003-10-10)
	 * @return String 데이터출력 형식 변환 데이터
	 */
	public static final String MaskDate(String str, String mask) {
		boolean Eumsu = false;
		boolean bbb = false;
		StringBuffer strBuf = new StringBuffer("");
		if (str == null || str.trim().equals("") || (str.trim().length() != 8)
				|| str.trim().equals("0"))
			return new String(str);
		str = MaskClear(str);
		if (str.length() != 8)
			return new String(str);

		strBuf.append(str.substring(0, 4));
		strBuf.append(mask).append(str.substring(4, 6)).append(mask);
		strBuf.append(str.substring(6, 8));

		return strBuf.toString();
	}

	/**
	 * 
	 * 숫자형 mask 처리
	 * 
	 * @param str
	 *            데이터 (100000 --> 100,000)
	 * @return String 데이터출력 형식 변환 데이터
	 */
	public static final String MaskAmount(String str) {

		int Len = 0;
		int Mok = 0;
		int Namerge = 0;
		boolean Eumsu = false;
		boolean bbb = false;
		StringBuffer strBuf = new StringBuffer("");
		String tempStr = new String("");
		if (str == null)
			return new String("0");

		if (str.trim().equals("") || str.trim().equals("0"))
			return new String("0");
		str = MaskClear(str);

		if (str.substring(0, 1).equals("-")) {
			if (str.length() == 1)
				return new String("0");
			str = str.substring(1, str.length());
			Eumsu = true;
		}

		int offset = 0;

		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1).charAt(0) != '0')
				break;
			offset++;
		}

		str = str.substring(offset, str.length());

		if (str.length() < 4) {
			if (Eumsu == true)
				str = "-" + str;
			return str;
		}
		Len = str.length();
		Mok = Len / 3;
		Namerge = Len % 3;

		int b = 0;
		for (int a = 0; a < Mok + 1; a++) {
			if (a == 0) {
				if (Namerge != 0) {
					strBuf.append(str.substring(0, Namerge));
					strBuf.append(",");
				}
			} else {
				strBuf.append(str.substring(((a - 1) * 3) + Namerge,
						((a - 1) * 3) + Namerge + 3));
				if (a != Mok)
					strBuf.append(",");
			}
		}
		tempStr = strBuf.toString();

		if (Eumsu == true)
			tempStr = "-" + strBuf.toString();
		else if (tempStr.trim().equals(""))
			tempStr = "0";

		return tempStr;

	}

	/* ============================================= */
	// 전문 관련 문자열 조작 zone
	/* ============================================= */

	/**
	 * 해당 데이터를 추출한다(전문 추출)
	 * 
	 * @param size
	 *            데이터크기 input 데이터 start 시작점 end 끝점
	 * @return String 리턴 데이터
	 */
	public String extData(int size, byte[] input, int start, int end) {
		byte[] tempbyte = new byte[size];
		int pos = 0;
		if (input.length >= end) {
			for (int i = start; i < end; i++) {
				tempbyte[pos] = input[i];
				pos++;
			}
			return new String(tempbyte).trim();
		} else {
			return "";
		}
	}

	/**
	 * 해당 데이터를 추출한다
	 * 
	 * @param input
	 *            데이터 start 시작점 end 끝점
	 * @return String 리턴 데이터
	 */
	public String extData(String input, int start, int end) {
		int len = input.length();
		if (start >= len) {
			return "";
		}
		if (end > len) {
			return input.substring(start, len);
		}

		return input.substring(start, end);
	}

	/**
	 * 해당 데이터의 FILLER를 추가한다.
	 * 
	 * @param size
	 *            데이터크기 input 데이터 filler FILLER 데이터 pos FILLER 첨가 위치(1 ==> 뒷쪽, 0
	 *            ==> 앞쪽)
	 * @return String 리턴 데이터
	 */
	public String setFiller(int size, String input, String filler, int pos) {
		if (input == null)
			input = "";
		int temp_len = input.getBytes().length;
		String temp_str = "";
		for (int i = 0; i < (size - temp_len); i++) {
			temp_str += filler;
		}
		if (pos == Code.SUFFIX_FILLER)
			return input + temp_str;
		else
			return temp_str + input;
	}

	/**
	 * 해당 위치에 데이터 삽입
	 * 
	 * @param origin
	 *            원본데이터 input 삽입데이터 start 삽입지점시작점
	 * @return String 리턴 데이터
	 */
	public String insertStr(String origin, String input, int start) {

		String headstr = origin.substring(0, start);
		String tailstr = origin.substring(start);
		String retstr = headstr + input + tailstr;

		return retstr;
	}

	/**
	 * 입력 문자열의 해당 문자를 짤라낸다.
	 * 
	 * @param origin
	 *            원본데이터
	 * @param start
	 *            시작점
	 * @param end
	 *            종료점
	 * @param append
	 *            추가 문자
	 * @return String 리턴 데이터
	 */
	public String cutStr(String origin, int start, int end, String append) {

		if (origin.length() <= end) {
			return origin;
		}

		String retstr = extData(origin, start, end) + append;

		return retstr;
	}

	/* ============================================= */
	// 숫자형 변형 zone
	/* ============================================= */
	/**
	 * 해당 데이터를 int형으로 변경한다(null이거나 space일 경우 0).
	 * 
	 * @param input
	 *            변형할 데이터
	 * @return int 리턴 데이터
	 */
	public int parseInt(String input) {
		if (input == null || input.equals("")) {
			return 0;
		} else {
			return Integer.parseInt(input);
		}
	}

	/**
	 * 해당 데이터를 long형으로 변경한다(null이거나 space일 경우 0).
	 * 
	 * @param input
	 *            변형할 데이터
	 * @return long 리턴 데이터
	 */
	public long parseLong(String input) {
		if (input == null || input.equals("")) {
			return 0;
		} else {
			return Long.parseLong(input);
		}
	}

	/**
	 * 해당 데이터를 float형으로 변경한다(null이거나 space일 경우 0).
	 * 
	 * @param input
	 *            변형할 데이터
	 * @return float 리턴 데이터
	 */
	public float parseFloat(String input) {
		if (input == null || input.equals("")) {
			return 0;
		} else {
			return Float.parseFloat(input);
		}
	}

	/**
	 * 해당 데이터를 double형으로 변경한다(null이거나 space일 경우 0).
	 * 
	 * @param input
	 *            변형할 데이터
	 * @return double 리턴 데이터
	 */
	public double parseDouble(String input) {
		if (input == null || input.equals("")) {
			return 0;
		} else {
			return Double.parseDouble(input);
		}
	}

	/**
	 * 해당 데이터를 double형으로 변경한다(null이거나 space일 경우 0).
	 * 
	 * @param input
	 *            변형할 데이터 form 포맷( 예) 0.00 ==> 소수점 2자리에서 반올림)
	 * @return double 리턴 데이터
	 */
	public double parseDouble(String input, String form) {
		if (input == null || input.equals("")) {
			return 0;
		} else {
			return Double.parseDouble(new DecimalFormat(form).format(Double
					.parseDouble(input)));
		}
	}

	/**
	 * DOUBLE형 문자열을 포맷형식으로 변경한 문자열로 리턴한다.
	 * 
	 * @param input
	 *            변형할 데이터 form 포맷( 예) 0.00 ==> 소수점 2자리에서 반올림)
	 * @return double 리턴 데이터
	 */
	public String formatDouble(String input, String form) {
		if (input == null || input.equals("")) {
			return "0";
		} else {
			return new DecimalFormat(form).format(parseDouble(input));
		}
	}

	/**
	 * DOUBLE형의 숫자를 특정자리에서 절삭한다.
	 * 
	 * @param input
	 *            입력값 pos 절삭할 자리 setNum 체워넣을 숫자
	 * @return String 리턴 데이터
	 */
	public String cutPoint(double input, int pos, String setNum) {
		String str = Long.toString((long) input);
		int length = str.length();
		str = str.substring(0, length - pos);
		str = setFiller(length, str, setNum, 1);
		return str;
	}

	/**
	 * 실수형을 정수형으로 올림한다.
	 * 
	 * @param input
	 *            입력값
	 * @return String 리턴 데이터
	 */
	public int roundPoint(String input) {
		double val = parseDouble(input);
		int val1 = (int) val;
		if (val > val1)
			return (val1 + 1);
		else
			return val1;
	}

	/**
	 * 실수형을 정수형으로 올림한다.
	 * 
	 * @param input
	 *            입력값
	 * @return String 리턴 데이터
	 */
	public int roundPoint(double input) {
		int val1 = (int) input;
		if (input > val1)
			return (val1 + 1);
		else
			return val1;
	}

	/**
	 * double 타입을 long 타입으로 변환한다.
	 * 
	 * @param String
	 *            입력값(double 형의 스트링)
	 * @return String 리턴 데이터
	 */
	public String convLong(String inputval) {
		double doub = 0;
		try {
			doub = Double.parseDouble(inputval);
		} catch (Exception e) {
			return inputval;
		}

		return Long.toString((long) doub);
	}

	/**
	 * double 타입을 long 타입으로 변환한다.
	 * 
	 * @param double
	 *            입력값(double 형의 스트링)
	 * @return String 리턴 데이터
	 */
	public String convLong(double inputval) {
		return Long.toString((long) inputval);
	}

	/**
	 * 배정밀도의 부동소수를 String으로 변환한다.
	 * 
	 * @param inputval
	 *            입력값(double 형)
	 * @return String 리턴 데이터
	 */
	public String getDouToStr(double inputval) {
		String str = Double.toString(inputval);

		return getDouToStr(str);
	}

	/**
	 * 배정밀도의 부동소수를 String으로 변환한다.
	 * 
	 * @param double
	 *            입력값(DOUBLE 형의 스트링)
	 * @return String 리턴 데이터
	 */
	public String getDouToStr(String str) {

		int pos = 0;
		if ((pos = str.indexOf("E")) == -1) {
			return str;
		} else {
			String prestr = str.substring(0, str.indexOf("."));
			String sufstr = str.substring(str.indexOf(".") + 1, pos);
			int zari = Integer.parseInt(str.substring(pos + 1));

			int templen = 0;

			if (zari > 0) {

				char[] temp = sufstr.toCharArray();
				templen = temp.length + 1;
				char[] temp1 = new char[templen];
				int crs = 0;
				for (int i = 0; i < templen - 1; i++) {
					if (zari < templen) {
						if (i == zari) {
							temp1[crs] = '.';
							temp1[++crs] = temp[i];
						} else
							temp1[crs] = temp[i];
					} else {
						if (i > templen - 1)
							temp1[crs] = '0';
						else
							temp1[crs] = temp[i];
					}

					crs++;
				}
				return prestr + String.valueOf(temp1);
			} else {
				char[] temp = prestr.toCharArray();
				char[] temp1 = new char[Math.abs(zari) + 2];
				templen = temp.length;
				int temp1len = temp1.length;
				for (int i = 0; i < temp1len; i++) {
					if (i == 1) {
						temp1[i] = '.';
						temp1[++i] = '0';
					} else if (i == temp1len - 1) {
						temp1[i] = temp[0];
					} else
						temp1[i] = '0';
				}
				return String.valueOf(temp1) + sufstr;
			}
		}
	}

	/* ============================================= */
	// 문자형변환 zone
	/* ============================================= */
	/**
	 * 해당 문자열을 변형한다.
	 * 
	 * @param input
	 *            변형할 문자열 전체 dest 변형할 문자열
	 * @return retstr 리턴 데이터
	 */
	public String repStr(String input, String target, String dest) {
		String s_Data = "";
		String s_Tmp = input;
		int i = s_Tmp.indexOf(target);

		while (i != -1) {
			s_Data = s_Data + s_Tmp.substring(0, i) + dest;
			s_Tmp = s_Tmp.substring(i + target.length());
			i = s_Tmp.indexOf(target);
		}
		s_Data = s_Data + s_Tmp;
		return s_Data;
	}

	/**
	 * 숫자형을 변경한다.(1 --> 01)
	 * 
	 * @param input
	 *            변형할 문자열 전체
	 * @return retstr 리턴 데이터
	 */
	public String addZero(String input) {
		int num = 0;
		try {
			num = Integer.parseInt(input);
		} catch (Exception e) {
			return input;
		}

		if (num < 10)
			return "0" + num;
		else
			return input;
	}

	/**
	 * LIST 형식을 문자열을 구성한다.
	 * 
	 * @param input
	 *            원본 오브젝트(Vector, String[], Hashtable, DataSet)
	 * @param deli
	 *            문자열 구분자
	 * @param target
	 *            대상 INDEX 정보
	 * @return retstr 리턴 데이터
	 */
	public String getListStr(Object input, String deli, String target) {
		String retstr = "";

		if (input instanceof Vector) { // VECTOR 형

		} else if (input instanceof String[]) { // String 배열

		} else if (input instanceof Hashtable) { // Hashtable

		} else if (input instanceof DataSet) { // DataSet

		}

		return retstr;

	}

	/* ============================================= */
	// Platform 확인 zone
	/* ============================================= */
	/**
	 * Platform을 확인한다.
	 * 
	 * @return int 플랫폼(UNIX(0) ==> 유닉스, WINDOW(1) ==> 윈도우)
	 */
	public int getPlatform() {
		int plat = Code.UNIX;
		String platstr = System.getProperty("os.name");
		if (platstr.indexOf("Windows") != -1) {
			plat = Code.WINDOW;
		}
		return plat;
	}

	/**
	 * Platform을 확인한다.
	 * 
	 * @return String 플랫폼 패스 구분자
	 */
	public String getPlatformPath() {
		if (getPlatform() == Code.UNIX)
			PATH_GUBUN = "/";
		else
			PATH_GUBUN = "\\";

		return PATH_GUBUN;
	}

	/* ============================================= */
	// 서버관련 zone
	/* ============================================= */
	/**
	 * 서버 IP를 읽어온다.
	 * 
	 * @return String 서버 IP
	 */
	public String getServerIP() {

		String pos = MNAME + ".getServerIP()";

		String ip_addr = null;
		try {
			InetAddress ip = InetAddress.getLocalHost();
			ip_addr = ip.getHostAddress();
		} catch (Exception e) {
			log(pos, "서버 IP호출에 에러 발생");
		}
		return ip_addr;
	}

	/* ============================================= */
	// 나이 관련 zone
	/* ============================================= */
	/**
	 * 주민등록번호의 나이를 리턴한다.
	 * 
	 * @param ssn
	 *            주민번호
	 * @return int 나이
	 */
	public int getSsnAge(String ssn) {

		String curdate = getDate(7); // 현재 날짜
		String curyear = curdate.substring(0, 4);
		String curmonth = curdate.substring(4);

		String flag = ssn.substring(6, 7);
		String first2 = ssn.substring(0, 2);

		if (flag.equals("1") || flag.equals("2")) {
			first2 = "19" + first2;
		} else {
			first2 = "20" + first2;
		}

		int i_curyear = parseInt(curyear);
		int i_curmonth = parseInt(curmonth);

		int i_ssnyear = parseInt(first2);
		int i_ssnmonth = parseInt(ssn.substring(2, 6));
		int age = i_curyear - i_ssnyear;
		if (i_curmonth >= i_ssnmonth) { // 생일이 자났다면
			age++;
		}

		return age;
	}

	/**
	 * NULL 이면 "" 를 리턴한다.
	 * 
	 * @param input
	 *            입력값
	 * @return String 리턴값
	 */
	public String setNull(String input) {
		if (input == null)
			return "";
		else
			return input;
	}

	/* ============================================= */
	// 프로세스 관련 zone
	/* ============================================= */

	/**
	 * 프로세스플 runtime으로 수행한다.
	 * 
	 * @param cmd
	 *            수행 COMMAND
	 * @return Hashtable 리턴값 {[SUCCESS("Y","N")],[STR]}
	 */
	public Hashtable runProcess(String cmd) {
		Runtime runtime = null;
		Process p = null;

		InputStream rin = null;
		OutputStream rout = null;
		InputStream ein = null;

		Hashtable rethash = new Hashtable();

		try {

			int rexit; // 런타임 실행 결과 코드 저장

			runtime = Runtime.getRuntime();

			p = runtime.exec(cmd);

			p.waitFor();

			rin = p.getInputStream();
			rout = p.getOutputStream();
			ein = p.getErrorStream();

			String retstr = "";
			byte[] rstr = new byte[100]; // 런타임 리턴값

			while (rin.read(rstr) > 0) {
				retstr += new String(rstr);
				rstr = new byte[100];
			}

			rexit = p.exitValue();

			if (rexit == 0) {
				rethash.put("SUCCESS", "Y");
				rethash.put("STR", retstr);
			} else {
				rethash.put("SUCCESS", "N");
				byte[] estr = new byte[100];
				String errstr = "";
				while (ein.read(estr) > 0) {
					errstr += new String(estr);
					estr = new byte[100];
				}
				rethash.put("STR", errstr);
			}

		} catch (Exception e) {
			rethash.put("SUCCESS", "N");
			rethash.put("STR", e.toString());
		} finally {
			try {
				p.destroy();
				rin.close();
				rout.close();
				ein.close();
			} catch (Exception e1) {
			}
		}

		return rethash;

	}

	/* ============================================= */
	// URL 관련 zone
	/* ============================================= */
	/**
	 * 
	 * URL에 대한 페이지를 실행한 결과를 리턴한다. (GET 요청에 대한 결과를 리턴)
	 * 
	 * @param url
	 *            호출할 url
	 * @return String URL호출 결과 null : URL호출 실패
	 */
	public String getUrlData(String url) {

		String pos = MNAME + ".getUrlData()";

		// String content = "";
		StringBuffer content = new StringBuffer();
		try {
			URL source = new URL(url);
			URLConnection sconnection = source.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					sconnection.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException me) {
			log(pos, "URL이 잘못되었습니다.");
			content = null;
		} catch (IOException ioe) {
			log(pos, "파일 I/O에 에러가 발생했습니다.");
			content = null;
		} catch (Exception e) {
			log(pos, "에러가 발생했습니다.\n" + e.toString());
			content = null;
		}
		return content.toString();
	}

	/**
	 * 
	 * URL에 대한 페이지를 실행한 결과를 리턴한다. (POST 요청에 대한 결과를 리턴)
	 * 
	 * @param inputHash
	 *            호출정보 [IP : 호출URL 주소(IP|URL), PORT : 포트번호, URLPATH : 호출 경로
	 *            REQDATA : 인자값(HASHTABLE), REFERER : 참조자 설정, COOKIE : 쿠키 설정값]
	 * @return String URL호출 결과 null : URL호출 실패
	 */
	public String getPostUrlData(Hashtable inputHash) {

		String pos = MNAME + ".getPostUrlData()";

		// 호출정보를 확인한다.
		String ip = getData("IP", inputHash);
		String port = getData("PORT", inputHash);
		String urlpath = getData("URLPATH", inputHash);
		Hashtable reqdata = (Hashtable) getObjData("REQDATA", inputHash);
		String referer = getData("REFERER", inputHash);
		String cookie = getData("COOKIE", inputHash);

		if (ip.equals("") || port.equals("") || urlpath.equals("")) {
			return null;
		}

		StringBuffer reqstr = new StringBuffer();

		if (reqdata != null) {
			String key = null;
			String temp = null;
			Enumeration e = reqdata.keys();
			while (e.hasMoreElements()) {
				key = (String) e.nextElement();
				temp = getData(key, reqdata);
				reqstr.append(key);
				reqstr.append("=");
				reqstr.append(getURLEncode(temp));
				reqstr.append("&");
			}
		}

		Socket soc = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			soc = new Socket(ip, parseInt(port));
			out = new PrintWriter(soc.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		} catch (UnknownHostException ukhe) {
			log(pos, "연결 에러\n" + ukhe.toString());
			return null;
		} catch (IOException ioe) {
			log(pos, "연결 에러\n" + ioe.toString());
			return null;
		} catch (Exception e) {
			log(pos, "연결 에러\n" + e.toString());
			return null;
		}

		// 호출 결과값 저장
		StringBuffer result = new StringBuffer();

		// 데이터 전송
		try {
			byte[] reqbyte = reqstr.toString().getBytes();
			int reqlen = reqbyte.length;

			StringBuffer sendstr = new StringBuffer();
			sendstr.append("POST ");
			sendstr.append(urlpath);
			sendstr.append(" HTTP/1.0\r\n");

			sendstr.append("Referer: ");
			sendstr.append(referer);
			sendstr.append("\r\n");

			sendstr
					.append("Content-type: application/x-www-form-urlencoded\r\n");

			sendstr.append("Content-length: ");
			sendstr.append(reqlen);
			sendstr.append(" \r\n\r\n");

			sendstr.append(reqstr.toString());

			out.println(sendstr.toString());

			String getstr = null;
			boolean body_yn = false;
			while ((getstr = in.readLine()) != null) {
				if (!body_yn) {
					if (getstr.equals(""))
						body_yn = true;
				} else {
					result.append(getstr);
				}
			}
		} catch (Exception e) {
			log(pos, "송수신 에러 \n" + e.toString());
			return null;
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
				if (soc != null)
					soc.close();
			} catch (Exception e1) {
			}
		}

		return result.toString();
	}

	/* ============================================= */
	// 데이터베이스 관련 ZONE
	/* ============================================= */
	/**
	 * 
	 * 데이터베이스 관련 처리문자을 변경한다. 예) 검색어에서 ['aaa] 일 경우 ['''aaa]로 변경
	 * 
	 * @param url
	 *            호출할 url
	 * @return String URL호출 결과 null : URL호출 실패
	 */
	public String getDBConvData(String str) {

		String pos = MNAME + ".getDBConvData()";

		// ['] ==> [''] 변환
		str = repStr(str, "'", "''");

		return str;
	}

	/***************************************************************************
	 * =================================================== URL Zone
	 * ===================================================
	 */

	/**
	 * 
	 * URL을 ENCODING한다.
	 * 
	 * @param url
	 *            인코딩할 스트링
	 * @return String 인코딩한 스트링
	 * 
	 */
	public String getURLEncode(String url) {
		return URLEncoder.encode(url);
	}

	/**
	 * 
	 * URL을 DECODING한다.
	 * 
	 * @param url
	 *            디코딩할 스트링
	 * @return String 디코딩한 스트링
	 * 
	 */
	public String getURLDecode(String url) {
		return (new URLDecoder()).decode(url);
	}

	/***************************************************************************
	 * =================================================== File 처리 Zone
	 * ===================================================
	 */

	/**
	 * 
	 * 파일 존재 여부를 확인한다.
	 * 
	 * @param path
	 *            파일패스
	 * @return boolean 파일 존재 여부
	 * 
	 */
	public boolean existFile(String path) {
		return new File(path).exists();
	}

	/**
	 * 
	 * 파일 존재 하면 기존 패스를 존재하지 않으면 대체문자를 리턴
	 * 
	 * @param path
	 *            체크할 파일의 파일패스
	 * @param reppath
	 *            파일이 존재안할 경우 리턴 패스
	 * @return String 파일 존재 여부
	 * 
	 */
	public String getFilePath(String path, String reppath) {
		if (existFile(path))
			return path;
		else
			return reppath;
	}

	/**
	 * 
	 * 파일 존재 하면 기존 패스를 존재하지 않으면 대체문자를 리턴
	 * 
	 * @param path
	 *            체크할 파일의 파일패스
	 * @param realpath
	 *            파일이 존재할 경우 리턴 패스
	 * @param reppath
	 *            파일이 존재안할 경우 리턴 패스
	 * @return String 파일 존재 여부
	 * 
	 */
	public String getFilePath(String path, String realpath, String reppath) {
		if (existFile(path))
			return realpath;
		else
			return reppath;
	}

}
