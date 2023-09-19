package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.lang.*;
import java.sql.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: TimeThread.java
 * Version 		: 1.0
 * 작성일 		: 2004/12/02
 * 작성자 		: 오범석
 * 수정일 		:
 * 수정자 		:
 * 설명			: 타임을 체크한다.
 *
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class TimeThread extends Thread {

	/** 모듈명 설정 */
	private String MNAME = "TimeThread";

	private WebUtil util = null;

	/**
	 * 생성자.
 	 */
	public TimeThread() {
		try {
			util = new WebUtil();
			util.log(MNAME,"TimeThread 작업 시작.............");
		} catch (Exception e) {
			util.log(MNAME,"TimeThread 작동을 실패했습니다.\n"+e.toString());
		}
	}

	/**
	 *	처리 쓰레드
	 **/
	public void run() {
		try {
			//시간을 체크하여 처리를 수행한다.
			//수신 로그 처리
			String cur_time = null;		//현재시간
			String pre_time = null;		//이전시간
			Object workTime = null;		//작업시간
			Vector workList = null;		//작업리스트
			String[] workInfo = null;	//작업정보
			boolean flag = false;		//처리여부
			while(true) {
				cur_time = util.getDate(Code.TM_YMDHMS);	//YYYYMMDDHHMISS
				//System.out.println(cur_time);
				cur_time = cur_time.substring(0,12);

				if(!cur_time.equals(pre_time)) {		//1분이 지났다면 작업수행

					Hashtable workListHash = getWorkList();
					Iterator iter = workListHash.keySet().iterator();
					while(iter.hasNext()) {
						flag = false;
						workTime = iter.next();
						//System.out.println("workTime : " + workTime);

						//분단위 처리 수행
						if(workTime.equals("")) {
							flag = true;
						}
						//시단위 처리 수행
						if(util.extData(cur_time,10,12).equals(workTime)) {
							flag = true;
						}
						//일단위 처리 수행
						if(util.extData(cur_time,8,12).equals(workTime)) {
							flag = true;
						}
						//월단위 처리 수행
						if(util.extData(cur_time,6,12).equals(workTime)) {
							flag = true;
						}
						//년단위 처리 수행
						if(util.extData(cur_time,4,12).equals(workTime)) {
							flag = true;
						}

						if(flag) {
							workList = (Vector)workListHash.get(workTime);
							for(int i=0; i<workList.size(); i++) {
								workInfo = (String[])workList.get(i);
								//System.out.print(cur_time + " : ");
								//System.out.print(workTime + "|");
								//System.out.print(workInfo[0] + "|");
								//System.out.println(workInfo[1]);
								new TmWorkThread(util, workInfo[0], workInfo[1]).start();
							}
						}
					}

				}

				pre_time = cur_time;

				sleep(1000);
      }

		} catch (Exception e) {
			util.log(e.toString());
		} finally {
		}
	}

	/**
	 * 작업 클래스 리스트를 추출한다.
	 * @return	Hashtable	작업리스트클래스 리스트
	 */
	public Hashtable getWorkList() {

		Hashtable workListHash = new Hashtable();
		Vector workClassList = null;
		String workList = null;
		String workNm = null;
		String workTime = null;
		String workClass = null;

		try {
			workList = util.getEnv("TM_WORK_LIST");
			try {
				StringTokenizer token = new StringTokenizer(workList,"|");
				while(token.hasMoreTokens()) {
					workNm = token.nextToken();
					workTime = util.getEnv(workNm + "_RUN_TERM");
					workClass = util.getEnv(workNm + "_RUN_CLASS");

					if(workTime == null) workTime = "";

					if(workClass != null) {
						if(workListHash.containsKey(workTime)) {
							workClassList = (Vector)workListHash.get(workTime);
						} else {
							workClassList = new Vector();
						}

						String[] workInfo = new String[2];
						workInfo[0] = workNm;
						workInfo[1] = workClass;

						workClassList.add(workInfo);
						workListHash.put(workTime, workClassList);
					}
				}
			} catch (Exception e) {
				util.log(e.toString());
			}
		} catch (Exception e) {
			util.log(e.toString());
		}
		return workListHash;
	}

}
