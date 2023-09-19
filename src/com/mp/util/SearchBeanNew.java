package com.mp.util;

import java.io.IOException;

/**
 * <pre>
 * 프로그램유형 : SearchBeanNew(java)
 * 프로그램명   : SearchBeanNew.java
 * Version      : 1.0
 * 작성일       : 2013/01/24
 * 작성자       : 이소라
 * 수정일       : 
 * 수정자       : 
 * 수정설명		: 
 *
 * 설명         : 
 *
 * 프로젝트명   : 
 * Copyright	: 
 * </pre>
 */
public class SearchBeanNew extends WebUtil {

	/*=============================================*/
	// Global Zone 
	/*=============================================*/
	private String MNAME = "SearchBeanNew";

	/** 생성자를 호출한다. 
	 * @throws IOException */
//	public MakeFile() {}
	java.util.Date current_date = new java.util.Date();
	int current_year = (current_date.getYear()+1900);
	int current_month = (current_date.getMonth()+1);
	
	public String getRecentYear()  // 현재 년도
	{
		return current_year+"";  
	}
	public String getRecentMonth()  // 현재 월
	{
		return current_month+"";    
	}

}








