package com.mp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;
//import com.certicom.net.ssl.internal.HttpURLConnection;

/**
 * <pre>
 * 프로그램유형 : Login(java)
 * 프로그램명   : Login.java
 * Version      : 1.0
 * 작성일       : 2013/01/10
 * 작성자       : 이소라
 * 수정일       : 
 * 수정자       : 
 * 수정설명		: 
 *
 * 설명         : 로그인 ad계정 연결 메소드를 구현한다.
 *
 * 프로젝트명   : 
 * Copyright	: 
 * </pre>
 */
public class WebLog extends WebUtil {

	/*=============================================*/
	// Global Zone 
	/*=============================================*/
	private String MNAME = "Login";

	/** 생성자를 호출한다. */
//	public Login() {}

    public boolean connectionWebService(String userId,String passwd)throws Exception { 
    	System.out.println("<<== login java 들어옴==>>");
        URL url = new URL("http://alticor.intranet.local/webservices/ad/login.asmx"); 
        StringBuffer ldapSoapXml = new StringBuffer(); 
        ldapSoapXml.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"); 
        ldapSoapXml.append("  <soap:Body>"); 
        ldapSoapXml.append("        <getLogin_LDAP xmlns=\"http://tempuri.org/\">"); 
        ldapSoapXml.append("          <userid><![CDATA["+userId+"]]></userid>"); 
        ldapSoapXml.append("          <password><![CDATA["+passwd+"]]></password>"); 
        ldapSoapXml.append("        </getLogin_LDAP>"); 
        ldapSoapXml.append("  </soap:Body>"); 
        ldapSoapXml.append("</soap:Envelope>"); 

        StringBuffer response = new StringBuffer(); 
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection(); 
        urlConnection.setDoOutput(true); 
        urlConnection.setRequestMethod("POST"); 
        urlConnection.setRequestProperty("Content-Type", "text/xml"); 
        
        OutputStreamWriter wr = null; 
        BufferedReader in = null; 
        
        int  iReturn=0; 
        boolean bReturn=false; 
        String sTemp=""; 
        try { 
        	System.out.println("<<== login java try==>> url ==>> " + urlConnection);
        	System.out.println("<<== login java try==>> " + urlConnection.getOutputStream());
                wr = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"); 
                System.out.println("<<== login java getOutputStream==>>");
                wr.write(ldapSoapXml.toString()); 
                wr.flush(); 
                String inputLine = null; 
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()) ); 
                while((inputLine = in.readLine()) != null ){ 
                        //response.append(inputLine.replaceAll("soap:", "")); 
                        response.append(inputLine); 
                } 
                
                //System.out.println("********************["+response.toString()+"]************************"); 
                sTemp=response.toString(); 
                iReturn=sTemp.indexOf("true"); 
                
                if (iReturn>0) 
                        bReturn=true;         
                //System.out.println("********************bReturn["+bReturn+"]************************"); 
                //System.out.println("********************iReturn["+iReturn+"]************************"); 
                
                
        } catch (Exception e) { 
                e.printStackTrace(); 
                throw e; 
        } finally { 
                if( in != null ) in.close(); 
                if( wr != null ) wr.close(); 
        } 
        System.out.println("<<== login java bReturn==>>");
        return bReturn; 
}

	
	
//	/**
//	 * 관리자 ID, PASSWORD 설정 여부를 확인한다.
//	 * @return	Hashtable		리턴값
//	 */
//	public boolean checkEmptyIDPASS() throws Err {
//
//		String pos = MNAME + ".checkEmpty()";
//		
//		String ENVadmin_id = "";
//		String ENVadmin_passwd = "";
//	
//		try {	
//			ENVadmin_id = getEnv("ENVadmin_id",false);
//			ENVadmin_passwd = getEnv("ENVadmin_passwd",false);
//		} catch (Err err) {}
//
//		if(ENVadmin_id.equals("") || ENVadmin_passwd.equals("")) {
//			return false;
//		} else return true;
//	}
//
//	/**
//	 * 관리자 ID, PASSWORD 를 확인한다.
//	 * @param	inputhash		확인할 ID, PASSWORD
//	 * @return	Hashtable		리턴값
//	 */
//	public Hashtable checkAdmin(Hashtable inputhash) throws Err {
//
//		String pos = MNAME + ".checkAdmin()";
//		
//		boolean errflag = false;
//		String errstr = "";
//
//		String p_admin_id = getData("p_admin_id",inputhash);
//		if(p_admin_id.equals("")) {
//			errflag = true;
//			errstr += " [ID] ";
//		}
//
//		String p_admin_passwd = getData("p_admin_passwd",inputhash);
//		if(p_admin_passwd.equals("")) {
//			errflag = true;
//			errstr += " [PASSWORD] ";
//		}
//	
//		if(errflag) {		
//			log(pos,"입력값 에러 입니다.\n다음 정보를 확인하세요.\n"+errstr);
//			throw new Err("B2E","입력값 에러 입니다.<br>다음 정보를 확인하세요.<br>"+errstr,pos);
//		}
//
//		Jcrypt jcrypt = new Jcrypt();
//		
//		//입력값을 암호화 한다.
//		String p_admin_id_crypt = jcrypt.crypt(p_admin_id); 	
//		String p_admin_passwd_crypt = jcrypt.crypt(p_admin_passwd); 	
//
//		//환경값에 설정 돼어 있는 ID,PASSWD를 읽어온다.
//		String raw_admin_id_crypt = null;
//		String raw_admin_passwd_crypt = null;
//		try {
//			raw_admin_id_crypt = getEnv("ENVadmin_id",false);		
//			raw_admin_passwd_crypt = getEnv("ENVadmin_passwd",false);		
//		} catch (Err err) {}
//
//		/* ==========================================
//			로그인 상태
//			[00] : 성공
//			[01] : 아이디 틀림
//			[02] : 비밀번호 틀림
//			[03] : 아이디, 비밀번호 틀림	
//			[04] : 아이디, 비밀번호 등록 안됐음
//		 ========================================== */
//
//		String login_sts = null;
//
//		if(raw_admin_id_crypt == null || raw_admin_passwd_crypt == null) {
//			login_sts = "04";
//		} else {
//
//			if(!p_admin_id_crypt.equals(raw_admin_id_crypt)) {
//				login_sts = "01";	
//			} else {
//				login_sts = "00";
//			}	
//
//			if(!p_admin_passwd_crypt.equals(raw_admin_passwd_crypt)) {
//				if(login_sts.equals("01")) login_sts = "03";	
//				else login_sts = "02";
//			} else {
//				if(login_sts.equals("00")) login_sts = "00";	
//			}	
//		}
//
//		Hashtable rethash = new Hashtable();		
//		rethash.put("login_sts",login_sts);
//		rethash.put("p_admin_id",p_admin_id);
//		rethash.put("p_admin_passwd",p_admin_passwd);
//		rethash.put("p_admin_id_crypt",p_admin_id_crypt);
//		rethash.put("p_admin_passwd_crypt",p_admin_passwd_crypt);
//		
//		return rethash;	
//
//	}
//	
//	/**
//	 * 관리자 ID, PASSWORD 를 설정한다.
//	 * @param	inputhash		설정할 ID, PASSWORD
//	 * @return	Hashtable		리턴값
//	 */
//	public Hashtable setAdminIDPASS(Hashtable inputhash) throws Err {
//
//		String pos = MNAME + ".checkAdmin()";
//		
//		boolean errflag = false;
//		String errstr = "";
//
//		String p_admin_id = getData("p_admin_id",inputhash);
//		if(p_admin_id.equals("")) {
//			errflag = true;
//			errstr += " [ADMIN ID] ";
//		}
//
//		String p_admin_passwd = getData("p_admin_passwd",inputhash);
//		if(p_admin_passwd.equals("")) {
//			errflag = true;
//			errstr += " [ADMIN PASSWORD] ";
//		}
//	
//		if(errflag) {		
//			log(pos,"입력값 에러 입니다.\n다음 정보를 확인하세요.\n"+errstr);
//			throw new Err("B2E","입력값 에러 입니다.<br>다음 정보를 확인하세요.<br>"+errstr,pos);
//		}
//
//		Jcrypt jcrypt = new Jcrypt();
//
//		//입력값을 암호화 한다.
//		String p_admin_id_crypt = jcrypt.crypt(p_admin_id); 	
//		String p_admin_passwd_crypt = jcrypt.crypt(p_admin_passwd); 	
//
//		setEnv("ENVadmin_id",p_admin_id_crypt,"\r\n# DEV_STAND ADMIN ID\r\n# DO NOT EDIT\r\n#");			
//		setEnv("ENVadmin_passwd",p_admin_passwd_crypt, "\r\n# DEV_STAND ADMIN PASSWD\r\n# DO NOT EDIT\r\n#");			
//
//		Hashtable rethash = new Hashtable();	
//		rethash.put("p_admin_id_crypt",p_admin_id_crypt);
//		rethash.put("p_admin_passwd_crypt",p_admin_passwd_crypt);
//
//		return rethash;
//
//	}
//
//	/**
//	 * 환경값 정보를 리턴한다.
//	 * @return	Hashtable		환경값 정보
//	 */
//	public Hashtable getEnvInfo() throws Err {
//		return getEnv();
//	}
//
//	/**
//	 * 환경값 복사본 정보를 리턴한다.
//	 * @return	Vector 	환경값 복사본 정보
//	 */
//	public Vector getEnvCopyInfo() throws Err {
//		return getEnvCopy();
//	}
//
//	/**
//	 * 환경값을 검색한다.
//	 * @param	inputhash	검색 인자
//	 * @return	Hashtable 	검색 환경값
//	 */
//	public Hashtable getSearchEnvInfo(Hashtable inputhash) throws Err {
//
//		//입력값을 확인한다.
//		String p_search_name = getData("p_search_name",inputhash);
//		String p_search_value = getData("p_search_value",inputhash);
//
//		//현재의 환경정보를 읽어온다.				
//		Hashtable admin_envhash = getEnv();
//
//		Enumeration e = admin_envhash.keys();		
//
//		String name = null;		
//		String value = null;
//
//		Hashtable rethash = new Hashtable();
//		while(e.hasMoreElements()) {		
//			name = (String)e.nextElement();
//			value = getData(name,admin_envhash);	
//			if(!p_search_name.equals("") && !p_search_value.equals("")) {
//				if(name.indexOf(p_search_name) != -1
//					&& value.indexOf(p_search_value) != -1) {
//					rethash.put(name,value);
//				}
//			} else if(!p_search_name.equals("")) {
//				if(name.indexOf(p_search_name) != -1) rethash.put(name,value);
//			} else if(!p_search_value.equals("")) {
//				if(value.indexOf(p_search_value) != -1) rethash.put(name,value);
//			} else {
//				rethash.put(name,value);
//			}
//		}
//
//		return rethash;
//
//	}
//
//	/**
//	 * 특정 환경값을 읽어온다.
//	 * @param	p_name	 	환경명
//	 * @return	Hashtable 	환경정보
//	 */
//	public Hashtable getEnvInfo(String p_name) throws Err {
//
//		//입력값을 확인한다.
//		Hashtable admin_envhash = getEnvInfo();
//
//		String value = getData(p_name,admin_envhash);
//
//		Hashtable rethash = new Hashtable();
//		rethash.put(p_name,value);
//
//		return rethash;
//
//	}
//
//	/**
//	 * 환경화일 재생성(복사본 정보를 이용하여 다시 생성한다)
//	 * @return	Vector 	환경값 복사본 정보
//	 */
//	public boolean createEnvFile() throws Err {
//
//		//기존의 환경값을 백업처리하고.......
//
//
//		//파일을 생성한다.
//
//		return true;
//
//	}
}






