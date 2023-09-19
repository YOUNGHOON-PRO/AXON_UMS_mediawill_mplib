package com.mp.util; 

import javax.naming.*;
import java.util.*;
import java.io.*;
import java.text.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : Env.java
 * Version      : 1.0
 * 작성일       : 2002/03/18
 * 작성자       : 오범석
 * 수정일       : 2003/12/02
 * 수정자       : 오범석
 * 수정설명		: 한글 환경값 설정 가능하도록 수정....
 * 수정일       : 2004/01/02
 * 수정자       : 오범석
 * 수정설명		: 환경값 파일 복사본 저장 및 수정값 복사본 저장 처리
 *
 * 설명         : 환경값을 저장한다.
 *				  환경값을 호출한다.
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class Env extends Log {
  
  	/** 모듈명 설정 */
  	private String MNAME = "Env";
  
	/** 환경값을 저장한다.	*/
	private static Hashtable envhash = null; 

	/** 환경화일의 내용을 저장한다.	*/
	private static Vector envcopy = null; 

	/** 생성자. 로그의 위치를 지정한다.  */
	public Env() {}

	/**
	 * 생성자.		
	 * 로그위치를 지정한다.<br>
	 * 환경화일을 읽어들인다.<br>
	 * @param	fname	환경파일
	 */
	public Env(String fname) {
		envhash = getEnvironment(fname);
	}

    /**
     *
     * 환경화일을 읽어들인다.
	 * 환경화일을 읽어 환경값(Hashtable)을 리턴한다.
     * @param   filename	환경화일
     * @return  Hashtable 	환경값(String, String)
     *
     */
    public Hashtable getEnvironment(String filename) {
		if(envhash == null) {
			return envhash = getEnvironload(filename);
		} else {
       		return envhash;
		}
    }

    /**
     *
	 * 환경값을 리턴한다.
     * @return  Hashtable 	환경값(String, String)
     *
     */
    public Hashtable getEnvironment() {
       		return envhash;
    }

    /**
     *
     * 환경화일을 재설정한다.
	 * 환경화일을 읽어 환경값(Hashtable)을 리턴한다.
     * @param   filename	환경화일
     * @return  Hashtable 	환경값(String, String)
     *
     */
    public Hashtable setEnvironment(String filename) {
		return envhash = getEnvironload(filename);
    }

    /**
     *
     * 환경화일을 읽어들인다.
	 * 환경화일을 읽어 환경값(Hashtable)을 리턴한다.
	 * 환경값을 한글 사용 가능하도록 함
     * @param   filename	환경화일
     * @return  Hashtable 	환경값(String, String)
     *
     */
    public Hashtable getEnvironload(String filename) {
		String pos = MNAME+".getEnvironload()";
        Hashtable rethash = null;
   	   	Properties prop = null;
       	FileInputStream in = null;
       	String key = null;

		BufferedReader copyin = null;
       	try {
           	in = new FileInputStream(filename);
           	prop = new Properties();
           	prop.load(in);
           	Enumeration enu = prop.propertyNames();
           	rethash = new Hashtable();
           	while(enu.hasMoreElements()) {
               	key = (String)enu.nextElement();
               	rethash.put(key,new String((prop.getProperty(key)).getBytes("ISO-8859-1"),"EUC-KR"));
               	//rethash.put(key,prop.getProperty(key));
           	}


			//카피 정보를 저장한다.
			envcopy = new Vector();
			copyin = new BufferedReader(new FileReader(filename));
			String row = null;
			int posi = 0;
			String name = null;
			String value = null;
			while(true) {
				row = copyin.readLine();	

				if(row == null) break;

				row = row.trim();

				if(row.equals("") || row.substring(0,1).equals("#")) {
					String[] rowdata = new String[2];
					rowdata[0] = Code.ENV_COMMENT;
					rowdata[1] = row;
					envcopy.addElement(rowdata);
				} else {
					posi = row.indexOf("=");
					name = row.substring(0,posi);
					value = row.substring(posi+1);
					String[] rowdata = new String[3];
					rowdata[0] = Code.ENV_VALUE;
					rowdata[1] = name;
					rowdata[2] = value;
					envcopy.addElement(rowdata);
				}
			}

       	} catch (Exception e) {
           	log(pos,"환경값로딩 실패.\n환경파일 : ["+filename+"]\n"+ e.toString());
       	} finally {
			try {
				in.close();
				copyin.close();
			} catch (Exception e1) {}
		}
        return rethash;
    }

	

    /**
     *
	 * 환경파일 복사본 전체를 리턴한다.
     * @return  	Vector 	환경화일복사본
     */
	public Vector getEnvCopy() { 
		String pos = MNAME+".getEnvCopy()";
		return envcopy;
	}

    /**
     *
     * 환경값 을 리턴한다.
	 * 환경값 전체를 리턴한다.
     * @return  	Hashtable   환경값(String, String)
     */
	public Hashtable getEnv() { 
		String pos = MNAME+".getEnv()";
		return envhash;
	}

    /**
     *
     * 특정 환경값을 리턴한다.
     * @param		name		환경명
     * @return  	String		환경값
     * @exception	MallErr		<br>에러코드	: 111
	 *							<br>메세지		: 시스템 에러 발생
     * 
     */
	public String getEnv(String name) throws Err {
		String pos = MNAME+".getEnv()";
		String value = null;
		if(envhash != null) {
			if(envhash.containsKey(name)) {
				value = (String)envhash.get(name);
			} else {
				log(pos,"등록된 환경값이 없습니다.\n환경이름 : [" + name + "]");
				throw new Err("1","5","G","시스템 에러 발생.",pos);
			}
		} else {
			log(pos,"환경값이 로드되지 않았습니다.\n환경이름 : [" + name + "]");
			throw new Err("1","5","G","시스템 에러 발생.",pos);
		}
		return value;
	}

    /**
     *
     * 특정 환경값을 리턴한다.
	 * 로그 출력 구분함
     * @param		name		환경명
	 *			    log_flag	로그 출력 여부			
     * @return  	String		환경값
     * @exception	MallErr		<br>에러코드	: 111
	 *							<br>메세지		: 시스템 에러 발생
     * 
     */
	public String getEnv(String name, boolean log_flag) throws Err {
		String pos = MNAME+".getEnv()";
		String value = null;
		if(envhash != null) {
			if(envhash.containsKey(name)) {
				value = (String)envhash.get(name);
			} else {
				if(log_flag) log(pos,"등록된 환경값이 없습니다.\n환경이름 : [" + name + "]");
				throw new Err("1","5","G","시스템 에러 발생.",pos);
			}
		} else {
			if(log_flag) log(pos,"환경값이 로드되지 않았습니다.\n환경이름 : [" + name + "]");
			throw new Err("1","5","G","시스템 에러 발생.",pos);
		}
		return value;
	}

    /**
     *
     * 특정 환경값을 리턴한다.
     * @param		name		환경명
     * 				envhash		환경리스트
     * @return  	String		환경값
     * 
     */
	public String getEnv(String name, Hashtable envhash) throws Err {
		String pos = MNAME+".getEnv()";
		String value = null;
		if(envhash != null) {
			if(envhash.containsKey(name)) {
				value = (String)envhash.get(name);
			} else {
				log(pos,"등록된 환경값이 없습니다.\n환경이름 : [" + name + "]");
				throw new Err("1","5","G","시스템 에러 발생.",pos);
			}
		} else {
			log(pos,"환경값이 로드되지 않았습니다.\n환경이름 : [" + name + "]");
			throw new Err("1","5","G","시스템 에러 발생.",pos);
		}
		return value;
	}

	/**
	 * 환경값을 설정한다.
	 * 환경화일 복사본에 저장하지 않는다.
	 * @param	 name		환경명
	 * 			 value		환경값
	 */
	public void setEnv(String name, String value) {
		String pos = MNAME+".setEnv()";
		if(envhash != null) envhash.put(name,value);
		else log(pos,"환경값이 등록돼어 있지 않습니다.");
	}

	/**
	 * 환경값을 설정한다.
	 * 환경화일 복사본에 저장한다.
	 * @param	 name		환경명
	 * 			 value		환경값
	 */
	public void setEnv(String name, String value, String comment) {
		String pos = MNAME+".setEnv()";
		if(envhash != null && envcopy != null) {
			envhash.put(name,value);
						
			int size = envcopy.size();
			String[] envdata = null;
			boolean being_yn = false;
			for(int i = 0; i < size; i++) {		
				envdata = (String[])envcopy.elementAt(i);				
				if(envdata[0].equals(Code.ENV_VALUE)) {
					if(envdata[1].equals(name)) {		
						envdata[2] = value;
						if(comment != null || comment.equals("")) {
							//복사본에 주석을 설정한다.
							String[] temp = new String[2];			
							temp[0] = Code.ENV_COMMENT;
							temp[1] = "# "+comment;
							envcopy.add(i-1,temp);	
						}
						being_yn = true;
						break;
					}
				}
			}

			if(!being_yn) {
				if(comment != null || comment.equals("")) {
					envdata = new String[2];
					envdata[0] = Code.ENV_COMMENT;
					envdata[1] = "# "+comment;
					envcopy.addElement(envdata);
				}

				envdata = new String[3];
				envdata[0] = Code.ENV_VALUE;
				envdata[1] = name;
				envdata[2] = value;
				envcopy.addElement(envdata);
			}
		} else log(pos,"환경값이 등록돼어 있지 않습니다.");
	}

}
