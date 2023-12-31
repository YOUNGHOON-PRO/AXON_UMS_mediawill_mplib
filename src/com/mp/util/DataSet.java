package com.mp.util; 

import java.util.*;
import java.lang.*;

/**
 * <pre>
 * 프로그램유형 : DataSet(java)
 * 프로그램명   : DataSet.java
 * Version      : 1.0
 * 작성일       : 2003/12/07
 * 작성자       : 오범석
 * 수정일       : 2004/01/01
 * 수정자       : 오범석
 * 수정설명		: ParamType 저장 추가
 * 수정일       : 2004/06/22
 * 수정자       : 오범석
 * 수정설명		: 데이터 조회 메소드 추가 [find 메소드]
 *
 * 설명         : 처리 데이터셋의 인터페이스를 구현한다.
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public interface DataSet {
	
	/**
	 * 데이터셋의 데이터의 수
	 * @return		int		데이터의 수(-1 이면 에러)
	 */
	public int size(); 

	/**
	 * 현재 데이터셋의 데이터셋 위치
	 * @return		int		데이터셋의 위치(-1 이면 에러)
	 */
	public int curPos(); 

	/**
	 * 데이터셋의 위치 설정
	 * @param		rowpos	설정 위치
	 * @return		int		데이터셋의 위치(-1 이면 에러)
	 */
	public int setCurPos(int rowpos); 

	/**
	 * 데이터셋의 현재 위치를 이전으로 이동한다.
	 * @return		boolean 	이동여부
	 */
	public boolean prev(); 

	/**
	 * 데이터셋의 현재 위치를 이전으로 이동한다.
	 * @param		cnt			이동할 데이터 수
	 * @return		boolean  	이동여부	
	 */
	public boolean prev(int cnt); 

	/**
	 * 데이터셋의 현재 위치를 이후로 이동한다.
	 * @return		boolean 	이동 여부
	 */
	public boolean next(); 

	/**
	 * 데이터셋의 현재 위치를 이후로 이동한다.
	 * @param		cnt			이동할 데이터 수
	 * @return		boolean 	이동여부	
	 */
	public boolean next(int cnt); 

	/**
	 * 데이터셋의 마지막으로 이동
	 * @return		boolean 	이동여부
	 */
	public boolean last(); 
	
	/**
	 * 데이터셋이 마지막여부를 확인한다.
	 * @return		boolean 	데이터셋의 마지막 여부
	 */
	public boolean isLast(); 

	/**
	 * 데이터셋의 처음으로 이동
	 * @return		boolean 	이동여부	
	 */
	public boolean first(); 

	/**
	 * 데이터셋이 처음인지를 확인한다.
	 * @return		boolean 	데이터셋의 처음 여부
	 */
	public boolean isFirst(); 

	/**
	 * 데이터셋을 초기화한다(포지션 초기화).
	 * @return		int 	초기화 커서 위치
	 */
	public int reset(); 

	/**
	 * 호출 SQL문을 리턴한다.  
	 * @return		String		실행 SQL문
	 */
	public String getSQL(); 

	/**
	 * 호출 SQL문을 설정한다.  
	 * @param		String		실행 SQL문
	 */
	public void setSQL(String SQL); 

	/**
	 * 호출 COUNT SQL문을 리턴한다.  
	 * @return		String		실행 COUNT SQL문
	 */
	public String getCSQL(); 

	/**
	 * 호출 COUNT SQL문을 설정한다.  
	 * @return		String		실행 COUNT SQL문
	 */
	public void setCSQL(String CSQL); 

	/**
	 * 데이터셋에 대한 메타정보를 리턴한다.
	 * @return		Vector		데이터셋에 대한 메타정보			
	 */
	public Vector getMetaInfo(); 

	/**
	 * 데이터셋에 대한 메타정보를 설정한다.
	 * @param		metainfo	데이터셋에 대한 메타정보
	 * @return		boolean 	설정성공여부
	 */
	public boolean setMetaInfo(Vector metainfo); 

	/**
	 * 리스트 데디터에 대한 전체 정보를 리턴한다.
	 * @return		Vector		리스트 전체 데이터
	 */
	public Vector getListData();

	/**
	 * 현재 ROW에 대한 COLUMN의 값
	 * @param		colname		컬럼명
	 * @return		String		데이터값
	 */
	public String getData(String colname);

	/**
	 * 현재 ROW에 대한 COLUMN의 값
	 * @param		colname		컬럼명
	 *				dataHash	데이터셋
	 * @return		String		데이터값
	 */
	public String getData(String colname, Hashtable dataHash);

	/**
	 * 현재 ROW에 대한 COLUMN의 값
	 * @param		colpos			컬럼의 포지션
	 * @return		String			데이터값
	 */
	public String getData(int colpos);

	/**
	 * 특정 ROW에 대한 COLUMN의 값
	 * @param		colnm			컬럼명
	 * @param		rowpos			로우의 위치
	 * @return		String			데이터값
	 */
	public String getData(String colnm, int rowpos);

	/**
	 * 페이지당 로우의 수를 리턴한다.
	 * @return		int			페이지당 로우 수
	 */
	public int getPagerow();

	/**
	 * 페이지당 로우의 수를 설정한다.
	 * @param		pagerow			페이지당 로우 수
	 * @return		boolean			설정 설공 여부
	 */
	public boolean setPagerow(int pagerow);

	/**
	 * 전체 데이터 수를 페이지로 계산했을때 전체 페이지 수를 리턴한다.
	 * @return		int			전체 페이지 수
	 */
	public int getTotPage();

	/**
	 * 전체 데이터 수를 페이지로 계산했을때 전체 페이지 수를 설정한다.
	 * @param		pagerow			전체 페이지 수
	 * @return		boolean			설정 설공 여부
	 */
	public boolean setTotPage(int totpage);

	/**
	 * 전체 데이터 수를 리턴한다.
	 * @return		int			전체 데이터 수
	 */
	public int getTotCnt();

	/**
	 * 전체 데이터 수를 설정한다.
	 * @param		totcnt			전체 데이터 수
	 * @return		boolean			설정 설공 여부
	 */
	public boolean setTotCnt(int totcnt);

	/**
	 * 현재 페이지 수를 리턴한다.
	 * @return		int			현재 데이터 수
	 */
	public int getPage();

	/**
	 * 현재 데이터 수를 설정한다.
	 * @param		page			현재 데이터 수
	 * @return		boolean			설정 설공 여부
	 */
	public boolean setPage(int page);

	/**
	 * 컬럼수를 리턴한다.
	 * @return		int			컬럼의 수
	 */
	public int getColCnt();

	/**
	 * 컴럼포지션에 대한 컬럼명을 리턴한다.
	 * @param		colpos			컬럼의 포지션		
	 * @return		String			컬럼명
	 */
	public String getColNM(int colpos);

	/**
	 * 컴럼명에 대한 컬럼포지션을 리턴한다.
	 * @param		colnm			컬럼명
	 * @return		int				컬럼포지션
	 */
	public int getColPos(String colnm);

	/**
	 * 컴럼포지션에 대한 컬럼유형을 리턴한다.
	 * @param		colpos			컬럼의 포지션		
	 * @return		String			컬럼유형
	 */
	public String getColType(int colpos);

	/**
	 * 컴럼포지션에 대한 컬럼유형을 리턴한다(INT형).
	 * @param		colpos			컬럼의 포지션		
	 * @return		int				컬럼유형�
	 */
	public String getColRawType(int colpos);

	/**
	 * 컴럼포지션에 대한 컬럼의 길이를 리턴한다.
	 * @param		colpos			컬럼의 포지션		
	 * @return		String			컬럼길이
	 */
	public String getColSize(int colpos);

	/**
	 * 컴럼포지션에 대한 컬럼의 테이블명을 리턴한다.
	 * @param		colpos			컬럼의 포지션		
	 * @return		String			컬럼의 테이블명
	 */
	public String getColTname(int colpos);

	/**
	 * 컴럼을 추가한다.
	 * 맨마지막으로 저장한다.
	 * @param		colinfo			컬럼정보
	 * @return		boolean			추가 여부
	 */
	public boolean addColumn(String[] colinfo);

	/**
	 * 컴럼을 추가한다.
	 * @param		colinfo			컬럼정보
	 *				pos				추가할 포지션
	 * @return		boolean			추가 여부
	 */
	public boolean addColumn(int pos, String[] colinfo);

	/**
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * [필드명,데이터] 형식
	 * @return		Hashtable		현재 로우의 정보
	 */
	public Hashtable getRow();

	/**
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * [필드명,데이터] 형식
	 * @param		rowpos			데이터셋의 위치
	 * @return		Vector			현재 로우의 정보
	 */
	public Hashtable getRow(int rowpos);

	/**
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * 데이터셋의 원본 데이터
	 * @return		Vector			원본로우 데이터
	 */
	public Vector getRawRow();

	/**
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * 데이터셋의 원본 데이터
	 * @param		rowpos			데이터셋의 위치
	 * @return		Vector			원본로우 데이터
	 */
	public Vector getRawRow(int rowpos);

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(Vector rowdata);

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(int rowpos, Vector rowdata);

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(Hashtable rowdata);

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(int rowpos, Hashtable rowdata);

	/**
	 * 로우 데이터를 설정한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean setRow(int rowpos, Vector rowdata);

	/**
	 * 로우 데이터를 설정한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean setRow(int rowpos, Hashtable rowdata);

    /**
     * 해당 로우의 해당 컬럼에 데이터를 설정한다.
     *
     * @param       data            설정 데이터
     * @param       rowpos          로우 취치
     * @param       columnpos       컬럼 위치
     * @return      boolean         로우 데이터 추가 여부
     */
    public boolean setData(String data, int rowpos, int columnpos); 

    /**
     * 해당 로우의 해당 컬럼에 데이터를 설정한다.
     *
     * @param       data            설정 데이터
     * @param       rowpos          로우 취치
     * @param       column          컬럼명
     * @return      boolean         로우 데이터 추가 여부
     */
    public boolean setData(String data, int rowpos, String column); 


	/**
	 * 헤더 메타 정보를 프린트(STD OUT)한다.
	 *
	 * @param	    print_yn		STD OUT 여부
	 * @param	    mname			호출 모듈 위치
	 * @return		String			프린트 스트링	
	 */
	public String metaPrint(boolean print_yn, String mname);

	/**
	 * 로우 데이터 정보를 프린트(STD OUT) 한다.
	 *
	 * @param	    print_yn		STD OUT 여부
	 * @param	    mname			호출 모듈 위치
	 * @return		String			프린트 스트링	
	 */
	public String rowPrint(boolean print_yn, String mname);

	/**
	 * 전체 데이터셋 정보를 프린트(STD OUT) 한다.
	 *
	 * @param	    print_yn		STD OUT 여부
	 * @param	    mname			호출 모듈 위치
	 * @return		String			프린트 스트링	
	 */
	public String print(boolean print_yn, String mname);

    /**
     * 파라미터 정보를 프린트(STD OUT) 한다.
     *
     * @param       print_yn        STD OUT 여부
     * @param       mname           호출 모듈 위치
     * @return      String          프린트 스트링
     */
    public String paramPrint(boolean print_yn, String mname);

    /**
     * ParamType을 설정한다.
     *
     * @param       param           ParamType
     */
    public void setParam(ParamType param);

    /**
     * ParamType을 리턴한다.
     *
     * @return      ParamType           ParamType
     */
    public ParamType getParam();

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 첫번째 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       data                찾을 데이터
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
     */
    public int findData(String colnm, String data); 

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 첫번째 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       data                찾을 데이터
     * @param       flag                대소문자 구분 여부
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
     */
    public int findData(String colnm, String data, boolean flag); 

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 마지막 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       data                찾을 데이터
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
     */
    public int findLastData(String colnm, String data); 

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 마지막 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       data                찾을 데이터
     * @param       flag                대소문자 구분 여부
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
     */
    public int findLastData(String colnm, String data, boolean flag); 

    /**
     * 특정 컬럼의 매핑 데이터 전체를 리턴한다.
     * [AAA] 컬럼의 [aaa] 값을 갖는 모든 로우를 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       dataStr             매핑 데이터
     * @return      Vector              데이터 리스트
     *
     */
    public Vector getMatchDataList(String colnm, String dataStr); 

}
