package com.mp.util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.mp.exception.Err;

/**
 * <pre>
 * 프로그램명	: RespThread.java
 * Version 		: 1.0
 * 작성일 		: 2004/11/11
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: 수신 로그를 DB에 저장한다.
 *
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class RespThread_bk extends Thread {

	/** 모듈명 설정 */
	private String MNAME = "RespThread";

	/** 데이터 베이스 UTIL */
	private DBUtil dbutil = null;	

	/** 처리 로그 파일 */
	private String log_file = null;	
	private String log_pos = null;	
	private String PATH_GUBUN = null;	
	private String resp_log_date = null;	
	private int log_term = 0;	//로그 처리 주기

	/** 수신로그 DB Connection */
	Connection resp_conn = null;
	/** 링크클릭 DB Connection */
	Connection link_conn = null;

	/** RespLog 객체  */
	RespLog resplog = null;

	/** 처리 로그 파일 */	
	private static PrintWriter resp_err_log_out = null;

	/** 로그 구분자 */
	private static String deli = "------------------------------------------------------------------------------";

	/**	
	 * 생성자.
	 * @param	log_file	처리 대상 로그파일 경로
 	 */
	public RespThread_bk(String log_file, String log_pos, String PATH_GUBUN, String resp_log_date, int log_term, RespLog resplog) {
		this.dbutil = new DBUtil();
		this.log_file = log_file;
		this.log_pos = log_pos;
		this.PATH_GUBUN = PATH_GUBUN;
		this.resp_log_date = resp_log_date;
		this.log_term = log_term;
		this.resplog = resplog;
		try {
			resp_err_log_out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(log_file+".result",true)));
		} catch (Exception e) {
			System.out.println(MNAME+" 에러 로그 생성을 실패했습니다.");
		}

	}

	/**
	 *	처리 쓰레드 
	 **/
	public void run() {

		dgWork();
		String pos = MNAME + ".run()";

		try {
			//로그 파일을 읽어 DB에 저장한다.
			String neo_product = resplog.getEnv("ENVNeo_product");		
			if(neo_product.indexOf("MP") != -1) {
				doWork();		//MPV30용
			} else if(neo_product.indexOf("TS") != -1) {
				doTSWork();
			} else {
				log("처리할 제품 정보가 없습니다.\n[ENVNeo_product 정보:" + neo_product +"]");
			}
			
		} catch (Err err) {
			log(err.getEXStr());
		} catch (Exception e) {
			log(e.toString());
		}
	}

	//추가
	  public void dgWork() {
		    PreparedStatement resp_pstmt = null;

		    String RESP_SQL = "INSERT INTO NEO_RESPLOG (OPEN_DT,DEPT_NO,SUB_TASK_NO, TASK_NO,  USER_ID,CUST_ID, RESP_AMT, CAMP_NO, CAMP_TY, TARGET_GRP_TY )VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		    String line = "";

		    String resp_open_dt = "";
		    String resp_target_grp_ty = "";
		    String resp_task_no = "";
		    String resp_sub_task_no = "";
		    String resp_dept_no = "";
		    String resp_user_id = "";
		    String resp_camp_ty = "";
		    String resp_camp_no = "";
		    String resp_cust_id = "";
		    BufferedReader reader = null;
		    try
		    {
		      this.resp_conn = this.dbutil.getConn("Resp");

		      this.resp_conn.setAutoCommit(false);

		      reader = new BufferedReader(new FileReader(this.log_file));

		      resp_pstmt = this.resp_conn.prepareStatement(RESP_SQL);

		      String temp1 = "";
		      String temp2 = "";
		      String temp3 = "";
		      while ((line = reader.readLine()) != null) {
		        if (line == null) {
		          break;
		        }
		        temp1 = line;

		        temp2 = line;
		        temp3 = temp2;

		        if (line.indexOf("$") == -1) {
		          line = temp2;
		        }
		      }

		      if (line == null) {
		        line = temp1;
		      }

		      StringTokenizer st = new StringTokenizer(line, "|");
		      int stCount = st.countTokens();

		      for (int i = 0; i < stCount; ++i) {
		        if (i == 0)
		          resp_open_dt = st.nextToken();
		        else if (i == 1) {
		          st.nextToken();
		        }
		        else if (i == 2)
		          resp_target_grp_ty = st.nextToken();
		        else if (i == 3)
		          resp_cust_id = st.nextToken();
		        else if (i == 4)
		          resp_task_no = st.nextToken();
		        else if (i == 5)
		          resp_sub_task_no = st.nextToken();
		        else if (i == 6)
		          resp_dept_no = st.nextToken();
		        else if (i == 7)
		          resp_user_id = st.nextToken();
		        else if (i == 8)
		          resp_camp_ty = st.nextToken();
		        else if (i == 9) {
		          resp_camp_no = st.nextToken();
		        }
		      }

		      resp_pstmt.setString(1, resp_open_dt);
		      resp_pstmt.setInt(2, Integer.parseInt(resp_dept_no));
		      resp_pstmt.setInt(3, Integer.parseInt(resp_sub_task_no));
		      resp_pstmt.setInt(4, Integer.parseInt(resp_task_no));
		      resp_pstmt.setString(5, resp_user_id);
		      resp_pstmt.setString(6, resp_cust_id);
		      resp_pstmt.setInt(7, 1);
		      resp_pstmt.setInt(8, Integer.parseInt(resp_camp_no));
		      resp_pstmt.setString(9, resp_camp_ty);
		      resp_pstmt.setString(10, resp_target_grp_ty);

		      resp_pstmt.executeUpdate();

		      this.resp_conn.commit();
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    } finally {
		      try {
		        if ((this.resp_conn != null) || (this.link_conn != null) || (reader != null)) {
		          this.resp_conn.close();

		          reader.close();
		        }
		      } catch (SQLException e) {
		        e.printStackTrace();
		      } catch (IOException e) {
		        e.printStackTrace();
		      }
		    }
		  }	
	

	/**
	 *
	 *	로그 파일의 데이터를 DB로 로딩한다. 
	 *
	 */
	public void doWork() {

	//System.out.println("111111111111111111111");
		
		//수신확인 PreparedStatement
		PreparedStatement resp_pstmt = null;
		//링크클릭 PreparedStatement
		PreparedStatement link_pstmt = null;

		try {	
			//데이터베이스 연결을 수행한다.
			resp_conn = dbutil.getConn("Resp");			
			link_conn = dbutil.getConn("Link");			
			resp_conn.setAutoCommit(false);	
			link_conn.setAutoCommit(false);	

			//로그 파일을 오픈한다.
			BufferedReader reader = new BufferedReader(
										new FileReader(log_file));		

			//전체 건수 저장
			long tot_cnt = 0;
			//처리 건수 저장
			long proc_cnt = 0;
			//실패 건수 저장
			long fail_cnt = 0;
			//수신 일자 지난 건수 저장
			long resp_end_cnt = 0;
			
//	      long tot_cnt = 0L;
//
//	      long proc_cnt = 0L;
//
//	      long fail_cnt = 0L;
//
//	      long resp_end_cnt = 0L;

			//DB 처리를 위한 SQL문을 PRE PARSING한다.
			//수신확인 SQL
			String RESP_SQL = " INSERT INTO NEO_RESPLOG					" + 
							  "		  (DEPT_NO, CAMP_TY, CAMP_NO,					" +
							  "		   TASK_NO, SUB_TASK_NO, CUST_ID,			" +
							  "		   USER_ID, OPEN_DT, BLOCKED_YN,			" +
							  "		   RESP_AMT, YEAR, MONTH, DAY, HOUR, 	" +
							  "		   TARGET_GRP_TY)											" +
							  "	VALUES(?,?,?,														" +	
							  "		   ?,?,?,															" +
							  "		   ?,?,?,															" +
							  "		   ?,?,?,?,?,													" + 
							  "		   ?)																	";

			//링크클릭 SQL
			String LINK_SQL = " INSERT INTO NEO_LINKLOG					" + 
							  "		  (DEPT_NO, CAMP_TY, CAMP_NO,					" +
							  "		   TASK_NO, SUB_TASK_NO, LINK_NO,			" +
							  "		   CUST_ID,	USER_ID, CLICK_DT, 				" +
							  "		   CLICK_AMT, YEAR, MONTH, DAY, HOUR,	" +
								"		   TARGET_GRP_TY)											" +
							  "	VALUES(?,?,?,														" +	
							  "		   ?,?,?,															" +
							  "		   ?,?,?,															" +
							  "		   ?,?,?,?,?,													" +
							  "			 ?)																	";
							
			resp_pstmt = resp_conn.prepareStatement(RESP_SQL);
			link_pstmt = link_conn.prepareStatement(LINK_SQL);

			//로그 유형
			String type = null;	//유형
			String resp_end_dt = null;	//수신 가능 일자
			String resp_dt = null;		//수신 일자

			//수신 로그 변수 선언
			String resp_dept_no = null;
			String resp_camp_ty = null;
			String resp_camp_no = null;
			String resp_task_no = null;
			String resp_sub_task_no = null;
			String resp_cust_id = null;
			String resp_user_id = null;
			String resp_blocked_yn = null;
			String resp_target_grp_ty = null;

			//링크 클릭 변수 선언
			String link_dept_no = null;
			String link_camp_ty = null;
			String link_camp_no = null;
			String link_task_no = null;
			String link_sub_task_no = null;
			String link_link_no = null;
			String link_cust_id = null;
			String link_user_id = null;
			String link_target_grp_ty = null;

			//System.out.println("2222222222222222222222222");
			
			//로그 한 라인 저장
			String line = null;
			while((line = reader.readLine()) != null) {
				try {

					//전체 건수 증가
					tot_cnt++;
					//System.out.println("333333333333333 tot_cnt : "  + tot_cnt);

					//StringToken으로 라인을 분석한다.
					StringTokenizer st = new StringTokenizer(line,"|");		
						
					//수신 일자를 먼저 읽어온다.
					if(st.hasMoreElements()) resp_dt = (String)st.nextElement();
					else {
						fail_cnt++;
						fail_log(line+"\n라인정보없음");	
						continue;
					}
				
					//수신 일자 체크를 수행한다.
					if(resp_dt.length() != 12) {
						fail_cnt++;
						fail_log(line+"\n수신일자 오류");	
						continue;
					}

					//수신 가능 일자를 먼저 읽어온다.
					if(st.hasMoreElements()) resp_end_dt = (String)st.nextElement();
					else {
						fail_cnt++;
						fail_log(line+"\n수신가능일자없음");	
						continue;
					}

					//수신 가능 일자를 체크한다.
					if(dbutil.parseLong(resp_end_dt) < dbutil.parseLong(resp_dt)) {
						resp_end_cnt++;
						continue;
					}

					if(st.hasMoreElements()) type = (String)st.nextElement();
					else {
						fail_cnt++;
						fail_log(line);	
						continue;
					}

					/*================================================
						저장 데이터 타입을 구분한다.
						000 ==> 수신확인 (NEO_RESPLOG)
						001 ==> 수신거부 (NEO_RESPLOG)
						002 ==> 링크클릭 (NEO_LINKLOG)
					=================================================*/
					if(type.equals("000")) {				//수신확인

						//System.out.println("44444444444 이건??? :"+st.countTokens());
						System.out.println("");
						while(st.hasMoreTokens()){
						//System.out.println("55555555555 이건??? : "+st.nextToken());
						}
						if(st.countTokens() != 8) {	
							fail_cnt++;
							fail_log(line);	
							continue;
						}
					
						//회원ID
						resp_cust_id = (String)st.nextElement();
						//TASK_NO
						resp_task_no = (String)st.nextElement();
						//SUB_TASK_NO
						resp_sub_task_no = (String)st.nextElement();
						//DEPT_NO
						resp_dept_no = (String)st.nextElement();
						//USER_NO
						resp_user_id = (String)st.nextElement();
						//CAMP_TY
						resp_camp_ty = (String)st.nextElement();
						//CAMP_NO
						resp_camp_no = (String)st.nextElement();
						//TARGET_GRP_TY
						resp_target_grp_ty = (String)st.nextElement();

						//System.out.println(" resp_cust_id : "  + resp_cust_id);
						//System.out.println(" resp_task_no : "  + resp_task_no);
						//System.out.println(" resp_sub_task_no : "  + resp_sub_task_no);
						//System.out.println(" resp_dept_no : "  + resp_dept_no);
						//System.out.println(" resp_user_id : "  + resp_user_id);
						//System.out.println(" resp_camp_ty : "  + resp_camp_ty);
						//System.out.println(" resp_camp_no : "  + resp_camp_no);
						//System.out.println(" resp_target_grp_ty : "  + resp_target_grp_ty);
						
						
						resp_pstmt.setLong(1,dbutil.parseLong(resp_dept_no));
						resp_pstmt.setString(2,resp_camp_ty);
						resp_pstmt.setLong(3,dbutil.parseLong(resp_camp_no));
						resp_pstmt.setLong(4,dbutil.parseLong(resp_task_no));
						resp_pstmt.setLong(5,dbutil.parseLong(resp_sub_task_no));
						resp_pstmt.setString(6,resp_cust_id);
						resp_pstmt.setString(7,resp_user_id);
						resp_pstmt.setString(8,resp_dt);
						resp_pstmt.setString(9,"N");
						resp_pstmt.setLong(10,1);
						resp_pstmt.setString(11,dbutil.extData(resp_dt,0,4));
						resp_pstmt.setString(12,dbutil.extData(resp_dt,4,6));
						resp_pstmt.setString(13,dbutil.extData(resp_dt,6,8));
						resp_pstmt.setString(14,dbutil.extData(resp_dt,8,10));
						resp_pstmt.setString(15,resp_target_grp_ty);
						resp_pstmt.executeUpdate();
					} else if(type.equals("001")) {			//수신거부
						if(st.countTokens() != 8) {	
							fail_cnt++;
							fail_log(line);	
							continue;
						}

						//회원ID
						resp_cust_id = (String)st.nextElement();
						//TASK_NO
						resp_task_no = (String)st.nextElement();
						//SUB_TASK_NO
						resp_sub_task_no = (String)st.nextElement();
						//DEPT_NO
						resp_dept_no = (String)st.nextElement();
						//USER_NO
						resp_user_id = (String)st.nextElement();
						//CAMP_TY
						resp_camp_ty = (String)st.nextElement();
						//CAMP_NO
						resp_camp_no = (String)st.nextElement();
						//TARGET_GRP_TY
						resp_target_grp_ty = (String)st.nextElement();

						resp_pstmt.setLong(1,dbutil.parseLong(resp_dept_no));
						resp_pstmt.setString(2,resp_camp_ty);
						resp_pstmt.setLong(3,dbutil.parseLong(resp_camp_no));
						resp_pstmt.setLong(4,dbutil.parseLong(resp_task_no));
						resp_pstmt.setLong(5,dbutil.parseLong(resp_sub_task_no));
						resp_pstmt.setString(6,resp_cust_id);
						resp_pstmt.setString(7,resp_user_id);
						resp_pstmt.setString(8,resp_dt);
						resp_pstmt.setString(9,"Y");
						resp_pstmt.setLong(10,1);
						resp_pstmt.setString(11,dbutil.extData(resp_dt,0,4));
						resp_pstmt.setString(12,dbutil.extData(resp_dt,4,6));
						resp_pstmt.setString(13,dbutil.extData(resp_dt,6,8));
						resp_pstmt.setString(14,dbutil.extData(resp_dt,8,10));
						resp_pstmt.setString(15,resp_target_grp_ty);
						resp_pstmt.executeUpdate();
					} else if(type.equals("002")) {			//링크클릭
						if(st.countTokens() != 10) {	
							fail_cnt++;
							fail_log(line);	
							continue;
						}

						//회원ID
						link_cust_id = (String)st.nextElement();
						//TASK_NO
						link_task_no = (String)st.nextElement();
						//SUB_TASK_NO
						link_sub_task_no = (String)st.nextElement();
						//DEPT_NO
						link_dept_no = (String)st.nextElement();
						//USER_NO
						link_user_id = (String)st.nextElement();
						//CAMP_TY
						link_camp_ty = (String)st.nextElement();
						//CAMP_NO
						link_camp_no = (String)st.nextElement();
						//TARGET_GRP_TY
						link_target_grp_ty = (String)st.nextElement();
						//LINK_NO
						link_link_no = (String)st.nextElement();

						link_pstmt.setLong(1,dbutil.parseLong(link_dept_no));
						link_pstmt.setString(2,link_camp_ty);
						link_pstmt.setLong(3,dbutil.parseLong(link_camp_no));
						link_pstmt.setLong(4,dbutil.parseLong(link_task_no));
						link_pstmt.setLong(5,dbutil.parseLong(link_sub_task_no));
						link_pstmt.setLong(6,dbutil.parseLong(link_link_no));
						link_pstmt.setString(7,link_cust_id);
						link_pstmt.setString(8,link_user_id);
						link_pstmt.setString(9,resp_dt);
						link_pstmt.setLong(10,1);
						link_pstmt.setString(11,dbutil.extData(resp_dt,0,4));
						link_pstmt.setString(12,dbutil.extData(resp_dt,4,6));
						link_pstmt.setString(13,dbutil.extData(resp_dt,6,8));
						link_pstmt.setString(14,dbutil.extData(resp_dt,8,10));
						link_pstmt.setString(15,link_target_grp_ty);
						link_pstmt.executeUpdate();
					}

					//COMMIT 수행 여부를 체크한다.
					if((proc_cnt%10000) == 0) {
						resp_conn.commit();	
						link_conn.commit();	
					}


					//처리 건수 증가
					proc_cnt++;

				} catch (Exception e) {
					//실패 건수 증가
					fail_cnt++;
					log("[라인정보] "+line+"\n"+e.toString());
				}
			}

			//commit 수행
			resp_conn.commit();
			link_conn.commit();

			//결과 로그 출력
			//전체 건수, 총처리 건수, 실패건수 기록
			String result_msg = "[전체 건수] " + tot_cnt + "\n";
				   result_msg += "[처리건수] " + proc_cnt + "\n";
				   result_msg += "[수신일지난건수] " + resp_end_cnt + "\n"; 
				   result_msg += "[실패건수] " + fail_cnt;

			log(result_msg);

		} catch (Err err) {
			log(err.getEXStr());
		} catch (Exception e) {
			log(e.toString());
		} finally {
			try {
				//PreparedStatement 해제
				if(resp_pstmt != null) resp_pstmt.close();
				if(link_pstmt != null) link_pstmt.close();
			} catch (Exception e) {}
			try {
				//DB CONNECTION CLOSE
				resp_conn.setAutoCommit(true);	
				dbutil.closeConn(resp_conn);
				link_conn.setAutoCommit(true);	
				dbutil.closeConn(link_conn);

				//로그 파일을 CLOSE 한다.
				resp_err_log_out.close();
				
			} catch (Exception e1) {
			}
		}
	}

	/**
	 *
	 *	로그 파일의 데이터를 DB로 로딩한다. 
	 *
	 */
	public void doTSWork() {

		//수신확인 PreparedStatement
		PreparedStatement resp_pstmt = null;

		try {	
			//데이터베이스 연결을 수행한다.
			resp_conn = dbutil.getConn("Resp");			

			//로그 파일을 오픈한다.
			BufferedReader reader = new BufferedReader(
										new FileReader(log_file));		

			//전체 건수 저장
			long tot_cnt = 0;
			//처리 건수 저장
			long proc_cnt = 0;
			//실패 건수 저장
			long fail_cnt = 0;

			//DB 처리를 위한 SQL문을 PRE PARSING한다.
			//수신확인 SQL
      String RESP_SQL = "INSERT INTO TS_RESPONSELOG       " +
                        " (RSID, MID, SUBID, REFMID,      " +
                        "  RSDATE, RNAME, RID, RMAIL)     " +
                        " VALUES(?,?,?,?,?,?,?,?)         ";


			resp_pstmt = resp_conn.prepareStatement(RESP_SQL);

			//수신 로그 집계변수 선언
			Hashtable resp_hash = new Hashtable();
			
			//수신 로그 변수 선언
			String resp_dt = null;		//수신 일자
			String resp_rsid = null;
			String resp_mid = null;
			String resp_subid = null;
			String resp_refmid = null;
			String resp_rname = null;
			String resp_rid = null;
			String resp_rmail = null;

			//로그 한 라인 저장
			String line = null;
			Enumeration enum2 = null;
			String resp_hash_key = null;
			
			while((line = reader.readLine()) != null) {
				try {

					//전체 건수 증가
					tot_cnt++;

					//StringToken으로 라인을 분석한다.
					StringTokenizer st = new StringTokenizer(line,"|");
						
					//수신 일자
					if(st.hasMoreElements()) {
						resp_dt = (String)st.nextElement();
					} else {
						fail_cnt++;
						fail_log(line+"\n라인정보없음");	
						continue;
					}

					//수신 일자 체크를 수행한다.
					if(resp_dt.length() != 12) {
						fail_cnt++;
						fail_log(line+"\n수신일자 오류");	
						continue;
					}
					
					resp_rsid = (String)st.nextElement();
					resp_mid = (String)st.nextElement();
					resp_rname = (String)st.nextElement();
					resp_rmail  = (String)st.nextElement();
					resp_rid = (String)st.nextElement();
					resp_refmid = (String)st.nextElement();
					resp_subid = (String)st.nextElement();

					resp_pstmt.setLong(1,dbutil.parseLong(resp_rsid));
					resp_pstmt.setLong(2,dbutil.parseLong(resp_mid));
					resp_pstmt.setLong(3,dbutil.parseLong(resp_subid));
					resp_pstmt.setLong(4,dbutil.parseLong(resp_refmid));
					resp_pstmt.setString(5,resp_dt);
					resp_pstmt.setString(6,resp_rname);
					resp_pstmt.setString(7,resp_rid);
					resp_pstmt.setString(8,resp_rmail);
					resp_pstmt.executeUpdate();

					//처리 건수 증가
					proc_cnt++;
					
					//수신 로그 집계변수 설정
					enum2 = resp_hash.keys();
					while(enum2.hasMoreElements()) {
						resp_hash_key = (String)enum2.nextElement();
						if(resp_mid.equals(resp_hash_key)) {
							if(!resp_mid.equals((String)resp_hash.get(resp_hash_key))) {
								resp_hash.put(resp_mid, resp_subid);
							}
						} else {
							resp_hash.put(resp_mid, resp_subid);
						}
					}

				} catch (Exception e) {
					//실패 건수 증가
					fail_cnt++;
					log("[라인정보] "+line+"\n"+e.toString());
				}
			}

			//결과 로그 출력
			//전체 건수, 총처리 건수, 실패건수 기록
			String result_msg = "[전체 건수] " + tot_cnt + "\n";
				   result_msg += "[처리건수] " + proc_cnt + "\n";
				   result_msg += "[실패건수] " + fail_cnt;

			log(result_msg);


			//수신로그 데이터 집계
			enum2 = resp_hash.keys();
			while(enum2.hasMoreElements()) {
				resp_hash_key = (String)enum2.nextElement();
				RESP_SQL =	"	UPDATE TS_RESPONSE_RSINFO A							" +	
							"	SET RSDATE = TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS'),	" +
							"		RSCOUNT = (SELECT COUNT(*)						" +
							"						FROM TS_RESPONSELOG B			" +
							"						WHERE B.MID = A.MID				" +
							"							AND B.SUBID = A.SUBID)		" +
							"	WHERE A.MID = ?		" +
							"		AND A.SUBID = ?	" ;
	
				resp_pstmt = resp_conn.prepareStatement(RESP_SQL);
				resp_pstmt.setLong(1,dbutil.parseLong(resp_hash_key));
				resp_pstmt.setLong(2,dbutil.parseLong((String)resp_hash.get(resp_hash_key)));
				resp_pstmt.executeUpdate();
				resp_pstmt.close();
			}

		} catch (Err err) {
			log(err.getEXStr());
		} catch (Exception e) {
			log(e.toString());
		} finally {
			try {
				//PreparedStatement 해제
				if(resp_pstmt != null) resp_pstmt.close();
			} catch (Exception e) {}
			try {
				//DB CONNECTION CLOSE
				dbutil.closeConn(resp_conn);
				//로그 파일을 CLOSE 한다.
				resp_err_log_out.close();
			} catch (Exception e1) {
			}
		}
	}

	/**
	 *
	 * 로그 파일을 출력한다.
	 * @param msg		로그 메세지
	 *
	 */
	public void fail_log(String msg) {
		msg = "[라인정보] " + msg;
		log(msg);
	}

	/**
	 *
	 * 로그 파일을 출력한다.
	 * @param msg		로그 메세지
	 *
	 */
	public void log(String msg) {
		try {
			resp_err_log_out.println(deli);
			resp_err_log_out.println(msg);
			resp_err_log_out.flush();
		} catch (Exception e) {
            System.out.println("로그 기록에 에러가 발생했습니다.\n" + e.toString());
		}
	}

}
	
		
	

		
