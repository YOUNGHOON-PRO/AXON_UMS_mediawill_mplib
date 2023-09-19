package com.mp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

import com.mp.exception.Err;
//import java.util.*;

/**
 * <pre>
 * 프로그램명	: CampLogProcess.java
 * Version 		: 1.0
 * 작성일 		: 2005/03/03
 * 작성자 		: 서창훈
 * 수정일 		:
 * 수정자 		:
 * 설명			: 캠페인 로그 적재 프로시져를 실행
 *
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class CampLogProcess_자바사용 implements TmRun {

	/** 모듈명 설정 */
	private String MNAME = "CampLogProcess";

	private WebUtil util = null;
	private String workname = null;
	private String workclass = null;
	Connection resp_conn = null;
	private ResultSet result = null;
	
	
	String pos = MNAME+".doWork()";
	
	//생성자
	public CampLogProcess_자바사용() {}
	/** 데이터 베이스 UTIL */
	private DBUtil dbutil = null;	
	//설정정보
	@Override
	public void setWork(Hashtable setinfohash) {
		this.util = (WebUtil)setinfohash.get("UTIL");
		this.workname = (String)setinfohash.get("WORKNAME");
		this.workclass = (String)setinfohash.get("WORKCLASS");
	}

	//작업내용
	@Override
	public void doWork() {
		
		//큐브리드 java 함수 사용
		//System.out.println("캠페인 로그 적재 프로시져 호출");
		SP_AG_CAMP_LOG();
		
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("캠페인 로그 적재 프로시져 실행 성공");
		System.out.println("-------------------------------------------------------------------------------");
		
		//기존 오라클/MSSQL 은 프로시져 사용
		/*
		String pos = MNAME+".doWork()";
		String SQL = "{call SP_AG_CAMP_LOG (?)}";
		ParamType param = new ParamType();
		param.put(1,"",Code.DB_VARCHAR);
		Hashtable inputhash = new Hashtable();
		inputhash.put("SQL",SQL);
		inputhash.put("PARAM",param);
				Hashtable rethash = null;
				try {
						rethash = util.callSData(inputhash);
			
					
			util.log(pos,"캠페인 로그 적재 프로시져 실행 성공");
		
		} catch (Err err) {
		  util.log(pos,err.getEXStr());
		} catch (Exception e) {
		  util.log(pos,e.toString());
		}
		*/
	}
	

//큐브리드 DB Connection 추가	
public Connection getConnection() throws Exception {
	
	Env env = new Env();
	
	String driver = env.getEnv("Defaultjdbcdriver");
	Class.forName(driver);	
	String url = env.getEnv("Defaulturl");
	String user = env.getEnv("Defaultjdbcuserid");
	String password = env.getEnv("Defaultjdbcpassword");
	/*
	System.out.println("driver : " + driver);
	System.out.println("url : " + url);
	System.out.println("user : " + user);
	System.out.println("password : " + password);
	*/
	/*
	Class.forName("cubrid.jdbc.driver.CUBRIDDriver");	
	String url = "jdbc:cubrid:localhost:30000:EUCKR:::?charset=EUC-KR";
	String user = "euckr";
	String password = "euckr";
*/
		return DriverManager.getConnection(url, user, password);
	}
		
		
		
		//SP_AG_CAMP_LOG 처리
		private void SP_AG_CAMP_LOG()
		{
			
			//수신확인 PreparedStatement
			PreparedStatement get_pstmt = null;
			
			PreparedStatement send_pstmt = null;
			PreparedStatement send_pstmt2 = null;
			PreparedStatement send_pstmt3 = null;
			PreparedStatement send_pstmt4 = null;
			
			PreparedStatement resp_pstmt = null;
			PreparedStatement resp_pstmt2 = null;		
			PreparedStatement resp_pstmt3 = null;
			PreparedStatement resp_pstmt4 = null;
			
			PreparedStatement link_pstmt = null;
			PreparedStatement link_pstmt2 = null;		
			PreparedStatement link_pstmt3 = null;
			PreparedStatement link_pstmt4 = null;
		
			try {
				//데이터베이스 연결을 수행한다.		
				Connection resp_conn = this.getConnection();
				//resp_conn =       dbcon.getConn("Resp");	

				
				String sSEND_DAY ="";
				String sOPEN_DAY ="";
				String sCLICK_DAY ="";
				//SENDLOG 처리1		
				String GET_SQL = " SELECT "+
								 " ( SELECT ISNULL(MAX(SEND_DAY),'00000000') FROM NEO_AR_SENDLOG) AS SEND_DAY "+
								 " , ( SELECT ISNULL(MAX(OPEN_DAY),'00000000') FROM NEO_AR_RESPLOG) AS OPEN_DAY "+
								 " , ( SELECT ISNULL(MAX(CLICK_DAY),'00000000') FROM NEO_AR_LINKLOG) AS CLICK_DAY ";
				get_pstmt = resp_conn.prepareStatement(GET_SQL);
				result = get_pstmt.executeQuery();
				while(result.next()){
					sSEND_DAY = result.getString("SEND_DAY").substring(0,8);
					sOPEN_DAY = result.getString("OPEN_DAY").substring(0,8);
					sCLICK_DAY= result.getString("CLICK_DAY").substring(0,8);	
				}
				
				//SENDLOG 처리1		
				String SEND_SQL = " DELETE FROM NEO_AR_SENDLOG WHERE SEND_DAY = ?";
				send_pstmt = resp_conn.prepareStatement(SEND_SQL);
				send_pstmt.setString(1,sSEND_DAY);
				
				send_pstmt.executeUpdate();
				
				//SENDLOG 처리2
				String SEND_SQL2  = " INSERT INTO NEO_AR_SENDLOG			"+
								   " (DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,TASK_NO,SUB_TASK_NO,RETRY_CNT,SEND_DT,SEND_RCODE,RCODE_STEP1,RCODE_STEP2,RCODE_STEP3,DOMAIN_CD,TARGET_GRP_TY,SEND_AR_AMT) "+
								   " SELECT /*+ ordered */ DEPT_NO,USER_ID,CAMP_TY, CAMP_NO,A.TASK_NO,A.SUB_TASK_NO, A.RETRY_CNT,SEND_DT,SEND_RCODE,RCODE_STEP1,RCODE_STEP2,RCODE_STEP3,DOMAIN_CD,TARGET_GRP_TY,SUM(SEND_AMT)"+
								   " FROM NEO_SENDLOG A	"+
								   " JOIN (SELECT TASK_NO, SUB_TASK_NO, CUST_ID, MAX(RETRY_CNT) AS MAX_RETRY_CNT "+
								   " FROM NEO_SENDLOG WHERE SEND_DT >= ? GROUP BY TASK_NO, SUB_TASK_NO, CUST_ID) B "+
								   " ON A.RETRY_CNT = B.MAX_RETRY_CNT "+
								   " AND A.TASK_NO = B.TASK_NO "+
								   " AND A.SUB_TASK_NO = B.SUB_TASK_NO "+
								   " AND A.CUST_ID = B.CUST_ID "+
								   " WHERE SEND_DT >= ? "+
								   " GROUP BY DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,A.TASK_NO,A.SUB_TASK_NO,A.RETRY_CNT,SEND_DT, "+
								   " SEND_RCODE,RCODE_STEP1,RCODE_STEP2,RCODE_STEP3,DOMAIN_CD,TARGET_GRP_TY ";
				

				send_pstmt2 = resp_conn.prepareStatement(SEND_SQL2);
				send_pstmt2.setString(1,sSEND_DAY+"0000");
				send_pstmt2.setString(2,sSEND_DAY+"0000");
				send_pstmt2.executeUpdate();
				
				System.out.println("INSERT INTO NEO_AR_SENDLOG");
				
				//SENDLOG 처리3
				String SEND_SQL3  = " UPDATE NEO_AR_SENDLOG SET SEND_DAY = SUBSTRING(SEND_DT,1,8) WHERE SEND_DAY IS NULL			";
				
				send_pstmt3 = resp_conn.prepareStatement(SEND_SQL3);
				send_pstmt3.executeUpdate();
				
				//SENDLOG 처리4
				String SEND_SQL4  =  " UPDATE NEO_AR_SENDLOG"+
									" SET DAYOFWEEK = "+
									" (SELECT CD FROM NEO_WEEKCD WHERE NEO_WEEKCD.YMD = SUBSTRING(NEO_AR_SENDLOG.SEND_DT,1,8)) "+
									" WHERE NEO_AR_SENDLOG.DAYOFWEEK IS NULL ";
				
				send_pstmt4 = resp_conn.prepareStatement(SEND_SQL4);
				send_pstmt4.executeUpdate();
			
				//RESPLOG 처리1
				String RESPLOG_SQL  =  " DELETE FROM NEO_AR_RESPLOG WHERE OPEN_DAY = ? ";
				
				resp_pstmt = resp_conn.prepareStatement(RESPLOG_SQL);
				resp_pstmt.setString(1,sOPEN_DAY);					
				resp_pstmt.executeUpdate();
				
				//RESPLOG 처리2
				String RESPLOG_SQL2  =  " INSERT INTO NEO_AR_RESPLOG "+
										" (DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,TASK_NO,SUB_TASK_NO,OPEN_DT,BLOCKED_YN,TARGET_GRP_TY,RESP_AR_AMT ) "+
										" SELECT DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,TASK_NO,SUB_TASK_NO,OPEN_DT,BLOCKED_YN,TARGET_GRP_TY,SUM(RESP_AMT) "+
										" FROM NEO_RESPLOG " +
										" WHERE OPEN_DT >= ? "+
										" GROUP BY DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,TASK_NO,SUB_TASK_NO,OPEN_DT,BLOCKED_YN,TARGET_GRP_TY ";
				
				resp_pstmt2 = resp_conn.prepareStatement(RESPLOG_SQL2);
				resp_pstmt2.setString(1,sOPEN_DAY+"0000");					
				resp_pstmt2.executeUpdate();
				
				System.out.println("INSERT INTO NEO_AR_RESPLOG");
				
				//RESPLOG 처리3
				String RESPLOG_SQL3  =  " UPDATE NEO_AR_RESPLOG SET OPEN_DAY = SUBSTRING(OPEN_DT,1,8) WHERE OPEN_DAY IS NULL ";
				
				resp_pstmt3 = resp_conn.prepareStatement(RESPLOG_SQL3);
				resp_pstmt3.executeUpdate();
				
				//RESPLOG 처리4
				String RESPLOG_SQL4  =  " UPDATE NEO_AR_RESPLOG "+
										" SET DAYOFWEEK = "+
										" (SELECT CD FROM NEO_WEEKCD "+
										" WHERE NEO_WEEKCD.YMD = SUBSTRING(NEO_AR_RESPLOG.OPEN_DT,1,8)) "+
										" WHERE NEO_AR_RESPLOG.DAYOFWEEK IS NULL ";
				
				resp_pstmt4 = resp_conn.prepareStatement(RESPLOG_SQL4);
				resp_pstmt4.executeUpdate();
			
				//LINKLOG 처리1
				String LINKLOG_SQL  =  " DELETE FROM NEO_AR_LINKLOG WHERE CLICK_DAY = (SELECT SUBSTRING(MAX(CLICK_DAY),1,8) FROM NEO_AR_LINKLOG) ";
				
				link_pstmt = resp_conn.prepareStatement(LINKLOG_SQL);
				link_pstmt.executeUpdate();
				
				//LINKLOG 처리2
				String LINKLOG_SQL2  =  " INSERT INTO NEO_AR_LINKLOG "+
										" (DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,TASK_NO,SUB_TASK_NO,LINK_NO,CLICK_DT,TARGET_GRP_TY,CLICK_AR_AMT,VALID_AR_AMT) "+
										" SELECT DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,A.TASK_NO,A.SUB_TASK_NO,A.LINK_NO,CLICK_DT,TARGET_GRP_TY,SUM(CLICK_AMT),SUM(VALID_AMT) "+
										" FROM NEO_LINKLOG A LEFT OUTER JOIN "+
										" (SELECT TASK_NO,SUB_TASK_NO,CUST_ID, MIN(LINK_NO) AS LINK_NO, 1 AS VALID_AMT "+
										" FROM NEO_LINKLOG "+
										" WHERE CLICK_DT >= ? "+
										" GROUP BY TASK_NO,SUB_TASK_NO,CUST_ID) B "+
										" ON A.TASK_NO = B.TASK_NO AND A.SUB_TASK_NO = B.SUB_TASK_NO AND A.CUST_ID = B.CUST_ID AND A.LINK_NO = B.LINK_NO "+
										" WHERE CLICK_DT >= ? "+
										" GROUP BY DEPT_NO,USER_ID,CAMP_TY,CAMP_NO,A.TASK_NO,A.SUB_TASK_NO,A.LINK_NO,CLICK_DT,TARGET_GRP_TY ";
				
				link_pstmt2 = resp_conn.prepareStatement(LINKLOG_SQL2);
				link_pstmt2.setString(1,sCLICK_DAY);
				link_pstmt2.setString(2,sCLICK_DAY);					
				link_pstmt2.executeUpdate();
				
				System.out.println("INSERT INTO NEO_AR_LINKLOG");
				
				//LINKLOG 처리3
				String LINKLOG_SQL3  =  " UPDATE NEO_AR_LINKLOG SET CLICK_DAY =SUBSTRING(CLICK_DT,1,8) WHERE CLICK_DAY IS NULL ";
				
				link_pstmt3 = resp_conn.prepareStatement(LINKLOG_SQL3);
				link_pstmt3.executeUpdate();
				
				//LINKLOG 처리4
				String LINKLOG_SQL4  =  " UPDATE NEO_AR_LINKLOG "+
										" SET DAYOFWEEK = "+
										" (SELECT CD FROM NEO_WEEKCD "+
										" WHERE NEO_WEEKCD.YMD = SUBSTRING(NEO_AR_LINKLOG.CLICK_DT,1,8)) "+
										" WHERE NEO_AR_LINKLOG.DAYOFWEEK IS NULL ";
				
				link_pstmt4 = resp_conn.prepareStatement(LINKLOG_SQL4);
				link_pstmt4.executeUpdate();
			
				SP_AG_DOMAIN_LOG(sSEND_DAY);
				
				//util.log(pos,"SP_AG_CAMP_LOG =========> 종료");
				
			
			} catch (Err err) {
				util.log(err.getEXStr());
			} catch (Exception e) {
				util.log(e.toString());
			} finally {
				try {
					//PreparedStatement 해제
					if(get_pstmt != null) get_pstmt.close();
					if(send_pstmt != null) send_pstmt.close();
					if(send_pstmt2 != null) send_pstmt2.close();
					if(send_pstmt3 != null) send_pstmt3.close();
					if(send_pstmt4 != null) send_pstmt4.close();
					
					if(resp_pstmt != null) resp_pstmt.close();
					if(resp_pstmt2 != null) resp_pstmt2.close();
					if(resp_pstmt3 != null) resp_pstmt3.close();
					if(resp_pstmt4 != null) resp_pstmt4.close();
					
					if(link_pstmt != null) link_pstmt.close();
					if(link_pstmt2 != null) link_pstmt2.close();
					if(link_pstmt3 != null) link_pstmt3.close();
					if(link_pstmt4 != null) link_pstmt4.close();
					
				} catch (Exception e) {}
				try {
					//DB CONNECTION CLOSE
					resp_conn.setAutoCommit(true);	
					//dbutil.closeConn(resp_conn);
					resp_conn.close();
					//로그 파일을 CLOSE 한다.
					//resp_err_log_out.close();
					
				} catch (Exception e1) {
				}
			}		
		}
		
		//SP_AG_DOMAIN_LOG 처리
		private void SP_AG_DOMAIN_LOG(String current_day)
		{
			//util.log(pos,"SP_AG_DOMAIN_LOG =========> 시작");
			//수신확인 PreparedStatement
			PreparedStatement del_pstmt = null;
			PreparedStatement sel_pstmt = null;
			PreparedStatement ins_pstmt = null;
			PreparedStatement ins_pstmt2 = null;
			try
			{		
				String send_day =""; 
			
				if(current_day !="" )
					send_day = current_day;
				//데이터베이스 연결을 수행한다.	
				Connection resp_conn = this.getConnection();
				//resp_conn = dbutil.getConn("Resp");	
				
				//DOMAINLOG 삭제	
				String Del_SQL = " DELETE FROM NEO_AR_DOMAINLOG WHERE SEND_DAY = ? ";
				del_pstmt = resp_conn.prepareStatement(Del_SQL);
				del_pstmt.setString(1,send_day);				
				del_pstmt.executeUpdate();			
				
				// NEO_SENDLOG 데이터 조회
				int sTask_No =0;
				int sSub_Task_No =0;
					
				String Sel_SQL = " SELECT DISTINCT TASK_NO,SUB_TASK_NO FROM NEO_SENDLOG WHERE SEND_DT >= ? ";
				
				sel_pstmt = resp_conn.prepareStatement(Sel_SQL);
				sel_pstmt.setString(1,send_day);	
				result = sel_pstmt.executeQuery();
				
				while(result.next()){
					
					sTask_No = Integer.parseInt(result.getString("TASK_NO"));
					sSub_Task_No = Integer.parseInt(result.getString("SUB_TASK_NO"));
					
					//NEO_AR_DOMAINLOG 등록1
					String In_SQL = " INSERT INTO NEO_AR_DOMAINLOG "+
									 " (TASK_NO,SUB_TASK_NO,SEND_RCODE,RCODE_STEP1,RCODE_STEP2,RCODE_STEP3,CUST_DOMAIN,SEND_AR_AMT,SEND_DAY) "+
									 " SELECT A.TASK_NO,A.SUB_TASK_NO,A.SEND_RCODE,A.RCODE_STEP1,A.RCODE_STEP2,A.RCODE_STEP3, "+
									 " A.CUST_DOMAIN,SUM(A.SEND_AMT) AS SEND_AMT,MAX(A.[YEAR] + A.[MONTH] + A.[DAY]) AS YMD "+
									 " FROM NEO_SENDLOG A "+
									 " JOIN (SELECT TASK_NO, SUB_TASK_NO, CUST_EM, CUST_NM, CUST_ID, MAX(RETRY_CNT) AS MAX_RETRY_CNT "+
									 " FROM NEO_SENDLOG WHERE TASK_NO = ? AND SUB_TASK_NO = ? "+
									 " GROUP BY TASK_NO, SUB_TASK_NO, CUST_EM, CUST_NM, CUST_ID) B "+
									 " ON A.TASK_NO = B.TASK_NO "+
									 " AND A.SUB_TASK_NO = B.SUB_TASK_NO "+
									 " AND A.CUST_EM = B.CUST_EM "+
									 " AND A.CUST_NM = B.CUST_NM "+
									 " AND A.CUST_ID = B.CUST_ID "+
									 " AND A.RETRY_CNT = B.MAX_RETRY_CNT "+
									 " JOIN (SELECT TASK_NO, SUB_TASK_NO, CUST_DOMAIN "+
									 " FROM NEO_SENDLOG WHERE TASK_NO = ? AND SUB_TASK_NO = ? AND RETRY_CNT = 0 AND  rownum between 1 and 19	 "+
									 " GROUP BY TASK_NO, SUB_TASK_NO, CUST_DOMAIN "+
									 " ORDER BY SUM(SEND_AMT) DESC) C "+
									 " ON A.TASK_NO = C.TASK_NO "+
									 " AND A.SUB_TASK_NO = C.SUB_TASK_NO "+
									 " AND A.CUST_DOMAIN = C.CUST_DOMAIN " +
									 " AND A.TASK_NO = ? AND A.SUB_TASK_NO = ? "+
									 " GROUP BY A.TASK_NO,A.SUB_TASK_NO,A.SEND_RCODE,RCODE_STEP1,RCODE_STEP2,RCODE_STEP3,A.CUST_DOMAIN ";
					
					ins_pstmt = resp_conn.prepareStatement(In_SQL);
					ins_pstmt.setInt(1,sTask_No);
					ins_pstmt.setInt(2,sSub_Task_No);	
					ins_pstmt.setInt(3,sTask_No);
					ins_pstmt.setInt(4,sSub_Task_No);
					ins_pstmt2.setInt(5,sTask_No);
					ins_pstmt2.setInt(6,sSub_Task_No);
					ins_pstmt.executeUpdate();
					
					System.out.println("INSERT INTO NEO_AR_DOMAINLOG 1");
					
					//NEO_AR_DOMAINLOG 등록 2
					String In_SQL2 = " INSERT INTO NEO_AR_DOMAINLOG "+
									" 	(TASK_NO,SUB_TASK_NO,SEND_RCODE,RCODE_STEP1,RCODE_STEP2,RCODE_STEP3,CUST_DOMAIN,SEND_AR_AMT,SEND_DAY) "+
									" SELECT A.TASK_NO,A.SUB_TASK_NO,A.SEND_RCODE,A.RCODE_STEP1,A.RCODE_STEP2,A.RCODE_STEP3, "+
									" 	'Etc' AS CUST_DOMAIN,SUM(A.SEND_AMT) AS SEND_AMT,MAX(A.[YEAR] + A.[MONTH] + A.[DAY]) AS YMD "+
									" FROM NEO_SENDLOG A "+
									" JOIN (SELECT TASK_NO, SUB_TASK_NO, CUST_EM, CUST_NM, CUST_ID, MAX(RETRY_CNT) AS MAX_RETRY_CNT "+
									" 	FROM NEO_SENDLOG WHERE TASK_NO = ? AND SUB_TASK_NO = ? "+
									" 	GROUP BY TASK_NO, SUB_TASK_NO, CUST_EM, CUST_NM, CUST_ID) B "+
									" ON A.TASK_NO = B.TASK_NO "+
									" 	AND A.SUB_TASK_NO = B.SUB_TASK_NO "+
									" 	AND A.CUST_EM = B.CUST_EM "+
									" 	AND A.CUST_NM = B.CUST_NM "+
									" 	AND A.CUST_ID = B.CUST_ID "+
									" 	AND A.RETRY_CNT = B.MAX_RETRY_CNT "+
									" WHERE NOT EXISTS "+
									" 	(SELECT '1' FROM "+
									"		(SELECT TASK_NO, SUB_TASK_NO, CUST_DOMAIN "+
									" 			FROM NEO_SENDLOG WHERE TASK_NO = ? AND SUB_TASK_NO = ? AND RETRY_CNT = 0 AND  rownum between 1 and 19 "+
									"		GROUP BY TASK_NO, SUB_TASK_NO, CUST_DOMAIN "+
									"		ORDER BY SUM(SEND_AMT) DESC) C "+
									"	WHERE C.CUST_DOMAIN = A.CUST_DOMAIN) " +
									" AND A.TASK_NO = ? AND A.SUB_TASK_NO = ? "+
									" GROUP BY A.TASK_NO,A.SUB_TASK_NO,A.SEND_RCODE,RCODE_STEP1,RCODE_STEP2,RCODE_STEP3 ";
			
					ins_pstmt2 = resp_conn.prepareStatement(In_SQL2);
					ins_pstmt2.setInt(1,sTask_No);
					ins_pstmt2.setInt(2,sSub_Task_No);
					ins_pstmt2.setInt(3,sTask_No);
					ins_pstmt2.setInt(4,sSub_Task_No);
					ins_pstmt2.setInt(5,sTask_No);
					ins_pstmt2.setInt(6,sSub_Task_No);
					ins_pstmt2.executeUpdate();
					
					System.out.println("INSERT INTO NEO_AR_DOMAINLOG 2");
					
				}
				
				//util.log(pos,"SP_AG_DOMAIN_LOG =========> 처리 완료");
			
			} catch (Err err) {
				//util.log(err.getEXStr());
			} catch (Exception e) {
				//util.log(e.toString());
			} finally {
				try {
					//PreparedStatement 해제
					if(del_pstmt != null) del_pstmt.close();
					if(sel_pstmt != null) sel_pstmt.close();
					if(ins_pstmt != null) del_pstmt.close();
					if(ins_pstmt2 != null) ins_pstmt2.close();
				} catch (Exception e) {}
				try {
					//DB CONNECTION CLOSE
					resp_conn.setAutoCommit(true);	
					//dbutil.closeConn(resp_conn);
					resp_conn.close();
					//로그 파일을 CLOSE 한다.
					//resp_err_log_out.close();
					
				} catch (Exception e1) {
				}
			}		
		}		
		
		
}
