package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;
import com.mp.util.*;
import com.mp.exception.*;

/**
 * <pre>
 * 프로그램명	: ExecTest.java
 * Version 		: 1.0
 * 작성일 		: 2003/02/18
 * 작성자 		: 오범석
 * 수정일 		: 
 * 수정자 		: 
 * 설명			: 작성한 모듈을 테스트 한다.
 *
 * 프로젝트		: 표준콤포넌트
 * Copyright	: (주)다코시스템
 * </pre> 
 */
public class ExecTest extends WebUtil {

	/**
	 * 실행 메소드
	 */
	public static void main(String[] args) throws Exception {

		/* ====================================================

			usage : java -Denvfile=[환경화일패스] com.mp.util.ExecTest

		   ==================================================== */
		
		InitAppCall init = new InitAppCall();

		FileDistLog log = new FileDistLog();

		new FileDist(log,"/user1/ohbs/zzzzz").start();
		

	}

}

			

