package com.mp.util;

import java.util.*;
import com.mp.exception.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : StatOper.java
 * Version      : 1.1
 * 작성일       : 2003/12/29
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 *
 * 설명         : dev_stand 사용의 통계 정보를 처리하는 클래스
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class StatOper extends StatData {

	/** 모듈명 설정 */
	private String MNAME = "StatOper";

	/** 생성자 */
	public StatOper() {
		//CONNECTION 종류
		STAT_CON_KIND = new Hashtable();
		//LOOKUP 종류
		STAT_LOOKUP_KIND = new Hashtable();
	}

	/**
	 * DB 접속시도 횟수 설정
	 */
	public void setSTATCONTRY() {
		++STAT_CON_TRY;
	}

	/**
	 * DB 접속시도 횟수 리턴
	 */
	public long getSTATCONTRY() {
		return STAT_CON_TRY;
	}

	/**
	 * DB 접속시도 성공 횟수 설정
	 */
	public void setSTATCONSUCCESS() {
		++STAT_CON_SUCCESS;
	}

	/**
	 * DB 접속시도 성공 횟수 리턴
	 */
	public long getSTATCONSUCCESS() {
		return STAT_CON_SUCCESS;
	}

	/**
	 * DB 접속시도 실패 횟수 설정
	 */
	public void setSTATCONFAIL() {
		++STAT_CON_FAIL;
	}

	/**
	 * DB 접속시도 실패 횟수 리턴
	 */
	public long getSTATCONFAIL() {
		return STAT_CON_FAIL;
	}

	/**
	 * DB(POOL) 접속시도 횟수 설정
	 */
	public void setSTATCONPOOLTRY() {
		++STAT_CON_POOL_TRY;
	}

	/**
	 * DB(POOL) 접속시도 횟수 리턴
	 */
	public long getSTATCONPOOLTRY() {
		return STAT_CON_POOL_TRY;
	}

	/**
	 * DB(POOL) 접속 성공 횟수 설정
	 */
	public void setSTATCONPOOLSUCCESS() {
		++STAT_CON_POOL_SUCCESS;
	}

	/**
	 * DB(POOL) 접속 성공 횟수 리턴
	 */
	public long getSTATCONPOOLSUCCESS() {
		return STAT_CON_POOL_SUCCESS;
	}

	/**
	 * DB(POOL) 접속 실패 횟수 설정
	 */
	public void setSTATCONPOOLFAIL() {
		++STAT_CON_POOL_FAIL;
	}

	/**
	 * DB(POOL) 접속 실패 횟수 리턴
	 */
	public long getSTATCONPOOLFAIL() {
		return STAT_CON_POOL_FAIL;
	}

	/**
	 * DB(JDBC) 접속시도 횟수 설정
	 */
	public void setSTATCONJDBCTRY() {
		++STAT_CON_JDBC_TRY;
	}

	/**
	 * DB(JDBC) 접속시도 횟수 리턴
	 */
	public long getSTATCONJDBCTRY() {
		return STAT_CON_JDBC_TRY;
	}

	/**
	 * DB(JDBC) 접속 성공 횟수 설정
	 */
	public void setSTATCONJDBCSUCCESS() {
		++STAT_CON_JDBC_SUCCESS;
	}

	/**
	 * DB(JDBC) 접속 성공 횟수 리턴
	 */
	public long getSTATCONJDBCSUCCESS() {
		return STAT_CON_JDBC_SUCCESS;
	}

	/**
	 * DB(JDBC) 접속 실패 횟수 설정
	 */
	public void setSTATCONJDBCFAIL() {
		++STAT_CON_JDBC_FAIL;
	}

	/**
	 * DB(JDBC) 접속 실패 횟수 리턴
	 */
	public long getSTATCONJDBCFAIL() {
		return STAT_CON_JDBC_FAIL;
	}

	/**
	 * DB 접속 해제 시도 횟수 설정
	 */
	public void setSTATCONCLOSETRY() {
		++STAT_CON_CLOSE_TRY;
	}

	/**
	 * DB 접속 해제 시도 횟수 리턴
	 */
	public long getSTATCONCLOSETRY() {
		return STAT_CON_CLOSE_TRY;
	}

	/**
	 * DB 접속 해제 성공 횟수 설정
	 */
	public void setSTATCONCLOSESUCCESS() {
		++STAT_CON_CLOSE_SUCCESS;
	}

	/**
	 * DB 접속 해제 성공 횟수 리턴
	 */
	public long getSTATCONCLOSESUCCESS() {
		return STAT_CON_CLOSE_SUCCESS;
	}

	/**
	 * DB 접속 해제 실패 횟수 설정
	 */
	public void setSTATCONCLOSEFAIL() {
		++STAT_CON_CLOSE_FAIL;
	}

	/**
	 * DB 접속 해제 실패 횟수 리턴
	 */
	public long getSTATCONCLOSEFAIL() {
		return STAT_CON_CLOSE_FAIL;
	}


	/**
	 * DB환경 이름별 통계 정보 설정
	 */
	public void setSTATCONKIND(String jdbcenv, int stat_kind) {
		if(STAT_CON_KIND.containsKey(jdbcenv)) {
			long[] stat_data = (long[])STAT_CON_KIND.get(jdbcenv);
			++stat_data[stat_kind];	
		} else {
			long[] stat_data = new long[3];	
			stat_data[Code.STAT_TRY] = 0;
			stat_data[Code.STAT_SUCCESS] = 0;
			stat_data[Code.STAT_FAIL] = 0;

			++stat_data[stat_kind];

			STAT_CON_KIND.put(jdbcenv,stat_data);
		}
	}

	/**
	 * DB환경 이름별 통계 정보 리턴
	 */
	public Hashtable getSTATCONKIND() {
		return STAT_CON_KIND;
	}

	/**
	 * INFO SELECT 시도 횟수 설정
	 */
	public void setSTATSQLINFOTRY() {
		++STAT_SQL_INFO_TRY;
	}

	/**
	 * INFO SELECT 시도 횟수 리턴
	 */
	public long getSTATSQLINFOTRY() {
		return STAT_SQL_INFO_TRY;
	}

	/**
	 * INFO SELECT 성공 횟수 설정
	 */
	public void setSTATSQLINFOSUCCESS() {
		++STAT_SQL_INFO_SUCCESS;
	}

	/**
	 * INFO SELECT 성공 횟수 리턴
	 */
	public long getSTATSQLINFOSUCCESS() {
		return STAT_SQL_INFO_SUCCESS;
	}

	/**
	 * INFO SELECT 실패 횟수 설정
	 */
	public void setSTATSQLINFOFAIL() {
		++STAT_SQL_INFO_FAIL;
	}

	/**
	 * INFO SELECT 실패 횟수 리턴
	 */
	public long getSTATSQLINFOFAIL() {
		return STAT_SQL_INFO_FAIL;
	}

	/**
	 * LIST SELECT 시도 횟수 설정
	 */
	public void setSTATSQLLISTTRY() {
		++STAT_SQL_LIST_TRY;
	}

	/**
	 * LIST SELECT 시도 횟수 리턴
	 */
	public long getSTATSQLLISTTRY() {
		return STAT_SQL_LIST_TRY;
	}

	/**
	 * LIST SELECT 성공 횟수 설정
	 */
	public void setSTATSQLLISTSUCCESS() {
		++STAT_SQL_LIST_SUCCESS;
	}

	/**
	 * LIST SELECT 성공 횟수 리턴
	 */
	public long getSTATSQLLISTSUCCESS() {
		return STAT_SQL_LIST_SUCCESS;
	}

	/**
	 * LIST SELECT 실패 횟수 설정
	 */
	public void setSTATSQLLISTFAIL() {
		++STAT_SQL_LIST_FAIL;
	}

	/**
	 * LIST SELECT 실패 횟수 리턴
	 */
	public long getSTATSQLLISTFAIL() {
		return STAT_SQL_LIST_FAIL;
	}

	/**
	 * COUNT SELECT 시도 횟수 설정
	 */
	public void setSTATSQLCOUNTTRY() {
		++STAT_SQL_COUNT_TRY;
	}

	/**
	 * COUNT SELECT 시도 횟수 리턴
	 */
	public long getSTATSQLCOUNTTRY() {
		return STAT_SQL_COUNT_TRY;
	}

	/**
	 * COUNT SELECT 성공 횟수 설정
	 */
	public void setSTATSQLCOUNTSUCCESS() {
		++STAT_SQL_COUNT_SUCCESS;
	}

	/**
	 * COUNT SELECT 성공 횟수 리턴
	 */
	public long getSTATSQLCOUNTSUCCESS() {
		return STAT_SQL_COUNT_SUCCESS;
	}

	/**
	 * COUNT SELECT 실패 횟수 설정
	 */
	public void setSTATSQLCOUNTFAIL() {
		++STAT_SQL_COUNT_FAIL;
	}

	/**
	 * COUNT SELECT 실패 횟수 리턴
	 */
	public long getSTATSQLCOUNTFAIL() {
		return STAT_SQL_COUNT_FAIL;
	}

	/**
	 * ALIST SELECT 시도 횟수 설정
	 */
	public void setSTATSQLALISTTRY() {
		++STAT_SQL_ALIST_TRY;
	}

	/**
	 * ALIST SELECT 시도 횟수 리턴
	 */
	public long getSTATSQLALISTTRY() {
		return STAT_SQL_ALIST_TRY;
	}

	/**
	 * ALIST SELECT 성공 횟수 설정
	 */
	public void setSTATSQLALISTSUCCESS() {
		++STAT_SQL_ALIST_SUCCESS;
	}

	/**
	 * ALIST SELECT 성공 횟수 리턴
	 */
	public long getSTATSQLALISTSUCCESS() {
		return STAT_SQL_ALIST_SUCCESS;
	}

	/**
	 * ALIST SELECT 실패 횟수 설정
	 */
	public void setSTATSQLALISTFAIL() {
		++STAT_SQL_ALIST_FAIL;
	}

	/**
	 * ALIST SELECT 실패 횟수 리턴
	 */
	public long getSTATSQLALISTFAIL() {
		return STAT_SQL_ALIST_FAIL;
	}

	/**
	 * MODIFY 시도 횟수 설정
	 */
	public void setSTATSQLMODIFYTRY() {
		++STAT_SQL_MODIFY_TRY;
	}

	/**
	 * MODIFY 시도 횟수 리턴
	 */
	public long getSTATSQLMODIFYTRY() {
		return STAT_SQL_MODIFY_TRY;
	}

	/**
	 * MODIFY 성공 횟수 설정
	 */
	public void setSTATSQLMODIFYSUCCESS() {
		++STAT_SQL_MODIFY_SUCCESS;
	}

	/**
	 * MODIFY 성공 횟수 리턴
	 */
	public long getSTATSQLMODIFYSUCCESS() {
		return STAT_SQL_MODIFY_SUCCESS;
	}

	/**
	 * MODIFY 실패 횟수 설정
	 */
	public void setSTATSQLMODIFYFAIL() {
		++STAT_SQL_INSERT_FAIL;
	}

	/**
	 * MODIFY 실패 횟수 리턴
	 */
	public long getSTATSQLMODIFYFAIL() {
		return STAT_SQL_MODIFY_FAIL;
	}

	/**
	 * INSERT 시도 횟수 설정
	 */
	public void setSTATSQLINSERTTRY() {
		++STAT_SQL_INSERT_TRY;
	}

	/**
	 * INSERT 시도 횟수 리턴
	 */
	public long getSTATSQLINSERTTRY() {
		return STAT_SQL_INSERT_TRY;
	}

	/**
	 * INSERT 성공 횟수 설정
	 */
	public void setSTATSQLINSERTSUCCESS() {
		++STAT_SQL_INSERT_SUCCESS;
	}

	/**
	 * INSERT 성공 횟수 리턴
	 */
	public long getSTATSQLINSERTSUCCESS() {
		return STAT_SQL_INSERT_SUCCESS;
	}

	/**
	 * INSERT 실패 횟수 설정
	 */
	public void setSTATSQLINSERTFAIL() {
		++STAT_SQL_INSERT_FAIL;
	}

	/**
	 * INSERT 실패 횟수 리턴
	 */
	public long getSTATSQLINSERTFAIL() {
		return STAT_SQL_INSERT_FAIL;
	}

	/**
	 * UPDATE 시도 횟수 설정
	 */
	public void setSTATSQLUPDATETRY() {
		++STAT_SQL_UPDATE_TRY;
	}

	/**
	 * UPDATE 시도 횟수 리턴
	 */
	public long getSTATSQLUPDATETRY() {
		return STAT_SQL_UPDATE_TRY;
	}

	/**
	 * UPDATE 성공 횟수 설정
	 */
	public void setSTATSQLUPDATESUCCESS() {
		++STAT_SQL_UPDATE_SUCCESS;
	}

	/**
	 * UPDATE 성공 횟수 리턴
	 */
	public long getSTATSQLUPDATESUCCESS() {
		return STAT_SQL_UPDATE_SUCCESS;
	}

	/**
	 * UPDATE 실패 횟수 설정
	 */
	public void setSTATSQLUPDATEFAIL() {
		++STAT_SQL_UPDATE_FAIL;
	}

	/**
	 * UPDATE 실패 횟수 리턴
	 */
	public long getSTATSQLUPDATEFAIL() {
		return STAT_SQL_UPDATE_FAIL;
	}

	/**
	 * DELETE 시도 횟수 설정
	 */
	public void setSTATSQLDELETETRY() {
		++STAT_SQL_DELETE_TRY;
	}

	/**
	 * DELETE 시도 횟수 리턴
	 */
	public long getSTATSQLDELETETRY() {
		return STAT_SQL_DELETE_TRY;
	}

	/**
	 * DELETE 성공 횟수 설정
	 */
	public void setSTATSQLDELETESUCCESS() {
		++STAT_SQL_DELETE_SUCCESS;
	}

	/**
	 * DELETE 성공 횟수 리턴
	 */
	public long getSTATSQLDELETESUCCESS() {
		return STAT_SQL_DELETE_SUCCESS;
	}

	/**
	 * DELETE 실패 횟수 설정
	 */
	public void setSTATSQLDELETEFAIL() {
		++STAT_SQL_DELETE_FAIL;
	}

	/**
	 * DELETE 실패 횟수 리턴
	 */
	public long getSTATSQLDELETEFAIL() {
		return STAT_SQL_DELETE_FAIL;
	}

	/**
	 * PROCEDURE 시도 횟수 설정
	 */
	public void setSTATSQLPROCTRY() {
		++STAT_SQL_PROC_TRY;
	}

	/**
	 * PROCEDURE 시도 횟수 리턴
	 */
	public long getSTATSQLPROCTRY() {
		return STAT_SQL_PROC_TRY;
	}

	/**
	 * PROCEDURE 성공 횟수 설정
	 */
	public void setSTATSQLPROCSUCCESS() {
		++STAT_SQL_PROC_SUCCESS;
	}

	/**
	 * PROCEDURE 성공 횟수 리턴
	 */
	public long getSTATSQLPROCSUCCESS() {
		return STAT_SQL_PROC_SUCCESS;
	}

	/**
	 * PROCEDURE 실패 횟수 설정
	 */
	public void setSTATSQLPROCFAIL() {
		++STAT_SQL_PROC_FAIL;
	}

	/**
	 * PROCEDURE 실패 횟수 리턴
	 */
	public long getSTATSQLPROCFAIL() {
		return STAT_SQL_PROC_FAIL;
	}

	/**
	 * LOOKUP 통계 설정
	 */
	public void setSTATLOOKUPKIND(String jndi_nm, int stat_kind) {
		if(STAT_LOOKUP_KIND.containsKey(jndi_nm)) {
			long[] stat_data = (long[])STAT_LOOKUP_KIND.get(jndi_nm);
			++stat_data[stat_kind];	
		} else {
			long[] stat_data = new long[3];	
			stat_data[Code.STAT_TRY] = 0;
			stat_data[Code.STAT_SUCCESS] = 0;
			stat_data[Code.STAT_FAIL] = 0;

			++stat_data[stat_kind];

			STAT_LOOKUP_KIND.put(jndi_nm,stat_data);
		}

	}

	/**
	 * LOOKUP 통계 리턴
	 */
	public Hashtable getSTATLOOKUPKIND() {
		return STAT_LOOKUP_KIND;
	}

	/**
	 * getConn 시도 횟수 설정
	 */
	public void setSTATGETCONNTRY() {
		++STAT_GETCONN_TRY;
	}

	/**
	 * getConn 시도 횟수 리턴 
	 */
	public long getSTATGETCONNTRY() {
		return STAT_GETCONN_TRY;
	}

	/**
	 * getConn 성공 횟수 설정
	 */
	public void setSTATGETCONNSUCCESS() {
		++STAT_GETCONN_SUCCESS;
	}

	/**
	 * getConn 성공 횟수 리턴
	 */
	public long getSTATGETCONNSUCCESS() {
		return STAT_GETCONN_SUCCESS;
	}

	/**
	 * getConn 실패 횟수 설정
	 */
	public void setSTATGETCONNFAIL() {
		++STAT_GETCONN_FAIL;
	}

	/**
	 * getConn 실패 횟수 리턴
	 */
	public long getSTATGETCONNFAIL() {
		return STAT_GETCONN_FAIL;
	}

	/**
	 * getConn1 시도 횟수 설정
	 */
	public void setSTATGETCONN1TRY() {
		++STAT_GETCONN1_TRY;
	}

	/**
	 * getConn1 시도 횟수 리턴 
	 */
	public long getSTATGETCONN1TRY() {
		return STAT_GETCONN1_TRY;
	}

	/**
	 * getConn1 성공 횟수 설정
	 */
	public void setSTATGETCONN1SUCCESS() {
		++STAT_GETCONN1_SUCCESS;
	}

	/**
	 * getConn1 성공 횟수 리턴
	 */
	public long getSTATGETCONN1SUCCESS() {
		return STAT_GETCONN1_SUCCESS;
	}

	/**
	 * getConn1 실패 횟수 설정
	 */
	public void setSTATGETCONN1FAIL() {
		++STAT_GETCONN1_FAIL;
	}

	/**
	 * getConn1 실패 횟수 리턴
	 */
	public long getSTATGETCONN1FAIL() {
		return STAT_GETCONN1_FAIL;
	}

	/**
	 * getConn2 시도 횟수 설정
	 */
	public void setSTATGETCONN2TRY() {
		++STAT_GETCONN2_TRY;
	}

	/**
	 * getConn2 시도 횟수 리턴 
	 */
	public long getSTATGETCONN2TRY() {
		return STAT_GETCONN2_TRY;
	}

	/**
	 * getConn2 성공 횟수 설정
	 */
	public void setSTATGETCONN2SUCCESS() {
		++STAT_GETCONN2_SUCCESS;
	}

	/**
	 * getConn2 성공 횟수 리턴
	 */
	public long getSTATGETCONN2SUCCESS() {
		return STAT_GETCONN2_SUCCESS;
	}

	/**
	 * getConn2 실패 횟수 설정
	 */
	public void setSTATGETCONN2FAIL() {
		++STAT_GETCONN2_FAIL;
	}

	/**
	 * getConn2 실패 횟수 리턴
	 */
	public long getSTATGETCONN2FAIL() {
		return STAT_GETCONN2_FAIL;
	}

	/**
	 * getISet 시도 횟수 설정
	 */
	public void setSTATGETISETTRY() {
		++STAT_GETISET_TRY;
	}

	/**
	 * getISet 시도 횟수 리턴 
	 */
	public long getSTATGETISETTRY() {
		return STAT_GETISET_TRY;
	}

	/**
	 * getISet 성공 횟수 설정
	 */
	public void setSTATGETISETSUCCESS() {
		++STAT_GETISET_SUCCESS;
	}

	/**
	 * getISet 성공 횟수 리턴
	 */
	public long getSTATGETISETSUCCESS() {
		return STAT_GETISET_SUCCESS;
	}

	/**
	 * getISet 실패 횟수 설정
	 */
	public void setSTATGETISETFAIL() {
		++STAT_GETISET_FAIL;
	}

	/**
	 * getISet 실패 횟수 리턴
	 */
	public long getSTATGETISETFAIL() {
		return STAT_GETISET_FAIL;
	}

	/**
	 * getMSet 시도 횟수 설정
	 */
	public void setSTATGETMSETTRY() {
		++STAT_GETMSET_TRY;
	}

	/**
	 * getMSet 시도 횟수 리턴 
	 */
	public long getSTATGETMSETTRY() {
		return STAT_GETMSET_TRY;
	}

	/**
	 * getMSet 성공 횟수 설정
	 */
	public void setSTATGETMSETSUCCESS() {
		++STAT_GETMSET_SUCCESS;
	}

	/**
	 * getMSet 성공 횟수 리턴
	 */
	public long getSTATGETMSETSUCCESS() {
		return STAT_GETMSET_SUCCESS;
	}

	/**
	 * getMSet 실패 횟수 설정
	 */
	public void setSTATGETMSETFAIL() {
		++STAT_GETMSET_FAIL;
	}

	/**
	 * getMSet 실패 횟수 리턴
	 */
	public long getSTATGETMSETFAIL() {
		return STAT_GETMSET_FAIL;
	}

	/**
	 * getSSet 시도 횟수 설정
	 */
	public void setSTATGETSSETTRY() {
		++STAT_GETSSET_TRY;
	}

	/**
	 * getSSet 시도 횟수 리턴 
	 */
	public long getSTATGETSSETTRY() {
		return STAT_GETSSET_TRY;
	}

	/**
	 * getSSet 성공 횟수 설정
	 */
	public void setSTATGETSSETSUCCESS() {
		++STAT_GETSSET_SUCCESS;
	}

	/**
	 * getSSet 성공 횟수 리턴
	 */
	public long getSTATGETSSETSUCCESS() {
		return STAT_GETSSET_SUCCESS;
	}

	/**
	 * getSSet 실패 횟수 설정
	 */
	public void setSTATGETSSETFAIL() {
		++STAT_GETSSET_FAIL;
	}

	/**
	 * getSSet 실패 횟수 리턴
	 */
	public long getSTATGETSSETFAIL() {
		return STAT_GETSSET_FAIL;
	}

	/**
	 * getLData 시도 횟수 설정
	 */
	public void setSTATGETLDATATRY() {
		++STAT_GETLDATA_TRY;
	}

	/**
	 * getLData 시도 횟수 리턴 
	 */
	public long getSTATGETLDATATRY() {
		return STAT_GETLDATA_TRY;
	}

	/**
	 * getLData 성공 횟수 설정
	 */
	public void setSTATGETLDATASUCCESS() {
		++STAT_GETLDATA_SUCCESS;
	}

	/**
	 * getLData 성공 횟수 리턴
	 */
	public long getSTATGETLDATASUCCESS() {
		return STAT_GETLDATA_SUCCESS;
	}

	/**
	 * getLData 실패 횟수 설정
	 */
	public void setSTATGETLDATAFAIL() {
		++STAT_GETLDATA_FAIL;
	}

	/**
	 * getLData 실패 횟수 리턴
	 */
	public long getSTATGETLDATAFAIL() {
		return STAT_GETLDATA_FAIL;
	}

	/**
	 * getDSLData 시도 횟수 설정
	 */
	public void setSTATGETDSLDATATRY() {
		++STAT_GETDSLDATA_TRY;
	}

	/**
	 * getDSLData 시도 횟수 리턴 
	 */
	public long getSTATGETDSLDATATRY() {
		return STAT_GETDSLDATA_TRY;
	}

	/**
	 * getDSLData 성공 횟수 설정
	 */
	public void setSTATGETDSLDATASUCCESS() {
		++STAT_GETDSLDATA_SUCCESS;
	}

	/**
	 * getDSLData 성공 횟수 리턴
	 */
	public long getSTATGETDSLDATASUCCESS() {
		return STAT_GETDSLDATA_SUCCESS;
	}

	/**
	 * getDSLData 실패 횟수 설정
	 */
	public void setSTATGETDSLDATAFAIL() {
		++STAT_GETDSLDATA_FAIL;
	}

	/**
	 * getDSLData 실패 횟수 리턴
	 */
	public long getSTATGETDSLDATAFAIL() {
		return STAT_GETDSLDATA_FAIL;
	}

	/**
	 * getALData 시도 횟수 설정
	 */
	public void setSTATGETALDATATRY() {
		++STAT_GETALDATA_TRY;
	}

	/**
	 * getALData 시도 횟수 리턴 
	 */
	public long getSTATGETALDATATRY() {
		return STAT_GETALDATA_TRY;
	}

	/**
	 * getALData 성공 횟수 설정
	 */
	public void setSTATGETALDATASUCCESS() {
		++STAT_GETALDATA_SUCCESS;
	}

	/**
	 * getALData 성공 횟수 리턴
	 */
	public long getSTATGETALDATASUCCESS() {
		return STAT_GETALDATA_SUCCESS;
	}

	/**
	 * getALData 실패 횟수 설정
	 */
	public void setSTATGETALDATAFAIL() {
		++STAT_GETALDATA_FAIL;
	}

	/**
	 * getALData 실패 횟수 리턴
	 */
	public long getSTATGETALDATAFAIL() {
		return STAT_GETALDATA_FAIL;
	}

	/**
	 * getDSALData 시도 횟수 설정
	 */
	public void setSTATGETDSALDATATRY() {
		++STAT_GETDSALDATA_TRY;
	}

	/**
	 * getDSALData 시도 횟수 리턴 
	 */
	public long getSTATGETDSALDATATRY() {
		return STAT_GETDSALDATA_TRY;
	}

	/**
	 * getDSALData 성공 횟수 설정
	 */
	public void setSTATGETDSALDATASUCCESS() {
		++STAT_GETDSALDATA_SUCCESS;
	}

	/**
	 * getDSALData 성공 횟수 리턴
	 */
	public long getSTATGETDSALDATASUCCESS() {
		return STAT_GETDSALDATA_SUCCESS;
	}

	/**
	 * getDSALData 실패 횟수 설정
	 */
	public void setSTATGETDSALDATAFAIL() {
		++STAT_GETDSALDATA_FAIL;
	}

	/**
	 * getDSALData 실패 횟수 리턴
	 */
	public long getSTATGETDSALDATAFAIL() {
		return STAT_GETDSALDATA_FAIL;
	}

	/**
	 * getIData 시도 횟수 설정
	 */
	public void setSTATGETIDATATRY() {
		++STAT_GETIDATA_TRY;
	}

	/**
	 * getIData 시도 횟수 리턴 
	 */
	public long getSTATGETIDATATRY() {
		return STAT_GETIDATA_TRY;
	}

	/**
	 * getIData 성공 횟수 설정
	 */
	public void setSTATGETIDATASUCCESS() {
		++STAT_GETIDATA_SUCCESS;
	}

	/**
	 * getIData 성공 횟수 리턴
	 */
	public long getSTATGETIDATASUCCESS() {
		return STAT_GETIDATA_SUCCESS;
	}

	/**
	 * getIData 실패 횟수 설정
	 */
	public void setSTATGETIDATAFAIL() {
		++STAT_GETIDATA_FAIL;
	}

	/**
	 * getIData 실패 횟수 리턴
	 */
	public long getSTATGETIDATAFAIL() {
		return STAT_GETIDATA_FAIL;
	}

	/**
	 * getDSIData 시도 횟수 설정
	 */
	public void setSTATGETDSIDATATRY() {
		++STAT_GETDSIDATA_TRY;
	}

	/**
	 * getDSIData 시도 횟수 리턴 
	 */
	public long getSTATGETDSIDATATRY() {
		return STAT_GETDSIDATA_TRY;
	}

	/**
	 * getDSIData 성공 횟수 설정
	 */
	public void setSTATGETDSIDATASUCCESS() {
		++STAT_GETDSIDATA_SUCCESS;
	}

	/**
	 * getDSIData 성공 횟수 리턴
	 */
	public long getSTATGETDSIDATASUCCESS() {
		return STAT_GETDSIDATA_SUCCESS;
	}

	/**
	 * getDSIData 실패 횟수 설정
	 */
	public void setSTATGETDSIDATAFAIL() {
		++STAT_GETDSIDATA_FAIL;
	}

	/**
	 * getDSIData 실패 횟수 리턴
	 */
	public long getSTATGETDSIDATAFAIL() {
		return STAT_GETDSIDATA_FAIL;
	}

	/**
	 * setMData 시도 횟수 설정
	 */
	public void setSTATSETMDATATRY() {
		++STAT_SETMDATA_TRY;
	}

	/**
	 * setMData 시도 횟수 리턴 
	 */
	public long getSTATSETMDATATRY() {
		return STAT_SETMDATA_TRY;
	}

	/**
	 * setMData 성공 횟수 설정
	 */
	public void setSTATSETMDATASUCCESS() {
		++STAT_SETMDATA_SUCCESS;
	}

	/**
	 * setMData 성공 횟수 리턴
	 */
	public long getSTATSETMDATASUCCESS() {
		return STAT_SETMDATA_SUCCESS;
	}

	/**
	 * setMData 실패 횟수 설정
	 */
	public void setSTATSETMDATAFAIL() {
		++STAT_SETMDATA_FAIL;
	}

	/**
	 * setMData 실패 횟수 리턴
	 */
	public long getSTATSETMDATAFAIL() {
		return STAT_SETMDATA_FAIL;
	}

	/**
	 * setMMData 시도 횟수 설정
	 */
	public void setSTATSETMMDATATRY() {
		++STAT_SETMMDATA_TRY;
	}

	/**
	 * setMMData 시도 횟수 리턴 
	 */
	public long getSTATSETMMDATATRY() {
		return STAT_SETMMDATA_TRY;
	}

	/**
	 * setMMData 성공 횟수 설정
	 */
	public void setSTATSETMMDATASUCCESS() {
		++STAT_SETMMDATA_SUCCESS;
	}

	/**
	 * setMMData 성공 횟수 리턴
	 */
	public long getSTATSETMMDATASUCCESS() {
		return STAT_SETMMDATA_SUCCESS;
	}

	/**
	 * setMMData 실패 횟수 설정
	 */
	public void setSTATSETMMDATAFAIL() {
		++STAT_SETMMDATA_FAIL;
	}

	/**
	 * setMMData 실패 횟수 리턴
	 */
	public long getSTATSETMMDATAFAIL() {
		return STAT_SETMMDATA_FAIL;
	}

	/**
	 * callSData 시도 횟수 설정
	 */
	public void setSTATCALLSDATATRY() {
		++STAT_CALLSDATA_TRY;
	}

	/**
	 * callSData 시도 횟수 리턴 
	 */
	public long getSTATCALLSDATATRY() {
		return STAT_CALLSDATA_TRY;
	}

	/**
	 * callSData 성공 횟수 설정
	 */
	public void setSTATCALLSDATASUCCESS() {
		++STAT_CALLSDATA_SUCCESS;
	}

	/**
	 * callSData 성공 횟수 리턴
	 */
	public long getSTATCALLSDATASUCCESS() {
		return STAT_CALLSDATA_SUCCESS;
	}

	/**
	 * callSData 실패 횟수 설정
	 */
	public void setSTATCALLSDATAFAIL() {
		++STAT_CALLSDATA_FAIL;
	}

	/**
	 * callSData 실패 횟수 리턴
	 */
	public long getSTATCALLSDATAFAIL() {
		return STAT_CALLSDATA_FAIL;
	}

	/**
	 * setTXMData 시도 횟수 설정
	 */
	public void setSTATSETTXMDATATRY() {
		++STAT_SETTXMDATA_TRY;
	}

	/**
	 * setTXMData 시도 횟수 리턴 
	 */
	public long getSTATSETTXMDATATRY() {
		return STAT_SETTXMDATA_TRY;
	}

	/**
	 * setTXMData 성공 횟수 설정
	 */
	public void setSTATSETTXMDATASUCCESS() {
		++STAT_SETTXMDATA_SUCCESS;
	}

	/**
	 * setTXMData 성공 횟수 리턴
	 */
	public long getSTATSETTXMDATASUCCESS() {
		return STAT_SETTXMDATA_SUCCESS;
	}

	/**
	 * setTXMData 실패 횟수 설정
	 */
	public void setSTATSETTXMDATAFAIL() {
		++STAT_SETTXMDATA_FAIL;
	}

	/**
	 * setTXMData 실패 횟수 리턴
	 */
	public long getSTATSETTXMDATAFAIL() {
		return STAT_SETTXMDATA_FAIL;
	}

	/**
	 * getTotalRow 시도 횟수 설정
	 */
	public void setSTATGETTOTALROWTRY() {
		++STAT_GETTOTALROW_TRY;
	}

	/**
	 * getTotalRow 시도 횟수 리턴 
	 */
	public long getSTATGETTOTALROWTRY() {
		return STAT_GETTOTALROW_TRY;
	}

	/**
	 * getTotalRow 성공 횟수 설정
	 */
	public void setSTATGETTOTALROWSUCCESS() {
		++STAT_GETTOTALROW_SUCCESS;
	}

	/**
	 * getTotalRow 성공 횟수 리턴
	 */
	public long getSTATGETTOTALROWSUCCESS() {
		return STAT_GETTOTALROW_SUCCESS;
	}

	/**
	 * getTotalRow 실패 횟수 설정
	 */
	public void setSTATGETTOTALROWFAIL() {
		++STAT_GETTOTALROW_FAIL;
	}

	/**
	 * getTotalRow 실패 횟수 리턴
	 */
	public long getSTATGETTOTALROWFAIL() {
		return STAT_GETTOTALROW_FAIL;
	}

	/**
	 * getSequence 시도 횟수 설정
	 */
	public void setSTATGETSEQUENCETRY() {
		++STAT_GETSEQUENCE_TRY;
	}

	/**
	 * getSequence 시도 횟수 리턴 
	 */
	public long getSTATGETSEQUENCETRY() {
		return STAT_GETSEQUENCE_TRY;
	}

	/**
	 * getSequence 성공 횟수 설정
	 */
	public void setSTATGETSEQUENCESUCCESS() {
		++STAT_GETSEQUENCE_SUCCESS;
	}

	/**
	 * getSequence 성공 횟수 리턴
	 */
	public long getSTATGETSEQUENCESUCCESS() {
		return STAT_GETSEQUENCE_SUCCESS;
	}

	/**
	 * getSequence 실패 횟수 설정
	 */
	public void setSTATGETSEQUENCEFAIL() {
		++STAT_GETSEQUENCE_FAIL;
	}

	/**
	 * getSequence 실패 횟수 리턴
	 */
	public long getSTATGETSEQUENCEFAIL() {
		return STAT_GETSEQUENCE_FAIL;
	}

	/**
	 * getRawSequence 시도 횟수 설정
	 */
	public void setSTATGETRAWSEQUENCETRY() {
		++STAT_GETRAWSEQUENCE_TRY;
	}

	/**
	 * getRawSequence 시도 횟수 리턴 
	 */
	public long getSTATGETRAWSEQUENCETRY() {
		return STAT_GETRAWSEQUENCE_TRY;
	}

	/**
	 * getRawSequence 성공 횟수 설정
	 */
	public void setSTATGETRAWSEQUENCESUCCESS() {
		++STAT_GETRAWSEQUENCE_SUCCESS;
	}

	/**
	 * getRawSequence 성공 횟수 리턴
	 */
	public long getSTATGETRAWSEQUENCESUCCESS() {
		return STAT_GETRAWSEQUENCE_SUCCESS;
	}

	/**
	 * getRawSequence 실패 횟수 설정
	 */
	public void setSTATGETRAWSEQUENCEFAIL() {
		++STAT_GETRAWSEQUENCE_FAIL;
	}

	/**
	 * getRawSequence 실패 횟수 리턴
	 */
	public long getSTATGETRAWSEQUENCEFAIL() {
		return STAT_GETRAWSEQUENCE_FAIL;
	}

	/**
	 * closeISet 시도 횟수 설정
	 */
	public void setSTATCLOSEISETTRY() {
		++STAT_CLOSEISET_TRY;
	}

	/**
	 * closeISet 시도 횟수 리턴 
	 */
	public long getSTATCLOSEISETTRY() {
		return STAT_CLOSEISET_TRY;
	}

	/**
	 * closeISet 성공 횟수 설정
	 */
	public void setSTATCLOSEISETSUCCESS() {
		++STAT_CLOSEISET_SUCCESS;
	}

	/**
	 * closeISet 성공 횟수 리턴
	 */
	public long getSTATCLOSEISETSUCCESS() {
		return STAT_CLOSEISET_SUCCESS;
	}

	/**
	 * closeISet 실패 횟수 설정
	 */
	public void setSTATCLOSEISETFAIL() {
		++STAT_CLOSEISET_FAIL;
	}

	/**
	 * closeISet 실패 횟수 리턴
	 */
	public long getSTATCLOSEISETFAIL() {
		return STAT_CLOSEISET_FAIL;
	}

	/**
	 * closeMSet 시도 횟수 설정
	 */
	public void setSTATCLOSEMSETTRY() {
		++STAT_CLOSEMSET_TRY;
	}

	/**
	 * closeMSet 시도 횟수 리턴 
	 */
	public long getSTATCLOSEMSETTRY() {
		return STAT_CLOSEMSET_TRY;
	}

	/**
	 * closeMSet 성공 횟수 설정
	 */
	public void setSTATCLOSEMSETSUCCESS() {
		++STAT_CLOSEMSET_SUCCESS;
	}

	/**
	 * closeMSet 성공 횟수 리턴
	 */
	public long getSTATCLOSEMSETSUCCESS() {
		return STAT_CLOSEMSET_SUCCESS;
	}

	/**
	 * closeMSet 실패 횟수 설정
	 */
	public void setSTATCLOSEMSETFAIL() {
		++STAT_CLOSEMSET_FAIL;
	}

	/**
	 * closeMSet 실패 횟수 리턴
	 */
	public long getSTATCLOSEMSETFAIL() {
		return STAT_CLOSEMSET_FAIL;
	}

	/**
	 * closeSSet 시도 횟수 설정
	 */
	public void setSTATCLOSESSETTRY() {
		++STAT_CLOSESSET_TRY;
	}

	/**
	 * closeSSet 시도 횟수 리턴 
	 */
	public long getSTATCLOSESSETTRY() {
		return STAT_CLOSESSET_TRY;
	}

	/**
	 * closeSSet 성공 횟수 설정
	 */
	public void setSTATCLOSESSETSUCCESS() {
		++STAT_CLOSESSET_SUCCESS;
	}

	/**
	 * closeSSet 성공 횟수 리턴
	 */
	public long getSTATCLOSESSETSUCCESS() {
		return STAT_CLOSESSET_SUCCESS;
	}

	/**
	 * closeSSet 실패 횟수 설정
	 */
	public void setSTATCLOSESSETFAIL() {
		++STAT_CLOSESSET_FAIL;
	}

	/**
	 * closeSSet 실패 횟수 리턴
	 */
	public long getSTATCLOSESSETFAIL() {
		return STAT_CLOSESSET_FAIL;
	}

	/**
	 * closeConn 시도 횟수 설정
	 */
	public void setSTATCLOSECONNTRY() {
		++STAT_CLOSECONN_TRY;
	}

	/**
	 * closeConn 시도 횟수 리턴 
	 */
	public long getSTATCLOSECONNTRY() {
		return STAT_CLOSECONN_TRY;
	}

	/**
	 * closeConn 성공 횟수 설정
	 */
	public void setSTATCLOSECONNSUCCESS() {
		++STAT_CLOSECONN_SUCCESS;
	}

	/**
	 * closeConn 성공 횟수 리턴
	 */
	public long getSTATCLOSECONNSUCCESS() {
		return STAT_CLOSECONN_SUCCESS;
	}

	/**
	 * closeConn 실패 횟수 설정
	 */
	public void setSTATCLOSECONNFAIL() {
		++STAT_CLOSECONN_FAIL;
	}

	/**
	 * closeConn 실패 횟수 리턴
	 */
	public long getSTATCLOSECONNFAIL() {
		return STAT_CLOSECONN_FAIL;
	}

	/**
	 * setParam 시도 횟수 설정
	 */
	public void setSTATSETPARAMTRY() {
		++STAT_SETPARAM_TRY;
	}

	/**
	 * setParam 시도 횟수 리턴 
	 */
	public long getSTATSETPARAMTRY() {
		return STAT_SETPARAM_TRY;
	}

	/**
	 * setParam 성공 횟수 설정
	 */
	public void setSTATSETPARAMSUCCESS() {
		++STAT_SETPARAM_SUCCESS;
	}

	/**
	 * setParam 성공 횟수 리턴
	 */
	public long getSTATSETPARAMSUCCESS() {
		return STAT_SETPARAM_SUCCESS;
	}

	/**
	 * setParam 실패 횟수 설정
	 */
	public void setSTATSETPARAMFAIL() {
		++STAT_SETPARAM_FAIL;
	}

	/**
	 * setParam 실패 횟수 리턴
	 */
	public long getSTATSETPARAMFAIL() {
		return STAT_SETPARAM_FAIL;
	}

	/**
	 * setRowData 시도 횟수 설정
	 */
	public void setSTATSETROWDATATRY() {
		++STAT_SETROWDATA_TRY;
	}

	/**
	 * setRowData 시도 횟수 리턴 
	 */
	public long getSTATSETROWDATATRY() {
		return STAT_SETROWDATA_TRY;
	}

	/**
	 * setRowData 성공 횟수 설정
	 */
	public void setSTATSETROWDATASUCCESS() {
		++STAT_SETROWDATA_SUCCESS;
	}

	/**
	 * setRowData 성공 횟수 리턴
	 */
	public long getSTATSETROWDATASUCCESS() {
		return STAT_SETROWDATA_SUCCESS;
	}

	/**
	 * setRowData 실패 횟수 설정
	 */
	public void setSTATSETROWDATAFAIL() {
		++STAT_SETROWDATA_FAIL;
	}

	/**
	 * setRowData 실패 횟수 리턴
	 */
	public long getSTATSETROWDATAFAIL() {
		return STAT_SETROWDATA_FAIL;
	}

	/**
	 * setMetaData 시도 횟수 설정
	 */
	public void setSTATSETMETADATATRY() {
		++STAT_SETMETADATA_TRY; 
	} 

	/**
	 * setMetaData 시도 횟수 리턴 
	 */
	public long getSTATSETMETADATATRY() {
		return STAT_SETMETADATA_TRY;
	}

	/**
	 * setMetaData 성공 횟수 설정
	 */
	public void setSTATSETMETADATASUCCESS() {
		++STAT_SETMETADATA_SUCCESS;
	}

	/**
	 * setMetaData 성공 횟수 리턴
	 */
	public long getSTATSETMETADATASUCCESS() {
		return STAT_SETMETADATA_SUCCESS;
	}

	/**
	 * setMetaData 실패 횟수 설정
	 */
	public void setSTATSETMETADATAFAIL() {
		++STAT_SETMETADATA_FAIL;
	}

	/**
	 * setMetaData 실패 횟수 리턴
	 */
	public long getSTATSETMETADATAFAIL() {
		return STAT_SETMETADATA_FAIL;
	}

	/**
	 * LOOKUP 시도 횟수 설정
	 */
	public void setSTATLOOKUPTRY() {
		++STAT_LOOKUP_TRY; 
	} 

	/**
	 * LOOKUP 시도 횟수 리턴 
	 */
	public long getSTATLOOKUPTRY() {
		return STAT_LOOKUP_TRY;
	}

	/**
	 * LOOKUP 성공 횟수 설정
	 */
	public void setSTATLOOKUPSUCCESS() {
		++STAT_LOOKUP_SUCCESS;
	}

	/**
	 * LOOKUP 성공 횟수 리턴
	 */
	public long getSTATLOOKUPSUCCESS() {
		return STAT_LOOKUP_SUCCESS;
	}

	/**
	 * LOOKUP 실패 횟수 설정
	 */
	public void setSTATLOOKUPFAIL() {
		++STAT_LOOKUP_FAIL;
	}

	/**
	 * LOOKUP 실패 횟수 리턴
	 */
	public long getSTATLOOKUPFAIL() {
		return STAT_LOOKUP_FAIL;
	}

	/**
	 * 통계 정보 리셋
	 */
	public void setSTATRESET() {
		STAT_CON_TRY = 0L;	
		STAT_CON_SUCCESS = 0L;	
		STAT_CON_FAIL = 0L;	

		STAT_CON_POOL_TRY = 0L;	
		STAT_CON_POOL_SUCCESS = 0L;	
		STAT_CON_POOL_FAIL = 0L;	

		STAT_CON_JDBC_TRY = 0L;	
		STAT_CON_JDBC_SUCCESS = 0L;	
		STAT_CON_JDBC_FAIL = 0L;	

		STAT_CON_KIND = new Hashtable();	

		STAT_SQL_INFO_TRY = 0L;	
		STAT_SQL_INFO_SUCCESS = 0L;	
		STAT_SQL_INFO_FAIL = 0L;	

		STAT_SQL_LIST_TRY = 0L;	
		STAT_SQL_LIST_SUCCESS = 0L;	
		STAT_SQL_LIST_FAIL = 0L;	

		STAT_SQL_COUNT_TRY = 0L;	
		STAT_SQL_COUNT_SUCCESS = 0L;	
		STAT_SQL_COUNT_FAIL = 0L;	

		STAT_SQL_ALIST_TRY = 0L;	
		STAT_SQL_ALIST_SUCCESS = 0L;	
		STAT_SQL_ALIST_FAIL = 0L;	

		STAT_SQL_MODIFY_TRY = 0L;	
		STAT_SQL_MODIFY_SUCCESS = 0L;	
		STAT_SQL_MODIFY_FAIL = 0L;	

		STAT_SQL_INSERT_TRY = 0L;	
		STAT_SQL_INSERT_SUCCESS = 0L;	
		STAT_SQL_INSERT_FAIL = 0L;	

		STAT_SQL_UPDATE_TRY = 0L;	
		STAT_SQL_UPDATE_SUCCESS = 0L;	
		STAT_SQL_UPDATE_FAIL = 0L;	

		STAT_SQL_DELETE_TRY = 0L;	
		STAT_SQL_DELETE_SUCCESS = 0L;	
		STAT_SQL_DELETE_FAIL = 0L;	

		STAT_SQL_PROC_TRY = 0L;	
		STAT_SQL_PROC_SUCCESS = 0L;	
		STAT_SQL_PROC_FAIL = 0L;	

		STAT_LOOKUP_KIND = new Hashtable();

		STAT_GETCONN_TRY = 0L;	
		STAT_GETCONN_SUCCESS = 0L;	
		STAT_GETCONN_FAIL = 0L;	

		STAT_GETCONN1_TRY = 0L;	
		STAT_GETCONN1_SUCCESS = 0L;	
		STAT_GETCONN1_FAIL = 0L;	

		STAT_GETCONN2_TRY = 0L;	
		STAT_GETCONN2_SUCCESS = 0L;	
		STAT_GETCONN2_FAIL = 0L;	

		STAT_GETISET_TRY = 0L;	
		STAT_GETISET_SUCCESS = 0L;	
		STAT_GETISET_FAIL = 0L;	

		STAT_GETMSET_TRY = 0L;	
		STAT_GETMSET_SUCCESS = 0L;	
		STAT_GETMSET_FAIL = 0L;	

		STAT_GETSSET_TRY = 0L;	
		STAT_GETSSET_SUCCESS = 0L;	
		STAT_GETSSET_FAIL = 0L;	

		STAT_GETLDATA_TRY = 0L;	
		STAT_GETLDATA_SUCCESS = 0L;	
		STAT_GETLDATA_FAIL = 0L;	

		STAT_GETDSLDATA_TRY = 0L;	
		STAT_GETDSLDATA_SUCCESS = 0L;	
		STAT_GETDSLDATA_FAIL = 0L;	

		STAT_GETALDATA_TRY = 0L;	
		STAT_GETALDATA_SUCCESS = 0L;	
		STAT_GETALDATA_FAIL = 0L;	

		STAT_GETDSALDATA_TRY = 0L;	
		STAT_GETDSALDATA_SUCCESS = 0L;	
		STAT_GETDSALDATA_FAIL = 0L;	

		STAT_GETIDATA_TRY = 0L;	
		STAT_GETIDATA_SUCCESS = 0L;	
		STAT_GETIDATA_FAIL = 0L;	

		STAT_GETDSIDATA_TRY = 0L;	
		STAT_GETDSIDATA_SUCCESS = 0L;	
		STAT_GETDSIDATA_FAIL = 0L;	

		STAT_SETMDATA_TRY = 0L;	
		STAT_SETMDATA_SUCCESS = 0L;	
		STAT_SETMDATA_FAIL = 0L;	

		STAT_SETMMDATA_TRY = 0L;	
		STAT_SETMMDATA_SUCCESS = 0L;	
		STAT_SETMMDATA_FAIL = 0L;	

		STAT_CALLSDATA_TRY = 0L;	
		STAT_CALLSDATA_SUCCESS = 0L;	
		STAT_CALLSDATA_FAIL = 0L;	

		STAT_SETTXMDATA_TRY = 0L;	
		STAT_SETTXMDATA_SUCCESS = 0L;	
		STAT_SETTXMDATA_FAIL = 0L;	

		STAT_GETTOTALROW_TRY = 0L;	
		STAT_GETTOTALROW_SUCCESS = 0L;	
		STAT_GETTOTALROW_FAIL = 0L;	

		STAT_GETSEQUENCE_TRY = 0L;	
		STAT_GETSEQUENCE_SUCCESS = 0L;	
		STAT_GETSEQUENCE_FAIL = 0L;	

		STAT_GETRAWSEQUENCE_TRY = 0L;	
		STAT_GETRAWSEQUENCE_SUCCESS = 0L;	
		STAT_GETRAWSEQUENCE_FAIL = 0L;	

		STAT_CLOSEISET_TRY = 0L;	
		STAT_CLOSEISET_SUCCESS = 0L;	
		STAT_CLOSEISET_FAIL = 0L;	

		STAT_CLOSEMSET_TRY = 0L;	
		STAT_CLOSEMSET_SUCCESS = 0L;	
		STAT_CLOSEMSET_FAIL = 0L;	

		STAT_CLOSESSET_TRY = 0L;	
		STAT_CLOSESSET_SUCCESS = 0L;	
		STAT_CLOSESSET_FAIL = 0L;	
	}

}
