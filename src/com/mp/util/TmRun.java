package com.mp.util; 

import java.util.*;
import java.lang.*;

/**
 * <pre>
 * 프로그램유형 : TIME THREAD(java)
 * 프로그램명   : TmRun.java
 * Version      : 1.0
 * 작성일       : 2005/02/26
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 *
 * 설명         : TIME THREAD 수행 클래스의 인터페이스를 정의한다.
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public interface TmRun {
	
	/**
	 * 작업을 수행한다.
	 */
	public void doWork(); 

	/**
	 * 초기화 설정 작업을 수행한다. 
	 */
	public void setWork(Hashtable setinfohash); 
	

}
