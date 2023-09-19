package com.mp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import com.tagfree.util.StringTokenizer;

/**
 * <pre>
 * 프로그램유형 : MakeFile(java)
 * 프로그램명   : MakeFile.java
 * Version      : 1.0
 * 작성일       : 2013/01/18
 * 작성자       : 이소라
 * 수정일       : 
 * 수정자       : 
 * 수정설명		: 
 *
 * 설명         : 수신거부자 파일 비교 메소드를 구현한다.
 *
 * 프로젝트명   : 
 * Copyright	: 
 * </pre>
 */
public class MakeFile extends WebUtil {

	/*=============================================*/
	// Global Zone 
	/*=============================================*/
	private String MNAME = "MakeFile";

	/** 생성자를 호출한다. 
	 * @throws IOException */
//	public MakeFile() {}

    public String makeFile(String filePath, String userId) throws IOException{ 

    	BufferedReader br = null;
    	BufferedReader tempBr = null;
    	
    	String oldFilePath = filePath;
    	String makeFilePath = "D:/solution/ABN_UMS/mpv3/front/upload/test/fileTest.csv";
    	
//    	ArrayList oldFile = new ArrayList();
    	ArrayList oldFileTmp = new ArrayList();
//    	ArrayList oldFileAlias = new ArrayList();
    	
    	ArrayList makeFile = new ArrayList();
    	ArrayList makeFileAlias = new ArrayList();
    	
    	ArrayList newFile = new ArrayList();
    	
    	int tmp_tot_cnt = 0;
    	
    	String oldData = "";
    	String makeData = "";
    	
    	String returnFileNm = "";
        try { 
        	// file load
        	br = new BufferedReader( new InputStreamReader (new FileInputStream(oldFilePath),"UTF-8"));
////        	br = new BufferedReader( new InputStreamReader (new FileInputStream(filePath), "UTF-8"));
        	tempBr = new BufferedReader( new InputStreamReader (new FileInputStream(makeFilePath),"UTF-8"));
        	
        	System.out.println("==>> tempBr ==>> " + tempBr);
        	
//        	int acnt = 0;	// 알리아스의 갯수
        	
        	int acnt2 = 0;	// 알리아스의 갯수
        	
        	if(br != null) br.close();
        	br = new BufferedReader( new InputStreamReader (new FileInputStream(oldFilePath),"UTF-8"));
        	tempBr = new BufferedReader( new InputStreamReader (new FileInputStream(makeFilePath),"UTF-8"));

        	// 사용자 upload list
        	while((oldData = br.readLine()) != null){
        		if("".equals(oldData.trim())) continue;
        		oldFileTmp.add(oldData);
        	}         	
        	
        	// 수신 동의 list
        	while((makeData = tempBr.readLine()) != null){
        		if("".equals(makeData.trim())) continue;
        		
        		StringTokenizer st = new StringTokenizer(makeData,",");
        		
        		if(tmp_tot_cnt == 0){
        			while(st.hasMoreTokens()){
        				makeFileAlias.add(st.nextToken());
        				acnt2++;
        				System.out.println("acnt " + acnt2 + "==>>> " + makeFileAlias);
        			}
        		}else{
        			if(makeData == null || makeData.equals("")) break;
        			Hashtable unitInfo = new Hashtable();
        			for(int cnt = 0; cnt < acnt2; cnt++){
        				unitInfo.put((String)makeFileAlias.get(cnt), st.nextToken());
        			}
        			makeFile.add(unitInfo.get("ID"));
        		}
        		tmp_tot_cnt++;
        	}        	
        	
        	System.out.println("비교 서버 값 ==>> " + makeFile.toString());
        	System.out.println("사용자 upload oldFileTmp ==>> " + oldFileTmp);
        
        int idTmp = 0;
        String oldTmp = "";
        String oldTmpId = "";
        int oldIdTmp = 0;
        for(int idx=0; idx<oldFileTmp.size(); idx++){
        	for(int jdx=0; jdx<makeFile.size(); jdx++){
        		idTmp = ((String) oldFileTmp.get(idx)).indexOf((String) makeFile.get(jdx));
        		oldTmp = (String) oldFileTmp.get(idx);
        		System.out.println("========================= START ==========================");
        		System.out.println("==>> idTmp ==>> " + idTmp);
        		System.out.println("==>> idx ==>> " + idx);
        		System.out.println("==>> makeFile.get(jdx) ==>> " + makeFile.get(jdx));
        		System.out.println("==>> oldFileTmp.get(idx) ==>> " + oldFileTmp.get(idx));
        		System.out.println("========================= END ==========================");
        		if(idx == 0 && jdx == 0){
        			newFile.add(oldFileTmp.get(idx));
        		}
        		if(idTmp != -1){
        			oldIdTmp = oldTmp.substring(idTmp).indexOf(",");
        			oldTmpId = oldTmp.substring(idTmp).substring(0,oldIdTmp);
        			if(oldTmpId.equals(makeFile.get(jdx))){
        				newFile.add(oldFileTmp.get(idx));
        			}
        			System.out.println("======================================================");
        			System.out.println("==>> oldFileTmp.get(idx) substring ==>> " + oldTmp.substring(idTmp));
        			System.out.println("==>> oldFileTmp.get(idx) substring 222 ==>> " + oldTmp.substring(idTmp).substring(0,oldIdTmp));
        			System.out.println("==>> oldFileTmp.get(idx) substring indexof==>> " + oldTmp.substring(idTmp).indexOf(","));
        		}
        	}
        }
        
        System.out.println("==>> newFile ==>> " + newFile);
        
        String uploadList = "";
        StringBuffer newSb = new StringBuffer();
        
        for(int i=0; i<newFile.size(); i++){
        	uploadList = (new StringBuffer().append(newFile.get(i)).append("\r\n")).toString();
        	newSb.append(uploadList);
        }
        
        System.out.println("==>> newSb ==>> " + newSb);

        
        //파일 만들기
        returnFileNm = fileList(newSb, userId, filePath);
        System.out.println("==>> returnFileNm ==>> " + returnFileNm);	
        } catch (Exception e) { 
                e.printStackTrace(); 
//                oldFile.clear();
//                oldFileAlias.clear();
                tmp_tot_cnt = 1;
//                tot_cnt = 1;
                if(br != null) br.close();
                
        } finally { 

        } 
        return returnFileNm; 
    }
    
    // 파일 만듬
    public String fileList(StringBuffer newSb, String userId, String filePath){
    	SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMddHHmmss");
    	GregorianCalendar gc = new GregorianCalendar();
    	String day = dayTime.format(gc.getTime());
//    	String newPath = "D:/solution/ABN_UMS/mpv3/front/upload/temp/"+ userId + "/";
//    	String newPath = filePath;
    	String newPath = filePath.substring(0, filePath.indexOf(userId))+userId+"/";
    	System.out.println("==>> filePath 11==>> " + filePath);
    	System.out.println("==>> filePath 22==>> " + filePath.indexOf(userId));
    	System.out.println("==>> filePath 33==>> " + filePath.substring(0, filePath.indexOf(userId))+userId+"/");
    	
    	File dir = new File(newPath);
    	String fileName = "";
    	if(!dir.isDirectory()){
    		dir.mkdirs();
    	}
    	
    	FileWriter fw = null;
    	try {
			fileName = new StringBuffer()
							.append(newPath)
							.append(userId)
							.append(day)
							.append(".csv")
							.toString();
			fw = new FileWriter(fileName, true);
			fw.write(newSb.toString());
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}finally{
			try {
				if(fw != null){
					fw.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
    	return fileName;
    }

}








