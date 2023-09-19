
package com.mp.util;

import java.io.*;
import java.util.*;

/**
 * <pre>
 * 프로그램유형 : DBPool(java)
 * 프로그램명   : DBPoolLog.java
 * Version      : 1.0
 * 작성일       : 2004/06/15
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 * 수정설명 	: 
 *
 * 설명         : DBPool 로그를 구현한다.
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class DBPoolLog {

    /*=============================================*/
    // Global Zone
    /*=============================================*/
	/**	로그출력여부	*/
    private static boolean LOGVERBOSE = true;
	/**	로그디리미터출력여부	*/
	private static boolean LOGDELIMETER = true;
	/**	로그화일디리미터출력여부	*/
	private static boolean LOGFILEDELIMETER = true;	
	/**	로그출력여부	*/
	private static boolean LOGYN = true;
	/**	로그파일출력여부	*/
	private static boolean LOGFILE = true;
	/**	로그위치	*/
	private String MNAME = "DBPoolLog";
	/**	로그딜리미터 */
	protected static String DELIMETER = "_______________________________________________________________________________";

	/**	에러로그파일 위치 */
    private static String ERRLOGPOS = null;
	/**	에러로그파일 Prefix	*/
    private static String ERRLOGHEAD = null;
	/**	에러파일생성일자	*/
    private static String err_log_date = null;
	/**	에러로그파일명	*/
    private static String err_log_file_name = null;
	/**	에러로그스트림	*/
    private static PrintWriter err_log_out = null;
	/**	메세지 로그파일 위치 */
    private static String MSGLOGPOS = null; 
	/**	메세지 로그파일 Prefix	*/
	private static String MSGLOGHEAD = null;
    
	/**	메세지 파일생성일자	*/
    private static String msg_log_date = null;
	/**	메세지 로그파일명	*/
    private static String msg_log_file_name = null;
	/**	메세지 로그스트림	*/
    private static PrintWriter msg_log_out = null;

	/** 경로 구분자 설정 */
	protected static String PATH_GUBUN = null;
	
	/** 로그 출력 일자별 화일로 할것인지, 아니면 한 화일로 할것인지.    */
	private static boolean daylog_yn;

	/** 생성자선언	*/
	public DBPoolLog() {}	

    /**
     *
     * 로그 스트림을 설정한다.
	 * <br>스트림의 존재여부를 확인하여 설정한다.
     * @param   lhead      로그명의 헤더
     * 			pos		   로그디렉토리
     *
     */
    public void setLogStream(String lhead, String pos) {
		setLogStream(lhead, pos, true);
	}

    /**
     *
     * 로그 스트림을 설정한다.
	 * <br>스트림의 존재여부를 확인하여 설정한다.
     * @param   lhead      로그명의 헤더
     * 			pos		   로그디렉토리
     * 			daylog_yn	로그 출력 구분(일자별 로그, 일자구분없는 로그)
     *
     */
    public void setLogStream(String lhead, String pos, boolean daylog_yn) {

		PATH_GUBUN = System.getProperty("file.separator");	

		this.daylog_yn = daylog_yn;

        ERRLOGPOS = pos;
		ERRLOGHEAD = lhead;
        MSGLOGPOS = pos;
		MSGLOGHEAD = lhead;

        //로그스트림을 생성한다.
        try {
        	String new_err_log_date = null;
 			if(daylog_yn) new_err_log_date = getDate(Code.TM_YMD);
			else new_err_log_date = getDate(Code.TM_YMDHMS);
            if(err_log_out == null) {   //에러로그가 널이라면 생성
            	err_log_date = new_err_log_date;
                err_log_file_name = pos + PATH_GUBUN + lhead + "_err_"+err_log_date;
                if(daylog_yn) err_log_out = new PrintWriter(new FileWriter(err_log_file_name,true));
                else err_log_out = new PrintWriter(new FileWriter(err_log_file_name));
            } else {
            	if(daylog_yn) {
         	       if(!new_err_log_date.equals(err_log_date)) {
           	         	err_log_out.close();
            	        err_log_date = new_err_log_date;
                    	err_log_file_name = pos+ PATH_GUBUN + lhead + "_err_"+err_log_date;
                    	err_log_out = new PrintWriter(new FileWriter(err_log_file_name,true));
                    }
                }
            }
        	String new_msg_log_date = null;
 			if(daylog_yn) new_msg_log_date = getDate(Code.TM_YMD);
			else new_msg_log_date = getDate(Code.TM_YMDHMS);
            if(msg_log_out == null) {   //메세지로그가 널이라면 생성
            	msg_log_date = new_msg_log_date;
                msg_log_file_name = pos+ PATH_GUBUN + lhead + "_msg_"+msg_log_date;
                if(daylog_yn ) msg_log_out = new PrintWriter(new FileWriter(msg_log_file_name,true));
                else msg_log_out = new PrintWriter(new FileWriter(msg_log_file_name));
            } else {
            	if(daylog_yn) {
                	if(!new_msg_log_date.equals(msg_log_date)) {
               	    	msg_log_out.close();
                    	msg_log_date = new_msg_log_date;
                    	msg_log_file_name = pos+ PATH_GUBUN + lhead + "_msg_"+msg_log_date;
                    	msg_log_out = new PrintWriter(new FileWriter(msg_log_file_name,true));                  
                    }
                }
            }
        } catch (IOException e) {
            log(MNAME,"로그 스트림 설정을 실패했읍니다.\n" + e.toString());
        }
    }

 
    /**
     *
     * 로그를 출력한다.
     * @param   msg		로그 메세지
     *
     */
	public void log(String msg) {
        if(LOGVERBOSE) {
	        msg = "[위치:"+MNAME+"] [시각:"+getDate(Code.TM_YMDHMS)+"]\n" +
				  "[메세지]\n"+msg;
			if(LOGYN) {
				if(LOGDELIMETER) {
       		     	System.out.println(DELIMETER);
           		 	System.out.println(msg);
           		 	//System.out.println(DELIMETER);
				} else {
            		System.out.println(msg);
				}
			}
			if(LOGFILE) {
            	logwriter(msg);
			}
        }
    }



    /**
     *
     * 로그를 출력한다.
     * @param   msg			로그 메세지
     * 		    type		로그 파일 기록 유형
	 *						DBPOOL_ERR_LOG(1) ==> 에러로그기록, 
	 *						DBPOOL_MSG_LOG(2) ==> 메세지로그기록, 
	 *						DBPOOL_ALL_LOG(3) ==> 둘다 기록	
     *
     */
	public void log(String msg, int type) {
        if(LOGVERBOSE) {
	        msg = "[위치:"+MNAME+"] [시각:"+getDate(Code.TM_YMDHMS)+"]\n" +
				  "[메세지]\n"+msg;
			if(LOGYN) {
				if(LOGDELIMETER) {
    	        	System.out.println(DELIMETER);
        	    	System.out.println(msg);
            		//System.out.println(DELIMETER);
				} else {
            		System.out.println(msg);
				}
			}
			if(LOGFILE) {
				if(type == Code.DBPOOL_ERR_LOG) {
            		logwriter(msg);
				} else if(type == Code.DBPOOL_MSG_LOG) {
            		msglogwriter(msg);
				} else if(type == Code.DBPOOL_ALL_LOG) {
            		logwriter(msg);
            		msglogwriter(msg);
				}
			}
        }
    }

    /**
     *
     * 로그를 출력한다.
	 * <br>로그발생 위치를 출력한다.
     * @param   loc		로그 기록 위치
     * @param	msg		로그 메세지
     *
     */
    public void log(String loc, String msg) {
        if(LOGVERBOSE) {
	        msg = "[위치 :"+loc+"] [시각 : "+getDate(Code.TM_YMDHMS)+"]\n" +
				  "[메세지]\n"+msg;
			if(LOGYN) {
				if(LOGDELIMETER) {
   		         	System.out.println(DELIMETER);
       		     	System.out.println(msg);
       		     	//System.out.println(DELIMETER);
				} else {
        	    	System.out.println(msg);
				}
			}	
			if(LOGFILE) {
           		logwriter(msg);
			}
        }
    }

    /**
     *
     * 로그를 출력한다.
	 * <br>로그발생 위치를 출력한다.
     * @param   loc		로그 기록 위치
     * 			msg		로그 메세지
     * 		    type		로그 파일 기록 유형
	 *						DBPOOL_ERR_LOG(1) ==> 에러로그기록, 
	 *						DBPOOL_MSG_LOG(2) ==> 메세지로그기록, 
	 *						DBPOOL_ALL_LOG(3) ==> 둘다 기록	
     *
     */
    public void log(String loc, String msg, int type) {
        if(LOGVERBOSE) {
	        msg = "[위치:"+loc+"] [시각:"+getDate(Code.TM_YMDHMS)+"]\n" +
				  "[메세지]\n"+msg;
			if(LOGYN) {
				if(LOGDELIMETER) {
   		         	System.out.println(DELIMETER);
       		     	System.out.println(msg);
          		  	//System.out.println(DELIMETER);
				} else {
       		     	System.out.println(msg);
				}	
			}
			if(LOGFILE) {
				if(type == Code.DBPOOL_ERR_LOG) {
            		logwriter(msg);
				} else if(type == Code.DBPOOL_MSG_LOG) {
            		msglogwriter(msg);
				} else if(type == Code.DBPOOL_ALL_LOG) {
            		logwriter(msg);
            		msglogwriter(msg);
				}
			}
        }
    }

    /**
     *
     * 로그를 출력한다.
     * @param   loc		로그 기록 위치
     * @param	msg     로그 메세지
     * @param	fflag	로그파일 출력여부
     * @param	dflag	로그딜리미터 출력여부
     *
     */
    public void log(String loc, String msg, boolean fflag, boolean dflag) {
        if(LOGVERBOSE) {
	        msg = "[위치:"+loc+"] [시각:"+getDate(Code.TM_YMDHMS)+"]\n" +
				  "[메세지]\n"+msg;
			if(LOGYN) {
				if(dflag) {
	            	System.out.println(DELIMETER);
 	        	   	System.out.println(msg);
           		 	//System.out.println(DELIMETER);
				} else {
            		System.out.println(msg);
				}
			}	
			if(fflag) {
           		logwriter(msg);
			}
        }
    }

    /**
     *
     * 로그를 출력한다.
     * @param   loc		로그 기록 위치
     * 			msg     로그 메세지
     * 			fflag	로그파일 출력여부
     * 			dflag	로그딜리미터 출력여부
     * 		    type	로그 파일 기록 유형
	 *						DBPOOL_ERR_LOG(1) ==> 에러로그기록, 
	 *						DBPOOL_MSG_LOG(2) ==> 디버그로그기록, 
	 *						DBPOOL_ALL_LOG(3) ==> 둘다 기록	
     *
     */
    public void log(String loc, String msg, boolean fflag, boolean dflag, int type) {
        if(LOGVERBOSE) {
	        msg = "[위치:"+loc+"] [시각:"+getDate(Code.TM_YMDHMS)+"]\n" +
				  "[메세지]\n"+msg;
			if(LOGYN) {
				if(dflag) {
            		System.out.println(DELIMETER);
            		System.out.println(msg);
            		//System.out.println(DELIMETER);
				} else {
            		System.out.println(msg);
				}
			}	
			if(fflag) {
				if(type == Code.DBPOOL_ERR_LOG) {
            		logwriter(msg);
				} else if(type == Code.DBPOOL_MSG_LOG) {
            		msglogwriter(msg);
				} else if(type == Code.DBPOOL_ALL_LOG) {
            		logwriter(msg);
            		msglogwriter(msg);
				}
			}
        }
    }

    /**
     *
     * 메세지 로그를 출력한다.
     * @param   id		획득ID
	 * @param	dbid	DB POOL ID
	 * @param	type	[GET],[RETURN]
	 * @param	con_id	Connectin ID
     *
     */
	public void mlog(long id, String dbid, int type, long con_id) {
        if(LOGVERBOSE && LOGFILE) {
				String type_nm = null;
				if(type == Code.DBPOOL_GET) {
					type_nm = "GET";
				} else if(type == Code.DBPOOL_RETURN) {
					type_nm = "RETURN";
				} else {
					type_nm = "";
				}
	        	String msg = "[시각:"+getDate(Code.TM_YMDHMS)+"][ID:"+id+"][DB_ID:"+dbid+"][TYPE:"+type_nm+"][CON_ID:"+con_id+"]"; 
            	msglogwriter(msg);
        }
    }

    /**
     *
     * 에러 로그를 로그 파일에 기록한다.
     * @param   msg		로그 메세지
     *
     */
    public void logwriter(String msg) {

        String temp_file_name = null;

        try {
        	if(ERRLOGPOS == null || PATH_GUBUN == null || ERRLOGHEAD == null) return;
            if(daylog_yn) temp_file_name = ERRLOGPOS + PATH_GUBUN + ERRLOGHEAD + "_err_" + getDate(Code.TM_YMD);
            else temp_file_name = ERRLOGPOS + PATH_GUBUN + ERRLOGHEAD + "_err_" + getDate(Code.TM_YMDHMS);
			if(err_log_out != null) {
				if(daylog_yn) {
     	     		if(!err_log_file_name.equals(temp_file_name)) { //로그화일의 날짜가 지났다면 ...
						err_log_out.close();
       	        		err_log_file_name = temp_file_name;
       		         	err_log_out = new PrintWriter(new FileWriter(err_log_file_name,true));
           		 	} 
           		 }
			} else {
               	err_log_file_name = temp_file_name;
               	if(daylog_yn) err_log_out = new PrintWriter(new FileWriter(err_log_file_name,true));
               	else err_log_out = new PrintWriter(new FileWriter(err_log_file_name));
			}

			if(LOGFILEDELIMETER) {
           		err_log_out.println(DELIMETER);
            	err_log_out.println(msg);
            	//err_log_out.println(DELIMETER);
			} else {
            	err_log_out.println(msg);
			}
            err_log_out.flush();

        } catch (Exception e) {
            System.out.println("로그 기록에 에러가 발생했습니다.\n" + e.toString());
        }
    }

    /**
     *
     * 메세지  로그를 로그 파일에 기록한다.
     * @param   msg		로그 메세지
	 *			dflag	딜리미터출력여부
     *
     */
    public void msglogwriter(String msg) {

        String temp_file_name = null;

        try {
        	if(MSGLOGPOS == null || PATH_GUBUN == null || MSGLOGHEAD == null) return;
            if(daylog_yn) temp_file_name = MSGLOGPOS + PATH_GUBUN + MSGLOGHEAD + "_msg_" + getDate(Code.TM_YMD);
            else temp_file_name = MSGLOGPOS + PATH_GUBUN + MSGLOGHEAD + "_msg_" + getDate(Code.TM_YMDHMS);
			if(msg_log_out != null) {
          		if(daylog_yn) {
	          		if(!msg_log_file_name.equals(temp_file_name)) { //로그화일의 날짜가 지났다면 ...
						msg_log_out.close();
       	        		msg_log_file_name = temp_file_name;
       		         	msg_log_out = new PrintWriter(new FileWriter(msg_log_file_name,true));
        	    	} 
        	    }
			} else {
               	msg_log_file_name = temp_file_name;
               	if(daylog_yn) msg_log_out = new PrintWriter(new FileWriter(msg_log_file_name,true));
               	else msg_log_out = new PrintWriter(new FileWriter(msg_log_file_name));
			}

           	msg_log_out.println(msg);
            msg_log_out.flush();

        } catch (Exception e) {
            System.out.println("메세지 로그 기록에 에러가 발생했습니다.\n" + e.toString());
        }
    }

    /**
     *
     * 헤더 없는 로그를 출력한다.
     * @param   msg		로그 메세지
     *
     */
	public void nlog(String msg) {
        if(LOGVERBOSE) {
			if(LOGYN) {
       		   	//System.out.println(DELIMETER);
           	 	System.out.println(msg);
           	 	//System.out.println(DELIMETER);
			}
			if(LOGFILE) {
            	logwriter(msg);
			}
        }
    }

    /**
     *
     * 로그화일을 닫는다.
     *
     */
	public void log_close() {
		try {
			err_log_out.close();
			msg_log_out.close();
			err_log_out = null;
			msg_log_out = null;
		} catch (Exception e) {
			err_log_out = null;
			msg_log_out = null;
		}
	}

    /**
     *
     * 로그 출력 여부를 설정한다.
	 * @param	flag		로그출력여부
     *
     */
	public void setLOGVerbose(boolean flag) {
		LOGVERBOSE = flag;
	}

    /**
     *
     * 로그 딜리미터 출력여부를 설정한다.
	 * @param	flag		로그딜리미터출력여부
     *
     */
	public void setLOGDelimeter(boolean flag) {
		LOGDELIMETER = flag;
	}

    /**
     *
     * 로그화일 딜리미터 출력여부를 설정한다.
	 * @param	flag		로그화일딜리미터출력여부
     *
     */
	public void setLOGFILEDelimeter(boolean flag) {
		LOGFILEDELIMETER = flag;
	}

    /**
     *
     * 로그 파일 출력 여부를 설정한다.
	 * @param	flag		로그파일여부
     *
     */
	public void setLOGFile(boolean flag) {
		LOGFILE = flag;
	}

    /**
     *
     * 로그 출력 여부를 설정한다.
	 * @param	flag		로그출력여부
     *
     */
	public void setLOGYN(boolean flag) {
		LOGYN = flag;
	}

    /**
     *
     * MNAME을 설정한다.
	 * @param	mname		로그 출력위치 정보
     *
     */
	public void setMNAME(String mname) {
		MNAME = mname;
	}

    /*=============================================*/
    // Time Zone
    /*=============================================*/

    /**
     *
     * 현재 시간을 읽어온다.
     * @param   int		시간 포맷
	 *					<pre>
     *   				레벨 :  TM_YMDHMS(1) ==> 년월일시분초, TM_MDHMS(2) ==> 월일시분초,
     *          				TM_DHMS(3) ==> 일시분초, TM_HMS(4) ==> 시분초,
     *          				TM_MS(5) ==> 분초,  TM_S(6) ==> 초,
     *          				TM_YMD(7) ==> 년월일, TM_YMDHM(8) ==> 년월일시분
     *					</pre>
     *
     */
    public String getDate(int level) {
        Calendar cal = Calendar.getInstance();
        
        String Y = Integer.toString(cal.get(cal.YEAR));
        String M = lPad(Integer.toString(cal.get(cal.MONTH)+1),2,"0");
        String D = lPad(Integer.toString(cal.get(cal.DAY_OF_MONTH)),2,"0");
        String H = lPad(Integer.toString(cal.get(cal.HOUR_OF_DAY)),2,"0");
        String MI = lPad(Integer.toString(cal.get(cal.MINUTE)),2,"0");
        String S = lPad(Integer.toString(cal.get(cal.SECOND)),2,"0");
        String MS = lPad(Integer.toString(cal.get(cal.MILLISECOND)),4,"0"); 
        
        String retval = "";
        
		switch(level) {
		
			case Code.TM_Y:		//년
					retval = Y;
					break;
			case Code.TM_M:		//월
					retval = M;
					break;
			case Code.TM_D:		//일
					retval = D;
					break;
			case Code.TM_H:		//시
					retval = H;
					break;
			case Code.TM_MI:	//분
					retval = MI;
					break;
			case Code.TM_S:		//초
					retval = S;
					break;
			case Code.TM_YM:	//년월
					retval = Y+M;
					break;
			case Code.TM_YMD:	//년월일
					retval = Y+M+D;
					break;
			case Code.TM_YMDH:	//년월일시
					retval = Y+M+D+H;
					break;
			case Code.TM_YMDHM:	//년월일시분
					retval = Y+M+D+H+MI;
					break;
			case Code.TM_YMDHMS:	//년월일시분초
					retval = Y+M+D+H+MI+S;
					break;
			case Code.TM_MDHMS:		//월일시분초
					retval = M+D+H+MI+S;
					break;
			case Code.TM_DHMS:		//일시분초
					retval = D+H+MI+S;
					break;
			case Code.TM_HMS:		//시분초
					retval = H+MI+S;
					break;
			case Code.TM_MS:		//분초
					retval = MI+S;
					break;
			case Code.TM_YMDHMSM:	//년월일시분초밀리세컨
					retval = Y+M+D+H+MI+S+MS;
					break;
		
		}					
		return retval;
    }

	/**
	 * 0 필러
	 * @param		data		해당 데이터
	 * 				size		단윈
	 *				filler		채울값
	 * @return		String		변형문자열
	 */
	public String lPad(String data, int size, String filler) {
		int csize = data.trim().length();
		if(size == csize) return data;

		String retstr = data;	
		for(int i = 0; i < (size-csize); i++) {
			retstr = "0" + retstr;
		}
		return retstr;
	}

}
				
