package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: FtpUtil.java
 * Version 		: 1.0
 * 작성일 		: 2002/10/16
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: FTP 유틸리티(파일 전송, 수신) 
 *
 * 프로젝트		: 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre> 
 */
public class FtpUtil extends Util {

	/** 모듈명 설정 */
	private String MNAME = "FtpUtil";

	private final int TIMEOUT = 30000;
	private Socket soc = null;
	private InputStream in = null;
	private OutputStream out = null;
	private String[] valid_code = null;
	private String CRLF = "\r\n";

	/**
	 * 생성자 
	 */
	public FtpUtil() {}

    /**
     *
     * 파일을 수신 받는다.
     * @param   inputhash		수신 정보
	 *							{서버IP(SVRIP), 서버PORT(SVRPORT), 
	 *							 USER(USER), PASSWORD(PASSWORD),
	 *							 서버전체경로(SVRPATH), 저장로컬전체경로(LOCALPATH), 	
	 *							 파일생성모드(MODE)[A|C]}
     *
     */
    public void getFile(Hashtable inputhash) throws Err {

		String SVRIP = null;
		String SVRPORT = null;
		String USER = null;
		String PASSWORD = null;
		String SVRPATH = null;
		String LOCALPATH = null;
		String MODE = null;

		boolean errflag = false;
		String errstr = "";

		//입력 값 체크	
		try {
			if((SVRIP = getData("SVRIP",inputhash)).equals("")) {		//서버 IP가 없다면
				SVRIP = getEnv("FTPSVRIP");	
			}
			if((SVRPORT = getData("SVRPORT",inputhash)).equals("")) {	//서버 PORT가 없다면	
				SVRPORT = getEnv("FTPSVRPORT");	
			}
			if((USER = getData("USER",inputhash)).equals("")) {		//USER ID
				USER = getEnv("FTPUSER");	
			}
			if((PASSWORD = getData("PASSWORD",inputhash)).equals("")) {	//USER PASSWORD
				PASSWORD = getEnv("FTPPASSWORD");	
			}
			if((SVRPATH = getData("SVRPATH",inputhash)).equals("")) {	//서버전체경로
				errflag = true;
				errstr = " [수신지패스] ";
			}
			if((LOCALPATH = getData("LOCALPATH",inputhash)).equals("")) {	//로컬전체경로
				errflag = true;
				errstr = " [전송할 파일 패스] ";
			}
			if((MODE = getData("MODE",inputhash)).equals("")) {		//파일 생성 모드
				MODE = "C";
			}
		} catch (Err err) {
			log(MNAME+".getFile()",err.getEXStr());
			throw err;
		}

		//인자값 에러 라면
		if(errflag) {
			log(MNAME+".getFile()","다음 인자가 필요합니다.\n"+errstr);
			throw new Err("1","5","E","인자값 에러");
		}

		//연결 확립
		try {
			getConnect(USER, PASSWORD, SVRIP, SVRPORT);
		} catch (Err err) {
			//리소스를 닫는다.
			close();
			throw err;
		} catch (IOException ioe) {
			//리소스를 닫는다.
			close();
			log(MNAME+".getConnect()","연결 및 사용자 인증 에러\n"+ioe.toString());
			throw new Err("1","5","6","연결 및 사용자 인증 에러");
		}

		//데이터 전송 소켓
		Socket dt_soc = null;
		InputStream input = null;
		OutputStream output = null;
		//저장할 파일 스트림
		FileOutputStream file_output = null;
		try {

			//디렉토리를 읽어온다.	
			String cdpath = SVRPATH.substring(0,SVRPATH.lastIndexOf('/'));	
			String getfilename = SVRPATH.substring(SVRPATH.lastIndexOf('/')+1);	

			//경로로 이동(CWD)
			write("CWD " + cdpath);
			valid_code = new String[1];
			valid_code[0] = "250";
			result_check(valid_code);

			//binary transfer 모드 설정
			write("TYPE I");
			valid_code = new String[1];
			valid_code[0] = "200";
			result_check(valid_code);

			dt_soc = getPassiveSocket(SVRIP);	

			//전송할 파일명을 전송한다(RETR).
			write("RETR " + getfilename);	
			valid_code = new String[2];
			valid_code[0] = "125";
			valid_code[1] = "150";
			result_check(valid_code);

			//파일을 전송받아 파일을 저장한다.
			input = dt_soc.getInputStream();	
			output = dt_soc.getOutputStream();	
		
			String save_file = LOCALPATH+getPlatformPath()+getfilename;
			if(MODE.equals("A")) {	//APPEND 모드
				file_output = new FileOutputStream(save_file,true);
			} else {				//CREATE 모드
				file_output = new FileOutputStream(save_file);
			}

			byte[] buf = new byte[1024];	
			int len = 0;
			while((len = input.read(buf)) != -1) {
				file_output.write(buf,0,len);	
				file_output.flush();
				buf = new byte[1024];
				len = 0;
			}	

			//파일 스트림 해제
			file_output.close();

			//연결 리소스 해제
			input.close();	
			output.close();
			dt_soc.close();	

			//파일 전송 완료 문자를 수신받아 체크한다(226).
			valid_code = new String[1];
			valid_code[0] = "226";
			result_check(valid_code);
		

			//QUIT를 전송한다.
			write("QUIT");

		} catch (Err err) {
			throw err;
		} catch (IOException ioe) {
			log(MNAME+".getFile()","데이터 수신 실패 \n"+ioe.toString());
			throw new Err("1","5","6","데이터 수신 실패");
		} catch (Exception e) {
			log(MNAME+".getFile()","데이터 수신 실패 \n"+e.toString());
			throw new Err("1","5","0","데이터 수신 실패");
		} finally {
			try {
				if(input != null) input.close();
				if(output != null) output.close();
				if(dt_soc != null) dt_soc.close();
				close();
			} catch (Exception e1) {}
		}

    }

    /**
     *
     * 파일을 전송 한다.
     * @param   inputhash		전송 정보
	 *							{서버IP(SVRIP), 서버PORT(SVRPORT), 
	 *							 USER(USER), PASSWORD(PASSWORD),
	 *							 서버전체경로(SVRPATH), 저장로컬전체경로(LOCALPATH)} 	
     *
     */
    public void setFile(Hashtable inputhash) throws Err {

		String SVRIP = null;
		String SVRPORT = null;
		String USER = null;
		String PASSWORD = null;
		String SVRPATH = null;
		String LOCALPATH = null;

		boolean errflag = false;
		String errstr = "";

		//입력 값 체크	
		try {
			if((SVRIP = getData("SVRIP",inputhash)).equals("")) {		//서버 IP가 없다면
				SVRIP = getEnv("FTPSVRIP");	
			}
			if((SVRPORT = getData("SVRPORT",inputhash)).equals("")) {	//서버 PORT가 없다면	
				SVRPORT = getEnv("FTPSVRPORT");	
			}
			if((USER = getData("USER",inputhash)).equals("")) {		//USER ID
				USER = getEnv("FTPUSER");	
			}
			if((PASSWORD = getData("PASSWORD",inputhash)).equals("")) {	//USER PASSWORD
				PASSWORD = getEnv("FTPPASSWORD");	
			}
			if((SVRPATH = getData("SVRPATH",inputhash)).equals("")) {	//서버전체경로
				errflag = true;
				errstr = " [수신지패스] ";
			}
			if((LOCALPATH = getData("LOCALPATH",inputhash)).equals("")) {	//로컬전체경로
				errflag = true;
				errstr = " [전송할 파일 패스] ";
			}
		} catch (Err err) {
			log(MNAME+".setFile()",err.getEXStr());
			throw err;
		}

		//인자값 에러 라면
		if(errflag) {
			log(MNAME+".setFile()","다음 인자가 필요합니다.\n"+errstr);
			throw new Err("1","5","E","인자값 에러");
		}

		//연결 확립
		try {
			getConnect(USER, PASSWORD, SVRIP, SVRPORT);
		} catch (Err err) {
			//리소스를 닫는다.
			close();
			throw err;
		} catch (IOException ioe) {
			//리소스를 닫는다.
			close();
			log(MNAME+".getConnect()","연결 및 사용자 인증 에러\n"+ioe.toString());
			throw new Err("1","5","6","연결 및 사용자 인증 에러");
		}

		//데이터 전송 소켓
		Socket dt_soc = null;
		InputStream input = null;
		OutputStream output = null;
		//전송할 파일 스트림
		FileInputStream file_input = null;
		try {

			//전송 파일명을 읽어온다.	
			String setfilename = LOCALPATH.substring(LOCALPATH.lastIndexOf(getPlatformPath())+1);	

			//경로로 이동(CWD)
			write("CWD " + SVRPATH);
			valid_code = new String[1];
			valid_code[0] = "250";
			result_check(valid_code);

			//binary transfer 모드 설정
			write("TYPE I");
			valid_code = new String[1];
			valid_code[0] = "200";
			result_check(valid_code);

			dt_soc = getPassiveSocket(SVRIP);	

			//전송할 파일명을 전송한다(STORE).
			write("STOR " + setfilename);	
			valid_code = new String[2];
			valid_code[0] = "125";
			valid_code[1] = "150";
			result_check(valid_code);

			//파일을 전송하여 저장한다.
			input = dt_soc.getInputStream();	
			output = dt_soc.getOutputStream();	
		
			file_input = new FileInputStream(LOCALPATH);

			byte[] buf = new byte[1024];	
			int len = 0;
			while((len = file_input.read(buf)) != -1) {
				output.write(buf,0,len);
				output.flush();
				buf = new byte[1024];
				len = 0;
			}

			//파일 스트림 해제
			file_input.close();

			//연결 리소스 해제
			input.close();	
			output.close();
			dt_soc.close();	

			//파일 전송 완료 문자를 수신받아 체크한다(226).
			valid_code = new String[1];
			valid_code[0] = "226";
			result_check(valid_code);
		

			//QUIT를 전송한다.
			write("QUIT");

		} catch (Err err) {
			throw err;
		} catch (IOException ioe) {
			log(MNAME+".setFile()","데이터 전송 실패 \n"+ioe.toString());
			throw new Err("1","5","6","데이터 전송 실패");
		} catch (Exception e) {
			log(MNAME+".setFile()","데이터 전송 실패 \n"+e.toString());
			throw new Err("1","5","0","데이터 수신 실패");
		} finally {
			try {
				if(input != null) input.close();
				if(output != null) output.close();
				if(dt_soc != null) dt_soc.close();
				close();
			} catch (Exception e1) {}
		}

    }

    /**
     *
     * FTP 서버 연결 
     * @param   user		USER ID
	 *			pass		USER PASSWORD
	 *			ip			SERVER IP
	 *			port		SERVER PORT
     */
	public void getConnect(String user, String pass, 
								String ip, String port) throws IOException, Err {

		try {
			soc = new Socket(ip,Integer.parseInt(port));
			soc.setSoTimeout(TIMEOUT);
		
			in = soc.getInputStream();
			out = soc.getOutputStream();	
		} catch (IOException ioe) {
			log(MNAME+".getConnect()","FTP 서버 접속 에러");
			throw new Err("1","5","6","FTP 서버 접속 에러");
		}

		//초기 접속 문자 체크
		valid_code = new String[1];
		valid_code[0] = "220";
		result_check(valid_code);

		//USER 전송
		write("USER " + user);
		valid_code = new String[3];
		valid_code[0] = "230";
		valid_code[1] = "331";
		valid_code[2] = "332";
		result_check(valid_code);

		//PASSWORD 전송
		write("PASS " + pass);
		valid_code = new String[2];
		valid_code[0] = "230";
		valid_code[1] = "332";
		result_check(valid_code);

		//시스템 확인 정송
		write("SYST");
		valid_code = new String[1];
		valid_code[0] = "215";
		result_check(valid_code);

		//파일 구조 전송
		write("STRU F");
		valid_code = new String[1];
		valid_code[0] = "200";
		result_check(valid_code);

		//Stream 모드 정송
		write("MODE S");
		valid_code = new String[1];
		valid_code[0] = "200";
		result_check(valid_code);
		
	}

    /**
     *
     * 결과 코드 체크
     * @param  	valid_code	기대되는 코드
     * @return 	String 		리턴값
     */
	public String result_check(String[] valid_code) throws IOException, Err { 

		String answer = read();					

		//System.out.println("answer ==> " + answer);

		if(answer.length() <= 0) { 
			log(MNAME+".result_check()","수신 데이터가 없음");
			throw new Err("1","5","E","수신 데이터가 없음");
		}

		String code = answer.substring(0,3);
		boolean flag = false;

		//유효 코드인지 확인한다.
		for(int i = 0; i < valid_code.length; i++) {
			if(code.equals(valid_code[i])) {	
				flag = true;
				break;
			}
		}
		
		if(!flag) {		//유효 코드가 아니라면...
			log(MNAME+".result_check()","FTP 명령어 에러입니다.\n수신값:["+answer+"]");	
			throw new Err("1","5","E","FTP 명령어 에러입니다.");
		}

		return answer;

	}

    /**
     *
     * 데이터 read
     * @return  String 		수신 데이터
     */
	public String read() throws IOException { 
		byte[] buf = new byte[200];
		int len = in.read(buf);
		return new String(buf,0,len).trim();
	}
	
    /**
     *
     * 데이터 write
     * @param  	send 		전송 데이터
     */
	public void write(String send) throws IOException { 
		out.write((send+CRLF).getBytes());
	}

	/**
	 *
	 * passive 소켓 생성
	 * @param 	ip		서버IP
	 * @return 	Socket 	소켓
	 */
	private Socket getPassiveSocket(String ip) throws IOException, Err {
        
		write("PASV");
		valid_code = new String[1];
		valid_code[0] = "227";
		String answer = result_check(valid_code);

		int port = parsePassivePort(answer);
		Socket dt_soc = new Socket(ip, port);
		dt_soc.setSoTimeout(TIMEOUT); // set timeout for the data socket
		return dt_soc;
	}

	/**
	 * passive 소켓 포트 읽기
	 * @param 	answer	서버 전송 문자
	 * @return 	int 	소켓 포트
	 */
    private int parsePassivePort(String answer) {

        StringTokenizer st = new StringTokenizer(answer, ",", false);

        int byte_one = 0; // init bits 8 - 15
        int byte_two = 0; // init bits 0 - 7

        int st_length = st.countTokens();
        for(int i = 1; st.hasMoreTokens(); i++) {
            if(i == (st_length - 1)) byte_one = Integer.parseInt(st.nextToken());
            else if(i == st_length) {
                StringTokenizer st_two = new StringTokenizer(st.nextToken(), ")", false);
                byte_two = Integer.parseInt(st_two.nextToken());

            } else st.nextToken();
        }

        int port = (byte_one << 8) | byte_two;

        return port;
    }   

    /**
     * 리소스를 close한다.
     */
	public void close() { 
		try {
			if(in != null) in.close();
			if(out != null) out.close();
			if(soc != null) soc.close();
		} catch (Exception e) {}
	}

	/**
	 * 실행 메소드
	 */
	public static void main(String[] args) throws Exception {

		/* ====================================================

			usage : java -Denvfile=[환경화일패스] com.mp.util.FtpUtil [G(수신)/S(전송)]

		   ==================================================== */
		
		WebUtil webutil = new WebUtil();
		FtpUtil ftputil = new FtpUtil();

	/**************************************************************
	 {서버IP(SVRIP), 서버PORT(SVRPORT), 
	 USER(USER), PASSWORD(PASSWORD),
	 서버전체경로(SVRPATH), 저장로컬전체경로(LOCALPATH), 	
	 파일생성모드(MODE)[A|C]}
	**************************************************************/
		Hashtable inputhash = new Hashtable();

		if(args[0].equals("G")) {	
			inputhash.put("SVRIP","localhost");
			inputhash.put("SVRPORT","21");
			inputhash.put("USER","ohbs");
			inputhash.put("PASSWORD","quick.");
			inputhash.put("SVRPATH","/user1/ohbs/lib/jndi.jar");
			inputhash.put("LOCALPATH","/user1/ohbs/temp/save");
			inputhash.put("MODE","A");
			ftputil.getFile(inputhash);
		} else if(args[0].equals("S")) {
			inputhash.put("SVRIP","localhost");
			inputhash.put("SVRPORT","21");
			inputhash.put("USER","mp");
			inputhash.put("PASSWORD","mp");
			inputhash.put("SVRPATH","/");
			inputhash.put("LOCALPATH","/user1/ohbs/temp/send");
			ftputil.setFile(inputhash);
		}


	}

}

			

