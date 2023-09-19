
package com.mp.util;

import java.io.*;
import java.util.*;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mp.exception.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : Upload.java
 * Version      : 1.0
 * 작성일       : 2002/10/14
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 * 설명         : 파일업로드 유틸리티
 *
 * 프로젝트명   : 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class Upload extends Util {

	/** 모듈명 설정 */
	private String MNAME = "Upload";

	/**	NEW LINE 문자	*/
	private static byte nl = (byte)'\n';  	//NEW LINE
	/**	EQUALS 문자	*/
	private static byte eq = (byte)'=';  	//EQUALS
	/**	SEMICOLUMN 문자	*/
	private static byte sc = (byte)';';  	//SEMICOLUMN
	/**	nameHeader 	*/
	private static String nameHeader = "Content-Disposition: form-data; name=";
	/**	업로드 화일 저장 패스	*/
	private String filepath;

	/**	BUF SIZE	*/
	private static int BUF_SIZE = 1024;
	/** 파일 사이즈 */
	private static int filesize = 2000000;
	/** 파일 prefix	*/
	private String prefix = null;

	/** 파일명 CHARSET */
	private String charset = null;

	/** 파일내용 CHARSET */
	private String body_charset = null;

	/** 
	 * 생성자.
	 * 로그위치와 파일저장위치를 설정한다.
	 */
	public Upload() {}

    /**
     *
     * 입력파라미터의 Name, Value 값을 리턴한다.
     * 입력파라미터의 전체 Name, Value를 Hashtable에 저장하여 리턴한다.
     *
     * @param   	req					HttpServletRequest	
     *				filepath			파일 저장 경로
     *				filesize			저장 파일 용량
     *				prefix				파일명 prefix
     * @return  	Hashtable       	Name, Value값을 저장 <br>	
	 *									(딜리미터 ==> "delistr")
	 * @exception	Err					<br>에러코드 	: 15E, 150
	 *									<br>메세지		: 상위에러메세지 또는 
	 *									<br>			  파일 업로드를 실패했습니다.
     */
	public Hashtable getNameValue(HttpServletRequest req, String filepath, int filesize, String prefix) throws Err { 
		this.filepath = filepath;
		this.filesize = filesize;
		this.prefix = prefix;
		Hashtable nvhash = null;
		this.charset = null;
		this.body_charset = null;
		try {
			nvhash = getNameValue(req);
		} catch (Err err) {
		 	log(MNAME+".getNameValue()","에러가 발생했습니다.\n"+err.getEXStr());
            throw err;	
		}
		return nvhash;
	}

    /**
     *
     * 입력파라미터의 Name, Value 값을 리턴한다.
     * 입력파라미터의 전체 Name, Value를 Hashtable에 저장하여 리턴한다.
     *
     * @param   	req					HttpServletRequest	
     *				filepath			파일 저장 경로
     *				filesize			저장 파일 용량
     *				prefix				파일명 prefix
     *				charset				파일명 encoding CHARSET
     * @return  	Hashtable       	Name, Value값을 저장 <br>	
	 *									(딜리미터 ==> "delistr")
	 * @exception	Err					<br>에러코드 	: 15E, 150
	 *									<br>메세지		: 상위에러메세지 또는 
	 *									<br>			  파일 업로드를 실패했습니다.
     */
	public Hashtable getNameValue(HttpServletRequest req, String filepath, int filesize, String prefix, String charset) throws Err { 
		this.filepath = filepath;
		this.filesize = filesize;
		this.prefix = prefix;
		Hashtable nvhash = null;
		this.charset = charset;
		this.body_charset = null;
		try {
			nvhash = getNameValue(req);
		} catch (Err err) {
		 	log(MNAME+".getNameValue()","에러가 발생했습니다.\n"+err.getEXStr());
            throw err;	
		}
		return nvhash;
	}

    /**
     *
     * 입력파라미터의 Name, Value 값을 리턴한다.
     * 입력파라미터의 전체 Name, Value를 Hashtable에 저장하여 리턴한다.
     *
     * @param   	req					HttpServletRequest	
     *				filepath			파일 저장 경로
     *				filesize			저장 파일 용량
     *				prefix				파일명 prefix
     *				charset				파일명 encoding CHARSET
     *				body_charset		파일내용 encoding CHARSET
     * @return  	Hashtable       	Name, Value값을 저장 <br>	
	 *									(딜리미터 ==> "delistr")
	 * @exception	Err					<br>에러코드 	: 15E, 150
	 *									<br>메세지		: 상위에러메세지 또는 
	 *									<br>			  파일 업로드를 실패했습니다.
     */
	public Hashtable getNameValue(HttpServletRequest req, String filepath, int filesize, String prefix, String charset, String body_charset) throws Err { 
		this.filepath = filepath;
		this.filesize = filesize;
		this.prefix = prefix;
		Hashtable nvhash = null;
		this.charset = charset;
		this.body_charset = body_charset;
		try {
			nvhash = getNameValue(req);
		} catch (Err err) {
		 	log(MNAME+".getNameValue()","에러가 발생했습니다.\n"+err.getEXStr());
            throw err;	
		}
		return nvhash;
	}


    /**
     *
     * 입력파라미터의 Name, Value 값을 리턴한다.
     * 입력파라미터의 전체 Name, Value를 Hashtable에 저장하여 리턴한다.
     *
     * @param   	req					HttpServletRequest	
     * @return  	Hashtable       	Name, Value값을 저장 <br>	
	 *									(딜리미터 ==> "delistr")
	 * @exception	Err					<br>에러코드 	: 15E, 150
	 *									<br>메세지		: 상위에러메세지 또는 
	 *									<br>			  파일 업로드를 실패했습니다.
     */
	public Hashtable getNameValue(HttpServletRequest req) throws Err { 

		boolean eof = true;		//화일의 끝을 저장
		Hashtable parahash = new Hashtable();	//리턴 값을 저장한다.
		DataInputStream dis = null;	

		try {
			byte hana;		//한바이트의 데이터를 저장한다.

			//전송된 IP를 저장한다.
			parahash.put("xxx-remoteip",req.getRemoteAddr());

			//파일스트림을 생성하고 저장한다.
			dis = new DataInputStream(req.getInputStream());
			parahash.put("xxx-filestream", dis);

			//딜리미터를 분리한다.
			int length = 0; 
			byte[]  deli = new byte[1000];
			for(int i = 0; (hana = dis.readByte()) != nl; i++) {
				deli[i] = hana;
				length++;
			}
			String delistr  = new String(deli,0,length-1);
			parahash.put("xxx-delistr", delistr);

			//데이터 저장변수를 선언한다.
			byte[] data = new byte[5000];	//라인단위별 데이터 저장
			String datastr = null;			//라인단위 데이터 저장(스트링)
			String parastr = "";			//딜리미터와 딜리미터사이의 데이터(스트링)
			String[] dataarr = null;		//Name, Value의 데이터 
											//배열(0 ==> Name, 1 ==> Value)
			int paragb = 0;

			while(eof) {	//화일의 끝일때 까지 처리한다.
				paragb++;
				length = 0;
				for(int i = 0; (hana = dis.readByte()) != nl; i++) {	//라인별로 저장
					data[i] = hana;
					length++;
				}


				if(length == 0) {
					datastr = new String(data,0,length);
				} else {
					if(charset != null && !charset.equals("")) {
						datastr = new String(data,0,length-1,"UTF-8");	
					} else {
						datastr = new String(data,0,length-1);	
					}
				}
				
				if(datastr.indexOf("filename") != -1 
						&& datastr.indexOf(nameHeader) != -1) {	 //파일에 대한 처리

					dataarr = getFileName(datastr);		
					
					if(dataarr[1].equals("")){
						int cnt = 0;
						while(true) {
							if(dis.readByte() == nl) cnt++;
							if(cnt == 4) break;
						}
						continue;
					}

					//실제 저장 파일의 전체 경로
					parahash.put("xxx-filename",dataarr[0]); 
					//실제 전송된 파일명
					parahash.put("xxx-real-filename",dataarr[1]);
					//저장된 파일명
					parahash.put("xxx-tmp-filename",dataarr[2]);
										
					Hashtable inputhash = new Hashtable();
					inputhash.put("xxx-filestream",dis);
					inputhash.put("xxx-filename",dataarr[0]);
					inputhash.put("xxx-delistr",delistr);
					int filesize = getFile(inputhash);
					parahash.put("xxx-filesize",Integer.toString(filesize));
				} else {	//Name, Value값 처리
					if(datastr.equals(delistr) || datastr.equals(delistr+"--")) { //딜리미터와 같을 경우
						//Name, Value 배열을 가져온다.
						dataarr = getSeparate(parastr); 
						parahash.put(dataarr[0],dataarr[1]); 
						parastr = "";
						paragb = 0;		//파라미터의 구분을 0으로 재설정						
					} else {	//딜리미터와 다를 경우	
						parastr += datastr + "\n";
					}
				}
				data = new byte[5000];
			} //while의 끝

		} catch (Err err) {
 			log(MNAME+".getNameValue()","에러가 발생했습니다.\n"+err.toString());
            throw new Err(err.getErrValue());
		} catch (FileNotFoundException fnfe) {
 			log(MNAME+".getNameValue()", "에러가 발생했습니다.\n"+fnfe.toString());
            throw new Err("1","5","0","파일 업로드 실패했습니다.");
		} catch (IOException ioe) {
			eof = false;
		} catch (Exception e) {
 			log(MNAME+".getNameValue()", "에러가 발생했습니다.\n"+e.toString());
            throw new Err("1","5","0","파일 업로드 실패했습니다.");
		} finally {
			try {
				dis.close();
			} catch (Exception e) {}
		}
		return parahash;
	}

    /**
     *
     * Name, Value 값을 분리한다.
	 * 하나의 Name, Value 쌍을 리턴한다. 
     *
	 * @param		parastr				딜리미터 정보
	 * @return  	String[]			Name, Value 배열	
	 * @exception	Err					<br>에러코드 	: 150 
	 *									<br>메세지		: 파일 업로드를 실패했습니다.  
	 *
     */
	public String[] getSeparate(String parastr) throws Err {
		String[] dataarr = new String[2];
		String name = null;
		String value = null;
		//맨 처음과 마지막 문자 하나(\n)를 짤라낸다.
		parastr = parastr.substring(0,parastr.length()-1);
		try {
			int ipos = parastr.indexOf("=");		
			String cutstr = parastr.substring(ipos+2);
			name = cutstr.substring(0,cutstr.indexOf("\""));
			value = cutstr.substring(cutstr.indexOf("\"")+1);
			value = value.substring(2);
			dataarr[0] = name;
			dataarr[1] = value;
		} catch (Exception e) {
 			log(MNAME+".getSeparate()","에러가 발생했습니다.\n"+e.toString());
            throw new Err("1","5","0","파일 업로드를 실패했습니다.");
		}
		return dataarr;
	}

    /**
     *
     * 화일명값을 분리한다.
     *
	 * @param		parastr				화일 딜리미터 정보
	 * @return  	String[]			Name, Filename 배열	
	 * @exception	Err					<br>에러코드 	: 150
	 *									<br>메세지		: 파일 업로드를 실패했습니다.
	 *
     */
	public String[] getFileName(String parastr) throws Err {
		String[] dataarr = new String[3];
		String name = null;
		String value = null;
		try {
			String cutstr = parastr.substring(parastr.indexOf("=")+2);		
			name = cutstr.substring(0,cutstr.indexOf("\""));
			cutstr = cutstr.substring(cutstr.indexOf("=")+2);
			value = cutstr.substring(0,cutstr.indexOf("\""));
			value = value.replace('\\','/'); 	//Unix, Window 파일 패스 처리
			String filename = value.substring(value.lastIndexOf("/")+1);

				
			//dataarr[0] = name;
			//dataarr[0] = "xxx-filename";
			if(filename.equals("")) {	//파일명이 없을경우 	
				dataarr[0] = "";
				dataarr[1] = "";
				dataarr[2] = "";
			} else {
				//dataarr[1] = filepath+getPlatformPath()+getDate(Code.TM_YMDHMS)+"-"+filename;
				if(prefix.equals("")) {	
					dataarr[0] = filepath+getPlatformPath()+filename;
					dataarr[1] = filename;
					dataarr[2] = filename;
				} else {
					dataarr[0] = filepath+getPlatformPath()+prefix+"-"+filename;
					dataarr[1] = filename;
					dataarr[2] = prefix+"-"+filename;
				}
			}
		} catch (Exception e) {
 			log(MNAME+".getFileName()","에러가 발생했습니다.\n"+e.toString());
            throw new Err("1","5","0","파일 업로드를 실패했습니다.");
		}
		return dataarr;
	}
		

    /**
     *
     * 화일내용을 저장한다.
     *
	 * @param		inputhash	화일 저장 정보<br>
	 *							(DataInputStream, filename, delistr)
	 * @return		int			첨부 파일 사이즈
	 * @exception	Err			<br>에러코드 	: 15E,150,156
	 *							<br>메세지		: 파일 업로드를 실패했습니다.
	 *
     */
	public int getFile(Hashtable inputhash) throws Err { 
		
		FileOutputStream dos = null;
		PrintWriter dos_utf8 = null;

		DataInputStream dis = null;
		String filename = null;
		String delistr = null;
		int maxsize = 0;
		if(inputhash.containsKey("xxx-filestream")) {
			dis = (DataInputStream)inputhash.get("xxx-filestream");
		} else {
 			log(MNAME+".getFile()","에러가 발생했습니다.\nInputStream 없음");
            throw new Err("1","5","E","파일 저장을 실패했습니다.");
		}
		if(inputhash.containsKey("xxx-filename")) {
			filename = (String)inputhash.get("xxx-filename");
		} else {
 			log(MNAME+".getFile()","에러가 발생했습니다.\n파일명 없음");
            throw new Err("1","5","E","파일 저장을 실패했습니다.");
		}
		if(inputhash.containsKey("xxx-delistr")) {
			delistr = (String)inputhash.get("xxx-delistr");
		} else {
 			log(MNAME+".getFile()","에러가 발생했습니다.\n딜리미터 없음");
            throw new Err("1","5","E","파일 저장을 실패했습니다.");
		}

		if(inputhash.containsKey("filesize")) {
			maxsize = Integer.parseInt((String)inputhash.get("filesize"));
		} else {
			maxsize = filesize;
		}	

		//파일 크기 초과 여부
		boolean fullsize = false;

		//파일의 크기
		int sizecnt = 0;
			
		try {
			int cnt = 0;
			while(true) {
				if(dis.readByte() == nl) cnt++;
				if(cnt == 2) break;
			}

			if(filename.equals("")) return 0;
		
			//해당 디렉토리 생성			
			String dir_str = filename.substring(0,filename.lastIndexOf(getPlatformPath()));
			new File(dir_str).mkdirs();

			//파일을 저장한다.
			if(body_charset == null) {
				dos = new FileOutputStream(filename);
			} else {
				dos_utf8 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename),body_charset));
			}

			//데이터 저장 관련 변수
			byte hana;		
			byte[] data = new byte[BUF_SIZE];	//라인단위별 데이터 저장
			String line = null;
			int len = 0;
			
			while(true) {	//화일의 끝일때 까지 저장
				
				for(len = 0; (hana = dis.readByte()) != nl; len++) {	//라인별로 저장
					sizecnt++; //파일 크기 증가
					data[len] = hana;
					if(len == (BUF_SIZE-1)) break;
				}
				
				if((sizecnt-len) > maxsize) {
					dos.close();

					File fl = new File(filename);
					fl.delete();
					fullsize = true;	//파일 용량 초과를 알림
					break;
				}					

				if(len < (BUF_SIZE-1)) { 
					data[len] = nl;
					//delimeter와 동일한 문자라면 break;
					if(len != 0) {
						line = new String(data,0,len-1);
						if(line.equals(delistr) || line.equals(delistr+"--")) {
							break;
						}
					}
				}	

				if(body_charset == null) {
					dos.write(data,0,len+1);
				} else {
					dos_utf8.print(new String(data,0,len+1));		
				}

				data = new byte[BUF_SIZE];

			}
		} catch (EOFException eofe) {
			//파일의 끝
		} catch (IOException ioe) {
 			log(MNAME+".getFile()","에러가 발생했습니다.\n"+ioe.toString());
            throw new Err("1","5","6","파일 저장을 실패했습니다.");
		} catch (Exception e) {
 			log(MNAME+".getFile()","에러가 발생했습니다.\n"+e.toString());
            throw new Err("1","5","0","파일 저장을 실패했습니다.");
		} finally {
			try {
				if(dos != null) dos.close();
				if(dos_utf8 != null) dos_utf8.close();
			} catch (Exception e) {}
		}

		if(fullsize) {
 			log(MNAME+".getFile()","전송 파일 사이즈가 너무큽니다.");
            throw new Err("B","5","E","파일 사이즈가 너무 큽니다.("+maxsize+"바이트)");
		}

		return sizecnt;

	}

    /**
     *
     * 파일 내용을 저장한다.
     *
	 * @param		dis			화일 딜리미터 정보
	 * @param		String		화일명 정보
	 * @param		String		딜리미터
	 * @exception	Err			<br>에러코드 	: 150,156
	 *							<br>메세지		: 파일 저장을 실패했습니다.
	 *
     */
	public void getFile(DataInputStream dis, 
					   String filename, String delistr) throws Err { 
		
		FileOutputStream dos = null;
		int len = 0;
		byte[] data = new byte[BUF_SIZE];

		try {
			int cnt = 0;
			while(true) {
				if(dis.readByte() == nl) cnt++;
				if(cnt == 2) break;
			}

			//파일을 저장한다.
			dos = new FileOutputStream(filepath+filename);

			//데이터 저장 관련 변수
			byte hana;		
			String dataline = null;

			while(true) {	//화일의 끝일때 까지 저장
				hana = dis.readByte();
				len++;
				if(hana != nl) {
					data[len-1] = hana;
					if(len == BUF_SIZE) {
						dos.write(data,0,len);
						len = 0;	
						data = new byte[BUF_SIZE];
					}
				} else {
					data[len-1] = hana;
					dataline = new String(data,0,len);
					if(dataline.indexOf(delistr) != -1) {
						break;
					}
					dos.write(data,0,len);
					len = 0;
					data = new byte[BUF_SIZE];
				}
			}
		} catch (EOFException eofe) {
			//파일의 끝
		} catch (IOException ioe) {
 			log(MNAME+".getFile()","에러가 발생했습니다.\n"+ioe.toString());
            throw new Err("1","5","6","파일 저장을 실패했습니다.");
		} catch (Exception e) {
 			log(MNAME+".getFile()","에러가 발생했습니다.\n"+e.toString());
            throw new Err("1","5","0","파일 저장을 실패했습니다.");
		} finally {
			try {
				dis.close();
				dos.close();
			} catch (Exception e) {}
		}
	}
	
}
