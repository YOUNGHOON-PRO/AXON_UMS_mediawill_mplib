package com.mp.util;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.mp.exception.*;
import com.mp.util.*;
import com.gauce.*;
import com.gauce.io.*;
import com.gauce.log.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : GauceUtil.java
 * Version      : 1.1
 * 작성일       : 2002/03/18
 * 작성자       : 오범석
 * 수정일       : 2002/10/20
 * 수정자       : 오범석
 * 설명         : Gauce 관련 Utility 메소드를 구현한다.
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class GauceUtil extends DBUtil {

    /*=============================================*/
    // Global Zone
    /*=============================================*/
	
	/** 모듈명 설정 */
	private String MNAME = "GauceUtil";


	/** 생성자 */
	public GauceUtil() {}

	/**
	 *
 	 * List DataSet을 리턴한다.	
	 * 다수의 데이터에 대한 데이터셋을 리턴한다(페이지를 설정한다).
	 *
	 * @param  		inputhash		리소스 관련 인자값(jdbc 환경값, SQL, CSQL, page,GauceDataSet...)  
	 * @return		Hashtable		DataSet(__EXEC_SQL은 수행 SQL문)
	 * @exception	Err				에러
	 */
	public Hashtable getGLData(Hashtable inputhash)  throws Err {

		//리스트 데이터 호출을 위한 변수
		int page = 1;
		int pagerow = 0;
		String jdbcenv = null;
		String SQL = null;
		String CSQL = null;
		ParamType param = null;
		ParamType cparam = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			if(inputhash.containsKey("jdbcenv")) {
				jdbcenv = (String)inputhash.get("jdbcenv");
			} else {
				jdbcenv = "Default";
			}
		
			if(inputhash.containsKey("SQL")) {
				SQL = (String)inputhash.get("SQL");
			} else {
				errflag = true;
				errstr += " [SQL] ";
			}

			if(inputhash.containsKey("CSQL")) {
				CSQL = (String)inputhash.get("CSQL");
			} else {
				errflag = true;
				errstr += " [CSQL] ";
			}

			if(inputhash.containsKey("param")) {
				param = (ParamType)inputhash.get("param");
			} else {
				errflag = true;
				errstr += " [param] ";
			}
			if(inputhash.containsKey("cparam")) {
				cparam = (ParamType)inputhash.get("cparam");
			} else {
				errflag = true;
				errstr += " [cparam] ";
			}

			if(inputhash.containsKey("p_page")) {
				page = Integer.parseInt((String)inputhash.get("p_page"));
			}

			if(inputhash.containsKey("p_pagerow")) {
				pagerow = Integer.parseInt((String)inputhash.get("p_pagerow"));
			}
		} catch(Exception e) {
			log(MNAME+".getGLData()","입력값 분리를 실패했습니다.\n"+e.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		}

		if(errflag) {
			log(MNAME+".getGLData()","입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.");
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
				ihash = getISet(jdbcenv,SQL);	
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(MNAME+".getGLData()","데이터 리소스 호출을 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(MNAME+".getGLData()","데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
			}
			
		
			/*==================================================*/
			//데이터 값 처리 인수를 생성한다.

			//skip할 페이지수
			if(pagerow == 0) pagerow = 1000;

			int skip = (page-1)*pagerow;

			//전체 데이터 수를 리턴한다.
			int totcnt = getTotalRow(jdbcenv,CSQL,cparam);
		
			//전체 페이지수를 리턴한다.	
			int totpage = 1;
			if(pagerow != 0) {
				if((totcnt%pagerow) == 0) {
					totpage = (totcnt/pagerow);
				} else {
					totpage = (totcnt/pagerow)+1;
				}
			}
	
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
				//GAUCE DataType을 생성한다.
				/*==================================================*/
				GauceDataSet dSet = new GauceDataSet();
				setGauceDataType(mrs,colcnt,dSet);

				/*==================================================*/
				//List DataSet을 생성한다.
				/*==================================================*/

				data = new Vector();
				Hashtable rowhash = null;
				String inputVar = null;
				GauceDataRow row = null;
				for(int j = 1; (j <= pagerow)&&(rs.next()); j++) {
					row = dSet.newDataRow();
					rowhash = setGauceDataRow(mrs,colcnt,row,rs);
					dSet.addDataRow(row);
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
				rethash.put("gauce",dSet);
		
				/*==================================================*/

			} catch(Err err) {
				log(MNAME+".getGLData()","쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(MNAME+".getGLData()","쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				throw new Err("1","5","C","데이터 처리를 실패했습니다.");
			} catch(Exception e) {
				log(MNAME+".getGLData()","쿼리 실행을 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
			}

		} catch (Err err) {
			throw err;
		} catch (Exception calle) {
			log(MNAME+".getGLData()","메소드 호출을 실패했습니다.\n"+calle.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				closeISet(ihash);
			} catch(Err err) {
				log(MNAME+".getGLData()","리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(MNAME+".getGLData()","리소스 해제를 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
			}
		}	
			
		return rethash;
	
	}

	/**
	 *
 	 * List DataSet을 리턴한다.	
	 * 다수의 데이터에 대한 데이터셋을 리턴한다(전체 리스트 출력).
	 *
	 * @param  		inputhash		리소스 관련 인자값(jdbc 환경값, SQL, GauceDataSet)  
	 * @return  	Hashtable 		DataSet(__EXEC_SQL은 수행 SQL문) 
	 * @exception	Err				에러
	 */
	public Hashtable getGALData(Hashtable inputhash)  throws Err {

		//리스트 데이터 호출을 위한 변수
		String jdbcenv = null;
		String SQL = null;
		ParamType param = null;

		//에러설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			if(inputhash.containsKey("jdbcenv")) {
				jdbcenv = (String)inputhash.get("jdbcenv");
			} else {
				jdbcenv = "Default";
			}
		
			if(inputhash.containsKey("SQL")) {
				SQL = (String)inputhash.get("SQL");
			} else {
				errflag = true;
				errstr += " [SQL] ";
			}
			if(inputhash.containsKey("param")) {
				param = (ParamType)inputhash.get("param");
			} else {
				errflag = true;
				errstr += " [param] ";
			}
		} catch(Exception e) {
			log(MNAME+".getGALData()","입력값 분리를 실패했습니다.\n"+e.toString());
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		}

		if(errflag) {
			log(MNAME+".getGALData()","입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리스트 데이터를 저장한다(리턴데이터).
		Hashtable dataSet = null;

		//ResultSet 선언
		ResultSet rs = null;

		try {

			try { //PreparedStatement를 호출한다.
				ihash = getISet(jdbcenv,SQL);	
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(MNAME+".getGALData()","데이터 리소스 호출을 실패했습니다.\n"+err.getEXStr());	
				throw err;
			} catch(Exception e) {
				log(MNAME+".getGALData()","데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
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
				//GAUCE DataType을 생성한다.
				/*==================================================*/
				GauceDataSet dSet = new GauceDataSet();
				setGauceDataType(mrs,colcnt,dSet);

				/*==================================================*/
				//List DataSet을 생성한다.
				/*==================================================*/
				Vector data = new Vector();
				Hashtable rowhash = null;
				String inputVar = null;
				GauceDataRow row = null;
				while(rs.next()) {
					row = dSet.newDataRow();
					rowhash = setGauceDataRow(mrs,colcnt,row,rs);
					data.addElement(rowhash);
					dSet.addDataRow(row);
				}

				//데이터 셋을 설정한다.
				dataSet = new Hashtable();
				dataSet.put("data",data);
				dataSet.put("__EXEC_SQL",SQL);
				dataSet.put("gauce",dSet);

				/*==================================================*/

			} catch(Err err) {
				log(MNAME+".getGALData()","쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(MNAME+".getGALData()","쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				throw new Err("1","5","C","데이터 처리를 실패했습니다.");
			} catch(Exception e) {
				log(MNAME+".getGALData()","쿼리 실행을 실패했습니다.\n"+e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
			}
		} catch (Err err) {
			throw err;
		} catch (Exception calle) {
			log(MNAME+".getGALData()","메소드 호출을 실패했습니다.\n"+calle.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				closeISet(ihash);
			} catch(Err err) {
				log(MNAME+".getGALData()","리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(MNAME+".getGALData()","리소스 해제를 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
			}
		}	
			
		return dataSet;

	}

	/**
	 *
 	 * Info DataSet을 리턴한다.	
	 * 한건의 데이터에 대한 데이터셋을 리턴한다.
	 *
	 * @param  		inputhash		리소스 관련 인자값(poolname, SQL, page, GauceDataSet ...)  		
	 * @return  	Hashtable		Data Set(__EXEC_SQL은 수행 SQL문, __EMPTY_YN은 데이터 존재 여부)
	 * @exception	Err				에러
	 */
	public Hashtable getGIData(Hashtable inputhash)  throws Err {

		//데이터 호출을 위한 변수
		String jdbcenv = null;
		String SQL = null;
		ParamType param = null;

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			if(inputhash.containsKey("jdbcenv")) {
				jdbcenv = (String)inputhash.get("jdbcenv");
			} else {
				jdbcenv = "Default";
			}
		
			if(inputhash.containsKey("SQL")) {
				SQL = (String)inputhash.get("SQL");
			} else {
				errflag = true;
				errstr += " [SQL] ";
			}
			if(inputhash.containsKey("param")) {
				param = (ParamType)inputhash.get("param");
			}
		} catch(Exception e) {
			log(MNAME+".getGIData()","입력값 분리를 실패했습니다.\n"+e.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
		}

		if(errflag) {
			log(MNAME+".getGIData()","입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		PreparedStatement pstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable dataSet = null;

		//ResultSet 선언
		ResultSet rs = null;

		try {

			try { //PreparedStatement를 호출한다.
				ihash = getISet(jdbcenv,SQL);	
				pstmt = (PreparedStatement)ihash.get("pstmt");
			} catch(Err err) {
				log(MNAME+".getGIData()","데이터 리소스 호출을 실패했습니다.\n"+err.toString());
				throw err;
			} catch(Exception e) {
				log(MNAME+".getGIData()","데이터 리소스 호출을 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
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
				//GAUCE DataType을 생성한다.
				/*==================================================*/
				GauceDataSet dSet = new GauceDataSet();
				setGauceDataType(mrs,colcnt,dSet);

				/*==================================================*/
				//Info DataSet을 생성한다.
				/*==================================================*/
				Hashtable rethash = new Hashtable();
				GauceDataRow row = null;
				if(rs.next()) {
					row = dSet.newDataRow();
					rethash = setGauceDataRow(mrs,colcnt,row,rs);
					dSet.addDataRow(row);
            	}

				dataSet = new Hashtable();
				dataSet.put("data",rethash);
				if(rethash.size() == 0) {		//쿼리에 대한 데이터 값 없음
					rethash.put("__EMPTY_YN","Y");
				} else {
					rethash.put("__EMPTY_YN","N");
				}
				dataSet.put("a",rethash);
				dataSet.put("__EXEC_SQL",SQL);
				dataSet.put("gauce",dSet);

			} catch(Err err) {
				log(MNAME+".getGIData()","쿼리 실행을 실패했습니다.\n"+SQL+"\n"+err.getEXStr());
				throw err;
			} catch (SQLException sqle) {
				log(MNAME+".getGIData()","쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				throw new Err("1","5","C","데이터 처리를 실패했습니다.");
			} catch(Exception e) {
				log(MNAME+".getGIData()","쿼리 실행을 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
			}
		} catch (Err err) {
			throw err;
		} catch (Exception calle) {
			log(MNAME+".getGIData()","메소드 호출을 실패했습니다.\n"+calle.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		} finally {
			//데이터 리소스를 닫는다.
			try {
				if(ihash != null && rs != null) ihash.put("rs",rs);
				closeISet(ihash);
			} catch(Err err) {
				log(MNAME+".getGIData()","리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(MNAME+".getGIData()","리소스 해제를 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
			}
		}	
			
		return dataSet;
	
	}

	/**
	 *
 	 * 데이터를 조작한다(저장 프로시져를 호출한다).	
	 * 저장 프로시져를 실행한다.
	 *
	 * @param  		inputhash 		리소스 관련 인자값<br>
	 *								(poolname, SQL, param, type, count,GauceDataSet ...)
	 * @return		Hashtable		결과값(조작여부, 조각결과수, ...), __EXEC_SQL은 수행 SQL문	
	 * @exception	Err				에러
	 */
	public Hashtable callGSData(Hashtable inputhash)  throws Err {

		//데이터 호출을 위한 변수
		String jdbcenv = null;		//jdbc 환경값
		String SQL = null;			//query
		String type = null;			//유형(insert, update, delete) 
		int count = 0;				//처리건수
		Hashtable param = null;		//파라미터
		int retparam = 0;  			//리턴파라미터

		//에러 설정
		boolean errflag = false;
		String errstr = "";

		//인자값을 분리한다.
		try {
			if(inputhash.containsKey("jdbcenv")) {
				jdbcenv = (String)inputhash.get("jdbcenv");
			} else {
				jdbcenv = "Default";
			}
			if(inputhash.containsKey("SQL")) {
				SQL = (String)inputhash.get("SQL");
			} else {
				errflag = true;
				errstr += " [SQL] ";
			}
			if(inputhash.containsKey("type")) {
				type = (String)inputhash.get("type");
			}
			if(inputhash.containsKey("count")) {
				count = Integer.parseInt((String)inputhash.get("count"));
			}
			if(inputhash.containsKey("param")) {
				param = (Hashtable)inputhash.get("param");
			}
			if(inputhash.containsKey("retparam")) {
				retparam = Integer.parseInt((String)inputhash.get("retparam"));
			}
		} catch(Exception e) {
			log(MNAME+".callGSData()","입력값 분리를 실패했습니다.\n"+e.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
		}

		if(errflag) {
			log(MNAME+".callGSData()","입력값 분리를 실패했습니다.\n다음 정보를 확인하세요.\n"+errstr);	
			throw new Err("1","5","E","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		}

		//데이터 호출을 위한 변수
		Hashtable ihash = null;
		CallableStatement cstmt = null;

		//리턴 데이터를 저장한다.
		Hashtable dataSet = null;

		try {

			try { 	//CallableStatement를 호출한다.
				ihash = getSSet(jdbcenv,SQL);	
				cstmt = (CallableStatement)ihash.get("cstmt");
			} catch(Err err) {
				log(MNAME+".callGSData()","데이터 리소스 호출을 실패했습니다.\n"+err.toString());	
				throw err;
			} catch(Exception e) {
				log(MNAME+".callGSData()","데이터 리소스 호출을 실패했습니다.\n"+e.toString());
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
			}
		
			/*==================================================*/
			String yn = null;	//성공여부

			try {
	
				int i;
	
				//파라미터를 설정한다.
				if(param != null) {
					for(i = 1; i <= param.size(); i++) { 
						if(i < retparam) {
							cstmt.setString(i,(String)param.get(Integer.toString(i)));
						} else {
							cstmt.setString(i,(String)param.get(Integer.toString(i)));
							cstmt.registerOutParameter(i,java.sql.Types.VARCHAR);
						}
					}
				}


				cstmt.executeUpdate();

				/*==================================================*/
				//GAUCE DataType을 생성한다.
				/*==================================================*/
				GauceDataSet dSet = new GauceDataSet();
				for(i = 1; i <= param.size(); i++) {		
					dSet.addDataColumn(new GauceDataColumn(Integer.toString(i),GauceDataColumn.TB_STRING));
				}
			
				//리턴데이터를 설정한다.
				GauceDataRow row = dSet.newDataRow();
				Hashtable rethash = new Hashtable();
				if(param != null) {
					for(i = 1; i <= param.size(); i++) {
						if(i >= retparam) {	
							rethash.put(Integer.toString(i),
										setNull(cstmt.getString(i)));
							row.addColumnValue(setNull(cstmt.getString(i)));
						}
					}
					dSet.addDataRow(row);
				}

				dataSet = new Hashtable();
				dataSet.put("data",rethash);
				dataSet.put("__EXEC_SQL",SQL);
				dataSet.put("gauce",dSet);

			} catch (SQLException sqle) {
				log(MNAME+".callGSData()","쿼리 실행을 실패했습니다.\n"+SQL+"\n"+sqle.toString());	
				throw new Err("1","5","C","데이터 처리를 실패했습니다.");
			} catch(Exception e) {
				log(MNAME+".callGSData()","쿼리 실행을 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
			}
		} catch (Err err) {
			throw err;
		} catch (Exception calle) {
			log(MNAME+".callGSData()","메소드 호출을 실패했습니다.\n"+calle.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
		} finally {
			//데이터 리소스를 닫는다.
			try {
				closeISet(ihash);
			} catch(Err err) {
				log(MNAME+".callGSData()","리소스 해제를 실패했습니다.\n"+err.getEXStr());
				throw err;
			} catch(Exception e) {
				log(MNAME+".callGSData()","리소스 해제를 실패했습니다.\n"+e.toString());	
				throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하십시요.");
			}
		}	

		return dataSet;
	
	}

	/**
	 *
 	 * 가우스 데이터셋을 설정한다.
	 *
	 * @param  		mrs				메타 데이타 <br>
	 *   			colcnt			컬럼수 <br>
	 *   			dataSet			Gauce DataSet <br>
	 */
	public void setGauceDataType(ResultSetMetaData mrs, int colcnt,
								 GauceDataSet dSet) throws Err {

		int COL_TYPE;
		String COL_NAME;
		int COL_SIZE;
		
		try {

			for(int cnt = 1; cnt <= colcnt; cnt++) {		
				COL_TYPE = mrs.getColumnType(cnt);	
				COL_NAME = mrs.getColumnName(cnt);	
				COL_SIZE = mrs.getColumnDisplaySize(cnt);
				switch(COL_TYPE) {		    	
					case java.sql.Types.VARCHAR:
						//log(COL_NAME+" : 타입 : VARCHAR");
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING,COL_SIZE,0));
						break;
					case java.sql.Types.INTEGER:
						//log(COL_NAME+" : 타입 : INTEGER");
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_INT,COL_SIZE,0));
						//dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING,COL_SIZE,0));
						break;
					case java.sql.Types.DECIMAL:
						//log(COL_NAME+" : 타입 : DECIMAL");
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_DECIMAL,COL_SIZE,0));
						//dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING,COL_SIZE,0));
						break;
					case java.sql.Types.DATE:
						//log(COL_NAME+" : 타입 : DATE");
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING));
						break;
					case java.sql.Types.BLOB:
						//dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_BLOB));
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING,COL_SIZE,0));
						break;
					case java.sql.Types.LONGVARCHAR:
						//log(COL_NAME+" : 타입 : LONGVARCHAR");
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING,COL_SIZE,0));
						break;
					case java.sql.Types.CHAR:
						//log(COL_NAME+" : 타입 : CHAR");
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING,COL_SIZE,0));
						break;
					case java.sql.Types.NUMERIC:
						//log(COL_NAME+" : 타입 : NUMERIC");
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_DECIMAL,COL_SIZE,0));
						//dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_NUMBER,COL_SIZE,0));
						break;
					default:
						dSet.addDataColumn(new GauceDataColumn(COL_NAME,GauceDataColumn.TB_STRING,COL_SIZE,0));
				
				}
			}
		} catch (SQLException sqle) {
			log(MNAME+".setGauceDataType()","데이터 타입 설정을 실패했습니다.\n"+sqle.toString());	
			throw new Err("1","5","C","데이터 처리를 실패했습니다.");
		} catch(Exception e) {
			log(MNAME+".setGauceDataType()","데이터 타입 설정을 실패했습니다.\n"+e.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
		}
	}


	/**
	 *
 	 * 가우스 데이터셋을 설정한다.
	 *
	 * @param  		mrs				메타 데이타 <br>
	 *   			colcnt			컬럼수 <br>
	 *   			row				GauceDataRow <br>
	 *   			rs				ResultSet <br>
	 */
	public Hashtable setGauceDataRow(ResultSetMetaData mrs, int colcnt,
								 GauceDataRow row, ResultSet rs) throws Err {

		int COL_TYPE;
		String COL_NAME;
		String inputVar = null;
		Hashtable rowhash = new Hashtable();		
		try {

			for(int cnt = 1; cnt <= colcnt; cnt++) {		
				COL_TYPE = mrs.getColumnType(cnt);	
				COL_NAME = mrs.getColumnName(cnt).toUpperCase();	
				inputVar = rs.getString(cnt);
				if(inputVar == null || inputVar.equals("")) inputVar = "";
				switch(COL_TYPE) {		    	
					case java.sql.Types.VARCHAR:											
						row.addColumnValue(inputVar);
						rowhash.put(COL_NAME,inputVar);		
						break;
					case java.sql.Types.INTEGER:
						if(inputVar.equals("")) {			
							row.addColumnValue(0);
							rowhash.put(COL_NAME,"");								
						} else {
							int var = rs.getInt(cnt);
							row.addColumnValue(var);
							rowhash.put(COL_NAME,Integer.toString(var));									
						}
						break;
					case java.sql.Types.DECIMAL:
						if(inputVar.equals("")) {
							row.addColumnValue(0);
							rowhash.put(COL_NAME,"");								
						} else {
							float var = rs.getFloat(cnt);
							row.addColumnValue(var);
							rowhash.put(COL_NAME,Float.toString(var));			
						}							
						break;
					case java.sql.Types.DATE:
						row.addColumnValue(inputVar);
						rowhash.put(COL_NAME,inputVar);						
						break;
					case java.sql.Types.BLOB:
						row.addColumnValue(inputVar);
						rowhash.put(COL_NAME,inputVar);						
						break;
					case java.sql.Types.LONGVARCHAR:
						row.addColumnValue(inputVar);
						rowhash.put(COL_NAME,inputVar);						
						break;
					case java.sql.Types.CHAR:
						row.addColumnValue(inputVar);
						rowhash.put(COL_NAME,inputVar);						
						break;
					case java.sql.Types.NUMERIC:
						if(inputVar.equals("")) {
							row.addColumnValue(0);
							rowhash.put(COL_NAME,"");
						} else {
							double var = rs.getDouble(cnt);
							if(inputVar.indexOf(".") == -1) 
								rowhash.put(COL_NAME,inputVar);
							else
								rowhash.put(COL_NAME,Double.toString(var));
							row.addColumnValue(var);
						}														
						break;
					default:
						row.addColumnValue(inputVar);
						rowhash.put(COL_NAME,inputVar);
						
				}
			}
		} catch (SQLException sqle) {
			log(MNAME+".setGauceDataRow()","데이터 설정을 실패했습니다.\n"+sqle.toString());	
			throw new Err("1","5","C","데이터 처리를 실패했습니다.");
		} catch(Exception e) {
			log(MNAME+".setGauceDataRow()","데이터 설정을 실패했습니다.\n"+e.toString());	
			throw new Err("1","5","0","시스템 에러입니다.\\n관리자에게 문의하시기 바랍니다.");
		}
	
		return rowhash;
	}

	
}

