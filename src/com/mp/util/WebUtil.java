
package com.mp.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.math.BigDecimal;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.text.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.mp.exception.*;
import com.mp.util.*;
import sun.misc.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : WebUtil.java
 * Version      : 1.0
 * 작성일       : 2002/03/18
 * 작성자       : 오범석
 * 수정일       : 2003/12/05
 * 수정자       : 오범석
 * 수정설명		: 페이지 리스트에서 입력 데이터 확인을 util의 getData로 수정
 *				  대소문자 구분 안함
 * 설명         : Web Utility 메소드를 구현한다.
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class WebUtil extends DBUtil {

	/*=============================================*/
	// Global Zone 
	/*=============================================*/

	/** 모듈명 설정 */
	private String MNAME = "WebUtil";

	/** 생성자를 호출한다. */
	public WebUtil() {}

	/*=============================================*/ 
	// Cookie Zone
	/*=============================================*/

	/** 
	 * 인증 쿠키값 확인.
	 * 인증 쿠키가 맞는지를 확인한다.
	 * @param		req			HttpServletRequest
	 * @return		boolean		확인 여부	
	 */
	public boolean checkCookie(HttpServletRequest req) {
		return checkCookie(req,Code.COOKIE_CRYPT_NONE);
	}

	/** 
	 * 인증 쿠키값 확인.
	 * 인증 쿠키가 맞는지를 확인한다.
	 * @param		req			HttpServletRequest
	 * @param		type		확인 방법(엔코딩, 암호화 방법등...)
	 * @return		boolean		확인 여부	
	 */
	public boolean checkCookie(HttpServletRequest req, int type) {

		String pos = MNAME+".checkCookie()";

		boolean cookieFound = false;
		Cookie[] cookies = req.getCookies();
		if(cookies == null) return cookieFound;
		Cookie thisCookie = null;
		String valstr = null;
		for(int i = 0; i < cookies.length; i++) {
			thisCookie = cookies[i];
            try {
				if(thisCookie.getName().equals(getEnv("ENVcookauthor"))) {
					if(getEnv("ENVreposit").equals("JNDI")) {
        	            valstr = (String)lookup("cookauthor");
					} else {
						valstr = getEnv("ENVcookauthor");
					}
			
					switch(type) {
						case Code.COOKIE_CRYPT_BASE64:
              				if(getBASE64D(thisCookie.getValue()).equals(valstr)) {
								cookieFound = true;
							}
							break;
						default:
							if(thisCookie.getValue().equals(valstr)) {
								cookieFound = true;			
							}
					}
				}
				if(cookieFound) break;
            } catch (Err err) {
               	log(pos,err.getEXStr());
				cookieFound = false;
				break;
			}
		}
		return cookieFound;
	}

	/** 
	 * 쿠키값 검사.
	 * 인증 쿠키값을 비교 검사한다.
	 * @param		req			HttpServletRequest
	 * @param		getObj		쿠키확인값
	 * @param		type		확인 방법(엔코딩, 암호화 방법등...)
	 * @return		boolean		확인 여부	
	 */
	public boolean isValidCookie(HttpServletRequest req, String getObj) {
		return isValidCookie(req,getObj,Code.COOKIE_CRYPT_NONE);
	}

	/** 
	 * 쿠키값 검사.
	 * 인증 쿠키값을 비교 검사한다.
	 * @param		req			HttpServletRequest
	 * @param		getObj		쿠키확인값
	 * @param		type		확인 방법(엔코딩, 암호화 방법등...)
	 * @return		boolean		확인 여부	
	 */
	public boolean isValidCookie(HttpServletRequest req, String getObj, int type) {

		String pos = MNAME+".isValidCookie()";

		boolean cookieFound = false;
		Cookie[] cookies = req.getCookies();
		if(cookies == null) return cookieFound;
		Cookie thisCookie = null;
		String valstr = null;
		for(int i = 0; i < cookies.length; i++) {
			thisCookie = cookies[i];
            try {
				if(thisCookie.getName().equals(getObj)) {
					if(getEnv("ENVreposit").equals("JNDI")) {
        	            valstr = (String)lookup("cookauthor");
					} else {
						valstr = getEnv("ENVcookauthor");
					}
					switch(type) {
						case Code.COOKIE_CRYPT_BASE64:
              				if(getBASE64D(thisCookie.getValue()).equals(valstr)) {
								cookieFound = true;
							}
							break;
						default:
							if(thisCookie.getValue().equals(valstr)) {
								cookieFound = true;			
							}
					}
				}
				if(cookieFound) break;
            } catch (Err err) {
            	log(pos,err.getEXStr());
				cookieFound = false;
				break;
            }
		}
		return cookieFound;
	}

	/** 
	 * 쿠키를 읽어온다.
	 * 쿠키명을 이용하여 쿠키 object를 리턴한다.
	 *
	 * @param		req			HttpServletRequest
	 * @param		getObj		쿠키확인값
	 * @return		Cookie		Cookie Object	
	 */
	public Cookie getValidCookie(HttpServletRequest req, String getObj) {
		boolean cookieFound = false;
		Cookie[] cookies = req.getCookies();
		if(cookies == null) return null;
		Cookie thisCookie = null;
		//쿠키내의 쿠키명과 쿠키값을 읽어온다.
		if(cookies == null) return null;	
		for(int i = 0; i < cookies.length; i++) {
			thisCookie = cookies[i];
			if(thisCookie.getName().equals(getObj)) {
				cookieFound = true;
				break;
			}
		}
		if(cookieFound) return thisCookie;
		else return null;
	}

	/** 
	 * 쿠키값을 가져온다.
	 * 특정키에 대한 쿠키값을 리턴한다.
	 * @param		req			HttpServletRequest
	 * @param		key			쿠키 Name
	 * @return		String		쿠키 Value
	 */
	public String getCookValue(HttpServletRequest req, String key) {
		return getCookValue(req,key,Code.COOKIE_CRYPT_NONE);
	}	

	/** 
	 * 쿠키값을 가져온다.
	 * 특정키에 대한 쿠키값을 리턴한다.
	 * @param		req			HttpServletRequest
	 * @param		key			쿠키 Name
	 * @param		type		확인 방법(엔코딩, 암호화 방법등...)
	 * @return		String		쿠키 Value
	 */
	public String getCookValue(HttpServletRequest req, String key, int type) {
		Cookie cook = null;
		String retval = null;
       	if((cook = getValidCookie(req, key)) != null) {
			switch(type) {
				case Code.COOKIE_CRYPT_BASE64:
					retval = getBASE64D(cook.getValue());						
					break;
				default:
					retval = cook.getValue();
			}
			return retval;
        } else {
            return "";
        }
    }

    /**
	 * 쿠키값을 설정한다.
	 * @param		req			HttpServletRequest
	 * @param		res			HttpServletResponse
	 * @param		key			쿠키 Name
	 * @param		value		쿠키 Value
	 * @exception	Err			<br>에러코드	: 전이	
	 *							<br>메세지		: 전이
	 */
    public void setCookValue(HttpServletRequest req, 
							 HttpServletResponse res, 
							 String key, String value) 
						throws Err {
		setCookValue(req,res,key,value,Code.COOKIE_CRYPT_NONE);
	}

    /**
	 * 쿠키값을 설정한다.
	 * @param		req			HttpServletRequest
	 * @param		res			HttpServletResponse
	 * @param		key			쿠키 Name
	 * @param		value		쿠키 Value
	 * @param		type		확인 방법(엔코딩, 암호화 방법등...)
	 * @exception	Err			<br>에러코드	: 전이	
	 *							<br>메세지		: 전이
	 */
    public void setCookValue(HttpServletRequest req, 
							 HttpServletResponse res, 
							 String key, String value, int type) 
						throws Err {

		String pos = MNAME+".setCookValue()";

		switch(type) {
			case Code.COOKIE_CRYPT_BASE64:
				value = getBASE64E(value);						
				break;
			default:
		}

		Cookie cook = null;
		if((cook = getValidCookie(req,key)) != null) {
			cook.setValue(value);
			return;
		} 

		cook = new Cookie(key, value);
        String setDomain = null;
        String setMaxAge = null;
        String setPath = null;
        try {
			if(getEnv("ENVreposit").equals("JNDI")) {
            	setDomain = (String)lookup("cookdomain");
            	setMaxAge = (String)lookup("cookage");
            	setPath = (String)lookup("cookpath");
			} else {
				setDomain = getEnv("ENVcookdomain");
				setMaxAge = getEnv("ENVcookage");
				setPath = getEnv("ENVcookpath");
			}
        } catch (Err err) {
            log(pos,err.getEXStr());
            throw err;
        }
        cook.setDomain(setDomain);
        cook.setMaxAge(Integer.parseInt(setMaxAge));
        cook.setPath(setPath);
		res.addCookie(cook);
    }

    /**
	 * 쿠키값을 삭제한다.
	 * 
	 * @param		req			HttpServletRequest
	 * @param		res			HttpServletResponse
	 * @param		key			쿠키 Name
	 * @exception	Err			<br>에러코드	: 전이	
	 *							<br>메세지		: 전이
	 */
    public void delCookValue(HttpServletRequest req, 
							 HttpServletResponse res, 
							 String key) throws Err {

		String pos = MNAME+".delCookValue()";

        String setDomain = null;
        String setMaxAge = null;
        String setPath = null;
        try {
			if(getEnv("ENVreposit").equals("JNDI")) {
            	setDomain = (String)lookup("kiup.conf.cookdomain");
            	setPath = (String)lookup("kiup.conf.cookpath");
			} else {
				setDomain = getEnv("ENVcookdomain");
				setPath = getEnv("ENVcookpath");
			}
        } catch (Err err) {
            log(pos,err.getEXStr());
            throw err;
        }
		Cookie cook = new Cookie(key,null);
		cook.setMaxAge(0);
		cook.setDomain(setDomain);
		cook.setPath(setPath);
		res.addCookie(cook);
    }

	/*=============================================*/ 
	// Session Zone
	/*=============================================*/

    /**
	 * 세션값을 리턴한다.
	 * 
	 * @param		key			세션 Name
	 * @param		req			HttpServletRequest
	 * @return		Object		세션값
	 *
	 */
    public Object getSesValue(String key, HttpServletRequest req) {
		return getSesValue(key, req, Code.SESSION_CRYPT_NONE);	
	}

    /**
	 * 세션값을 리턴한다.
	 * 
	 * @param		key			세션 Name
	 * @param		req			HttpServletRequest
	 * @param		type		확인 방법(엔코딩, 암호화 방법등...)
	 * @return		Object		세션값
	 *
	 */
    public Object getSesValue(String key, HttpServletRequest req, int type) {
		HttpSession session = req.getSession(true);
        if(isValidSession(key,req)) {
            Object sesval = session.getAttribute(key);
            if(sesval instanceof String) {
				String retval = null;
				switch(type) {
					case Code.COOKIE_CRYPT_BASE64:
						retval = getBASE64D((String)sesval);						
						break;
					default:
						retval = (String)sesval;
				}
				return retval;
            } else {
            	return sesval;
			}            	
        } else {
            return "";
        }
    }

    /**
	 * 세션값을 설정한다.
	 * 
	 * @param		key			세션 Name
	 * @param		value		세션 Value
	 * @param		req			HttpServletRequest
	 *
	 */
    public void setSesValue(String key, Object value, HttpServletRequest req) {
		setSesValue(key, value, req, Code.SESSION_CRYPT_NONE);
	}

    /**
	 * 세션값을 설정한다.
	 * 
	 * @param		key			세션 Name
	 * @param		value		세션 Value
	 * @param		type		확인 방법(엔코딩, 암호화 방법등...)
	 * @param		req			HttpServletRequest
	 *
	 */
    public void setSesValue(String key, Object value, HttpServletRequest req, int type) {
		HttpSession session = req.getSession(true);
        if(value instanceof String) {
			switch(type) {
				case Code.COOKIE_CRYPT_BASE64:
        			session.setAttribute(key, getBASE64E((String)value));
					break;
				default:
					session.setAttribute(key, value);
			}
		} else {
			session.setAttribute(key, value);
		}        	
    }

    /**
	 * 세션값을 삭제한다.
	 * 
	 * @param		name		세션 Name
	 * @param		req			HttpServletRequest
	 *
	 */
	public void removeSession(String name, HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		session.removeAttribute(name);
	}	

    /**
	 * 세션을 삭제한다.
	 * 현재 사용자의 세션을 완전 소멸한다.
	 * 
	 * @param		req			HttpServletRequest
	 *
	 */
	public void delSession(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		session.invalidate();
	}

    /**
	 * 등록 세션의 존재여부를 검사한다.
	 * 
	 * @param		getObj		세션 Name
	 * @param		req			HttpServletRequest
	 * @return		boolean		등록 세션의 존재여부
	 *
	 */
	public boolean isValidSession(String getObj, HttpServletRequest req) {

		String pos = MNAME+".isValidSession()";

		HttpSession session = req.getSession(true);
		Enumeration sessionNames = session.getAttributeNames();
		int index = 0;
		try {
			while(sessionNames.hasMoreElements()) { 
				String name = (String)sessionNames.nextElement();
				if(name.equals(getObj)) return true;
			}
		} catch(NullPointerException ne) {
			log(pos,"에러 발생했습니다. \n" + ne.toString());
		}catch(Exception ee) {
			log(pos,"에러 발생했습니다. \n" + ee.toString());
		}
		return false;
	}

    /**
	 * 권한을 검사한다.
	 * 
	 * @param		getObj		세션 Name
	 * @param		req			HttpServletRequest
	 * @return		boolean		권한 여부
	 *
	 */
	public boolean isAuthor(String getObj, HttpServletRequest req) {

		String pos = MNAME+".isAuthor()";

		HttpSession session = req.getSession(true);
		Enumeration sessionNames = session.getAttributeNames();
		int index = 0;
		try {
			while(sessionNames.hasMoreElements()) { 
				String name = (String)sessionNames.nextElement();
				if(name.equals(getObj)) {
					if(getEnv(getObj).equals((String)getSesValue(name,req))) {
						return true;
					} else {
						return false;
					}
				}
			}
		} catch(NullPointerException ne) {
			log(pos,"에러 발생했습니다. \n" + ne.toString());
		}catch(Exception ee) {
			log(pos,"에러 발생했습니다. \n" + ee.toString());
		}
		return false;
	}
	
	/*=============================================*/ 
	// Page Zone
	/*=============================================*/

    /**
	 * 리스트 페이지 화면 리턴한다.
	 * 리스트 페이지에 대한 페이지 번호를 출력한다.
	 * @param		listhash		페이지번호 출력 인자<br>
	 *								(전체페이지수(String),현재페이지(String),<br>
	 *								 한번에 보여질 페이지순번(String), <br>  
	 *								 페이지 링크명=자바스크립트사용(String))  
	 * @return		String			페이지리스트 번호
	 */
    public String getPage(Hashtable listhash) throws Err {

        boolean valid = true;
        //페이지 출력을 위한 인자
        String totpage = null;
        String curpage = null;
        String pagelist = null;
        String linkname = null;
        //링크 출력을 위한 변수
        String retVal = "";

		totpage = getData("totpage",listhash);	

		curpage = (String)getObjData("curpage",listhash);
		if(curpage == null) curpage = "1";

		pagelist = (String)getObjData("pagelist",listhash);
		if(pagelist == null) {		
            try {
				if(getEnv("ENVreposit").equals("JNDI")) {
	                pagelist = (String)lookup("mp.conf.pagelist");
				} else {
					pagelist = getEnv("ENVpagelist");
				}
            } catch (Err err) {
                valid = false;
                throw err;
            }
        }

		linkname = (String)getObjData("linkname",listhash);	
		if(linkname == null) {
            linkname = "goPage";
        }

        if(valid) {
            Hashtable pageopt = new Hashtable();
            pageopt.put("totpage", totpage);
            pageopt.put("curpage", curpage);
            pageopt.put("pagelist", pagelist);
            pageopt.put("linkname", linkname);
            retVal = getPageList(pageopt);
        }

        return  retVal;

    }

    /**
     *
     * 페이지 링크를 출력한다. <br> HTML TAG 사용
     * @param   opt     입력값의 해쉬테이블 <br>
     * 					(totpage 전체페이지의 수,<br>
     *  				 curpage 현재페이지 위치,<br>
     *  				 pagelist 페이지수를 보여줄 리스트 수,<br>
     *  				 linkname 링크를 실행할 스크립트 함수명)
     * @return  String  페이지리스트(HTML TAG 사용)
     *
     */
    public String getPageList(Hashtable opt) {
		String link = getPageTextList(opt); 
        String retstr = "";
        retstr += "<table width=\"100%\" cellspacing=0 cellpadding=0 border=0>";
        retstr += "<tr><td align=\"center\">";
        retstr += link;
        retstr += "</td></tr></table>";
        return retstr;
	}


    /**
     *
     * 페이지 링크를 출력한다.
     * @param   opt     입력값의 해쉬테이블 <br>
     * 					(totpage 전체페이지의 수,<br>
     *  				 curpage 현재페이지 위치,<br>
     *  				 pagelist 페이지수를 보여줄 리스트 수,<br>
     *  				 linkname 링크를 실행할 스크립트 함수명)
     * @return  String  페이지리스트
     *
     */
    public String getPageTextList(Hashtable opt) {
        int totpage = 10;
        int curpage = 1;
        int pagelist = 10;
        String linkname  = null;

		String firstImgPath	= null;	//처음
		String lastImgPath	= null;	//마지막
		String nextImgPath	= null;	//이후
		String prevImgPath	= null;	//이전
		try {
			firstImgPath	= getEnv("ENVFirstImg",false);	//처음
			lastImgPath	= getEnv("ENVLastImg",false);		//마지막
			nextImgPath	= getEnv("ENVNextImg",false);		//이후
			prevImgPath	= getEnv("ENVPrevImg",false);		//이전
		} catch(Err err) {
			//환경값 호출 실패 라면 모두 null로 치환
			firstImgPath = null;
			lastImgPath = null;
			nextImgPath = null;
			prevImgPath = null;
		}

        //리턴할 페이지 링크를 저장
        String link = "";

		String totpage_str = (String)getObjData("totpage",opt);	
		if(totpage_str != null) {
            totpage = parseInt(totpage_str);
        }
		String curpage_str = (String)getObjData("curpage",opt);
		if(curpage_str != null) {
            curpage = parseInt(curpage_str);
        }
		String pagelist_str = (String)getObjData("pagelist",opt);
		if(pagelist_str != null) {
            pagelist = parseInt(pagelist_str);
        }
		linkname = (String)getObjData("linkname",opt);
		if(linkname == null) linkname = "goPage";
		

        /*===============================================*
            현재 페이지가 전체페이지수보다 크며 총 페이지수가 1일때
        *===============================================*/
        if(curpage > totpage) curpage = totpage; 
        if(totpage == 0 || totpage == 1) return "";

        /*===============================================*
            루프를 돌리기 위한 시작, 끝변수 결정
        *===============================================*/
        int loopcnt = 0;
        if(curpage%pagelist == 0) {
           //현재 페이지가 리스트페이지의 배수일 경우
            loopcnt = (curpage/pagelist)*pagelist;
        } else {
            loopcnt = (curpage/pagelist+1)*pagelist;
        }
        int initcnt = 1;
        if(totpage > pagelist) {
            //전체 자료의 총페이지수가 전체 페이지수보다 클때
            initcnt = (loopcnt-pagelist)+1;
        }

		if(curpage > 1) {
			//처음 1페이지 링크
			if(firstImgPath == null) {
				link += "<a href=\"javascript:"+linkname+"('1');\">";
				link += "[처음]</a>&nbsp;";
			} else {
				link += "<a href=\"javascript:"+linkname+"('1');\">";
				link += "<img src=\"";
				link += firstImgPath;
				link += "\" border=0 align=\"absmiddle\">";
				link += "</a>&nbsp;";
			}
		}
		
        /*===============================================*
            "이전"을 링크걸기 위해
        *===============================================*/
        if(initcnt > pagelist) {
			if(prevImgPath == null) {
          		link += "<a href=\"javascript:"+linkname+"('"+(initcnt-1)+"');\">";
           	 	link += "[<<]</a>&nbsp;";
			} else {
				link += "<a href=\"javascript:"+linkname+"('"+(initcnt-1)+"');\">";
				link += "<img src=\"";
				link += prevImgPath;
				link += "\" border=0 align=\"absmiddle\">";
				link += "</a>&nbsp;";
			}
        }

        /*===============================================*
            링크를 위해 전체페이지만큼 돌림
        *===============================================*/
        int pos = 0;
        for(int i = initcnt; i <= loopcnt; i++) {
            if(i > totpage) break;

            if(curpage == i) {
                link += "<Font color='red'><b>"+i+"</b></Font>&nbsp;&nbsp;";
            } else {
                link += "<a href=\"javascript:"+linkname+"('"+i+"');\">";
                link += "["+i+"]</a>&nbsp;";
            }
            pos = i;
        }

        /*===============================================*
            "이후"을 링크걸기 위해
        *===============================================*/
        if(pos < totpage && pos==loopcnt) {
			if(nextImgPath == null) {
         	   	link += "<a href=\"javascript:"+linkname+"('"+(loopcnt+1)+"');\">";
           		link += "[>>]</a>&nbsp;";
			} else {
				link += "<a href=\"javascript:"+linkname+"('"+(loopcnt+1)+"');\">";
				link += "<img src=\"";
				link += nextImgPath;
				link += "\" border=0 align=\"absmiddle\">";
				link += "</a>&nbsp;";
			}
        }


		if(curpage < totpage) {	
			//마지막 페이지 링크
			if(lastImgPath == null) {
				link += "<a href=\"javascript:"+linkname+"('"+totpage+"');\">";
				link += "[마지막]</a>&nbsp;";
			} else {
				link += "<a href=\"javascript:"+linkname+"('"+totpage+"');\">";
				link += "<img src=\"";
				link += lastImgPath;
				link += "\" border=0 align=\"absmiddle\">";
				link += "</a>&nbsp;";
			}
		}

        return link;
    }

	/*===================================================*
		Error Zone
	*===================================================*/
	/**
	 *
	 * 에러 정보를 설정한다.
	 *
	 * @param	req					HttpServletRequest	
	 *			p_code				에러코드
	 * 			p_msg				에러메세지
	 * @return	boolean				저장 성공 여부 	
	 *
	 */
	public boolean setErr(HttpServletRequest req, String p_code, String p_msg) {
		req.setAttribute("__erryn","Y");
		req.setAttribute("__p_code",p_code);
		req.setAttribute("__p_msg",p_msg);
		return true;
	}

	/**
	 *
	 * 에러 정보를 설정한다.
	 *
	 * @param	req					HttpServletRequest	
	 *			p_code				에러코드
	 * 			p_pos				에러위치
	 * 			p_msg				에러메세지
	 * @return	boolean				저장 성공 여부 	
	 *
	 */
	public boolean setErr(HttpServletRequest req, String p_code, String p_pos, String p_msg) {
		req.setAttribute("__erryn","Y");
		req.setAttribute("__p_code",p_code);
		req.setAttribute("__p_pos",p_pos);
		req.setAttribute("__p_msg",p_msg);
		return true;
	}

	/**
	 *
	 * 에러 정보를 설정한다.
	 *
	 * @param	req					HttpServletRequest	
				err					Err 클래스
	 * @return	boolean				저장 성공 여부 	
	 *
	 */
	public boolean setErr(HttpServletRequest req, Err err) {
		req.setAttribute("__erryn","Y");
		req.setAttribute("__p_code",err.getCode());
		req.setAttribute("__p_pos",err.getPos());
		req.setAttribute("__p_msg",err.getMsg());
		return true;
	}

	/**
	 *
	 * 에러 코드를 읽어온다.
	 *
	 * @param	req					HttpServletRequest	
	 * @return	String				에러코드
	 *
	 */
	public String getECode(HttpServletRequest req) {
		String p_code = (String)req.getAttribute("__p_code");
		if(p_code == null) p_code = "";
		return p_code;
	}

	/**
	 *
	 * 에러 위치를 읽어온다.
	 *
	 * @param	req					HttpServletRequest	
	 * @return	String				에러위치
	 *
	 */
	public String getEPos(HttpServletRequest req) {
		String p_pos = (String)req.getAttribute("__p_pos");
		if(p_pos == null) p_pos = "";
		return p_pos;
	}

	/**
	 *
	 * 에러 메세지를 읽어온다.
	 *
	 * @param	req					HttpServletRequest	
	 * @return	String				에러메세지
	 *
	 */
	public String getEMsg(HttpServletRequest req) {
		String p_msg = (String)req.getAttribute("__p_msg");
		if(p_msg == null) p_msg = "";
		return p_msg;
	}

	/**
	 *
	 * 에러 여부를 리턴한다.
	 *
	 * @param	req					HttpServletRequest	
	 * @return	boolean				에러메세지�	 *
	 */
	public boolean checkErr(HttpServletRequest req) {
		String erryn = (String)req.getAttribute("__erryn");
		if(erryn != null && erryn.equals("Y")) return true;
		else return false;
	}

	/*===================================================*
		문자 전송 Zone
	*===================================================*/
	/**
	 *
	 * 전송 문자를 생성한다.
	 *
	 * @param	inputhash			전송 데이터
	 * 			target				변환할 문자
	 * 			enc					변경될 문자
	 * @return	String				전송 문자열
	 *
	 */
	public String getSendStr(Hashtable inputhash,char target, char enc) {
		
		Enumeration Enumeration = inputhash.keys();

		String key = null;
		String value = null;
		String retstr = "";
		while(Enumeration.hasMoreElements()) {
			key = (String)Enumeration.nextElement();	
			value = getData(key,inputhash);
			retstr += getEncoding(key,target,enc) + target + getEncoding(value,target,enc) + enc;
		}	

		
		return retstr;
	}

	/**
	 *
	 * 해당 문자를 인코딩한다.
	 *
	 * @param	input				입력 데이터
	 * 			target				변환할 문자
	 * 			enc					변경될 문자
	 * @return	String				변환 데이터
	 *
	 */
	public String getEncoding(String input,char target, char enc) {
		String retstr = input;
		retstr = retstr.replace(target,enc);
		return retstr;
	}


	/**
	 *
	 * 입력 파라미터 처리
	 *
	 * @param	request				HttpServletRequest
	 * @param	data				체크 데이터
	 * @param	setData				셋팅데이터
	 * @return	String				입력 데이터
	 *
	 */
	public String getRequestData(HttpServletRequest request,String data, String setData) {

		String temp = request.getParameter(data);
		if(temp == null || temp.equals("")) {
			return setData;	
		} 
		return temp;
	}

	/**
	 *
	 * 설정 어트리뷰트 처리
	 *
	 * @param	request				HttpServletRequest
	 * @param	data				체크 데이터
	 * @param	setData				셋팅데이터
	 * @return	String				입력 데이터
	 *
	 */
	public String getRequestAttr(HttpServletRequest request,String data, String setData) {

		String temp = (String)request.getAttribute(data);
		if(temp == null || temp.equals("")) {
			return setData;	
		} 
		return temp;
	}

	/*===================================================*
		MPv3 세션 체크
	*===================================================*/
	/**
	 *
	 * 어드민 권한 여부를 확인한다.
	 *
	 * @param	전송 데이터
	 * 			target				변환할 문자
	 * 			enc					변경될 문자
	 * @return	String				전송 문자열
	 *
	 */
	public boolean checkAdmin(HttpServletRequest req) {
		String dept_no = (String)getSesValue("NEO_DEPT_NO",req);
		if(dept_no.equals("1")) {	//어드민 그룹	
			return true;
		} else {
			return false;
		}
	}

    /**
     *
     * REQUEST 파라미터 재생성
     * 요청한 HttpServletRequest의 파라미터를 모두 추출하여
     * QUERY STRING 으로 재생성한다.
     *
     * @param   request             HttpServletRequest
     * @param   append             	기본 값 append 여부
     * @return  String              QUERY STRING
     *
     */
    public String makeRequest(HttpServletRequest request, boolean append) {
        return makeRequest(request, null, null, append);
    }

    /**
     *
     * REQUEST 파라미터 재생성
     * 요청한 HttpServletRequest의 파라미터를 모두 추출하여
     * QUERY STRING 으로 재생성한다.
     *
     * @param   request             HttpServletRequest
     * @return  String              QUERY STRING
     *
     */
    public String makeRequest(HttpServletRequest request) {
        return makeRequest(request, null, null,true);
    }

    /**
     *
     * REQUEST 파라미터 재생성
     * 요청한 HttpServletRequest의 파라미터를 모두 추출하여
     * QUERY STRING 으로 재생성한다.
     *
     * @param   request             HttpServletRequest
     * @param   url                 호출 URL
     * @return  String              QUERY STRING
     *
     */
    public String makeRequest(HttpServletRequest request,String url) {
        return makeRequest(request, url, null,true);
    }

    /**
     *
     * REQUEST 파라미터 재생성
     * 요청한 HttpServletRequest의 파라미터를 모두 추출하여
     * QUERY STRING 으로 재생성한다.
     *
     * @param   request             HttpServletRequest
     * @param   url                 호출 URL
     * @param   append              기본값 추가 여부
     * @return  String              QUERY STRING
     *
     */
    public String makeRequest(HttpServletRequest request,String url, boolean append) {
        return makeRequest(request, url, null,append);
    }

    /**
     *
     * REQUEST 파라미터 재생성
     * 요청한 HttpServletRequest의 파라미터를 모두 추출하여
     * QUERY STRING 으로 재생성한다.
     *
     * @param   request             HttpServletRequest
     * @param   url                 호출 URL
     * @param   append              추가 파라미터
     * @return  String              QUERY STRING
     *
     */
    public String makeRequest(HttpServletRequest request,String url, String append) {
        return makeRequest(request, url, append,true);
    }

    /**
     *
     * REQUEST 파라미터 재생성
     * 요청한 HttpServletRequest의 파라미터를 모두 추출하여
     * QUERY STRING 으로 재생성한다.
     *
     * @param   request             HttpServletRequest
     * @param   url                 호출 URL
     * @param   append              추가 파라미터
     * @param   append_flag         기본값 append 여부
     * @return  String              QUERY STRING
     *
     */
    public String makeRequest(HttpServletRequest request,String url, String append, boolean append_flag) {

		boolean flag = false;

        String retstr = "";
        if(url != null && !url.equals("")) retstr += url;

		if(append_flag) {
   	    	Enumeration Enumeration = request.getParameterNames();
   	    	if(Enumeration.hasMoreElements() || (append != null && !append.equals(""))){ 
				retstr += "?";
				flag = true;
			}
        	while(Enumeration.hasMoreElements()) {
            	String name = (String)Enumeration.nextElement();
            	String value = getURLEncode(request.getParameter(name));
            	retstr += (name + "=" + value);
            	if(Enumeration.hasMoreElements()) retstr += "&";
        	}
		}
		
        if(append != null && !append.equals("")) {
			if(flag) retstr += "&" + append;
			else retstr += "?" + append;
		} 

        return retstr;

    }

}

