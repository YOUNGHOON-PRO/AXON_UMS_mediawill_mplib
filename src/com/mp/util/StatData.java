package com.mp.util;

import java.util.*;
import com.mp.exception.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : StatData.java
 * Version      : 1.1
 * 작성일       : 2003/12/27
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 *
 * 설명         : dev_stand 사용의 통계 정보를 저장 클래스.
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class StatData {

	/** 모듈명 설정 */
	protected String MNAME = "StatData";

	/** 생성자 */
	public StatData() {}

    /*=============================================*/
    // 통계 정보 FIELD
    /*=============================================*/

	/* ======================= 쿼리 유형별 통계 정보 ==========================*/	

	/** DB 접속시도 횟수	*/
	protected static long STAT_CON_TRY = 0L;
	/** DB 접속 성공 횟수	*/
	protected static long STAT_CON_SUCCESS = 0L;
	/** DB 접속 에러 횟수	*/
	protected static long STAT_CON_FAIL = 0L;

	/** DB(POOL) 접속시도 횟수	*/
	protected static long STAT_CON_POOL_TRY = 0L;
	/** DB(POOL) 접속 성공 횟수	*/
	protected static long STAT_CON_POOL_SUCCESS = 0L;
	/** DB(POOL) 접속 에러 횟수	*/
	protected static long STAT_CON_POOL_FAIL = 0L;

	/** DB(JDBC) 접속시도 횟수	*/
	protected static long STAT_CON_JDBC_TRY = 0L; 
	/** DB(JDBC) 접속 성공 횟수	*/
	protected static long STAT_CON_JDBC_SUCCESS = 0L;
	/** DB(JDBC) 접속 에러 횟수	*/
	protected static long STAT_CON_JDBC_FAIL = 0L; 

	/** DB 연결 해제 시도 횟수	*/
	protected static long STAT_CON_CLOSE_TRY = 0L;
	/** DB 연결 해제 성공 횟수	*/
	protected static long STAT_CON_CLOSE_SUCCESS = 0L;
	/** DB 연결 해제 에러 횟수	*/
	protected static long STAT_CON_CLOSE_FAIL = 0L;

	/** DB 환경 이름별 통계 정보 */ 
	protected static Hashtable STAT_CON_KIND = null;

	/** INFO SELECT 쿼리 시도 횟수	*/
	protected static long STAT_SQL_INFO_TRY = 0L;
	/** INFO SELECT 쿼리 성공 횟수	*/
	protected static long STAT_SQL_INFO_SUCCESS = 0L;
	/** INFO SELECT 쿼리 실패 횟수	*/
	protected static long STAT_SQL_INFO_FAIL = 0L;

	/** LIST SELECT 쿼리 시도 횟수	*/
	protected static long STAT_SQL_LIST_TRY = 0L;
	/** LIST SELECT 쿼리 성공 횟수	*/
	protected static long STAT_SQL_LIST_SUCCESS = 0L;
	/** LIST SELECT 쿼리 실패 횟수	*/
	protected static long STAT_SQL_LIST_FAIL = 0L;

	/** COUNT SELECT 쿼리 시도 횟수	*/
	protected static long STAT_SQL_COUNT_TRY = 0L;
	/** COUNT SELECT 쿼리 성공 횟수	*/
	protected static long STAT_SQL_COUNT_SUCCESS = 0L;
	/** COUNT SELECT 쿼리 실패 횟수	*/
	protected static long STAT_SQL_COUNT_FAIL = 0L;

	/** ALIST SELECT 쿼리 시도 횟수	*/
	protected static long STAT_SQL_ALIST_TRY = 0L;
	/** ALIST SELECT 쿼리 성공 횟수	*/
	protected static long STAT_SQL_ALIST_SUCCESS = 0L;
	/** ALIST SELECT 쿼리 실패 횟수	*/
	protected static long STAT_SQL_ALIST_FAIL = 0L;

	/** 데이터 처리 쿼리 시도 횟수	*/
	protected static long STAT_SQL_MODIFY_TRY = 0L;
	/** 데이터 쿼리 성공 횟수	*/
	protected static long STAT_SQL_MODIFY_SUCCESS = 0L;
	/** 데이터 쿼리 실패 횟수	*/
	protected static long STAT_SQL_MODIFY_FAIL = 0L;

	/** INSERT 쿼리 시도 횟수	*/
	protected static long STAT_SQL_INSERT_TRY = 0L;
	/** INSERT 쿼리 성공 횟수	*/
	protected static long STAT_SQL_INSERT_SUCCESS = 0L;
	/** INSERT 쿼리 실패 횟수	*/
	protected static long STAT_SQL_INSERT_FAIL = 0L;

	/** UPDATE 쿼리 시도 횟수	*/
	protected static long STAT_SQL_UPDATE_TRY = 0L;
	/** UPDATE 쿼리 성공 횟수	*/
	protected static long STAT_SQL_UPDATE_SUCCESS = 0L;
	/** UPDATE 쿼리 실패 횟수	*/
	protected static long STAT_SQL_UPDATE_FAIL = 0L;

	/** DELETE 쿼리 시도 횟수	*/
	protected static long STAT_SQL_DELETE_TRY = 0L;
	/** DELETE 쿼리 성공 횟수	*/
	protected static long STAT_SQL_DELETE_SUCCESS = 0L;
	/** DELETE 쿼리 실패 횟수	*/
	protected static long STAT_SQL_DELETE_FAIL = 0L;

	/** PROCEDURE 쿼리 시도 횟수	*/
	protected static long STAT_SQL_PROC_TRY = 0L;
	/** PROCEDURE 쿼리 성공 횟수	*/
	protected static long STAT_SQL_PROC_SUCCESS = 0L;
	/** PROCEDURE 쿼리 실패 횟수	*/
	protected static long STAT_SQL_PROC_FAIL = 0L;

	/** LOOKUP 통계 정보	*/
	protected static Hashtable STAT_LOOKUP_KIND = null;

	/* ======================= 메소드별 유형별 통계 정보 ==========================*/	

	/** getConn 시도 횟수	*/
	protected static long STAT_GETCONN_TRY = 0L;
	/** getConn 성공 횟수	*/
	protected static long STAT_GETCONN_SUCCESS = 0L;
	/** getConn 실패 횟수	*/
	protected static long STAT_GETCONN_FAIL = 0L;

	/** getConn1 시도 횟수	*/
	protected static long STAT_GETCONN1_TRY = 0L;
	/** getConn1 성공 횟수	*/
	protected static long STAT_GETCONN1_SUCCESS = 0L;
	/** getConn1 실패 횟수	*/
	protected static long STAT_GETCONN1_FAIL = 0L;

	/** getConn2 시도 횟수	*/
	protected static long STAT_GETCONN2_TRY = 0L;
	/** getConn2 성공 횟수	*/
	protected static long STAT_GETCONN2_SUCCESS = 0L;
	/** getConn2 실패 횟수	*/
	protected static long STAT_GETCONN2_FAIL = 0L;
		
	/** getISet 시도 횟수	*/
	protected static long STAT_GETISET_TRY = 0L;
	/** getISet 성공 횟수	*/
	protected static long STAT_GETISET_SUCCESS = 0L;
	/** getISet 실패 횟수	*/
	protected static long STAT_GETISET_FAIL = 0L;

	/** getMSet 시도 횟수	*/
	protected static long STAT_GETMSET_TRY = 0L;
	/** getMSet 성공 횟수	*/
	protected static long STAT_GETMSET_SUCCESS = 0L;
	/** getMSet 실패 횟수	*/
	protected static long STAT_GETMSET_FAIL = 0L;

	/** getSSet 시도 횟수	*/
	protected static long STAT_GETSSET_TRY = 0L;
	/** getSSet 성공 횟수	*/
	protected static long STAT_GETSSET_SUCCESS = 0L;
	/** getSSet 실패 횟수	*/
	protected static long STAT_GETSSET_FAIL = 0L;
	
	/** getLData 시도 횟수	*/
	protected static long STAT_GETLDATA_TRY = 0L;
	/** getLData 성공 횟수	*/
	protected static long STAT_GETLDATA_SUCCESS = 0L;
	/** getLData 실패 횟수	*/
	protected static long STAT_GETLDATA_FAIL = 0L;
	
	/** getDSLData 시도 횟수	*/
	protected static long STAT_GETDSLDATA_TRY = 0L;
	/** getDSLData 성공 횟수	*/
	protected static long STAT_GETDSLDATA_SUCCESS = 0L;
	/** getDSLData 실패 횟수	*/
	protected static long STAT_GETDSLDATA_FAIL = 0L;

	/** getALData 시도 횟수	*/
	protected static long STAT_GETALDATA_TRY = 0L;
	/** getALData 성공 횟수	*/
	protected static long STAT_GETALDATA_SUCCESS = 0L;
	/** getALData 실패 횟수	*/
	protected static long STAT_GETALDATA_FAIL = 0L;
	
	/** getDSALData 시도 횟수	*/
	protected static long STAT_GETDSALDATA_TRY = 0L;
	/** getDSALData 성공 횟수	*/
	protected static long STAT_GETDSALDATA_SUCCESS = 0L;
	/** getDSALData 실패 횟수	*/
	protected static long STAT_GETDSALDATA_FAIL = 0L;

	/** getIData 시도 횟수	*/
	protected static long STAT_GETIDATA_TRY = 0L;
	/** getIData 성공 횟수	*/
	protected static long STAT_GETIDATA_SUCCESS = 0L;
	/** getIData 실패 횟수	*/
	protected static long STAT_GETIDATA_FAIL = 0L;

	/** getDSIData 시도 횟수	*/
	protected static long STAT_GETDSIDATA_TRY = 0L;
	/** getDSIData 성공 횟수	*/
	protected static long STAT_GETDSIDATA_SUCCESS = 0L;
	/** getIData 실패 횟수	*/
	protected static long STAT_GETDSIDATA_FAIL = 0L;

	/** setMData 시도 횟수	*/
	protected static long STAT_SETMDATA_TRY = 0L;
	/** getMData 성공 횟수	*/
	protected static long STAT_SETMDATA_SUCCESS = 0L;
	/** getMData 실패 횟수	*/
	protected static long STAT_SETMDATA_FAIL = 0L;

	/** setMMData 시도 횟수	*/
	protected static long STAT_SETMMDATA_TRY = 0L;
	/** getMMData 성공 횟수	*/
	protected static long STAT_SETMMDATA_SUCCESS = 0L;
	/** getMMData 실패 횟수	*/
	protected static long STAT_SETMMDATA_FAIL = 0L;

	/** callSData 시도 횟수	*/
	protected static long STAT_CALLSDATA_TRY = 0L;
	/** callSData 성공 횟수	*/
	protected static long STAT_CALLSDATA_SUCCESS = 0L;
	/** callSData 실패 횟수	*/
	protected static long STAT_CALLSDATA_FAIL = 0L;

	/** setTXMData 시도 횟수	*/
	protected static long STAT_SETTXMDATA_TRY = 0L;
	/** setTXMData 성공 횟수	*/
	protected static long STAT_SETTXMDATA_SUCCESS = 0L;
	/** setTXMData 실패 횟수	*/
	protected static long STAT_SETTXMDATA_FAIL = 0L;

	/** getTotalRow 시도 횟수	*/
	protected static long STAT_GETTOTALROW_TRY = 0L;
	/** getTotalRow 성공 횟수	*/
	protected static long STAT_GETTOTALROW_SUCCESS = 0L;
	/** getTotalRow 실패 횟수	*/
	protected static long STAT_GETTOTALROW_FAIL = 0L;

	/** getSequence 시도 횟수	*/
	protected static long STAT_GETSEQUENCE_TRY = 0L;
	/** getSequence 성공 횟수	*/
	protected static long STAT_GETSEQUENCE_SUCCESS = 0L;
	/** getSequence 실패 횟수	*/
	protected static long STAT_GETSEQUENCE_FAIL = 0L;

	/** getRawSequence 시도 횟수	*/
	protected static long STAT_GETRAWSEQUENCE_TRY = 0L;
	/** getRawSequence 성공 횟수	*/
	protected static long STAT_GETRAWSEQUENCE_SUCCESS = 0L;
	/** getRawSequence 실패 횟수	*/
	protected static long STAT_GETRAWSEQUENCE_FAIL = 0L;

	/** closeISet 시도 횟수	*/
	protected static long STAT_CLOSEISET_TRY = 0L;
	/** closeISet 성공 횟수	*/
	protected static long STAT_CLOSEISET_SUCCESS = 0L;
	/** closeISet 실패 횟수	*/
	protected static long STAT_CLOSEISET_FAIL = 0L;

	/** closeMSet 시도 횟수	*/
	protected static long STAT_CLOSEMSET_TRY = 0L;
	/** closeMSet 성공 횟수	*/
	protected static long STAT_CLOSEMSET_SUCCESS = 0L;
	/** closeMSet 실패 횟수	*/
	protected static long STAT_CLOSEMSET_FAIL = 0L;

	/** closeSSet 시도 횟수	*/
	protected static long STAT_CLOSESSET_TRY = 0L;
	/** closeSSet 성공 횟수	*/
	protected static long STAT_CLOSESSET_SUCCESS = 0L;
	/** closeSSet 실패 횟수	*/
	protected static long STAT_CLOSESSET_FAIL = 0L;

	/** closeConn 시도 횟수	*/
	protected static long STAT_CLOSECONN_TRY = 0L;
	/** closeConn 성공 횟수	*/
	protected static long STAT_CLOSECONN_SUCCESS = 0L;
	/** closeConn 실패 횟수	*/
	protected static long STAT_CLOSECONN_FAIL = 0L;

	/** setParam 시도 횟수	*/
	protected static long STAT_SETPARAM_TRY = 0L;
	/** setParam 성공 횟수	*/
	protected static long STAT_SETPARAM_SUCCESS = 0L;
	/** setParam 실패 횟수	*/
	protected static long STAT_SETPARAM_FAIL = 0L;

	/** setRowData 시도 횟수	*/
	protected static long STAT_SETROWDATA_TRY = 0L;
	/** setRowData 성공 횟수	*/
	protected static long STAT_SETROWDATA_SUCCESS = 0L;
	/** setRowData 실패 횟수	*/
	protected static long STAT_SETROWDATA_FAIL = 0L;

	/** setMetaData 시도 횟수	*/
	protected static long STAT_SETMETADATA_TRY = 0L;
	/** setMetaData 성공 횟수	*/
	protected static long STAT_SETMETADATA_SUCCESS = 0L;
	/** setMetaData 실패 횟수	*/
	protected static long STAT_SETMETADATA_FAIL = 0L;

	/** LOOKUP 시도 횟수	*/
	protected static long STAT_LOOKUP_TRY = 0L;
	/** LOOKUP 성공 횟수	*/
	protected static long STAT_LOOKUP_SUCCESS = 0L;
	/** LOOKUP 실패 횟수	*/
	protected static long STAT_LOOKUP_FAIL = 0L;
}
