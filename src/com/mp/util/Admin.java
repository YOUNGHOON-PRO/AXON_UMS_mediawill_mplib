package com.mp.util;

import java.io.*;
import java.util.*;
import java.lang.*;
import com.mp.exception.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : Admin(java)
 * 프로그램명   : Admin.java
 * Version      : 1.0
 * 작성일       : 2004/01/06
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 * 수정설명		: 
 *
 * 설명         : 관리 화면을 위하 메소드를 구현한다.
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class Admin extends WebUtil {

	/*=============================================*/
	// Global Zone 
	/*=============================================*/
	private String MNAME = "Admin";

	/** 생성자를 호출한다. */
	public Admin() {}

	/**
	 * 관리자 ID, PASSWORD 설정 여부를 확인한다.
	 * @return	Hashtable		리턴값
	 */
	public boolean checkEmptyIDPASS() throws Err {

		String pos = MNAME + ".checkEmpty()";
		
		String ENVadmin_id = "";
		String ENVadmin_passwd = "";
	
		try {	
			ENVadmin_id = getEnv("ENVadmin_id",false);
			ENVadmin_passwd = getEnv("ENVadmin_passwd",false);
		} catch (Err err) {}

		if(ENVadmin_id.equals("") || ENVadmin_passwd.equals("")) {
			return false;
		} else return true;
	}

	/**
	 * 관리자 ID, PASSWORD 를 확인한다.
	 * @param	inputhash		확인할 ID, PASSWORD
	 * @return	Hashtable		리턴값
	 */
	public Hashtable checkAdmin(Hashtable inputhash) throws Err {

		String pos = MNAME + ".checkAdmin()";
		
		boolean errflag = false;
		String errstr = "";

		String p_admin_id = getData("p_admin_id",inputhash);
		if(p_admin_id.equals("")) {
			errflag = true;
			errstr += " [ID] ";
		}

		String p_admin_passwd = getData("p_admin_passwd",inputhash);
		if(p_admin_passwd.equals("")) {
			errflag = true;
			errstr += " [PASSWORD] ";
		}
	
		if(errflag) {		
			log(pos,"입력값 에러 입니다.\n다음 정보를 확인하세요.\n"+errstr);
			throw new Err("B2E","입력값 에러 입니다.<br>다음 정보를 확인하세요.<br>"+errstr,pos);
		}

		Jcrypt jcrypt = new Jcrypt();
		
		//입력값을 암호화 한다.
		String p_admin_id_crypt = jcrypt.crypt(p_admin_id); 	
		String p_admin_passwd_crypt = jcrypt.crypt(p_admin_passwd); 	

		//환경값에 설정 돼어 있는 ID,PASSWD를 읽어온다.
		String raw_admin_id_crypt = null;
		String raw_admin_passwd_crypt = null;
		try {
			raw_admin_id_crypt = getEnv("ENVadmin_id",false);		
			raw_admin_passwd_crypt = getEnv("ENVadmin_passwd",false);		
		} catch (Err err) {}

		/* ==========================================
			로그인 상태
			[00] : 성공
			[01] : 아이디 틀림
			[02] : 비밀번호 틀림
			[03] : 아이디, 비밀번호 틀림	
			[04] : 아이디, 비밀번호 등록 안됐음
		 ========================================== */

		String login_sts = null;

		if(raw_admin_id_crypt == null || raw_admin_passwd_crypt == null) {
			login_sts = "04";
		} else {

			if(!p_admin_id_crypt.equals(raw_admin_id_crypt)) {
				login_sts = "01";	
			} else {
				login_sts = "00";
			}	

			if(!p_admin_passwd_crypt.equals(raw_admin_passwd_crypt)) {
				if(login_sts.equals("01")) login_sts = "03";	
				else login_sts = "02";
			} else {
				if(login_sts.equals("00")) login_sts = "00";	
			}	
		}

		Hashtable rethash = new Hashtable();		
		rethash.put("login_sts",login_sts);
		rethash.put("p_admin_id",p_admin_id);
		rethash.put("p_admin_passwd",p_admin_passwd);
		rethash.put("p_admin_id_crypt",p_admin_id_crypt);
		rethash.put("p_admin_passwd_crypt",p_admin_passwd_crypt);
		
		return rethash;	

	}
	
	/**
	 * 관리자 ID, PASSWORD 를 설정한다.
	 * @param	inputhash		설정할 ID, PASSWORD
	 * @return	Hashtable		리턴값
	 */
	public Hashtable setAdminIDPASS(Hashtable inputhash) throws Err {

		String pos = MNAME + ".checkAdmin()";
		
		boolean errflag = false;
		String errstr = "";

		String p_admin_id = getData("p_admin_id",inputhash);
		if(p_admin_id.equals("")) {
			errflag = true;
			errstr += " [ADMIN ID] ";
		}

		String p_admin_passwd = getData("p_admin_passwd",inputhash);
		if(p_admin_passwd.equals("")) {
			errflag = true;
			errstr += " [ADMIN PASSWORD] ";
		}
	
		if(errflag) {		
			log(pos,"입력값 에러 입니다.\n다음 정보를 확인하세요.\n"+errstr);
			throw new Err("B2E","입력값 에러 입니다.<br>다음 정보를 확인하세요.<br>"+errstr,pos);
		}

		Jcrypt jcrypt = new Jcrypt();

		//입력값을 암호화 한다.
		String p_admin_id_crypt = jcrypt.crypt(p_admin_id); 	
		String p_admin_passwd_crypt = jcrypt.crypt(p_admin_passwd); 	

		setEnv("ENVadmin_id",p_admin_id_crypt,"\r\n# DEV_STAND ADMIN ID\r\n# DO NOT EDIT\r\n#");			
		setEnv("ENVadmin_passwd",p_admin_passwd_crypt, "\r\n# DEV_STAND ADMIN PASSWD\r\n# DO NOT EDIT\r\n#");			

		Hashtable rethash = new Hashtable();	
		rethash.put("p_admin_id_crypt",p_admin_id_crypt);
		rethash.put("p_admin_passwd_crypt",p_admin_passwd_crypt);

		return rethash;

	}

	/**
	 * 환경값 정보를 리턴한다.
	 * @return	Hashtable		환경값 정보
	 */
	public Hashtable getEnvInfo() throws Err {
		return getEnv();
	}

	/**
	 * 환경값 복사본 정보를 리턴한다.
	 * @return	Vector 	환경값 복사본 정보
	 */
	public Vector getEnvCopyInfo() throws Err {
		return getEnvCopy();
	}

	/**
	 * 환경값을 검색한다.
	 * @param	inputhash	검색 인자
	 * @return	Hashtable 	검색 환경값
	 */
	public Hashtable getSearchEnvInfo(Hashtable inputhash) throws Err {

		//입력값을 확인한다.
		String p_search_name = getData("p_search_name",inputhash);
		String p_search_value = getData("p_search_value",inputhash);

		//현재의 환경정보를 읽어온다.				
		Hashtable admin_envhash = getEnv();

		Enumeration e = admin_envhash.keys();		

		String name = null;		
		String value = null;

		Hashtable rethash = new Hashtable();
		while(e.hasMoreElements()) {		
			name = (String)e.nextElement();
			value = getData(name,admin_envhash);	
			if(!p_search_name.equals("") && !p_search_value.equals("")) {
				if(name.indexOf(p_search_name) != -1
					&& value.indexOf(p_search_value) != -1) {
					rethash.put(name,value);
				}
			} else if(!p_search_name.equals("")) {
				if(name.indexOf(p_search_name) != -1) rethash.put(name,value);
			} else if(!p_search_value.equals("")) {
				if(value.indexOf(p_search_value) != -1) rethash.put(name,value);
			} else {
				rethash.put(name,value);
			}
		}

		return rethash;

	}

	/**
	 * 특정 환경값을 읽어온다.
	 * @param	p_name	 	환경명
	 * @return	Hashtable 	환경정보
	 */
	public Hashtable getEnvInfo(String p_name) throws Err {

		//입력값을 확인한다.
		Hashtable admin_envhash = getEnvInfo();

		String value = getData(p_name,admin_envhash);

		Hashtable rethash = new Hashtable();
		rethash.put(p_name,value);

		return rethash;

	}

	/**
	 * 환경화일 재생성(복사본 정보를 이용하여 다시 생성한다)
	 * @return	Vector 	환경값 복사본 정보
	 */
	public boolean createEnvFile() throws Err {

		//기존의 환경값을 백업처리하고.......


		//파일을 생성한다.

		return true;

	}
}






