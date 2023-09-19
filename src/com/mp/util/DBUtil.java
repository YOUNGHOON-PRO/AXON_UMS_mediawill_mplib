package com.mp.util;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.mp.exception.*;
import com.mp.util.*;


/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : DBUtil.java
 * Version      : 1.1
 * 작성일       : 2002/03/18
 * 작성자       : 오범석
 * 수정일       : 2002/10/20
 * 수정자       : 오범석
 * 수정일       : 2003/11/26
 * 수정자       : 오범석
 * 수정사유		: 입력 파라미터 수정과 에러로그 및 에러 throw 수정
 * 수정일       : 2003/12/03
 * 수정자       : 오범석
 * 수정사유		: getLData에서 DB Connection 두번 일어나는 부분 수정
 * 수정일 		: 2003/12/16
 * 수정자		: 오범석
 * 수정사유		: MSSQL에서 ResultSet 두번 읽어을때 에러 나는 부분 수정
 *				  setRowData() 에서 동일한 컬럼값 ResultSet 두 번 읽는 부분 수정
 * 수정일 		: 2003/12/19
 * 수정자		: 오범석
 * 수정사유		: DataSet 사용 메소드 추가
 *				  추가 메소드 : getDSLData(), getDSALData(), getDSIData()
 * 수정일 		: 2004/01/01
 * 수정자		: 오범석
 * 수정사유		: 통계정보를 위한 field 정보 추가 및 관련 메소드 구현
 * 수정일 		: 2004/01/14
 * 수정자		: 오범석
 * 수정사유		: 숫자형일때 NULL값 대신 0값이 입력돼는 부분을 수정
 * 수정일 		: 2005/03/14
 * 수정자		: 오범석
 * 수정사유		: 에러 리턴 시 원래의 에러 정보도 같이 리턴하도록 수정
 *
 * 설명         : 데이터베이스 관련 Utility 메소드를 구현한다.
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class DBUtil extends Util {

    /*=============================================*/
    // Global Zone
    /*=============================================*/
	/** JDBCENV를 설정한다.				*/
	private String JDBCENV = "Default";

	/** 모듈명 설정 */
	private String MNAME = "DBUtil";

	/** 생성자 */
	public DBUtil() {}

	/**
	 *
 	 * 데이터베이스 환경명을 설정한다.
	 * @param		jdbcenv			데이터베이스 환경명
	 */
	protected void setJDBCENV(String jdbcenv) {
		JDBCENV = jdbcenv;
	} 
	
	/**
	 *
 	 * 데이터베이스 Connection을 호출한다.	
	 * default 데이터베이스로 연결한다. 
	 *
	 * @return		Connection  	Connection	
	 * @exception	Err				에러
	 */
	public Connection getConn()  throws Err {
		String pos = MNAME+".getConn()";	//모듈 위치
		String dbname = JDBCENV;
		Connection conn = null;
		setSTATCONTRY(); 
		setSTATGETCONNTRY(); 
		try {
			conn = getConn(dbname);
		} catch(Err err) {
			setSTATCONFAIL(); 
			setSTATGETCONNFAIL(); 
			log(pos,"Connection 호출을 실패했습니다.\n"+err.getEXStr());	
			throw err;
		} catch(Exception e) {
			setSTATCONFAIL(); 
			setSTATGETCONNFAIL(); 
			log(pos,"Connection 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATCONSUCCESS(); 
		setSTATGETCONNSUCCESS(); 
		return conn;
	}

	/**
	 *
 	 * 데이터베이스 Connection을 호출한다.	
	 *
	 * @param  		dbname			DB명(환경화일의 dbname) 		
	 * @return		Connection  	Connection	
	 * @exception	Err				에러
	 */
	public Connection getConn(String dbname)  throws Err {
		
		String pos = MNAME+".getConn(String)";
		String conntype = null;
		Connection conn = null;
		setSTATCONTRY(); 
		setSTATGETCONNTRY(); 
		setSTATCONKIND(dbname,Code.STAT_TRY);
		try {
			conntype = getEnv(dbname+"conntype");	
			if(conntype.equals("pool")) {
				conn = getConn1(dbname);
			} else if(conntype.equals("jdbc")) {
				conn = getConn2(dbname);
			} else {
				log(pos,"환경값에 DB연결 정보가 없습니다.\n");	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos);
			}
		} catch(Err err) {
			setSTATCONFAIL(); 
			setSTATGETCONNFAIL(); 
			setSTATCONKIND(dbname,Code.STAT_FAIL);
			log(pos,"Connection 호출을 실패했습니다.\n"+err.getEXStr());	
			throw err;
		} catch(Exception e) {
			setSTATCONFAIL(); 
			setSTATGETCONNFAIL(); 
			setSTATCONKIND(dbname,Code.STAT_FAIL);
			log(pos,"Connection 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATCONSUCCESS(); 
		setSTATGETCONNSUCCESS(); 
		setSTATCONKIND(dbname,Code.STAT_SUCCESS);
		return conn;
	}

	/**
	 *
 	 * 데이터베이스 Connection을 호출한다.	
	 * 데이터베이스 풀을 사용하는 경우 데이터베이스풀을 리턴한다.
	 *
	 * @param  		dbname			DB명(환경화일의 dbname) 		
	 * @return		Connection  	Connection	
	 * @exception	Err				에러
	 */
	public Connection getConn1(String dbname)  throws Err {
		String pos = MNAME+".getConn1()";
		String poolname = null;
		Connection conn = null;
		setSTATCONPOOLTRY();
		setSTATGETCONN1TRY();
		try {
			poolname = getEnv(dbname+"poolname");
			DataSource ds = (DataSource)lookup(poolname);
			conn = ds.getConnection();
		} catch(Err err) {
			setSTATCONPOOLFAIL();
			setSTATGETCONN1FAIL();
			log(pos,"Connection 호출을 실패했습니다.\n"+err.getEXStr());	
			throw err;
		} catch(Exception e) {
			setSTATCONPOOLFAIL();
			setSTATGETCONN1FAIL();
			log(pos,"Connection 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos, sysmsg);
		}
		setSTATCONPOOLSUCCESS();
		setSTATGETCONN1SUCCESS();
		return conn;
	}

	/**
	 *
 	 * 데이터베이스 Connection을 호출한다.	
	 * JDBC Connection 을 리턴한다.
	 *
	 * @param  		dbname 			DB명(환경화일의 dbname)
	 * @return		Connection  	Connection	
	 * @exception	Err				에러
	 */
	public Connection getConn2(String dbname)  throws Err {
		String pos = MNAME+".getConn2()";
		Connection conn = null;
		String driver = null;
		String url = null;
		String userid = null;
		String password = null;
		setSTATCONJDBCTRY();
		setSTATGETCONN2TRY();
		try {
			
	        //복호화 추가
			String ALGORITHM = "PBEWithMD5AndDES";
			String KEYSTRING = "ENDERSUMS";
			EncryptUtil enc =  new EncryptUtil();

			driver = getEnv(dbname+"jdbcdriver");
			url = getEnv(dbname+"url");
			userid = getEnv(dbname+"jdbcuserid");
			password = getEnv(dbname+"jdbcpassword");
			
			//복호화 추가
			password =enc.getJasyptDecryptedString(ALGORITHM, KEYSTRING, password);
			
			
		} catch(Err err) {
			setSTATCONJDBCFAIL();
			setSTATGETCONN2FAIL();
			log(pos,"환경값 호출을 실패했습니다.\n"+err.getEXStr());	
			throw err;
		} catch(Exception e) {
			setSTATCONJDBCFAIL();
			setSTATGETCONN2FAIL();
			log(pos,"Connection 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		try {
			Class.forName(driver);		
			conn = DriverManager.getConnection(url,userid,password);
		} catch(Exception e) {
			setSTATCONJDBCFAIL();
			setSTATGETCONN2FAIL();
			log(pos,"Connection 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATCONJDBCSUCCESS();
		setSTATGETCONN2SUCCESS();
		return conn;
	}

	/**
	 *
 	 * 데이터베이스 Connection을 호출한다.	
	 * JDBC Connection 을 리턴한다.
	 *
	 * @param  		dbdriver 		드라이버
	 * @param  		dburl 			연결 URL
	 * @param  		dbuserid 		ID
	 * @param  		dbuserpwd 		PASSWORD
	 * @return		Connection  	Connection	
	 * @exception	Err				에러
	 */
	public Connection getConn3(String dbdriver, String dburl, 
								String dbuserid, String dbuserpwd)  throws Err {
		String pos = MNAME+".getConn3()";
		Connection conn = null;
		setSTATCONJDBCTRY();
		setSTATGETCONN2TRY();
		try {
			
			Class.forName(dbdriver);		
			conn = DriverManager.getConnection(dburl,dbuserid,dbuserpwd);
		} catch(Exception e) {
			setSTATCONJDBCFAIL();
			setSTATGETCONN2FAIL();
			log(pos,"Connection 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATCONJDBCSUCCESS();
		setSTATGETCONN2SUCCESS();
		return conn;
	}

	/**
	 *
 	 * 데이터변수를 리턴한다(데이터 검색).	
	 * 검색데이터 한건에 대한 데이터변수를 리턴한다.
	 *
	 * @param  		jdbcenv			jdbc 환경값
	 * @param		SQL				SQL문  		
	 * @return  	Hashtable		데이터변수(Connection, Statement)
	 * @exception	Err				에러
	 */
	public Hashtable getISet(String jdbcenv, String SQL)  throws Err {
		String pos = MNAME+".getISet()";

		setSTATGETISETTRY();		//통계정보설정

		Connection conn = null;
		PreparedStatement pstmt = null;
		Hashtable ihash = null;
		try {
			
			//System.out.println("<===== DBUtil.java=======>");
			//System.out.println("SQL  =>" +SQL);
			//System.out.println("<===== DBUtil.java=======>");
			//System.out.println("");
    		//System.out.println("");
			conn = getConn(jdbcenv);
			pstmt = conn.prepareStatement(SQL);
			ihash = new Hashtable();
			ihash.put("conn",conn);
			ihash.put("pstmt",pstmt);
		} catch(Err err) {
			setSTATGETISETFAIL();		//통계정보설정
			log(pos,"getISet() 호출을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());	
			throw err;
		} catch(SQLException sqle) {
			setSTATGETISETFAIL();		//통계정보설정
			log(pos,"getISet() 호출을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			log(pos,"getISet() 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		setSTATGETISETSUCCESS();		//통계정보설정
		return ihash;
	}

	/**
	 *
 	 * 데이터변수를 리턴한다(데이터 검색).	
	 * 검색데이터 한건에 대한 데이터변수를 리턴한다.
	 *
	 * @param  		conn			Connection
	 * @param		SQL				SQL문  		
	 * @return  	Hashtable		데이터변수(Connection, Statement)
	 * @exception	Err				에러
	 */
	public Hashtable getISet(Connection conn, String SQL)  throws Err {
		String pos = MNAME+".getISet()";

		setSTATGETISETTRY();		//통계정보설정

		PreparedStatement pstmt = null;
		Hashtable ihash = null;
		try {
			pstmt = conn.prepareStatement(SQL);
			ihash = new Hashtable();
			ihash.put("conn",conn);
			ihash.put("pstmt",pstmt);
		} catch(SQLException sqle) {
			setSTATGETISETFAIL();		//통계정보설정
			log(pos,"getISet() 호출을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATGETISETFAIL();		//통계정보설정
			log(pos,"getISet() 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATGETISETSUCCESS();		//통계정보설정
		return ihash;
	}

	/**
	 *
 	 * 데이터변수를 리턴한다(데이터 조작).	
	 * 데이터조작을 위한 데이터변수를 리턴한다.
	 *
	 * @param  		jdbcenv			jdbc 환경값
	 * @param		SQL				SQL문  		
	 * @return  	Hashtable		데이터변수(Connection, PreparedStatement)
	 * @exception	Err				에러	
	 */
	public Hashtable getMSet(String jdbcenv, String SQL)  throws Err {
		String pos = MNAME+".getMSet()";
		
		setSTATGETMSETTRY();		//통계정보설정

		Connection conn = null;
		PreparedStatement pstmt = null;
		Hashtable mhash = null;
		try {
			conn = getConn(jdbcenv);
			pstmt = conn.prepareStatement(SQL);
			mhash = new Hashtable();
			mhash.put("conn", conn);
			mhash.put("pstmt", pstmt);
		} catch(Err err) {
			setSTATGETMSETFAIL();		//통계정보설정
			log(pos,"setMSet() 호출을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());	
			throw err;
		} catch(SQLException sqle) {
			setSTATGETMSETFAIL();		//통계정보설정
			log(pos,"setMSet() 호출을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATGETMSETFAIL();		//통계정보설정
			log(pos,"setMSet() 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		setSTATGETMSETSUCCESS();		//통계정보설정
		return mhash;
	}

	/**
	 *
 	 * 데이터변수를 리턴한다(데이터 조작).	
	 * 데이터조작을 위한 데이터변수를 리턴한다.
	 *
	 * @param  		conn			Connection
	 * @param		SQL				SQL문  		
	 * @return  	Hashtable		데이터변수(Connection, PreparedStatement)
	 * @exception	Err				에러	
	 */
	public Hashtable getMSet(Connection conn, String SQL)  throws Err {
		String pos = MNAME+".getMSet()";

		setSTATGETMSETTRY();		//통계정보설정

		PreparedStatement pstmt = null;
		Hashtable mhash = null;
		try {
			pstmt = conn.prepareStatement(SQL);
			mhash = new Hashtable();
			mhash.put("conn", conn);
			mhash.put("pstmt", pstmt);
		} catch(SQLException sqle) {
			setSTATGETMSETFAIL();		//통계정보설정
			log(pos,"setMSet() 호출을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATGETMSETFAIL();		//통계정보설정
			log(pos,"setMSet() 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		setSTATGETMSETSUCCESS();		//통계정보설정
		return mhash;
	}

	/**
	 *
 	 * 데이터변수를 리턴한다(저장 프로시져 호출).	
	 * 프로시져 호출을 위한 데이터변수를 리턴한다.
	 *
	 * @param  		jdbcenv			jdbc 환경값
	 * @param		SQL				SQL문  		
	 * @return  	Hashtable		데이터변수(Connection, CallableStatement)
	 * @exception	Err				에러
	 */
	public Hashtable getSSet(String jdbcenv, String SQL)  throws Err {
		String pos = MNAME+".getSSet()";

		setSTATGETSSETTRY(); 		//통계정보설정
		
		Connection conn = null;
		CallableStatement cstmt = null;
		Hashtable shash = null;
		try {
			conn = getConn(jdbcenv);
			cstmt = conn.prepareCall(SQL);
			shash = new Hashtable();
			shash.put("conn", conn);
			shash.put("cstmt", cstmt);
		} catch(Err err) {
			setSTATGETSSETFAIL(); 		//통계정보설정
			log(pos,"setSSet() 호출을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());	
			throw err;
		} catch(SQLException sqle) {
			setSTATGETSSETFAIL(); 		//통계정보설정
			log(pos,"setSSet() 호출을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATGETSSETFAIL(); 		//통계정보설정
			log(pos,"setSSet() 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATGETSSETSUCCESS(); 		//통계정보설정
		return shash;
	}

	/**
	 *
 	 * 데이터변수를 리턴한다(저장 프로시져 호출).	
	 * 프로시져 호출을 위한 데이터변수를 리턴한다.
	 *
	 * @param  		conn			Connection
	 * @param		SQL				SQL문  		
	 * @return  	Hashtable		데이터변수(Connection, CallableStatement)
	 * @exception	Err				에러
	 */
	public Hashtable getSSet(Connection conn, String SQL)  throws Err {
		String pos = MNAME+".getSSet()";

		setSTATGETSSETTRY(); 		//통계정보설정

		CallableStatement cstmt = null;
		Hashtable shash = null;
		try {
			cstmt = conn.prepareCall(SQL);
			shash = new Hashtable();
			shash.put("conn", conn);
			shash.put("cstmt", cstmt);
		} catch(SQLException sqle) {
			setSTATGETSSETFAIL(); 		//통계정보설정
			log(pos,"setSSet() 호출을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATGETSSETFAIL(); 		//통계정보설정
			log(pos,"setSSet() 호출을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATGETSSETSUCCESS(); 		//통계정보설정
		return shash;
	}

	/**
	 *
 	 * List DataSet을 리턴한다.	
	 * 다수의 데이터에 대한 데이터셋을 리턴한다(페이지를 설정한다).
	 *
	 * DEPRECATED 메소드 = getDSALData() 로 대체됐음.
	 *
	 * @param  		inputhash		리소스 관련 인자값<br>
	 *								key => jdbcenv(String,선택), SQL(String,필수), CSQL(String,필수), <br> 
	 *										param(ParamType,선택), cparam(ParamType,필수), <br>
	 *										p_page(String,선택), p_pagerow(String,선택)
	 *										conn(Connection, 선택)
	 * @return		Hashtable		DataSet	(key => data(Vector), totcnt(String), page(String), totpage(String))<br>
	 *								# data => Vector 내에 DB 로우에 대한 Hashtable 저장<br>
	 *								# totcnt ==> 검색 SQL의 총 데이터 수<br>
	 *								# page ==> 읽어올 구간이 번호<br>
	 *								# totpage ==> 총 데이터 수에 대한 전체 페이지수<br>
	 *								# __EXEC_SQL ==> 실행 SQL<br>
	 * @exception	Err				에러
	 */
	public Hashtable getLData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".getLData()";

		setSTATSQLLISTTRY();	//통계 정보 설정
		setSTATGETLDATATRY();	//통계 정보 설정

		//리스트 데이터 호출을 위한 변수
		int page = 1;
		int pagerow = 0;
		String jdbcenv = null;
		String SQL = null;
		String CSQL = null;
		ParamType param = null;
		ParamType cparam = null;

		Connection conn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;
		
			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}

			CSQL = getData("CSQL",inputhash);
			if(CSQL.equals("")) {
				errflag = true;
				errstr += " [CSQL] ";
			}

			param = (ParamType)getObjData("param",inputhash);

			cparam = (ParamType)getObjData("cparam",inputhash);

			conn = (Connection)getObjData("conn",inputhash);

			page = parseInt(getData("p_page",inputhash));
			if(page == 0) page = 1;

			pagerow = parseInt(getData("p_pagerow",inputhash));
			if(pagerow == 0) pagerow = parseInt(getEnv("ENVpagerow"));

		} catch(Exception e) {
			setSTATSQLLISTFAIL();	//통계 정보 설정
			setSTATGETLDATAFAIL();	//통계 정보 설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLLISTFAIL();	//통계 정보 설정
			setSTATGETLDATAFAIL();	//통계 정보 설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable rethash = null;

		//ResultSet 선언
		ResultSet rs = null;

		try {

			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getISet(jdbcenv,SQL);	
				else ihash = getISet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
			

			/*===============================================*/
			//데이터 값 처리 인수를 생성한다.

			//전체 데이터 수를 리턴한다.
			//int totcnt = getTotalRow(jdbcenv,CSQL,cparam);
			Hashtable cnthash = new Hashtable();
			cnthash.put("conn",getObjData("conn",ihash));	
			cnthash.put("CSQL",CSQL);
			if(cparam != null) cnthash.put("cparam",cparam);
			int totcnt = getTotalRow(cnthash);
			
			//전체 페이지수를 리턴한다.	
			int totpage = 1;
			if(pagerow != 0) {
				if((totcnt%pagerow) == 0) {
					totpage = (totcnt/pagerow);
				} else {
					totpage = (totcnt/pagerow)+1;
				}
			}

			/* ================================================= */
		 	// 입력 페이지가 전체 페이지 보다 클경우는 전체 페이지를 
		 	// 입력 페이지로 설정한다.
		 	/* ================================================= */
			if(page > totpage) page = totpage;

			int skip = (page-1)*pagerow;
			
			/*==================================================*/
			//리스트 데이터를 저장한다.
			Vector data = null;
	
			ResultSetMetaData mrs = null;
	
			try {
	
				//파리미터를 설정한다.			
				if(param != null) {				
					setParam(pstmt,param);
				}
	
				rs = pstmt.executeQuery();
				mrs = rs.getMetaData();


				//컬럼의 수를 저장한다.
				int colcnt = mrs.getColumnCount();

				/*==================================================*/
				//데이터를 스킵한다.
				/*==================================================*/
				for(int i = 1; i <= skip; i++) {
					rs.next();
				}

				/*==================================================*/
				//List DataSet을 생성한다.
				/*==================================================*/

				data = new Vector();
				Hashtable rowhash = null;
				String inputVar = null;
				for(int j = 1; (j <= pagerow)&&(rs.next()); j++) {
					rowhash = setRowData(rs);
					data.addElement(rowhash);
				}


				/*==================================================*/
				//   리턴 데이터를 설정한다.
	
				rethash = new Hashtable();
				rethash.put("data", data);
				rethash.put("totcnt", Integer.toString(totcnt));
				rethash.put("page", Integer.toString(page));
				rethash.put("totpage", Integer.toString(totpage));
				rethash.put("__EXEC_SQL", SQL);
				rethash.put("__EXEC_CSQL", CSQL);
		
				/*==================================================*/

			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}

		} catch (Err err) {
			setSTATSQLLISTFAIL();	//통계 정보 설정
			setSTATGETLDATAFAIL();	//통계 정보 설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLLISTFAIL();	//통계 정보 설정
			setSTATGETLDATAFAIL();	//통계 정보 설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}		
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	

		setSTATSQLLISTSUCCESS();	//통계 정보 설정
		setSTATGETLDATASUCCESS();	//통계 정보 설정
		return rethash;
	
	}


	
	/**
	 *
 	 * List DataSet을 리턴한다.	
	 * 다수의 데이터에 대한 데이터셋을 리턴한다(페이지를 설정한다).
	 * DataSet 적용 메소드
	 *
	 * @param  		inputhash		리소스 관련 인자값<br>
	 *								key => jdbcenv(String,선택), SQL(String,필수), CSQL(String,필수), <br> 
	 *										param(ParamType,선택), cparam(ParamType,필수), <br>
	 *										p_page(String,선택), p_pagerow(String,선택)
	 *										conn(Connection, 선택)
	 * @return		DataSet 		DataSet 참고
	 * @exception	Err				에러
	 */
	public DataSet getDSLData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".getDSLData()";

		setSTATSQLLISTTRY();		//통계정보설정
		setSTATGETDSLDATATRY();		//통계정보설정

		//리스트 데이터 호출을 위한 변수
		int page = 1;
		int pagerow = 0;
		String jdbcenv = null;
		String SQL = null;
		String CSQL = null;
		ParamType param = null;
		ParamType cparam = null;

		Connection conn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;
		
			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}

			CSQL = getData("CSQL",inputhash);
			if(CSQL.equals("")) {
				errflag = true;
				errstr += " [CSQL] ";
			}

			param = (ParamType)getObjData("param",inputhash);

			cparam = (ParamType)getObjData("cparam",inputhash);

			conn = (Connection)getObjData("conn",inputhash);

			page = parseInt(getData("p_page",inputhash));
			if(page == 0) page = 1;

			pagerow = parseInt(getData("p_pagerow",inputhash));
			if(pagerow == 0) pagerow = parseInt(getEnv("ENVpagerow"));

		} catch(Exception e) {
			setSTATSQLLISTFAIL();		//통계정보설정
			setSTATGETDSLDATAFAIL();	//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLLISTFAIL();		//통계정보설정
			setSTATGETDSLDATAFAIL();	//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//데이터셋 선언
		DataSet ds = null;

		//ResultSet 선언
		ResultSet rs = null;

		try {

			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getISet(jdbcenv,SQL);	
				else ihash = getISet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
			

			/*==================================================*/
			//데이터 값 처리 인수를 생성한다.

			//전체 데이터 수를 리턴한다.
			//int totcnt = getTotalRow(jdbcenv,CSQL,cparam);
			Hashtable cnthash = new Hashtable();
			cnthash.put("conn",getObjData("conn",ihash));	
			cnthash.put("CSQL",CSQL);
			if(cparam != null) cnthash.put("cparam",cparam);
			int totcnt = getTotalRow(cnthash);
			
			//전체 페이지수를 리턴한다.	
			int totpage = 1;
			if(pagerow != 0) {
				if((totcnt%pagerow) == 0) {
					totpage = (totcnt/pagerow);
				} else {
					totpage = (totcnt/pagerow)+1;
				}
			}

			/* ================================================= */
		 	// 입력 페이지가 전체 페이지 보다 클경우는 전체 페이지를 
		 	// 입력 페이지로 설정한다.
		 	/* ================================================= */
			if(page > totpage) page = totpage;

			int skip = (page-1)*pagerow;
			
			try {
	
				//파리미터를 설정한다.			
				if(param != null) {				
					setParam(pstmt,param);
				}
	
				rs = pstmt.executeQuery();
				ResultSetMetaData mrs = rs.getMetaData();

				//데이터셋 생성
				ds = new DataSetImpl();

				//메타 데이터 설정		
				setMetaData(ds, mrs); 

				/*==================================================*/
				//데이터를 스킵한다.
				/*==================================================*/
				for(int i = 1; i <= skip; i++) {
					rs.next();
				}

				/*==================================================*/
				//List DataSet을 생성한다.
				/*==================================================*/

				for(int j = 1; (j <= pagerow)&&(rs.next()); j++) {
					setRowData(ds,rs);
				}


				/*==================================================*/
				//   리턴 데이터를 설정한다.
	
				ds.setCSQL(CSQL);			//COUNT 쿼리 설정
				ds.setSQL(SQL);				//리스트 SQL 쿼리 설정
				ds.setPage(page);			//현재 페이지 설정
				ds.setPagerow(pagerow);		//페이지당 로우수 설정
				ds.setTotCnt(totcnt);		//전체 데이터 수 설정
				ds.setTotPage(totpage);		//전체 페이지 수 설정
				ds.setParam(param);			//파라미터 정보 설정
		
				/*==================================================*/

			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}

		} catch (Err err) {
			setSTATSQLLISTFAIL();			//통계정보설정
			setSTATGETDSLDATAFAIL();		//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLLISTFAIL();			//통계정보설정
			setSTATGETDSLDATAFAIL();		//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}		
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	

		setSTATSQLLISTSUCCESS();		//통계정보설정
		setSTATGETDSLDATASUCCESS();		//통계정보설정
		return ds;
	
	}

	/**
	 *
 	 * List DataSet을 리턴한다.	
	 * 다수의 데이터에 대한 데이터셋을 리턴한다(전체 리스트 출력).
   	 *
	 * DEPRECATED 메소드 = getDSALData() 로 대체됐음.
	 *
	 * @param  		inputhash		리소스 관련 인자값
	 *								key => jdbcenv(String,선택), SQL(String,필수),<br> 
	 *										param(ParamType,선택), conn(Connection, 선택)<br>
	 * @return		Vector			DataSet	(구조 : Vector내에 각 DB TABLE의 로우에 해당하는 Hashtable)
	 * @exception	Err				에러
	 */
	public Vector getALData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".getALData()";

		setSTATSQLALISTTRY();			//통계정보설정
		setSTATGETALDATATRY();			//통계정보설정

		//리스트 데이터 호출을 위한 변수
		String jdbcenv = null;
		String SQL = null;
		ParamType param = null;

		Connection conn = null;

		//에러설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;
		
			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}
			
			param = (ParamType)getObjData("param",inputhash);

			conn = (Connection)getObjData("conn",inputhash);

		} catch(Exception e) {
			setSTATSQLALISTFAIL();			//통계정보설정
			setSTATGETALDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLALISTFAIL();			//통계정보설정
			setSTATGETALDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리스트 데이터를 저장한다(리턴데이터).
		Vector data = null;
		
		//RecordSet 선언
		ResultSet rs = null;

		try {

			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getISet(jdbcenv,SQL);	
				else ihash = getISet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.getEXStr());	
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
			

			/*==================================================*/

			//데이터셋 생성을 위한 변수 
			ResultSetMetaData mrs = null;
	
			try {

				//파라미터를 설정한다.
				if(param != null) {
					setParam(pstmt,param);
				}

				rs = pstmt.executeQuery();
				mrs = rs.getMetaData();

				//컬럼의 수를 저장한다.
				int colcnt = mrs.getColumnCount();

				/*==================================================*/
				//List DataSet을 생성한다.
				/*==================================================*/

				data = new Vector();
				Hashtable rowhash = null;
				String inputVar = null;
				while(rs.next()) {
					rowhash = setRowData(rs);
					data.addElement(rowhash);
				}
	
			/*==================================================*/

			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLALISTFAIL();			//통계정보설정
			setSTATGETALDATAFAIL();			//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLALISTFAIL();			//통계정보설정
			setSTATGETALDATAFAIL();			//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	
			
		setSTATSQLALISTSUCCESS();		//통계정보설정
		setSTATGETALDATASUCCESS();			//통계정보설정
		return data;
	
	}

	/**
	 *
 	 * List DataSet을 리턴한다.	
	 * 다수의 데이터에 대한 데이터셋을 리턴한다(전체 리스트 출력).
	 * DataSet 적용버전
	 *
	 * @param  		inputhash		리소스 관련 인자값
	 *								key => jdbcenv(String,선택), SQL(String,필수),<br> 
	 *										param(ParamType,선택), conn(Connection, 선택)<br>
	 * @return		DataSet			DataSet 참고
	 * @exception	Err				에러
	 */
	public DataSet getDSALData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".getDSALData()";

		setSTATSQLALISTTRY();				//통계정보설정
		setSTATGETDSALDATATRY();			//통계정보설정

		//리스트 데이터 호출을 위한 변수
		String jdbcenv = null;
		String SQL = null;
		ParamType param = null;

		Connection conn = null;

		//에러설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;
		
			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] " + SQL;
			}
			
			param = (ParamType)getObjData("param",inputhash);

			conn = (Connection)getObjData("conn",inputhash);

		} catch(Exception e) {
			setSTATSQLALISTFAIL();				//통계정보설정
			setSTATGETDSALDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLALISTFAIL();				//통계정보설정
			setSTATGETDSALDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//DataSet 선언
		DataSet ds = null;

		//RecordSet 선언
		ResultSet rs = null;

		try {

			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getISet(jdbcenv,SQL);	
				else ihash = getISet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.getEXStr());	
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
			

			/*==================================================*/

			try {

				//파라미터를 설정한다.
				if(param != null) {
					setParam(pstmt,param);
				}

				rs = pstmt.executeQuery();
				ResultSetMetaData mrs = rs.getMetaData();

				//데이터셋 생성
				ds = new DataSetImpl();

				//메타 데이터 설정		
				setMetaData(ds, mrs); 

				/*==================================================*/
				//List DataSet을 생성한다.
				/*==================================================*/

				while(rs.next()) setRowData(ds,rs);
	
				/*==================================================*/
				//   리턴 데이터를 설정한다.
	
				ds.setSQL(SQL);				//리스트 SQL 쿼리 설정
				ds.setParam(param);			//파라미터 정보 설정
		
				/*==================================================*/

			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLALISTFAIL();				//통계정보설정
			setSTATGETDSALDATAFAIL();			//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLALISTFAIL();				//통계정보설정
			setSTATGETDSALDATAFAIL();			//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	
			
		setSTATSQLALISTSUCCESS();			//통계정보설정
		setSTATGETDSALDATASUCCESS();			//통계정보설정
		return ds; 
	
	}

	/**
	 *
 	 * Info DataSet을 리턴한다.	
	 * 한건의 데이터에 대한 데이터셋을 리턴한다.
	 *
	 * @param  		inputhash		리소스 관련 인자값	
	 *								key => jdbcenv(String,선택), SQL(String,필수), <br> 
	 *										param(ParamType,선택), conn(Connectino, 선택)									
	 * @return		Hashtable		DataSet	(Hashtable => [KEY(DB Field), VALUE(Field Value)])<br>
	 *								KEY 중에 __EXEC_SQL 은 수행 SQL문임
	 *								KEY 중에 __EMPTY_YN 은 SQL 결과 데이터 존재 여부임
	 * @exception	Err				에러
	 */
	public Hashtable getIData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".getIData()";

		setSTATSQLINFOTRY();	//통계 정보 설정
		setSTATGETIDATATRY();	//통계 정보 설정

		//데이터 호출을 위한 변수
		String jdbcenv = null;
		String SQL = null;
		ParamType param = null;
		Connection conn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) {
				jdbcenv = JDBCENV;
			}
		
			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}

			conn = (Connection)getObjData("CONN",inputhash);
				
			param = (ParamType)getObjData("param",inputhash);

		} catch(Exception e) {
			setSTATSQLINFOFAIL();		//통계정보설정
			setSTATGETIDATAFAIL();		//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLINFOFAIL();		//통계정보설정
			setSTATGETIDATAFAIL();		//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable rethash = new Hashtable();

		//ResultSet 선언
		ResultSet rs = null;

		try {

			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getISet(jdbcenv,SQL);	
				else ihash = getISet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
			
			/*==================================================*/
			//메타 데이터 선언	
			ResultSetMetaData mrs = null;

			try {

				//파리미터를 설정한다.			
				if(param != null) {				
					setParam(pstmt,param);
				}

				rs = pstmt.executeQuery();
				mrs = rs.getMetaData();
			
				//컬럼의 수를 저장한다.
				int colcnt = mrs.getColumnCount();

				/*==================================================*/
				//Info DataSet을 생성한다.
				/*==================================================*/
				if(rs.next()) {
					rethash = setRowData(rs);
            	}
			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLINFOFAIL();		//통계정보설정
			setSTATGETIDATAFAIL();		//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLINFOFAIL();		//통계정보설정
			setSTATGETIDATAFAIL();		//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}		
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	
			
		if(rethash.size() == 0) {		//쿼리에 대한 데이터 값 없음
			rethash.put("__EMPTY_YN","Y");
		} else {
			rethash.put("__EMPTY_YN","N");
		}
		rethash.put("__EXEC_SQL",SQL);

		setSTATSQLINFOSUCCESS();	//통계정보설정
		setSTATGETIDATASUCCESS();	//통계정보설정

		return rethash;
	
	}

	/**
	 *
 	 * Info DataSet을 리턴한다.	
	 * 한건의 데이터에 대한 데이터셋을 리턴한다.
	 *
	 * @param  		inputhash		리소스 관련 인자값	
	 *								key => jdbcenv(String,선택), SQL(String,필수), <br> 
	 *										param(ParamType,선택), conn(Connectino, 선택)									
	 * @return		DataSet			데이타셋
	 * @exception	Err				에러
	 */
	public DataSet getDSIData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".getDSIData()";

		setSTATSQLINFOTRY();		//통계정보설정
		setSTATGETDSIDATATRY();		//통계정보설정

		//데이터 호출을 위한 변수
		String jdbcenv = null;
		String SQL = null;
		ParamType param = null;
		Connection conn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		
		
		
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) {
				jdbcenv = JDBCENV;
			}
		
			SQL = getData("SQL",inputhash);
			
//			System.out.println("====================");
//			System.out.println(":DBUtil.getDSIData() ===>" + SQL);
//			System.out.println("====================");
			
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}

			conn = (Connection)getObjData("CONN",inputhash);
				
			param = (ParamType)getObjData("param",inputhash);

		} catch(Exception e) {
			setSTATSQLINFOFAIL();			//통계정보설정
			setSTATGETDSIDATAFAIL();		//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLINFOFAIL();			//통계정보설정
			setSTATGETDSIDATAFAIL();		//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable rethash = new Hashtable();

		//ResultSet 선언
		ResultSet rs = null;

		//DataSet 선언	
		DataSet ds = null;

		try {

			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getISet(jdbcenv,SQL);	
				else ihash = getISet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
			
			try {

				//파리미터를 설정한다.			
				if(param != null) {				
					setParam(pstmt,param);
				}

				rs = pstmt.executeQuery();
				ResultSetMetaData mrs = rs.getMetaData();

				//데이터셋 생성
				ds = new DataSetImpl();

				//메타 데이터 설정		
				setMetaData(ds, mrs); 
			
				/*==================================================*/
				//Info DataSet을 생성한다.
				/*==================================================*/
				if(rs.next()) {
					setRowData(ds,rs);
            	}
			
				/*==================================================*/
				//   리턴 데이터를 설정한다.
	
				ds.setCSQL("");				//COUNT 쿼리 설정
				ds.setSQL(SQL);				//리스트 SQL 쿼리 설정
				ds.setTotCnt(ds.size());	//전체 데이터 수 설정
				ds.setParam(param);			//파라미터 정보 설정
		
				/*==================================================*/

			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLINFOFAIL();			//통계정보설정
			setSTATGETDSIDATAFAIL();		//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLINFOFAIL();			//통계정보설정
			setSTATGETDSIDATAFAIL();		//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}		
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	
			
		setSTATSQLINFOSUCCESS();		//통계정보설정
		setSTATGETDSIDATASUCCESS();		//통계정보설정
		return ds;
	
	}

	/**
	 *
 	 * 데이터를 조작한다.	
	 * DML을 실행한다.
	 *
	 * @param  		inputhash		리소스 관련 인자값 <br>
	 *								key => jdbcenv(String,선택), SQL(String,필수), <br> 
	 *										param(ParamType,선택), type(String,선택), count(String,선택)<br>
	 *										conn(Connection, 선택)
	 *									 # type ==> INSERT, UPDATE, DELETE 중 하나 <br>
	 *									 # count ==> 처리되어야할 레코드의 수
	 * @return		Hashtable		결과값 (key => yn(처리성공여부,"Y" or "N"),mcnt(처리된 레코드수), __EXEC_SQL 은 수행 SQL)
	 * @exception	Err				에러
	 */
	public Hashtable setMData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".setMData()";

		setSTATSQLMODIFYTRY();			//통계정보설정
		setSTATSETMDATATRY();			//통계정보설정

		//데이터 호출을 위한 변수
		String jdbcenv = null;		//jdbc 환경값
		String SQL = null;			//query
		String type = null;			//유형(insert, update, delete) 
		int count = -1;				//처리건수
		ParamType param = null;		//파라미터

		Connection conn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;

			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}

			type = getData("type",inputhash);
			
			String cnt = (String)getObjData("count",inputhash);
			if(cnt != null) count = parseInt(cnt);

			param = (ParamType)getObjData("param",inputhash);

			conn = (Connection)getObjData("conn",inputhash);

		} catch(Exception e) {
			setSTATSQLMODIFYFAIL();			//통계정보설정
			setSTATSETMDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTTRY();
		else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATETRY();
		else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETETRY();

		if(errflag) {
			setSTATSQLMODIFYFAIL();			//통계정보설정
			setSTATSETMDATAFAIL();			//통계정보설정

			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();

			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable rethash = null;

		try {

			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getMSet(jdbcenv,SQL);	
				else ihash = getMSet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());	
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
			
			/*==================================================*/
			int mcnt = 0;		//처리건수
			String yn = "N";	//성공여부

			try {

				//파라미터를 설정한다.
				if(param != null) {
					setParam(pstmt,param);
				}

				mcnt = pstmt.executeUpdate();

				//처리여부를 설정한다.
				if(count != -1) {
					if(count == mcnt) {
						yn = "Y";
					}
				} else {
					yn = "Y";
				}
					
				//리턴데이터를 설정한다.
				rethash = new Hashtable();
				rethash.put("mcnt", Integer.toString(mcnt));
				rethash.put("yn", yn);
				rethash.put("__EXEC_SQL", SQL);
				
			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLMODIFYFAIL();
			setSTATSETMDATAFAIL();			//통계정보설정
			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();
			throw err;
		} catch (Exception calle) {
			setSTATSQLMODIFYFAIL();
			setSTATSETMDATAFAIL();			//통계정보설정
			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	
			
		if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTSUCCESS();
		else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATESUCCESS();
		else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETESUCCESS();

		setSTATSQLMODIFYSUCCESS();
		setSTATSETMDATASUCCESS();			//통계정보설정

		return rethash;
	
	}

	/**
	 *
 	 * 데이터를 조작한다(동일한 쿼리에 대한 여러건의 데이터를 조작한다).	
	 * DML을 실행한다.
	 *
	 * @param  		inputhash		리소스 관련 인자값 <br>
	 *								key => jdbcenv(String,선택), SQL(String,필수), <br> 
	 *										param(Vector(ParamType리스트),필수), type(String,선택) <br>
	 *										conn(Connection, 선택)<br>
	 *									 # type ==> INSERT, UPDATE, DELETE 중 하나 <br>
	 * @return		Hashtable		결과값 (key => yn(처리성공여부,"Y" or "N"),mcnt(실제 처리된 레코드수), txcnt(처리되어야 할 레코드수), __EXEC_SQL(수행 SQL))
	 * @exception	Err				에러
	 */
	public Hashtable setMMData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".setMMData()";

		setSTATSQLMODIFYTRY();			//통계정보설정
		setSTATSETMMDATATRY();			//통계정보설정

		//데이터 호출을 위한 변수
		String jdbcenv = null;		//jdbc 환경값
		String SQL = null;			//query
		String type = null;			//유형(insert, update, delete) 
		Vector paramvec = null;		//처리 데이터 파라미터

		Connection conn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {

			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;
		
			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}

			type = getData("type",inputhash);
			
			paramvec = (Vector)getObjData("param",inputhash);
			if(paramvec == null) {
				errflag = true;
				errstr += " [param] ";
			}

			conn = (Connection)getObjData("conn",inputhash);

		} catch(Exception e) {
			setSTATSQLMODIFYFAIL();			//통계정보설정
			setSTATSETMMDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTTRY();
		else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATETRY();
		else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETETRY();

		if(errflag) {
			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();
			setSTATSQLMODIFYFAIL();			//통계정보설정
			setSTATSETMMDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable rethash = null;

		try {
			try { //PreparedStatement를 호출한다.
				if(conn == null) ihash = getMSet(jdbcenv,SQL);	
				else ihash = getMSet(conn,SQL);
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());	
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
			
			/*==================================================*/
			int mcnt = 0;		//처리건수
			String yn = "Y";	//성공여부
	
			ParamType param = null;	//파라미터 정보

			try {
	
				for(int i = 0; i < paramvec.size(); i++) {
					param = (ParamType)paramvec.elementAt(i);
	
					//파라미터를 설정한다.
					if(param != null) {
						setParam(pstmt,param);
					}

					mcnt += pstmt.executeUpdate();

					if(type.equals(Code.SQL_TYPE_INSERT)) 
						setSTATSQLINSERTSUCCESS();
					else if(type.equals(Code.SQL_TYPE_UPDATE)) 
						setSTATSQLUPDATESUCCESS();
					else if(type.equals(Code.SQL_TYPE_DELETE)) 
						setSTATSQLDELETESUCCESS();

					setSTATSQLMODIFYSUCCESS();

				}
					
				//리턴데이터를 설정한다.
				rethash = new Hashtable();
				rethash.put("mcnt", Integer.toString(mcnt));
				rethash.put("txcnt", Integer.toString(paramvec.size()));
				rethash.put("yn", yn);
				rethash.put("__EXEC_SQL", SQL);
				
			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(pos,"쿼리를 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();
			setSTATSQLMODIFYFAIL();
			setSTATSETMMDATAFAIL();			//통계정보설정
			throw err;
		} catch (Exception calle) {
			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();
			setSTATSQLMODIFYFAIL();
			setSTATSETMMDATAFAIL();			//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	
			
		setSTATSETMMDATASUCCESS();			//통계정보설정
		return rethash;
	
	}

	/**
	 *
 	 * 데이터를 조작한다(저장 프로시져를 호출한다).	
	 * 저장 프로시져를 실행한다.
	 *
	 * @param  		inputhash		리소스 관련 인자값 <br>
	 *								key => jdbcenv(String,선택), SQL(String,필수), <br> 
	 *										param(ParamType,선택), retparam(String, 선택) <br>
	 *									# retparam ==> 프로시져 리턴값에 대한 리턴 파라미터의 순번
	 *										conn(Connection, 선택)
	 *									$$ 주의 사항 : SQL문을 만들때(call procedure(?,?)) 스트링의 앞쪽과 뒷쪽에 공백 없앨것
	 *												   이 메소드에서 없애주긴 하지만 미리 없앨것..
	 * @return		Hashtable		결과값 (key => 프로시져리턴값의 번호, __EXEC_SQL(수행 SQL문) 
	 * @exception	Err				에러
	 */
	public Hashtable callSData(Hashtable inputhash)  throws Err {

		String pos = MNAME+".callSData()";

		setSTATSQLPROCTRY();			//통계정보설정
		setSTATCALLSDATATRY();			//통계정보설정

		//데이터 호출을 위한 변수
		String jdbcenv = null;		//jdbc 환경값
		String SQL = null;			//query
		ParamType param = null;		//파라미터
		int retparam = 0;  			//리턴파라미터

		Connection conn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;

			SQL = getData("SQL",inputhash);
			if(SQL.equals("")) {
				errflag = true;
				errstr += " [SQL] ";
			}
			
			param = (ParamType)getObjData("param",inputhash);

			retparam = parseInt(getData("retparam",inputhash));
			if(param != null || retparam == 0) retparam = param.size()+1;

			conn = (Connection)getObjData("conn",inputhash);

		} catch(Exception e) {
			setSTATSQLPROCFAIL();			//통계정보설정
			setSTATCALLSDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLPROCFAIL();			//통계정보설정
			setSTATCALLSDATAFAIL();			//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		CallableStatement cstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable rethash = null;

		try {
			try { 	//CallableStatement를 호출한다.
				if(conn == null) ihash = getSSet(jdbcenv,SQL.trim());	
				else ihash = getSSet(conn,SQL.trim());
				cstmt = (CallableStatement)ihash.get("cstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());	
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		
			/*==================================================*/
			String yn = null;	//성공여부

			try {

				int i;

				//파라미터를 설정한다.
				if(param != null) {
					for(i = 1; i <= param.size(); i++) { 
						if(i < retparam) {
							cstmt.setString(i,param.getValue(i));
						} else {
							cstmt.setString(i,null);															
							cstmt.registerOutParameter(i,java.sql.Types.VARCHAR);
						}
					}
				}
				cstmt.executeUpdate();

				//리턴데이터를 설정한다.
				rethash = new Hashtable();
				if(param != null) {
					for(i = 1; i <= param.size(); i++) {
						if(i >= retparam) {	
							rethash.put(Integer.toString(i),
										setNull(cstmt.getString(i)));
						}
					}
				}
			} catch (SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLPROCFAIL();			//통계정보설정
			setSTATCALLSDATAFAIL();			//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLPROCFAIL();			//통계정보설정
			setSTATCALLSDATAFAIL();			//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(conn == null) closeISet(ihash);
				else {
					ihash.remove("conn");
					closeISet(ihash);
				}
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	

		setSTATSQLPROCSUCCESS();			//통계정보설정
		setSTATCALLSDATASUCCESS();			//통계정보설정

		rethash.put("__EXEC_SQL",SQL);
		return rethash;
	
	}

	/**
	 *
 	 * 데이터를 조작한다(트랜잭션 기능 지원).	
	 *
	 * @param  		inputhash		리소스 관련 인자값 <br>
	 *								key ==> (jdbcenv, SQLHASH, tflag)<br>
	 *								# tflag ==> 트랜잭션 처리 여부(Y,N)<br>
	 *								# SQLHASH(hashtable)의 구조 ==>	순번, HASHTABLE(SQLINFO)<br>	
	 *								# SQLINFO(hashtable)	==><br>							
	 *									SQL(쿼리문), param(파라미터),<br> 
	 *									type(유형(INSERT,UPDATE,DELETE), count(처리건수),<br>	
	 *									tpoint(TX Point), conn(Connection,선택)
	 *							
	 * @return		Hashtable		결과값(key => yn(처리성공여부,"Y" or "N"),rcnt(실제 처리된 레코드수), __EXEC_SQL(수행 SQL문))
	 */
	public Hashtable setTXMData(Hashtable inputhash) throws Err {

		String pos = MNAME+".setTXMData()";

		setSTATSETTXMDATATRY();		//통계정보설정

		//데이터 호출을 위한 변수
		String jdbcenv = null;			//jdbc 환경값
		Hashtable sqlhash = null;		//query 리스트
		String tflag = "N";				//트랜잭션 여부

		Connection iconn = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) jdbcenv = JDBCENV;

			sqlhash = (Hashtable)getObjData("SQLHASH",inputhash);
			if(sqlhash == null) {
				errflag = true;
				errstr += " [SQLHASH] ";
			}

			tflag = getData("tflag",inputhash);	
			if(tflag.equals("")) tflag = "N";

			iconn = (Connection)getObjData("conn",inputhash);

		} catch(Exception e) {
			setSTATSETTXMDATAFAIL();		//통계정보설정 
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	 
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSETTXMDATAFAIL();		//통계정보설정 
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		//데이터 호출을 위한 변수
		Connection conn = null;

		//리턴 데이터를 저장한다.
		Hashtable rethash = null;

		//PreparedStatement 선언
		PreparedStatement pstmt = null;	

		//처리유형(INSERT, UPDATE, DELETE)
		String type = null;

		try {

			try { //Connection을 호출한다.
				if(iconn == null) conn = getConn(jdbcenv);	
				else conn = iconn;
				if(tflag.equals("Y")) {
					conn.setAutoCommit(false);
				}
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());	
				throw err;
			} catch(SQLException sqle) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}

			/*==================================================*/
			String yn = null;

			/*==================================================*/
			//데이터 조작을 위한 변수를 선언한다.
			/*==================================================*/
			Hashtable sqlinfo = null;		//질의문정보 
			String SQL = null;				//질의문
			ParamType param = null;			//파라미터
			String tpoint = null;			//TX Point
			int count = 0;					//처리건수
			int rcnt = -1;					//실제처리건수
			int mcnt = 0;					//질의당처리건수

			try {

				for(int i = 1; i <= sqlhash.size(); i++) {

					sqlinfo = null;	
					SQL = null;	
					param = null;	
					type = null;	
					tpoint = null;	
					count = -1;
					mcnt = 0;

					sqlinfo = (Hashtable)sqlhash.get(Integer.toString(i));
	
					if(sqlinfo.containsKey("SQL")) {
						SQL = (String)sqlinfo.get("SQL");
					} else {
						log(pos,"질의문이 없습니다.");	
						//시스템 에러 추가
						Hashtable sysmsg = new Hashtable();
						sysmsg.put("SYSMSG","SQL 문이 없습니다.");
						throw new Err("1","5","E","데이터 처리를 실패했습니다.",pos,sysmsg);
					}

					param = (ParamType)getObjData("param",sqlinfo);

					type = getData("type",sqlinfo);

					tpoint = getData("tpoint",sqlinfo);

					if(type.equals(Code.SQL_TYPE_INSERT)) 
						setSTATSQLINSERTTRY();
					else if(type.equals(Code.SQL_TYPE_UPDATE)) 
						setSTATSQLUPDATETRY();
					else if(type.equals(Code.SQL_TYPE_DELETE)) 
						setSTATSQLDELETETRY();
					
					String cnt = (String)getObjData("count",inputhash);
					if(cnt != null) count = parseInt(cnt);

					pstmt = conn.prepareStatement(SQL);
				
					//파라미터를 설정한다.
					if(param != null) {
						setParam(pstmt,param);
					}

					//DB 처리
					mcnt = pstmt.executeUpdate();

					//처리여부를 설정한다.
					if(count != -1) {
						if(count != mcnt) { //처리 대상 건수가 틀리다면 ...
							log(pos,"데이터 처리를 실패했습니다.\n");	
							//시스템 에러 추가
							Hashtable sysmsg = new Hashtable();
							sysmsg.put("SYSMSG","처리 대상 건수가 틀립니다.");
							throw new Err("1","5","E","데이터 처리를 실패했습니다.",pos,sysmsg);
						}
					}

					pstmt.close();
	
					rcnt += mcnt;	
			
					if(tflag.equals("Y")) {
						//트랙잭션 point를 확인한다.
						if(!tpoint.equals("")) { 
							if(tpoint.equals("C")) {
								conn.commit();
							} else {
								conn.rollback();
							}
						}
					}

					if(type.equals(Code.SQL_TYPE_INSERT)) 
						setSTATSQLINSERTSUCCESS(); 
					else if(type.equals(Code.SQL_TYPE_UPDATE)) 
						setSTATSQLUPDATESUCCESS();
					else if(type.equals(Code.SQL_TYPE_DELETE)) 
						setSTATSQLDELETESUCCESS();
				}

				rethash = new Hashtable();
				rethash.put("rcnt", Integer.toString(rcnt));
				rethash.put("yn", "Y");
				rethash.put("__EXEC_SQL", sqlhash);

			} catch (Err err) {
				if(tflag.equals("Y")) {
					try {
						conn.rollback();
					} catch (SQLException sqle1) {}
				}
				log(pos,"데이터 처리를 실패했습니다.\n"+err.getEXStr());	
				throw err;
			} catch (SQLException sqle) {
				if(tflag.equals("Y")) {
					try {
						conn.rollback();
					} catch (SQLException sqle1) {}
				}
				log(pos,"데이터 처리를 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sysmsg);
			} catch(Exception e) {
				if(tflag.equals("Y")) {
					try {
						conn.rollback();
					} catch (SQLException sqle1) {}
				}
				log(pos,"데이터 처리를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","데이터 처리를 실패했습니다.",pos,sysmsg);
			}

		
			try {
				if(tflag.equals("Y")) {
					conn.commit();
				}
			} catch (SQLException sqle) {
				log(pos,"데이터 처리를 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"데이터 처리를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","데이터 처리를 실패했습니다.",pos,sysmsg);
			} finally {
				try {
					if(iconn == null) conn.close();
				} catch(SQLException sqle1) {
					log(pos,"데이터 처리를 실패했습니다.\n"+sqle1.toString());	
					//시스템 에러 추가
					Hashtable sysmsg = new Hashtable();
					sysmsg.put("SYSMSG",sqle1.toString());
					throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle1.getErrorCode(),sysmsg);
				}
			}

		} catch(Err err) {
			setSTATSETTXMDATAFAIL();		//통계정보설정 
			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();
			throw err;
		} catch(Exception calle) {
			setSTATSETTXMDATAFAIL();		//통계정보설정 
			if(type.equals(Code.SQL_TYPE_INSERT)) setSTATSQLINSERTFAIL();
			else if(type.equals(Code.SQL_TYPE_UPDATE)) setSTATSQLUPDATEFAIL();
			else if(type.equals(Code.SQL_TYPE_DELETE)) setSTATSQLDELETEFAIL();
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			try {
				conn.setAutoCommit(true);
				if(pstmt != null) pstmt.close();
				if(iconn == null) {
					if(conn != null) conn.close();
				}
			} catch (Exception e10) { }	
		}

		setSTATSETTXMDATASUCCESS();		//통계정보설정 
		return rethash;
	
	}

    /**
     *
     * 검색쿼리의 레코드수를 리턴한다.
     *
     * @param   	CSQL           	질의문
     * 				param 			jdbc 파라미터
	 * @return  	int           	레코드수
	 * @exception	Err				에러
     *
     */
    public int getTotalRow(String CSQL, ParamType param) throws Err {

		String pos = MNAME+".getTotalRow()";

		setSTATSQLCOUNTTRY();			//통계정보설정
		setSTATGETTOTALROWTRY();		//통계정보설정

		ResultSet rs = null;
		Hashtable ihash = null;
		PreparedStatement pstmt = null;
		int totcnt = 0;

		try {
			try { 	//PreparedStatement를 호출한다.
				ihash = getISet(JDBCENV,CSQL);	
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}

			try {
				//파리미터를 설정한다.			
				if(param != null) {				
					setParam(pstmt,param);
				}

				rs = pstmt.executeQuery();
				rs.next();
				totcnt = rs.getInt(1);
			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+err.getEXStr());
				throw err;
			} catch(SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+sqle.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				closeISet(ihash);
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	

		setSTATSQLCOUNTSUCCESS();			//통계정보설정
		setSTATGETTOTALROWSUCCESS();		//통계정보설정
        return totcnt;

    }

    /**
     *
     * 검색쿼리의 레코드수를 리턴한다.
     *
     * @param   	CSQL           	질의문
     * 				jdbcenv 		jdbc 환경값
     * 				param 			jdbc 파라미터
	 * @return  	int           	레코드수
	 * @exception	Err				에러
     *
     */
    public int getTotalRow(String jdbcenv, String CSQL, ParamType param) throws Err {

		String pos = MNAME+".getTotalRow()";

		setSTATSQLCOUNTTRY();			//통계정보설정
		setSTATGETTOTALROWTRY();		//통계정보설정

		ResultSet rs = null;
		Hashtable ihash = null;
		PreparedStatement pstmt = null;
		int totcnt = 0;

		try {
			try { 	//PreparedStatement를 호출한다.
				ihash = getISet(jdbcenv,CSQL);	
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());
				throw err;
			} catch(Exception e) {
				log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}

			try {
				//파리미터를 설정한다.			
				if(param != null) {				
					setParam(pstmt,param);
				}

				rs = pstmt.executeQuery();
				rs.next();
				totcnt = rs.getInt(1);

			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+err.getEXStr());
				throw err;
			} catch(SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+sqle.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				closeISet(ihash);
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	

		setSTATSQLCOUNTSUCCESS();			//통계정보설정
		setSTATGETTOTALROWSUCCESS();		//통계정보설정
        return totcnt;

    }

    /**
     *
     * 검색쿼리의 레코드수를 리턴한다.
     *
     * @param   	inputhash		입력파라미터	
	 * @return  	int           	레코드수
	 * @exception	Err				에러
     *
     */
    public int getTotalRow(Hashtable inputhash) throws Err {

		String pos = MNAME+".getTotalRow()";

		setSTATSQLCOUNTTRY();			//통계정보설정
		setSTATGETTOTALROWTRY();		//통계정보설정

		String jdbcenv = null;			//jdbc 환경값
		String CSQL = null;				//query
		ParamType param = null;			//파라미터
		Connection conn = null;			//DB Connection
		
		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			jdbcenv = getData("jdbcenv",inputhash);
			if(jdbcenv.equals("")) {
				jdbcenv = JDBCENV;
			}

			conn = (Connection)getObjData("conn",inputhash);
		
			CSQL = getData("CSQL",inputhash);
			if(CSQL.equals("")) {
				errflag = true;
				errstr += " [CSQL] ";
			}
				
			param = (ParamType)getObjData("cparam",inputhash);

		} catch(Exception e) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		if(errflag) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			log(pos,"입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",errstr);
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		ResultSet rs = null;
		Hashtable ihash = null;
		PreparedStatement pstmt = null;
		int totcnt = 0;

		try {

			if(conn == null) {	//Connection 이 없다면...

				try { 	//PreparedStatement를 호출한다.
					ihash = getISet(jdbcenv,CSQL);	
					pstmt = (PreparedStatement)ihash.get("pstmt");
				} catch(Err err) {
					log(pos,"데이터 리소스 호출을 실패했습니다.\n"+err.toString());
					throw err;
				} catch(Exception e) {
					log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString());	
					//시스템 에러 추가
					Hashtable sysmsg = new Hashtable();
					sysmsg.put("SYSMSG",e.toString());
					throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
				}
			} else {	
				try {				
					pstmt = conn.prepareStatement(CSQL);
				} catch(Exception e) {
					log(pos,"데이터 리소스 호출을 실패했습니다.\n"+e.toString()+"\n" + inputhash);	
					//시스템 에러 추가
					Hashtable sysmsg = new Hashtable();
					sysmsg.put("SYSMSG",e.toString());
					throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
				}
			}

			try {
				//파리미터를 설정한다.			
				if(param != null) {				
					setParam(pstmt,param);
				}

				rs = pstmt.executeQuery();
				rs.next();
				totcnt = rs.getInt(1);

			} catch(Err err) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+err.getEXStr());
				throw err;
			} catch(SQLException sqle) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+sqle.toString());
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",sqle.toString());
				throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sqle.getErrorCode(),sysmsg);
			} catch(Exception e) {
				log(pos,"쿼리 실행을 실패했습니다.\n"+CSQL+"\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
			}
		} catch (Err err) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			throw err;
		} catch (Exception calle) {
			setSTATSQLCOUNTFAIL();			//통계정보설정
			setSTATGETTOTALROWFAIL();		//통계정보설정
			log(pos,"메소드 호출을 실패했습니다.\n"+calle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",calle.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				closeISet(ihash);
			} catch(Err err) {
				log(pos,"리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(pos,"리소스 해제를 실패했습니다.\n"+e.toString());	
				//시스템 에러 추가
				Hashtable sysmsg = new Hashtable();
				sysmsg.put("SYSMSG",e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
			}
		}	

		setSTATSQLCOUNTSUCCESS();			//통계정보설정
		setSTATGETTOTALROWSUCCESS();		//통계정보설정
        return totcnt;

    }


    /**
     *
     * 변형 SEQUENCE를 읽어온다.
	 * 사용 DB : Oracle
     *
     * @param   	seq_nm			Sequence 명
	 * @return  	String        	Sequence 번호
	 * @exception	Err				에러
     *
     */
    public String getSequence(String seq_nm) throws Err {
		
		String pos = MNAME+".getSequence()";

		setSTATGETSEQUENCETRY();		//통계정보설정

		String seq = null;
		try {
			seq = getRawSequence(seq_nm);
		} catch (Err err) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,err.getEXStr());
			throw err;
		} catch (Exception e) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,"에러 발생.\n" + e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("150","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}
			
		setSTATGETSEQUENCESUCCESS();		//통계정보설정
		return seq;

    }

    /**
     *
     * 변형 SEQUENCE를 읽어온다.
	 * 사용 DB : Oracle
     *
     * @param   	jdbcenv 		jdbc 환경값(환경화일의 dbname)
	 * 				seq_name		Sequence 명
	 * @return  	String        	Sequence 번호
	 * @exception	Err				에러
     *
     */
    public String getSequence(String jdbcenv, String seq_nm) throws Err {
		
		String pos = MNAME+".getSequence()";

		setSTATGETSEQUENCETRY();		//통계정보설정

		String seq = null;
		try {
			seq = getRawSequence(jdbcenv, seq_nm);
		} catch (Err err) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,err.getEXStr());
			throw err;
		} catch (Exception e) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,"에러 발생.\n" + e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("150","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}
			
		setSTATGETSEQUENCESUCCESS();		//통계정보설정
	
		return seq;

    }

    /**
     *
     * SEQUENCE를 읽어온다.
	 * 사용 DB : Oracle
     *
     * @param   	seq_nm		Sequence 명
	 *				size		filler할 크기(전체 리턴될 문자열 길이)
	 *				filler		filler 값 (채울 값)
	 *				loc			filler 의 위치(앞(Code.PREFIX_FILLER),뒤(Code.SUFFIX.FILLER))
	 * @return  	String      Sequence 번호
	 * @exception	Err			에러
     *
     */
    public String getSequence(String seq_nm, int size, 
								String filler, int loc) throws Err {

		String pos = MNAME+".getSequence()";

		setSTATGETSEQUENCETRY();		//통계정보설정


		String seq = null;
		try {
			seq = getRawSequence(seq_nm);
		} catch (Err err) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,err.getEXStr());
			throw err;
		} catch (Exception e) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,"에러 발생.\n" + e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("150","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		setSTATGETSEQUENCESUCCESS();		//통계정보설정
		return setFiller(size, seq, filler, loc);

    }
	
    /**
     *
     * 변형 SEQUENCE를 읽어온다.
	 * 사용 DB : Oracle
     *
     * @param   	jdbcenv 	jdbc 환경값(환경화일의 dbname)
     *				seq_nm		Sequence 명
	 *				size		filler할 크기(전체 리턴될 문자열 길이)
	 *				filler		filler 값 (채울 값)
	 *				loc			filler 의 위치(앞(Code.PREFIX_FILLER),뒤(Code.SUFFIX.FILLER))
	 * @return  	String      Sequence 번호
	 * @return  	String          Sequence 번호
	 * @exception	Err				에러
     *
     */
    public String getSequence(String jdbcenv, String seq_nm, int size, 
								String filler, int loc) throws Err {

		String pos = MNAME+".getSequence()";

		setSTATGETSEQUENCETRY();		//통계정보설정

		String seq = null;
		try {
			seq = getRawSequence(jdbcenv, seq_nm);
		} catch (Err err) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,err.getEXStr());
			throw err;
		} catch (Exception e) {
			setSTATGETSEQUENCEFAIL();		//통계정보설정
			log(pos,"에러 발생.\n" + e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("150","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}

		setSTATGETSEQUENCESUCCESS();		//통계정보설정
		return setFiller(size, seq, filler, loc);

    }

    /**
     *
     * 기본 SEQUENCE를 읽어온다.
	 * 사용 DB : Oracle
     *
     * @param   	seq_nm			Sequence 명
	 * @return  	String          Sequence 번호
	 * @exception	Err				에러
     *
     */
    public String getRawSequence(String seq_nm) throws Err {

		String pos = MNAME+".getRawSequence()";

		setSTATGETRAWSEQUENCETRY();		//통계정보설정

		String SQL = " SELECT " + seq_nm + ".NEXTVAL SEQ FROM DUAL ";							

		Hashtable _qryhash = new Hashtable();
		_qryhash.put("SQL",SQL);
		_qryhash.put("param",new ParamType());
			
		Hashtable retHash = null;

		try {
			retHash = getIData(_qryhash);
		} catch (Err err) {
			setSTATGETRAWSEQUENCEFAIL();		//통계정보설정
			log(pos,err.getEXStr());
			throw err;
		} catch (Exception e) {
			setSTATGETRAWSEQUENCEFAIL();		//통계정보설정
			log(pos,"에러 발생.\n" + e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("150","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}
			
		String seq = getData("SEQ",retHash);	

		setSTATGETRAWSEQUENCESUCCESS();		//통계정보설정
		return seq;
    }

    /**
     *
     * 기본 SEQUENCE를 읽어온다.
	 * 사용 DB : Oracle
     *
     * @param		jdbcenv			jdbc환경(환경화일의 dbname)
     * 				seq_name		Sequence 명
	 * @return  	String          Sequence 번호
	 * @exception	Err				에러
     *
     */
    public String getRawSequence(String jdbcenv, String seq_nm) throws Err {

		String pos = MNAME+".getRawSequence()";

		setSTATGETRAWSEQUENCETRY();		//통계정보설정

		String SQL = " SELECT " + seq_nm + ".NEXTVAL SEQ FROM DUAL ";							

		Hashtable _qryhash = new Hashtable();
		_qryhash.put("jdbcenv",jdbcenv);
		_qryhash.put("SQL",SQL);
		_qryhash.put("param",new ParamType());
			
		Hashtable retHash = null;

		try {
			retHash = getIData(_qryhash);
		} catch (Err err) {
			setSTATGETRAWSEQUENCEFAIL();		//통계정보설정
			log(pos,err.getEXStr());
			throw err;
		} catch (Exception e) {
			setSTATGETRAWSEQUENCEFAIL();		//통계정보설정
			log(pos,"에러 발생.\n" + e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("150","시스템 에러입니다.\\n관리자에게 문의하십시요.",pos,sysmsg);
		}
			
		String seq = getData("SEQ",retHash);	

		setSTATGETRAWSEQUENCESUCCESS();		//통계정보설정

		return seq;
    }


	/**
	 *
 	 * 데이터베이스 리소스를 해제한다.	
	 * 검색 실행에 대한 데이터베이스 리소스를 해제한다.
	 *
	 * @param 		ihash			데이터리소스. 
	 * @exception	Err				에러
	 */
	public void closeISet(Hashtable ihash)  throws Err {

		String pos = MNAME+".closeISet()";

		setSTATCLOSEISETTRY();		//통계정보설정

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if(ihash == null) return;
			if(ihash.containsKey("conn")) conn = (Connection)ihash.get("conn");
			if(ihash.containsKey("pstmt")) pstmt = (PreparedStatement)ihash.get("pstmt");
			if(ihash.containsKey("rs")) rs = (ResultSet)ihash.get("rs");
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) closeConn(conn);
		} catch(Err err) {
			setSTATCLOSEISETFAIL();		//통계정보설정
			throw err;
		} catch(SQLException sqle) {
			setSTATCLOSEISETFAIL();		//통계정보설정
			log(pos,"데이터 리소스 해제를 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATCLOSEISETFAIL();		//통계정보설정
			log(pos,"데이터 리소스 해제를 실패했습니다.\n"+e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		} finally {
			try {
				if(rs != null) rs.close();			
				if(pstmt != null) pstmt.close();			
				if(!conn.isClosed()) closeConn(conn);			
			} catch(Exception e1) {}
		}
		setSTATCLOSEISETSUCCESS();		//통계정보설정
	}

	/**
	 *
 	 * 데이터베이스 리소스를 해제한다.	
	 * 데이터 수정에 대한 데이터 리소스를 해제한다.
	 *
	 * @param 		ihash			데이터리소스. 
	 * @exception	Err				에러
	 */
	public void closeMSet(Hashtable ihash)  throws Err {

		String pos = MNAME+".closeMSet()";

		setSTATCLOSEMSETTRY();		//통계정보설정

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			if(ihash == null) return;
			if(ihash.containsKey("conn")) conn = (Connection)ihash.get("conn");
			if(ihash.containsKey("pstmt")) pstmt = (PreparedStatement)ihash.get("pstmt");
			if(pstmt != null) pstmt.close();
			if(conn != null) closeConn(conn);
		} catch(Err err) {
			setSTATCLOSEMSETFAIL();		//통계정보설정
			throw err;
		} catch(SQLException sqle) {
			setSTATCLOSEMSETFAIL();		//통계정보설정
			log(pos,"데이터 리소스 해제를 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATCLOSEMSETFAIL();		//통계정보설정
			log(pos,"데이터 리소스 해제를 실패했습니다.\n"+e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		} finally {
			try {
				if(pstmt != null) pstmt.close();			
				if(!conn.isClosed()) closeConn(conn);			
			} catch(Exception e1) {}
		}

		setSTATCLOSEMSETSUCCESS();		//통계정보설정
	}

	/**
	 *
 	 * 데이터베이스 Connection을 닫는다.	
	 * 저장 프로시져에 대한 데이터 리소스를 해제한다.
	 *
	 * @param 		ihash 			데이터리소스. 
	 * @exception	Err				에러
	 */
	public void closeSSet(Hashtable ihash)  throws Err {

		String pos = MNAME+".closeSSet()";

		setSTATCLOSESSETTRY();		//통계정보설정

		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			if(ihash == null) return;
			if(ihash.containsKey("conn")) conn = (Connection)ihash.get("conn");
			if(ihash.containsKey("cstmt")) cstmt = (CallableStatement)ihash.get("cstmt");
			if(cstmt != null) cstmt.close();
			if(conn != null) closeConn(conn);
		} catch(Err err) {
			setSTATCLOSESSETFAIL();		//통계정보설정
			throw err;
		} catch(SQLException sqle) {
			setSTATCLOSESSETFAIL();		//통계정보설정
			log(pos,"데이터 리소스 해제를 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATCLOSESSETFAIL();		//통계정보설정
			log(pos,"데이터 리소스 해제를 실패했습니다.\n"+e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		} finally {
			try {
				if(cstmt != null) cstmt.close();			
				if(!conn.isClosed()) closeConn(conn);			
			} catch(Exception e1) {}
		}

		setSTATCLOSESSETSUCCESS();		//통계정보설정
	}

	/**
	 *
 	 * 데이터베이스 Connection을 닫는다.	
	 *
	 * @param 		conn 			DB Connection
	 * @exception	Err				에러
	 */
	public void closeConn(Connection conn)  throws Err {

		String pos = MNAME+".closeConn()";

		setSTATCLOSECONNTRY();			//통계정보설정
		setSTATCONCLOSETRY();			//통계정보설정

		try {
			if(conn != null) conn.close();
		} catch(SQLException sqle) {
			setSTATCLOSECONNFAIL();			//통계정보설정
			setSTATCONCLOSEFAIL();			//통계정보설정
			log(pos,"Connection 해제를 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch(Exception e) {
			setSTATCLOSECONNFAIL();			//통계정보설정
			setSTATCONCLOSEFAIL();			//통계정보설정
			log(pos,"Connection 해제를 실패했습니다.\n"+e.toString());
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		} finally {
			try {
				if(!conn.isClosed()) conn.close();			
			} catch(Exception e1) {}
		}
		setSTATCLOSECONNSUCCESS();			//통계정보설정
		setSTATCONCLOSESUCCESS();			//통계정보설정
	}

	/**
	 * 파리미터를 설정한다. 
	 * @param 		pstmt				PreparedStatement
	 *				param				ParamType
	 *									DB_INT(1) ==> int, 
	 *									DB_VARCHAR(2) ==> varchar, 
	 *									DB_LONG(3) ==> long	
	 */
	public void setParam(PreparedStatement pstmt, ParamType param)  throws Err {

		String pos = MNAME+".setParam()";

		setSTATSETPARAMTRY();		//통계정보설정

		int type = -1;
		int length = param.size();
		String value = null;	//입력값
		//log("length ==> " + length);	
		String temp_str = "";
		try {
			for(int i = 1; i <= length; i++) {
				//System.out.println(i + " ==> " + (param.getValue(i)).getBytes().length);
				value = param.getValue(i);
				switch(param.getType(i)) {			
					case Code.DB_INT: 		//int 형일 경우
						if(value.equals("")) 
							pstmt.setNull(i,java.sql.Types.INTEGER);
						else
							pstmt.setInt(i,parseInt(value));
						break;
					case Code.DB_VARCHAR:	//varchar 형일 경우
						if(value.equals("")) 
							pstmt.setNull(i,java.sql.Types.VARCHAR);
						else 	
							pstmt.setString(i,value);
					
						//JDBC 전송 버퍼보다 큰 데이터를 전송시 
						//4000바이트를 전송하기 위해 스트림 사용
						//pstmt.setCharacterStream(i,new StringReader(param.getValue(i)),param.getValue(i).length());
						break;
					case Code.DB_LONG:		//long 형일 경우
						if(value.equals("")) 
							pstmt.setNull(i,java.sql.Types.NUMERIC);
						else
							pstmt.setLong(i,parseLong(value));
						break;
					case Code.DB_FLOAT:		//float 형일 경우
						if(value.equals("")) 
							pstmt.setNull(i,java.sql.Types.NUMERIC);
						else
							pstmt.setFloat(i,parseFloat(value));
						break;
					case Code.DB_DOUBLE:	//double 형일 경우
						if(value.equals("")) 
							pstmt.setNull(i,java.sql.Types.NUMERIC);
						else
							pstmt.setDouble(i,parseDouble(value));
						break;
					case Code.DB_LVARCHAR:	//오라클 long이나 text형
						pstmt.setCharacterStream(i,new StringReader(value),value.length());
						break;
					default:
						pstmt.setString(i,value);	
				}
				//temp_str += i+"번째필드 ==>  유형 : [" + param.getType(i) + "], 값 : [" + param.getValue(i) + "]\n";
			}
			//log(temp_str);
		} catch (SQLException sqle) {
			setSTATSETPARAMFAIL();		//통계정보설정
			log(pos,"데이터 파라미터 설정을 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sqle.getErrorCode(),sysmsg);
		} catch (Exception e) {	
			setSTATSETPARAMFAIL();		//통계정보설정
			log(pos,"데이터 파라미터 설정을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
		setSTATSETPARAMSUCCESS();		//통계정보설정
	}


	/**
	 *
 	 * 데이터를 설정한다.
	 *
	 * @param  		rs				ResultSet <br>
	 * @return		Hashtable		데이터 row
	 */
	public Hashtable setRowData(ResultSet rs) throws Err {

		String pos = MNAME+".setRowData()";

		setSTATSETROWDATATRY();			//통계정보설정
		
		int COL_TYPE;	
		String COL_NAME;
		String inputVar = null;
		Hashtable rowhash = new Hashtable();		

		try {

			ResultSetMetaData mrs = rs.getMetaData();			
			int colcnt = mrs.getColumnCount();
		
			for(int cnt = 1; cnt <= colcnt; cnt++) {		
				COL_TYPE = mrs.getColumnType(cnt);	
				COL_NAME = mrs.getColumnName(cnt).toUpperCase();	
				inputVar = rs.getString(cnt);
				if(inputVar == null || inputVar.equals("")) {
					inputVar = "";
					rowhash.put(COL_NAME,inputVar);
					continue;
				}	
				switch(COL_TYPE) {		    	
					case java.sql.Types.VARCHAR:
						rowhash.put(COL_NAME,inputVar);						
						break;
					case java.sql.Types.INTEGER:						
						//rowhash.put(COL_NAME,Integer.toString(rs.getInt(cnt)));	
						rowhash.put(COL_NAME,inputVar);						
						break;
					case java.sql.Types.DECIMAL:						
						//rowhash.put(COL_NAME,Float.toString(rs.getFloat(cnt)));						
						rowhash.put(COL_NAME,inputVar);						
						break;
					case java.sql.Types.DATE:
						rowhash.put(COL_NAME,inputVar);	
						break;
					case java.sql.Types.BLOB:
						rowhash.put(COL_NAME,inputVar);	
						break;
					case java.sql.Types.LONGVARCHAR:
						rowhash.put(COL_NAME,inputVar);	
						break;
					case java.sql.Types.CHAR:
						rowhash.put(COL_NAME,inputVar);	
						break;
					case java.sql.Types.NUMERIC:
						if(inputVar.indexOf(".") == -1) 
							rowhash.put(COL_NAME,inputVar);
						else						
							rowhash.put(COL_NAME,getDouToStr(inputVar));							
						break;
					default:
						rowhash.put(COL_NAME,inputVar);							
				
				}
			}
		} catch (SQLException sqle) {
			setSTATSETROWDATAFAIL();			//통계정보설정
			log(pos,"데이터 설정을 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sysmsg);
		} catch(Exception e) {
			setSTATSETROWDATAFAIL();			//통계정보설정
			log(pos,"데이터 설정을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
	
		setSTATSETROWDATASUCCESS();			//통계정보설정
		return rowhash;
	} 

	/**
	 *
 	 * 데이터를 설정한다.
	 *
	 * @param  		ds				DataSet
	 * 				rs				ResultSet <br>
	 */
	public void setRowData(DataSet ds, ResultSet rs) throws Err {

		String pos = MNAME+".setRowData()";

		setSTATSETROWDATATRY();			//통계정보설정
		
		int COL_RAW_TYPE;	
		String COL_NAME;
		String inputVar = null;
		Vector rowvec = new Vector();		
		try {

			int colcnt = ds.getColCnt();
		
			for(int cnt = 1; cnt <= colcnt; cnt++) {		
				COL_RAW_TYPE = parseInt(ds.getColRawType(cnt-1));	
				inputVar = rs.getString(cnt);
				if(inputVar == null || inputVar.equals("")) {
					inputVar = "";
					rowvec.addElement(inputVar);
					continue;
				}	
				switch(COL_RAW_TYPE) {		    	
					case java.sql.Types.VARCHAR:
						rowvec.addElement(inputVar);
						break;
					case java.sql.Types.INTEGER:						
						//rowhash.put(COL_NAME,Integer.toString(rs.getInt(cnt)));	
						rowvec.addElement(inputVar);
						break;
					case java.sql.Types.DECIMAL:						
						//rowhash.put(COL_NAME,Float.toString(rs.getFloat(cnt)));						
						rowvec.addElement(inputVar);
						break;
					case java.sql.Types.DATE:
						rowvec.addElement(inputVar);
						break;
					case java.sql.Types.BLOB:
						rowvec.addElement(inputVar);
						break;
					case java.sql.Types.LONGVARCHAR:
						rowvec.addElement(inputVar);	
						break;
					case java.sql.Types.CHAR:
						rowvec.addElement(inputVar);	
						break;
					case java.sql.Types.NUMERIC:
						if(inputVar.indexOf(".") == -1) 
							rowvec.addElement(inputVar);
						else						
							rowvec.addElement(getDouToStr(inputVar));
						break;
					default:
						rowvec.addElement(inputVar);
				
				}
			}

			//데이터셋에 로우 정보 추가
			ds.addRow(rowvec);

		} catch (SQLException sqle) {
			setSTATSETROWDATAFAIL();			//통계정보설정
			log(pos,"데이터 설정을 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","데이터 처리를 실패했습니다.",pos,sysmsg);
		} catch(Exception e) {
			setSTATSETROWDATAFAIL();			//통계정보설정
			log(pos,"데이터 설정을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}
	
		setSTATSETROWDATASUCCESS();			//통계정보설정
	} 

	/** 
	 * 
	 * 메타데이타를 설정한다.
  	 *
	 * @param  		ds				DataSet
	 *   			mrs				ResultSetMetaData
	 */
	public void setMetaData(DataSet ds, ResultSetMetaData mrs) throws Err {

		String pos = MNAME+".setMetaData()";

		setSTATSETMETADATATRY();			//통계정보설정

		if(ds == null) return;

		//메타데이터 정보 선언
		Vector metainfo = null;

		try {
			metainfo = new Vector();
			int colcnt = mrs.getColumnCount();
			String COL_NAME = null;
			String COL_TYPE = null;
			String COL_RAW_TYPE = null;
			String COL_SIZE = null;
			String COL_TNAME = null;
			String[] metaset = null;
			for(int cnt = 1; cnt <= colcnt; cnt++) {		
				metaset = new String[Code.DB_META_SIZE];
				COL_NAME = mrs.getColumnName(cnt).toUpperCase();	
				COL_TYPE = mrs.getColumnTypeName(cnt);	
				COL_RAW_TYPE = Integer.toString(mrs.getColumnType(cnt));	
				COL_SIZE = Integer.toString(mrs.getColumnDisplaySize(cnt));	
				try {
					COL_TNAME = mrs.getCatalogName(cnt);	
				} catch (Exception e1) {
					COL_TNAME = "";
				}
				metaset[Code.DB_META_COL_NM] = COL_NAME;
				metaset[Code.DB_META_COL_TYPE] = COL_TYPE;
				metaset[Code.DB_META_COL_RAW_TYPE] = COL_RAW_TYPE;
				metaset[Code.DB_META_COL_SIZE] = COL_SIZE;
				metaset[Code.DB_META_TNAME] = COL_TNAME;
				metainfo.addElement(metaset);	
			}
		} catch (SQLException sqle) {
			setSTATSETMETADATAFAIL();			//통계정보설정
			log(pos,"메타데이터 설정을 실패했습니다.\n"+sqle.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",sqle.toString());
			throw new Err("1","5","C","메타데이터 처리를 실패했습니다.",pos,sysmsg);
		} catch(Exception e) {
			setSTATSETMETADATAFAIL();			//통계정보설정
			log(pos,"메타데이터 설정을 실패했습니다.\n"+e.toString());	
			//시스템 에러 추가
			Hashtable sysmsg = new Hashtable();
			sysmsg.put("SYSMSG",e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.",pos,sysmsg);
		}

		//DataSet 메타정보 저장
		ds.setMetaInfo(metainfo);

		setSTATSETMETADATASUCCESS();			//통계정보설정
	}
}


