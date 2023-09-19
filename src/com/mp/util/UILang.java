package com.mp.util; 

import javax.naming.*;
import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : UILang.java
 * Version      : 1.0
 * 작성일       : 2004/09/16
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 * 수정설명		: 
 *
 * 설명         : UI 언어권 정보 파일 로딩 및 오퍼레이션 
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class UILang extends Env {
  
  	/** 모듈명 설정 */
  	private String MNAME = "UILang";
  
	/** 언어권 정보를 저장한다.	*/
	private static Hashtable langhash = null; 

	/** 공백 언어권 정보[해당 언어권 정보가 없을 때 리턴해준다]  	*/
	private static String[] defaultarr = null;

	/** 생성자.  */
	public UILang() {
		//langhash = new Hashtable();
		//defaultarr = new String[Code.UI_LANG_SIZE];
		//for(int i = 0; i < Code.UI_LANG_SIZE; i++) {
		//	defaultarr[i] = "UNDEFINED";
		//}
	}

    /**
     *
     * UI 언어 화일을 읽어들인다.
     * @param   filename	언어 설정 화일
     * @return  Hashtable 	언어권 정보
     *
     */
    public void loadUILang(String filename) {
		String pos = MNAME+".loadUILang()";
   	   	Properties prop = null;
       	FileInputStream in = null;
       	String key = null;
       	String value = null;

		langhash = new Hashtable();

       	try {
           	in = new FileInputStream(filename);
           	prop = new Properties();
           	prop.load(in);
           	Enumeration enu = prop.propertyNames();
           	while(enu.hasMoreElements()) {
               	key = (String)enu.nextElement();
				if(key.length() < 4) { //스킵		
					continue;
				}

				value = prop.getProperty(key);

				//key값의 처음 3자리는 언어권 지정이며, 나머지를 구분코드이다.
				String lang = key.substring(0,3);
				String code = key.substring(3);

				lang = lang.toUpperCase();
				code = code.toUpperCase();

				String[] arr = null;
				if((arr = (String[])langhash.get(code)) == null) {	//이미 존재 하는 코드라면.	
					arr = new String[Code.UI_LANG_SIZE];
				}
				
				if(lang.equals(Code.UI_LANG_KOR)) {	//한극어				
					arr[Code.UI_LANG_KOR_POS] = new String(value.getBytes("ISO-8859-1"),"UTF-8");
					//arr[Code.UI_LANG_KOR_POS] = value;
				} else if(lang.equals(Code.UI_LANG_ENG)) {	//영어			
					arr[Code.UI_LANG_ENG_POS] = new String(value.getBytes("ISO-8859-1"),"UTF-8");
				} else if(lang.equals(Code.UI_LANG_JPN)) {	//일본어		
					arr[Code.UI_LANG_JPN_POS] = value;
				} else if(lang.equals(Code.UI_LANG_CHI)) {	//중국어		
					arr[Code.UI_LANG_CHI_POS] = new String(value.getBytes("ISO-8859-1"),"UTF-8");
					//arr[Code.UI_LANG_CHI_POS] = value;
				} 
				
				langhash.put(code,arr);

           	}
       	} catch (Exception e) {
           	log(pos,"UI 언어권 정보 로딩 실패.\n언어정보파일 : ["+filename+"]\n"+ e.toString());
       	} finally {
			try {
				in.close();
			} catch (Exception e1) {}
		}
    }

    /**
     *
     * 언어권 정보를 리턴한다.
	 * 언어권 정보 전체를 리턴한다.
     * @return  	Hashtable   언어권 (String, String)
     */
	public Hashtable getUILang() { 
		String pos = MNAME+".getUILang()";
		return langhash;
	}

    /**
     *
     * 언어권 정보를 리턴한다.
 	 * 코드값에 대한 언어권 정보를 리턴한다.[배열정보]
     * @param		code		코드값
     * @return  	String[]	언어권 정보	
     * @exception	Err			<br>에러코드	: 111
	 *							<br>메세지		: 시스템 에러 발생
     * 
     */
	public String[] getUILang(String code) throws Err {
		String pos = MNAME+".getUILang(String)";
		String[] arr = null;
		code = code.toUpperCase();
		if(langhash != null) {
			if(langhash.containsKey(code)) {
				arr = (String[])langhash.get(code);
			} else {
				arr = defaultarr;
			}
		} else {
			log(pos,"UI 언어권 정보가 로드되지 않았습니다.\n코드명 : [" + code + "]");
			throw new Err("1","5","G","시스템 에러 발생.",pos);
		}
		return arr;
	} 

	/**
	 *
	 * 특정 UI 언어권 정보를 리턴한다.
     * @param		lang		언어구분
	 *			    code		코드값
     * @return  	String		언어권 정보
     * @exception	Err			<br>에러코드	: 111
	 *							<br>메세지		: 시스템 에러 발생
     * 
     */
	public String getUILang(String lang, String code) throws Err {
		String pos = MNAME+".getUILang()";
		String[] arr = null;
		String value = "UNDEFINED";
		lang = lang.toUpperCase();
		code = code.toUpperCase();
		if(langhash != null) {
			if(langhash.containsKey(code)) {
				arr = (String[])langhash.get(code);
				if(lang.equals(Code.UI_LANG_KOR)) {	//한국어
					value = arr[Code.UI_LANG_KOR_POS];
				} else if(lang.equals(Code.UI_LANG_ENG)) {	//영어
					value = arr[Code.UI_LANG_ENG_POS];
				} else if(lang.equals(Code.UI_LANG_JPN)) {	//일본어
					value = arr[Code.UI_LANG_JPN_POS];
				} else if(lang.equals(Code.UI_LANG_CHI)) {	//중국어
					value = arr[Code.UI_LANG_CHI_POS];
				}
				if(value == null) value = "UNDEFINED";
			}
		} else {
			log(pos,"UI 언어권 정보가 로드되지 않았습니다.\n언어권 : [" + lang + "], 코드 : ["+code+"]");
			throw new Err("1","5","G","시스템 에러 발생.",pos);
		}
		return value;
	}

	/**
	 *
	 * 특정 UI 언어권 정보를 리턴한다.
     * @param		request		HttpServletRequest
	 *			    code		코드값
     * @return  	String		언어권 정보
     * @exception	Err			<br>에러코드	: 111
	 *							<br>메세지		: 시스템 에러 발생
     * 
     */
	public String getUILang(HttpServletRequest request, String code) throws Err {
		String pos = MNAME+".getUILang()";

		HttpSession session = request.getSession(true);
		Object sesval = session.getAttribute("NEO_UILANG");
		if(sesval == null) return "UNDEFINED";
			
		return getUILang((String)sesval,code);

	}

	/**
	 * 특정 UI 언어권 정보를 설정한다.
	 * @param	 code		코드
	 * 			 arr		설정값
	 */
	public void setUILang(String code, String[] arr) {
		String pos = MNAME+".setUILang()";
		if(langhash != null) langhash.put(code.toUpperCase(),arr);
		else log(pos,"UI 언어권 정보가 등록돼어 있지 않습니다.");
	}

	/**
	 * 특정 UI 언어권 정보를 설정한다.
	 * @param	 lang		언어권
	 * 			 code		코드
	 * 			 value		설정값
	 */
	public void setUILang(String lang, String code, String value) {
		String pos = MNAME+".setUILang()";

		lang = lang.toUpperCase();
		code = code.toUpperCase();

		if(langhash != null) {
			String[] arr = new String[Code.UI_LANG_SIZE];			
			if(lang.equals(Code.UI_LANG_KOR)) {			//한국어
				arr[Code.UI_LANG_KOR_POS] = value;		
			} else if(lang.equals(Code.UI_LANG_ENG)) {	//영어
				arr[Code.UI_LANG_ENG_POS] = value;		
			} else if(lang.equals(Code.UI_LANG_JPN)) {	//일본어
				arr[Code.UI_LANG_JPN_POS] = value;		
			} else if(lang.equals(Code.UI_LANG_CHI)) {	//중국어
				arr[Code.UI_LANG_CHI_POS] = value;		
			}
			langhash.put(code,arr);
		} else log(pos,"UI 언어권 정보가 등록돼어 있지 않습니다.");
	}

}
