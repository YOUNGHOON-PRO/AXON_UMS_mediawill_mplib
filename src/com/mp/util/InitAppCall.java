
package com.mp.util;

import java.io.*;
import java.util.*;
import java.lang.*;
import com.mp.exception.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : Util(java)
 * 프로그램명   : InitAppCall.java
 * Version      : 1.0
 * 작성일       : 2003/06/09
 * 작성자       : 오범석
 * 수정일       : 
 * 수정자       : 
 * 설명         : 초기화 메소드를 구현한다.
 *				  일반 어플리 케이션에서 콤포넌트를 사용하여 어플리케이션을 실행하기 위한 
 *				  초기화 클래스
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class InitAppCall extends WebUtil {

	/*=============================================*/
	// Global Zone 
	/*=============================================*/

	/** 생성자를 호출한다. */
	public InitAppCall() {

		//환경 화일 위치를 읽어온다.
		String envfile = System.getProperty("envfile");

		//초기화를 실시한다.
		Hashtable envhash = getEnvironment(envfile);

		setMNAME("InitApp");
		//플랫폼을 설정한다.
		getPlatformPath();
	}

}






