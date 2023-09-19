
package com.mp.exception;

import java.util.*;
import java.rmi.RemoteException;


/**
 * <pre>
 * 프로그램유형 : Exception(java)
 * 프로그램명   : Err.java
 * Version      : 1.0
 * 작성일       : 2002/04/06
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 * 설명         : 에러를 처리한다.
 *				  Err_CD1	==> 대분류_에러구분코드(숫자값)
 *				  Err_CD2 	==> 중분류_에러구분코드(숫자값)
 *				  Err_CD3   ==> 소분류_에러구분코드(숫자값)
 *				  Err_Msg 	==> 사용자 에러 메시지 (문자값)
 *				  Err_Raw_Msg 	==> 시스템 에러 메시지 (문자값)
 * 프로젝트명   : 표준개발
 * Copyright	: BUMSUK OH
 * </pre>
 */

public class Err extends RemoteException {

	/** 리턴할 에러값 저장 */
	private Hashtable errHash = null;

	/**
	 * 함수명 : getErrValue()<br>
	 * 목  적 : 에러 값을 리턴한다.
	 * @return	Hashtable 	에러값
	 */	 
	public Hashtable getErrValue() {
		return errHash;
	}

	/**
	 * 함수명 : getMsg()<br>
	 * 목  적 : 에러 메세지를 리턴한다.
	 * @return	String		에러 메세지(사용자 메세지)
	 */
	public String getMsg() {
		return (String)errHash.get("Err_Msg");		
	}

	/**
	 * 함수명 : getCode()<br>
	 * 목  적 : 에러 코드를 리턴한다.
	 * @return	String		에러코드(관리자용)
	 */
	public String getCode() {
		String excode = null;
		excode = (String)errHash.get("Err_CD1") + 
			   	 (String)errHash.get("Err_CD2") + 
			   	 (String)errHash.get("Err_CD3");  
	
		return excode;
	}
	
	/**
	 * 함수명 : getEXStr()<br>
	 * 목  적 : 전체  에러 메세지를 리턴한다.
	 * @return	String		전체 에러 메세지(에러 코드 포함)
	 */
	public String getEXStr() {
		String exstr = null;
		exstr = "[에러코드] : " + getCode() + "\n" +
				"[에러메세지] : " + getMsg(); 

		String errpos = getPos();
		if(!errpos.equals("")) exstr += "\n[에러위치] : " + errpos;
	
		return exstr;
	}

	/**
	 * 함수명 : getDBErrorCode()<br>
	 * 목  적 : 데이터베이스 에러 코드를 리턴한다.
	 * @return	int		데이터베이스 에러 코드
	 */
	public String getDBErrorCode() {
		String dbcode = (String)errHash.get("DBErrorCode");
		if(dbcode == null || dbcode.equals("")) {
			dbcode = "";
		}
		return dbcode;
	}

	/**
	 * 함수명 : getUserMsg()<br>
	 * 목  적 : 사용자 정의 에러 메세지를 리턴한다.
	 * @return	Hashtable		사용자 정의 에러 메세지
	 */
	public Hashtable getUserMsg() {
		if(errHash.containsKey("usermsg")) {
			return (Hashtable)errHash.get("usermsg");
		} else {
			return new Hashtable();
		}
	}

	/**
	 * 함수명 : getPos()<br>
	 * 목  적 : 에러가 발생한 지점을 리턴한다.
	 * @return	Hashtable		사용자 정의 에러 메세지
	 */
	public String getPos() {
		String errpos = (String)errHash.get("Err_Pos");
		if(errpos == null || errpos.equals("")) {
			errpos = "";
		}
		return errpos;
	}

	
	/**
	 * 함수명 : Err()<br>
	 * 목  적 : 에러값 생성
	 */
	public Err() {
		errHash = new Hashtable();
	}
	

	/**
	 * 함수명 : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_CD1		대분류_에러구분코드
	 * 			Err_CD2		중분류_에러구분코드	
	 * 			Err_CD3		소분류_에러구분코드 
	 * 			Err_Msg		사용자_에러메세지
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
	}


/* ============================================================================= */
	/**
	 * 함수명 : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_CD1		대분류_에러구분코드
	 * 			Err_CD2		중분류_에러구분코드	
	 * 			Err_CD3		소분류_에러구분코드 
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos		에러발생지점
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("Err_Pos", Err_Pos);
	}

	/**
	 * 함수명 : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, int DBErrorCode)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_CD1		대분류_에러구분코드
	 * 			Err_CD2		중분류_에러구분코드	
	 * 			Err_CD3		소분류_에러구분코드 
	 * 			Err_Msg		사용자_에러메세지
	 * 			DBErrorCode	데이터베이스 에러코드
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, int DBErrorCode) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
	}

/* ============================================================================ */
	/**
	 * 함수명 : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, int DBErrorCode)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_CD1		대분류_에러구분코드
	 * 			Err_CD2		중분류_에러구분코드	
	 * 			Err_CD3		소분류_에러구분코드 
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos		에러발생지점
	 * 			DBErrorCode	데이터베이스 에러코드
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, int DBErrorCode) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("Err_Pos", Err_Pos);
		errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
	}

	/**
	 * 함수명 : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, Hashtable msghash)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_CD1		대분류_에러구분코드
	 * 			Err_CD2		중분류_에러구분코드	
	 * 			Err_CD3		소분류_에러구분코드 
	 * 			Err_Msg		사용자_에러메세지
	 * 			msghash		사용자 정의 에러 메세지
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, Hashtable msghash) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("usermsg",msghash);
	}

/* ============================================================================ */
	/**
	 * 함수명 : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, Hashtable msghash)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_CD1		대분류_에러구분코드
	 * 			Err_CD2		중분류_에러구분코드	
	 * 			Err_CD3		소분류_에러구분코드 
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos		에러발생지점
	 * 			msghash		사용자 정의 에러 메세지
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, Hashtable msghash) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("usermsg",msghash);
		errHash.put("Err_Pos",Err_Pos);
	}

	/**
	 * 함수명 : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, Hashtable msghash)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_CD1		대분류_에러구분코드
	 * 			Err_CD2		중분류_에러구분코드	
	 * 			Err_CD3		소분류_에러구분코드 
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos		에러발생지점
	 * 			DBErrorCode	데이터베이스 에러코드
	 * 			msghash		사용자 정의 에러 메세지
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, int DBErrorCode, Hashtable msghash) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		errHash.put("usermsg",msghash);
		errHash.put("Err_Pos",Err_Pos);
	}


	/**
	 * 함수명 : Err(String Err_Code, String Err_Msg)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_Code	에러코드
	 * 			Err_Msg		사용자_에러메세지
	 */
	public Err(String Err_Code, String Err_Msg) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
		}
		
	}


/* ======================================================================== */
	/**
	 * 함수명 : Err(String Err_Code, String Err_Msg, String Err_Pos)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_Code	에러코드
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos		에러 발생 지점
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
		}
		
	}

	/**
	 * 함수명 : Err(String Err_Code, String Err_Msg, String Err_Pos,Hashtable msghash)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_Code	에러코드
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos		에러 발생 지점
	 * 			msghash 	사용자 정의 에러 메세지
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos, Hashtable msghash) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("usermsg",msghash);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("usermsg",msghash);
		}
		
	}


	/**
	 * 함수명 : Err(String Err_Code, String Err_Msg, int DBErrorCode)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_Code	에러코드
	 * 			Err_Msg		사용자_에러메세지
	 * 			DBErrorCode	데이터베이스 에러코드
	 */
	public Err(String Err_Code, String Err_Msg, int DBErrorCode) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		}
		
	}

	/**
	 * 함수명 : Err(String Err_Code, String Err_Msg, int DBErrorCode, Hashtable msghash)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_Code	에러코드
	 * 			Err_Msg		사용자_에러메세지
	 * 			DBErrorCode	데이터베이스 에러코드
	 * 			msghash 	사용자 정의 에러 메세지
	 */
	public Err(String Err_Code, String Err_Msg, int DBErrorCode, Hashtable msghash) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		}
		
	}

/* =========================================================================== */
	/**
	 * 함수명 : Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_Code	에러코드
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos 	에러발생위치
	 * 			DBErrorCode	데이터베이스 에러코드
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		}
		
	}

	/**
	 * 함수명 : Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode, Hashtable msghash)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	Err_Code	에러코드
	 * 			Err_Msg		사용자_에러메세지
	 * 			Err_Pos 	에러발생위치
	 * 			DBErrorCode	데이터베이스 에러코드
	 * 			msghash 	사용자 정의 에러 메세지
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode, Hashtable msghash) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		}
		
	}
	
	/**
	 * 함수명 : Err(Hashtable ierHash)<br>
	 * 목  적 : 에러값을 생성한다.
	 * @param	ierrHash		에러값
	 */
	public Err(Hashtable ierrHash) {
		errHash = ierrHash;
	}

	/**
	 * 함수명 : Throwable의 toString() 오버라이드
	 * 목  적 : 에러값 출력
	 * @return		String 		에러값
	 */
	public String toString() {
		return getEXStr();
	}	

	/**
	 * 함수명 : Throwable의 getMessage() 오버라이드
	 * 목  적 : 에러값 출력
	 * @return		String 		에러값
	 */
	public String getMessage() {
		return getEXStr();
	}		

	/**
	 * 함수명 : setPos() 
	 * 목  적 : 에러위치를 설정한다.
	 * @param	errpos			에러위치
	 */
	public void setPos(String errpos) {
		errHash.put("Err_Pos",errpos);	
	}		
	
}
			
