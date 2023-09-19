package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: MailUtil.java
 * Version 		: 1.0
 * 작성일 		: 2002/10/15
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: SMTP서버에 접속하여 메일을 전송한다.
 *				  메일전송 쓰레드로 호출한다.
 *				  / ========== 입력값 ============ /
 *				  Object mail ==>  Hashtable OR Vector
 *					
 *					메일 정보 Hashtable mailinfo ==>				
 *						{[MAILFROM, String],	==> 필수아님
 *						 [RCPTTO, String], 		==> 필수 
 *						 [DATA||URL, String], 	==> 필수아님 
 *						 [CC, String], 			==> 필수아님 
 *						 [RETURNMAIL, String], 	==> 필수아님 
 *						 [ENCODING, String], 	==> 필수아님 
 *						 [SUBJECT, String], 	==> 필수 
 *						 [TO_NAME, String], 	==> 필수아님 
 *						 [FROM_NAME, String], 	==> 필수아님 
 *						 [FRPM_MAIL, String],	==> 필수아님
 *						 [MAILTYPE, String], 	==> 필수아님 
 *				  / ========== 입력값 ============ /
 *
 * 프로젝트		: 표준콤포넌트
 * Copyright	: (주)다코시스템	 
 * </pre> 
 */
public class MailUtil extends MailLog {

	/** 모듈명 설정 */
	private String MNAME = "MailUtil";

	/**
   	 *
   	 * 생성자(환경값을 읽는다)
   	 * 
   	 **/
	public MailUtil() {
		setMNAME("MailUtil");
	}

	/**
     *
     * 메일을 전송한다(쓰레드로 메일을 발송한다).
	 * @param	mail 	발송메일정보
     *
     **/
	public void SendMail(Object mail) {

		//메일을 발송한다.
		if(mail instanceof Vector) {
			int smtpcnt = 10;
			try {	
				smtpcnt = Integer.parseInt(getEnv("SMTPCNT")); //버퍼할 메일 수
			} catch (Err err) {
				log(MNAME,err.getEXStr());
			}
			Vector mailvec = (Vector)mail;
			Vector tempvec = new Vector();
			for(int i = 0; i < mailvec.size(); i++) {
				tempvec.addElement((Hashtable)mailvec.elementAt(i));
				if(((i+1)%smtpcnt) == 0) {
					new MailSendThread(tempvec,this).start();	
					tempvec = new Vector();
				}
			}
			if((mailvec.size() % smtpcnt) != 0) {
				new MailSendThread(tempvec,this).start();	
			}
		} else if(mail instanceof Hashtable) {
			new MailSendThread((Hashtable)mail,this).start();
		} else {
            log(MNAME,"SendMail() 호출을 실패했습니다.\n메일정보 오브젝트가 잘못되었습니다.");
		}
	}

	public static void main(String args[]) throws Exception {

		if(args.length != 2) {
			System.out.println("usage : java -Denvfile=[환경화일패스] MailUtil [발송수] [받는 메일 주소]");
			System.exit(0);
		}

		InitAppCall initapp = new InitAppCall();	
		MailUtil sm = new MailUtil();
		Hashtable mailsend = new Hashtable();
		Vector maillist = new Vector();

		Date d = new Date();	//시간 체크 시작

		int cnt = Integer.parseInt(args[0]);
		String sendaddr = args[1];

		for(int i = 0; i < cnt; i++) {
			Hashtable mailinfo = new Hashtable();
			mailinfo.put("MAILFROM","hahaha@hammail.net");
			mailinfo.put("RCPTTO",sendaddr);
			//mailinfo.put("DATA","우하하하하하\r\n");
			mailinfo.put("URL","http://www.daum.net");
			mailinfo.put("CC","hahaha@hanmail.net");
			mailinfo.put("RETURNMAIL","hahah@hanmail.net");
			//mailinfo.put("ENCODING","base64");
			mailinfo.put("SUBJECT","test mail......");
			mailinfo.put("TO_NAME","야 임마......");
			mailinfo.put("FROM_NAME","나다 나 .........");
			maillist.addElement(mailinfo);
		}
		sm.SendMail(maillist);	
		Date d1 = new Date();	//시간 체크 끝
		sm.log("경과 시간 ==> " + (d1.getTime() - d.getTime()));
	}

}

			

