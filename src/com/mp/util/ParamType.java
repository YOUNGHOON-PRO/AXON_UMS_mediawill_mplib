package com.mp.util; 

import java.util.*;
import java.lang.*;

/**
 * <pre>
 * 프로그램유형 : type(java)
 * 프로그램명   : ParamType.java
 * Version      : 1.0
 * 작성일       : 2002/12/07
 * 작성자       : 오범석
 * 수정일       : 2004/01/14
 * 수정자       : 오범석
 * 수정설명		: 파라미터 정보를 프린트한다.
 *
 * 설명         : 데이터베이스 파라미터 설정
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class ParamType implements java.io.Serializable {
	
	/********************************************/
	//		Grobal Zone
	/********************************************/
	/** 데이터저장	*/
	Hashtable typehash = null;
	
	/** 순번		*/
	private int num;
	/** 데이터		*/
	private String value;
	/** 데이터타입 	*/
	private int type;

	/**
	 * 생성자
	 */
	public ParamType() {
		typehash = new Hashtable();
	}

	/**
	 * 한필드의 데이터 타입추가
	 * @param		num		순번
	 *				value	값
	 *				type	데이터타입
	 */
	public void put(int num, String value, int type) {
		String[] typearr = new String[2];
		typearr[0] = value;
		typearr[1] = Integer.toString(type);
		typehash.put(Integer.toString(num), typearr); 
	}

	/**
	 * 한필드의 데이터 타입추가
	 * @param		num		순번
	 *				value	값
	 *				type	데이터타입
	 */
	public void put(int num, String value, String type) {
		String[] typearr = new String[2];
		typearr[0] = value;
		typearr[1] = type;
		typehash.put(Integer.toString(num), typearr); 
	}
	
	/**
	 * 한 필드의 값을 읽어온다.
	 * @param		num			순번
	 * @return		String[]	데이터값, 데이터유형
	 */
	public String[] getRow(String num) {
		if(typehash.containsKey(num)) {	
			return (String[])typehash.get(num);
		} else {
			return null;
		}
	}

	/**
	 * 특정순번의 값을 읽어온다.
	 * @param		num			순번
	 * @return		String		데이터값
	 */
	public String getValue(int num) {
		String numstr = Integer.toString(num);
		if(typehash.containsKey(numstr)) {	
			return ((String[])typehash.get(numstr))[0];
		} else {
			return null;
		}
	}

	/**
	 * 특정순번의 데이터유형을 읽어온다.
	 * @param		num			순번
	 * @return		int			데이터유형	
	 */
	public int getType(int num) {
		String numstr = Integer.toString(num);
		if(typehash.containsKey(numstr)) {	
			return Integer.parseInt(((String[])typehash.get(numstr))[1]);
		} else {
			return -1;
		}
	}

	/**
	 * 데이터수
	 * @return		int		데이터수
	 */
	public int size() {
		return typehash.size();
	}

	/**
	 * 파라미터 프린트
	 * @param	flag	프린트 여부
	 * @param	pos		출력 모듈 위치
	 */
	public void print(boolean flag, String pos) {
		if(flag) {
			System.out.println("[출력위치]" + pos);	
			System.out.println("[파라미터 정보 프린트 시작]");	
			for(int i = 1; i <= size(); i++) {
				System.out.println("["+i+" 번째][타입:"+getType(i)+"][값:"+getValue(i)+"]");
			}
			System.out.println("[파라미터 정보 프린트 끝]");	
		}
	}
}
