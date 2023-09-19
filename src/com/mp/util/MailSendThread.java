package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;
import java.lang.*;
import javax.commerce.util.BASE64Encoder;
import javax.activation.*;
import javax.mail.internet.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: MailSendThread.java
 * Version 		: 1.0
 * 작성일 		: 2002/10/15
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: 메일을 전송하는 쓰레드.
 *				  mailsend 메소드는 RCPT TO와 DATA부분을 전송한다.
 * 프로젝트		: 표준 콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class MailSendThread extends Thread {

	/** 모듈명 설정 */
	private String MNAME = "MailSendThread";

	/** 소켓 */
	private Socket soc = null;
	/** 입력 스트림 */
	private BufferedReader in = null;
	/** 출력 스트림 */
	private PrintWriter out = null;
	/** 입력 스트림 */
	private InputStream iStream = null;
	/** 출력 스트림 */
	private OutputStream oStream = null;
	/** 메인 클래스 */
	private MailUtil sm = null;
	/** 버퍼 크기 */
	private int BUF_SIZE = 1024;

	/** 메일정보 */
	private Hashtable 	mailinfo = null;
	private Vector maillist = null;

	/** 라인피드문자 */
	private String CRLF = "\r\n";
	/** 전송내용길이 */
	private int length;
	/** 받는 버퍼 */
	private byte[] recevbuf = null;
	
	/** 그냥 넣어 보는겁니다.	*/
	private String x_maker = "Dacosystem Best IT company"; 

	/** 메일 타입을 저장한다(단건, 대량). */
	private String TYPE = null;

	/** 메일 타입을 저장한다. */
	private String SEND_TYPE = null;

	/** 받는사람의 메일 주소 */
	private String TO = null;

	/** 수신 메세지 저장 */
	String strRetMsg = null;

	/**	
	 * 생성자.
 	 */
	public MailSendThread() {}

	/**
     * 생성자.
	 * 메일리스트와 메일전송메인을 설정한다.
     * @param	imail	 	메일발송정보
     * @param	ism			메일클래스
	 **/
	public MailSendThread(Object imail, MailUtil ism) {
		sm = ism;	
		if(imail instanceof Vector) {
			TYPE = "M";
			maillist = (Vector)imail;
		} else if(imail instanceof Hashtable) {
			TYPE = "S";
			mailinfo = (Hashtable)imail;
		} else {
			sm.log(MNAME,"MailSendThread() 호출을 실패했습니다.\n" +
					"메일정보 오브젝트가 잘못되었습니다.");
		}	
	}

	/**
	 *	메일 발송 쓰레드 
	 **/
	public void run() {

		try {
			if(TYPE.equals("M")) {
				for(int i = 0; i < maillist.size(); i++) {
					//메일 한건을 SMTP 서버 버퍼에 저장한다.
					mailsend((Hashtable)maillist.elementAt(i));	
				}
			} else {
				//메일 한건을 SMTP 서버 버퍼에 저장한다.
				mailsend(mailinfo);
			}
		} catch (Exception e) {
			sm.log(MNAME,"메일 전송을 실패했습니다.");
		}
	}


	/**
	 *
	 *	메일을 한건을 SMTP 서버 버퍼에 저장한다.
	 *
	 *	@param		Hashtable	메일 데이터
	 *
	 **/
	public void mailsend(Hashtable mailinfo) {

		String MAIL_FROM = null;		//mail from 저장
		String MAIL_DOMAIN = null;		//DOMAIN 저장
		String RCPT_TO = null;			//rcpt to 저장

		String SMTP_SERVER_IP = null;	//수신 서버 IP

		//헤더 생성 데이터
		String SUBJECT = null;			//메일 제목 저장

		//메일 데이터
		String maildata = null;			//메일 데이터

		if(mailinfo.containsKey("MAILTYPE")) {
			SEND_TYPE = (String)mailinfo.get("MAILTYPE");
		} else {
			SEND_TYPE = "일반";
		}

		if(mailinfo.containsKey("MAILFROM")) {
			MAIL_FROM = (String)mailinfo.get("MAILFROM");
		} else {
				sm.log(MNAME,"MAILFROM 없음");
				sm.erlog("609","MAIL FROM 없음",SEND_TYPE);	
				return;
		}

		if(mailinfo.containsKey("RCPTTO")) {
			RCPT_TO = (String)mailinfo.get("RCPTTO");
			TO = RCPT_TO;
			if((MAIL_DOMAIN = checkMAILAddr(RCPT_TO)) == null) {	
				sm.log(MNAME,"잘못된 DOMAIN");
				sm.erlog("609"," ",SEND_TYPE);	
				return;
			}
		} else {
			sm.log("RCPTTO가 없습니다.");	
			sm.erlog("609",MAIL_FROM,SEND_TYPE);	
			return;
		}

		//메일 데이터를 저장한다.
		if(!mailinfo.containsKey("DATA")) {
			sm.log("DATA가 없습니다.");
			sm.erlog("609",RCPT_TO,SEND_TYPE,"DATA 없음");	
			return;
		}

		//메일 제목을 저장한다.
		if(mailinfo.containsKey("SUBJECT")) {
			SUBJECT = (String)mailinfo.get("SUBJECT");
		} else {
			sm.log("SUBJECT 가 없습니다.");	
			sm.erlog("609",RCPT_TO,SEND_TYPE);	
			return;
		} 


	/* =======================================================================
	 		메일 내용을 GENERATE 한다.
	   ======================================================================= */
		String content = getContent(mailinfo);	

		if(content == null || content.equals("")) {
			sm.erlog("609",TO,SEND_TYPE,"내용 없음","CONTENT GENERATE");
			return;
		}


	/* =======================================================================
	 		SMTP 발송을 위한 변수 선언
	   ======================================================================= */

		//SMTP COMMAND 저장
		String smtpCommand = null;

		//전송 일자 설정
    SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss a z", java.util.Locale.US);
    String dateStr = sdf.format(new java.util.Date());

		//전송 문자열 선언
		StringBuffer sb = null;
		
		//서버와 연결한다.
		try {
			//수신 서버 IP를 조회한다.
			DNSLookup lookup = new DNSLookup(MAIL_DOMAIN.toLowerCase(),sm.getEnv("SMTPDNSSERVER"));	
			SMTP_SERVER_IP = lookup.lookupAll().trim();

			sb = new StringBuffer();
			smtpCommand = sb.append("CONNECT:").append(SMTP_SERVER_IP).toString();	
			soc = new Socket();
			soc.setReuseAddress(true);
			soc.setSoTimeout(10*1000); //TIME OUT을 10초로 설정한다.
			soc.setTcpNoDelay(true);
			soc.setSoLinger(true,1);
			soc.setKeepAlive(true);

			soc.connect(new InetSocketAddress(SMTP_SERVER_IP,25),30*1000); 

			iStream = soc.getInputStream();
			oStream = soc.getOutputStream();

			in = new BufferedReader(new InputStreamReader(iStream));
			out = new PrintWriter(oStream,true);

			soc.setSendBufferSize(2048);
			soc.setReceiveBufferSize(200);

			/* ==================================================================== */
				//CONNECT 처리
			/* ==================================================================== */
			if(!handshaking(0)) return;	

			/* ==================================================================== */
				//HELO 전송	시작	
			/* ==================================================================== */
			sb = new StringBuffer();
			smtpCommand = (sb.append("HELO ").append(InetAddress.getLocalHost().getHostName())).toString();

			sendLine(smtpCommand);
			if(!handshaking(1)) return;	

			/* ==================================================================== */
				//MAIL FROM 전송 시작	
			/* ==================================================================== */
			sb = new StringBuffer();
			smtpCommand = (sb.append("MAIL FROM: <").append(MAIL_FROM).append(">")).toString();

			sendLine(smtpCommand);

			if(!handshaking(2)) return;	
	
			/* ==================================================================== */
				//RCPT TO 전송 시작	
			/* ==================================================================== */
			sb = new StringBuffer();
			smtpCommand = (sb.append("RCPT TO: <").append(RCPT_TO).append(">")).toString();

			sendLine(smtpCommand);

			if(!handshaking(3)) return;	
	
			/* ==================================================================== */
				//DATA 전송 시작	
			/* ==================================================================== */
			smtpCommand = "DATA";

			sendLine(smtpCommand);

			if(!handshaking(4)) return;	

			/* ==================================================================== */
				//DATA 본문 시작	
			/* ==================================================================== */
			sb = new StringBuffer();	
			sb.append("Return-Path: <").append(MAIL_FROM).append(">"+CRLF).append(content);
	
			sendData(sb.toString().getBytes());
		
			smtpCommand = CRLF+".";

			sendLine(smtpCommand);

			if(!handshaking(5)) return;					

			/* ==================================================================== */
				//QUIT 전송 시작	
			/* ==================================================================== */
			smtpCommand = "QUIT";	

			sendLine(smtpCommand);

			handshaking(6);	

			/** 전송 성공 로그 출력 */
			sm.log(MNAME,"["+RCPT_TO+"] 에게 ["+SEND_TYPE+"] 메일을 전송했습니다.");
			sm.trlog("003",RCPT_TO,SEND_TYPE);

		} catch (UnknownHostException ukhe) {
			sm.log(MNAME,"메일 발송 에러 \n" + ukhe.toString()); 
			sm.erlog("607",RCPT_TO,SEND_TYPE);	
			return;
		} catch (IOException ioe) {
			sm.log(MNAME,"메일 발송 에러 \n" + ioe.toString());
			sm.erlog("607",RCPT_TO,SEND_TYPE);	
			return;
		} catch (Exception e) {
			sm.log(MNAME,"메일 발송 에러 \n" + e.toString());
			sm.erlog("607",RCPT_TO,SEND_TYPE);	
			return;
		} finally {
			try {
				close();
			} catch (Exception e1) {}
		}

	}

	/**
	 *
	 *	핸드쉐이킹 성공여부를 확인한다.
	 *
	 *	@param		flag		<pre>전송 단계(0==>초기,1==>HELO,2==>MAIL FROM,
     *		 								   3==>RCPT TO,	4==>DATA,5==>.,
     *										   6==>QUIT)</pre>
	 *	@return		boolean		성공여부
	 *
	 **/
	public boolean handshaking(int flag) {
		/*******************************
			코드의 값을 읽어 에러로그에 
			기록한다. 에러가 없다면 기록
			하지 않는다. 
			에러일 시는 전송로그에도 기록한다.
			에러코드라면 false를 리턴한다.	
		********************************/
		boolean retval = true;

		String code = getResponse();

		switch(flag) {
			case 0:  		//CONNECT
					if(!code.equals("220")) { 
						sm.log("초기 에러 발생 발생 : "+strRetMsg);
						sm.erlog(code,TO,SEND_TYPE,strRetMsg,"CONNECT");
						retval = false;
					}
					break;
			case 1:  		//HELO
					if(!code.equals("250")) {
						sm.log("HELO 에러 발생 발생 : "+strRetMsg);
						sm.erlog(code,TO,SEND_TYPE,strRetMsg,"HELO");
						retval = false;
					}
					break;
			case 2:			//MAIL FROM
					if(!code.equals("250")) {
						sm.log("MAIL FROM 에러 발생 발생 : "+strRetMsg);
						sm.erlog(code,TO,SEND_TYPE,strRetMsg,"MAIL FROM");
						retval = false;
					}
					break;
			case 3:			//RCPT TO
					if(!code.equals("250") && !code.equals("251")) {
						sm.log("RCPT TO 에러 발생 발생 : "+strRetMsg);
						sm.erlog(code,TO,SEND_TYPE,strRetMsg,"RCPT TO");
						retval = false;
					}
					break;
			case 4:			//DATA
					if(!code.equals("354")) {
						sm.log("DATA 에러 발생 발생 : "+strRetMsg);
						sm.erlog(code,TO,SEND_TYPE,strRetMsg,"DATA");
						retval = false;
					}
					break;
			case 5:			//DOT
					if(!code.equals("250")) {
						sm.log("DOT 에러 발생 발생 : "+strRetMsg);
						sm.erlog(code,TO,SEND_TYPE,strRetMsg,"DOT");
						retval = false;
					}
					break;
			case 6:			//QUIT
					if(!code.equals("221")) {
						sm.log("QUIT 에러 발생 발생 : "+strRetMsg);
						sm.erlog(code,TO,SEND_TYPE,strRetMsg,"QUIT");
						retval = false;
					}
					break;
			default:
					retval = false;
					break;
		}
		return retval;
	}

	/**
	 *
	 * 메일 주소의 유효성을 확인한다.
	 * @param	mailaddr	메일 주소
	 * @return	String		메일 도메인을 리턴한다.
	 *  					유효하다면 		: 메일 도메인
	 *  					유효하지 않다면 : NULL
	 *
	 */
	public String checkMAILAddr(String mailaddr) {
		String host = null;
	    int host_index = mailaddr.indexOf("@");
		if(host_index == -1 ||
		   host_index == 0  ||
		   host_index == (mailaddr.length()-1)) {
			sm.log("메일 주소가 올바르지 않습니다.");
			return null;
		}
		return mailaddr.substring(host_index+1);
	}		

	/**
	 * Base64 인코딩
	 * @param	input		인코딩할 문자
	 * @return	String		인코딩한 문자
	 */
	public String getBASE64E(String input) {
		String base64E = null;
		try {
			byte[] inputBytes = input.getBytes("UTF8");
			BASE64Encoder encoder = new BASE64Encoder();
			base64E = encoder.encode(inputBytes);
		} catch(Exception e) {}
		return base64E;
	}

    /**
     *
     * URL에 대한 페이지를 실행한 결과를 리턴한다.
     *
     * @param       url         호출할 url
     * @return      String      URL호출 결과
	 *						    null : URL호출 실패		
     */
    public String getUrlData(String url) {
        String content = "";
        try {
            URL source = new URL(url);
            URLConnection sconnection = source.openConnection();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    sconnection.getInputStream()
                )
            );
            String inputLine = null;
            while((inputLine = in.readLine()) != null) {
                content += inputLine + CRLF;
            }
            in.close();
        } catch(MalformedURLException me) {
            sm.log("URL이 잘못되었습니다.\n");
			content = null;
        } catch(IOException ioe) {
            sm.log("파일 I/O에 에러가 발생했습니다.\n");
			content = null;
        } catch(Exception e) {
            sm.log("에러가 발생했습니다.\n" + e.toString());
			content = null;
        }
        return content;
    }

    /**
     *
     * 전송 코드를 읽어 리턴한다.
     * @return  String         핸드쉐이킹 코드
     *
     */
    public String getResponse() {

    	StringBuffer sb = new StringBuffer();
			this.strRetMsg = "";
    	String returnVal = "";

    	boolean bResult = readFully(sb);

    	if( bResult ) {
      	this.strRetMsg = replaceBraket(sb.toString());
      	String temp = "";

      	StringTokenizer st = new StringTokenizer(this.strRetMsg, "\n");
      	int nCount = st.countTokens();
      	int nIndex = 0;
      	String[] buffResp = new String[nCount];
      	while( st.hasMoreElements() ) {
        	buffResp[nIndex++] = st.nextToken().trim();
      	}

      	returnVal = buffResp[nCount - 1].substring(0, 3);
    	} else {
      	returnVal = "";
    	}

    	return returnVal;
    }

 	public boolean readFully(StringBuffer sb) {
  	if(sb == null) {
   		sb = new StringBuffer();
  	}

  	boolean bResult = true;
  	DataInputStream dis = null;

  	try {
   		dis = new DataInputStream(this.iStream);
   		byte[] data = new byte[2048];
   		int readcnt = 0;

   		while((readcnt = dis.read(data)) != -1) {
    		if(readcnt < 2048) {
     			byte[] tmp = new byte[readcnt];
     			System.arraycopy(data, 0, tmp, 0, readcnt);
     			sb.append(new String(tmp));
     			if(tmp[readcnt - 1] != 10) { // Line Feed Check..
      		try {
       			Thread.sleep(10);
      		} catch(Exception ex) { }
      			continue;
     			}
     			break;
    		}
    		sb.append(new String(data));
   		}
  	} catch(Exception e) {
   		bResult = false;
  	}
  	return bResult;
 	}

  private String replaceBraket(String msg) {
    msg = msg.trim();
    StringBuffer sb = null;

    int startPos = 0;
    int nPos = msg.indexOf("<", startPos);
    if( nPos != -1 ) {
      sb = new StringBuffer();
      while( nPos != -1 ) {
        sb.append(msg.substring(startPos, nPos)).append("&lt;");
        startPos = nPos + 1;
        nPos = msg.indexOf("<", startPos);
        if( nPos == -1 ) {
          sb.append(msg.substring(startPos));
        }
      }
      msg = sb.toString();
    }

    startPos = 0;
    nPos = msg.indexOf(">", startPos);

    if( nPos != -1 ) {
      sb = new StringBuffer();
      while( nPos != -1 ) {
        sb.append(msg.substring(startPos, nPos)).append("&gt;");
        startPos = nPos + 1;
        nPos = msg.indexOf(">", startPos);
        if( nPos == -1 ) {
          sb.append(msg.substring(startPos));
        }
      }
      msg = sb.toString();
    }

    return msg;
  }

	/**
	 * 리소스 해제
	 */
  private void close () {
    try {
      if( in != null ){ in.close(); in = null; }
      if( out != null ){ out.close(); out= null; }
      if( soc != null ){ soc.close(); soc = null; }
    } catch(IOException e) { }
  }
	
	/**
	 * 데이터 전송
	 */
  private void sendLine(String line) {
    try {
      out.print(line);
      out.print(CRLF);
      out.flush();
    } catch(Exception e) {
      sm.log("데이터 전송 실패\n" +e.toString());
    }
  }

	/**
	 * DATA 를 전송한다.
	 */
  private void sendData(byte[] data)
  {

    ByteArrayInputStream  bis = null;
    DataOutputStream dos = null;
    int buffsize = 2048;

    try {
      bis = new ByteArrayInputStream(data);
      dos = new DataOutputStream(oStream);
      byte[] sendbuff = new byte[buffsize];
      int readcnt = 0;
      while( (readcnt = bis.read(sendbuff, 0, buffsize)) != -1 ) {
        if( readcnt < buffsize ) {
          byte[] sTemp = new byte[readcnt];
          System.arraycopy(sendbuff, 0, sTemp, 0, readcnt);
          dos.write(sTemp);
          dos.flush();
          sTemp = null;
          break;
        }

        dos.write(sendbuff);
        dos.flush();
      }

      sendbuff = null;
    } catch(Exception e) {
      sm.log("데이터 전송 실패\n" +e.toString());
    } finally {
      try {
        bis.close();
        bis = null;
      } catch(Exception e) {}
    }
	}

	/** 
	 * 메일 내용을 생성한다.
	 * @param		mailinfo		메일 발송 정보
	 * @return	String			메일 내용
	 */
	public String getContent(Hashtable imailinfo) {

		String content = null;


		//콘텐츠타입 호출
		String content_type = sm.getData("CONTENT_TYPE",imailinfo);

		if(content_type.equals("URL")) {
			//URL 호출을 수행한다.		
			String url = sm.getData("DATA",imailinfo);
			content = getUrlData(url);
			if(content == null || content.equals(""))	return null;
		} else {
			content = sm.getData("DATA",imailinfo);
		}

		boolean isHtml = checkHtml(content);		//HTML 여부
		boolean isAttach = false;						//첨부 여부

		String attachContent = null;				//첨부 파일 내용

		//CHARSET 호출
		String charset = sm.getData("CHARSET",imailinfo);
		if(charset.equals("")) charset = "euc-kr";

		//인코딩 추출
		String encoding = sm.getData("ENCODING",imailinfo);	
		if(encoding.equals("")) encoding = "8bit";

		String mailfrom = sm.getData("MAILFROM",imailinfo);
		String rcptto = sm.getData("RCPTTO",imailinfo);
		String toname = sm.getData("TO_NAME",imailinfo);
		String fromname = sm.getData("FROM_NAME",imailinfo);
		String subject = sm.getData("SUBJECT",imailinfo);

		//첨부파일 정보를 읽어온다.
		Vector attachlist = (Vector)imailinfo.get("ATTACH");	
		if(attachlist != null && attachlist.size() != 0) {
			attachContent = getAttachFile(attachlist,charset); 	
		}

		if(attachContent != null && !attachContent.equals("")) {
			isAttach = true;
		}

		/* ======================================================================
			//메일 DATE 생성
		 ====================================================================== */
    SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss a z", java.util.Locale.US);
    String dateStr = sdf.format(new java.util.Date());

		StringBuffer sb = new StringBuffer();		//메일 내용 생성을 위한 STRING BUFFER
		sb.append("From:").append(dataToEnc(fromname,mailfrom,charset)).append(CRLF);
		sb.append("To:").append(dataToEnc(toname,rcptto,charset)).append(CRLF);
		sb.append("Subject:").append(dataToEnc(subject,charset)).append(CRLF);
		sb.append("Date:").append(dateStr).append(CRLF);
		sb.append("MIME-VERSION:").append("1.0").append(CRLF);
		if(isAttach) {		//첨부 파일이 있다면...
			sb.append("Content-Type:").append("multipart/mixed").append(";")
							.append(CRLF).append("\t").append("boundary").append("=\"")
							.append("MPLIB_BOUNDARY---NextPart_000").append("\"")
						  .append(CRLF);
		} else {
      sb.append("Content-Type:").append("text/html").append(';').append(CRLF)
          .append("\t").append("charset").append("=\"").append(charset)
					.append('\"').append(CRLF);

      //content_transfer-encoding타입(멀티파트타입인경우에는 헤더에서는 빠진다)
      sb.append("Content-Transfer-Encoding:").append(encoding).append(CRLF);
    }

    //마지막으로 한줄을 띄워준다.
    sb.append(CRLF);

			
		content = getTransferEncoding(content,encoding);

		if(isAttach) {
			String bodyheader = getContentHeader(charset,encoding,"MPLIB_BOUNDARY---NextPart_000");
			sb.append(bodyheader).append(content).append(CRLF).append(attachContent).append("--")
				.append("MPLIB_BOUNDARY---NextPart_000").append("--");
		} else {
			sb.append(content);
		}

		return sb.toString();
	}

  	/**
   	 * 인코딩된 첨부파일들을 전부 얻어온다.
   	 * @param attachFileList 첨부파일 경로 리스트
   	 * @param charset 				문자셋
   	 * @return String 인코딩된 첨부파일의 내용들
   	 */
    public String getAttachFile(Vector attachFileList, String charset) {

			Vector encattachFileList = null;
      if ((encattachFileList = setAttachFile(attachFileList)) == null) {
        return "";
      }

      BufferedReader br = null;
      String allEncAttahStr = "";
      String return_value = "";
      StringBuffer sb = null;

      try {
        //일단은 만들어준다.
        int attachSize = attachFileList.size();
        for (int i = 0; i < attachSize; i++) {
					String rawfilename = (String)attachFileList.elementAt(i);
					String encfilename = (String)encattachFileList.elementAt(i);
          if ((rawfilename != null && !rawfilename.equals("")) &&
							(encfilename != null && !encfilename.equals(""))) {

            sb = new StringBuffer();
            //일단 MID 폴더를 만들어준다.
            File rawFile = new File(rawfilename);
            File encFile = new File(encfilename);

            sb = null;
            br = new BufferedReader(new FileReader(encFile));
            String str = "";

            StringBuffer sb2 = new StringBuffer();
            //이러면 하나의 파일에서 내용을 읽어들이게 된다.
            while ( (str = br.readLine()) != null) {
              sb2.append(str).append("\n");
            }

            String fileName = rawFile.getName();
            //컨덴츠 헤더+첨부파일 내용을 연결시켜준다.
            String tempHeader = getAttachHeader(getContentTypeFromFile(rawFile),
																								charset,
                                                "base64",
																								"MPLIB_BOUNDARY---NextPart_000",
                                                fileName);
            sb = new StringBuffer();
            allEncAttahStr = sb.append(allEncAttahStr).append(tempHeader).
                append(
                sb2.toString())
                .append(CRLF).toString();
            sb = null;
            sb2 = null;

						br.close();

						encFile.delete();

          }
        }

        return_value = allEncAttahStr;
      }
      catch (Exception e) {
      	sm.log(MNAME+".getAttachFile()","첨부파일 BASE64 인코딩 실패\n" +e.toString());
        return_value = null;
      }
      finally {
        try {
          if (br != null) {
            br.close();
            br = null;
          }
        }
        catch (IOException e) {
        }
      }

      return return_value;
    }

    /**
     * 첨부파일을 얻어온후에 그것을 Base64로 인코딩한다
     * @param attachFileList attachFileList 첨부파일 경로 리스트
     * @return  boolean true - 인코딩 성공, false - 인코딩 실패
     */
    private Vector setAttachFile(Vector attachFileList) {

      FileInputStream fi = null;
      FileOutputStream fo = null;
      StringBuffer sb = null;

      Vector return_value = new Vector();

      try {
        //일단은 만들어준다.
        int attachSize = attachFileList.size();
				sb = new StringBuffer();
        String attachEncFolder = sb.append(sm.getEnv("SMTPATTACHFOLDER")).append(File.separator).toString();

        for (int i = 0; i < attachSize; i++) {
					String rawfilename = (String)attachFileList.elementAt(i);
          if (rawfilename != null && !rawfilename.equals("")) {

						//타임을 읽어온다.
						String tm = sm.getDate(Code.TM_YMDHMSM);

            BASE64Encoder encoder = new BASE64Encoder();

            sb = new StringBuffer();

						String encfilename = sb.append(attachEncFolder).append(tm).append(".txt").toString();

            fi = new FileInputStream(new File(rawfilename));
            fo = new FileOutputStream(new File(encfilename));
            sb = null;
            encoder.encode(fi, fo);
						return_value.addElement(encfilename);	

						fi.close();
						fo.close();

          } else {
						return_value.addElement("");
					}
				}

      } catch (Exception e) {
      	sm.log(MNAME+".setAttachFile()","첨부파일 BASE64 인코딩 실패\n" +e.toString());
				return null;
      } finally {
        try {
          if (fi != null) {
            fi.close();
          }
        } catch (IOException e) { }

        try {
          if (fo != null) {
            fo.close();
          }
        } catch (IOException e) { }
      }
      return return_value;
    }

    /**
     * 첨부파일의 Content-Type을 알아오기
     * @param attachFile 첨부파일
     * @return String 첨부파일의 Content-Type
     */
    public String getContentTypeFromFile(File attachFile) {
      MimetypesFileTypeMap fileType = new MimetypesFileTypeMap();
      return fileType.getContentType(attachFile);
    }

    /**
     * 첨부파일이 있을경우 첨부파일쪽의 헤더 내용을 얻는다.
     * @param cType 컨덴츠 타입
     * @param charSet CharacterSet
     * @param encType 인코딩 타입
     * @param boundary Boundary
     * @param filename 화일 이름
     * @return String 첨부파일의 헤더를 생성해준다.
     */
    public String getAttachHeader(String cType, String charSet, String encType,
                                  String boundary, String filename) {
      String returnVal = "";
      StringBuffer sb = new StringBuffer();
      sb.append("--").append(boundary).append(CRLF)
          .append("Content-Type:").append(cType)
          .append(";").append(CRLF)
          .append("\t").append("name").append("=\"")
          .append(filename).append('\"').append(CRLF)
          .append("Content-Transfer-Encoding:").append(encType).append(CRLF)
          .append("Content-Disposition:").append("attachment;").append(CRLF)
          .append("\t").append("filename").append("=\"").append(filename)
					.append('\"').append(CRLF).append(CRLF);
      returnVal = sb.toString();
      sb = null;
      return returnVal;
    }

    /**
     * 첨부파일이 있을경우 본문 내용 헤더 내용을 얻는다.
     * @param cType 컨덴츠 타입
     * @param charSet CharacterSet
     * @param encType 인코딩 타입
     * @param boundary Boundary
     * @return String 첨부파일의 헤더를 생성해준다.
     */
    public String getContentHeader(String charSet, String encType,
                                  String boundary) {

      //텍스트라도 이제는 html로 바꾸어준다
      String cType = "text/html";
      String retVal = "";

      StringBuffer sb = new StringBuffer();
      sb.append("--").append(boundary).append(CRLF)
          .append("Content-Type:").append(cType)
          .append(";").append(CRLF)
          .append("\t").append("charset").append("=\"")
          .append(charSet).append('\"').append(CRLF)
          .append("Content-Transfer-Encoding:").append(encType)
					.append(CRLF).append(CRLF);
      retVal = sb.toString();
      sb = null;
      return retVal;

    }

	/**
   * DATA를 인코딩한다.
   * @param data1 인코딩할 이름
   * @param data2 인코딩할 이메일
   * @param charset 문자셋
   * @return String 인코딩한 문자열
   */
  public String dataToEnc(String data1, String data2, String charset) {

    StringBuffer tempSb = new StringBuffer();
    String encodeStr = "";
    try {
    	if( data1 != null) {
      	encodeStr = (tempSb.append(MimeUtility.encodeText(data1,charset,"B")).append(' ')
       						.append('<').append(data2).append('>')).toString();
      } else {
        encodeStr = (tempSb.append('<').append(data2).append('>')).toString();
      }
    } catch(Exception e) {
     	sm.log("문자셋 인코딩 실패\n" +e.toString());
    }

    return encodeStr;
	}

	/**
   * DATA를 인코딩한다.
   * @param data1 인코딩할 이름
   * @param charset 문자셋
   * @return String 인코딩한 문자열
   */
  public String dataToEnc(String data1, String charset) {
		try {
		return MimeUtility.encodeText(data1,charset,"B");
    } catch(Exception e) {
     	sm.log("문자셋 인코딩 실패\n" +e.toString());
    }
		return "";
	}



	/**
	 * 내용이 HTML 인지 여부 확인
	 */
  public boolean checkHtml(String tmpContent) {

    if (tmpContent == null) {
      return false;
    }

    if ( (tmpContent.toUpperCase()).indexOf("<HTML>") == -1) {
    	return false; //아무것도 찾은것이 없다
    } else {
    	return true;
    }
  }

	/**
	 * 메일 내용을 인코딩한다.
	 */
  private String getTransferEncoding(String tmpContent, String encType) {

    //인코딩을 해준다. 만일에 8bit나 혹은 7bit이면 컨덴츠 내용 그대로 들어가구
    //Base64로 인코딩 해준다.
    if ( (encType.toUpperCase()).equals("BASE64")) {
      try {
        BASE64Encoder encoder = new BASE64Encoder();
        tmpContent = encoder.encode(tmpContent.getBytes());
        return tmpContent;
      } catch (Exception e) {
        return "";
      }
    } else if ( (encType.toUpperCase()).equals("QUOTED-PRINTABLE")) {
      //!!!!!! 아직 본문 내용에 대해서 Quoted-printable은 지원대상에서 빼기로 하자...ㅡ.ㅡ;;
      try {
        return MimeUtility.encodeText(tmpContent, "ks_c_5601-1987", "Q");
      } catch (Exception e) {
        return "";
      }
    } else { //Base64나 Quoted-printable가 아니면 그냥 그 내용 그대로 넘겨준다.
      return tmpContent;
    }
  }
}
	
		
	

		
