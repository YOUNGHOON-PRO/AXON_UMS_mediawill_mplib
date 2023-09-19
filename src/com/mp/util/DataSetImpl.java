package com.mp.util; 

import java.util.*;
import java.lang.*;
import com.mp.util.*;

/**
 * <pre>
 * 프로그램유형 : DataSet(java)
 * 프로그램명   : DataSetImpl.java
 * Version      : 1.0
 * 작성일       : 2003/12/07
 * 작성자       : 오범석
 * 수정일       : 2004/01/17
 * 수정자       : 오범석 
 * 수정설명		: 데이터셋 정보 Print할때 호출 위치를 설정하도록 수정
 * 수정일       : 2004/06/22
 * 수정자       : 오범석
 * 수정설명     : 데이터 조회 메소드 추가 [find 메소드]
 *
 * 설명         : 처리 데이터셋을 구현한다.
 *
 * 프로젝트명   : 표준컴포넌트
 * Copyright	: (주)다코시스템
 * </pre>
 */
public class DataSetImpl implements com.mp.util.DataSet, java.io.Serializable {

	/* ================================================================ 
		GLOBAL ZONE
	 ================================================================ */
	/** 메타데이터 정보 */
	private Vector metainfo = null;

	/** 데이터셋 정보  	*/
	private Vector data = null;

	/** 로우 포지션		*/
	private int rowpos = -1;

	/** 페이지 로우수 	*/
	private int pagerow = 0;

	/** SQL 정보 		*/
	private String SQL = null; 

	/** COUNT SQL 정보 	*/
	private String CSQL = null; 

	/** 전체 페이지 수 	*/
	private int totpage = 0; 

	/** 전체 데이터 수 	*/
	private int totcnt = 0; 

	/** 현재 데이터 위치 	*/
	private int page = 0; 

	/** 파라미터 설정 	*/
	private ParamType param = null; 

	/**
	 * 생성자
	 */
	public DataSetImpl() {
		//데이터 정보 초기화
		rowpos = -1;
		metainfo = new Vector();
		data = new Vector();
	}

	/**
	 * 생성자
	 */
	public DataSetImpl(Vector metainfo, Vector data) {
		//데이터 정보 초기화
		rowpos = 0;
		metainfo = this.metainfo;
		data = this.data;
	}
	
	/**
	 * 데이터셋의 데이터의 수
	 * @return		int		데이터의 수(-1 이면 에러)
	 */
	public int size() {
		if(data == null) return -1;
		return data.size();		
	}

	/**
	 * 현재 데이터셋의 데이터 셋 위치
	 * @return		int 	데이터셋위치(-1 이면 에러)
	 */
	public int curPos() {
		return rowpos;	
	}

	/**
	 * 데이터셋의 데이터위치를 설정한다.
	 * @param		rowpos	데이터셋의 설정 위치
	 * @return		int 	데이터셋위치(-1 이면 에러)
	 */
	public int setCurPos(int rowpos) {
		if(size()-1 < rowpos) this.rowpos = size()-1;
		else if(0 > rowpos) this.rowpos = -1;
		else this.rowpos = rowpos;

		return this.rowpos;
	}

	/**
	 * 데이터셋의 현재 위치를 이전으로 이동한다.
	 * 가장 첫번째 위치라면 처음을 리턴한다.
	 * @return		boolean 	이동여부	
	 */
	public boolean prev() {
		int tmp_pos = rowpos;
		if(tmp_pos <= 0) return false;
		else {
			--rowpos;	
			return true;
		}
	}

	/**
	 * 데이터셋의 현재 위치를 이전으로 이동한다.
	 * @param		cnt		이동할 데이터 수
	 * @return		boolean 이동여부
	 */
	public boolean prev(int cnt) {
		int tmp_pos = rowpos;
		tmp_pos -= cnt;
		if(tmp_pos <= 0) return false;
		else {
			rowpos = tmp_pos;
			return true;
		}
	}


	/**
	 * 데이터셋의 현재 위치를 이후로 이동한다.
	 * @return		boolean 	마지막 여부	
	 */
	public boolean next() {
		if(isLast()) return false;
		else {
			++rowpos;
			return true;
		}
	}

	/**
	 * 데이터셋의 현재 위치를 이후로 이동한다.
	 * @param		int			이동할 데이터 수
	 * @return		boolean 	마지막 여부
	 */
	public boolean next(int cnt) {
		int tmp_pos = 0;
		tmp_pos = rowpos + cnt;
		if(tmp_pos >= size()) return false;
		else {
			rowpos = tmp_pos;
			return false;
		}
	}

	/**
	 * 데이터셋의 마지막으로 이동한다.
	 * @return		boolean 	마지막 이동 여부
	 */
	public boolean last() {
		rowpos = size()-1;

		return true;		
	}
	
	/**
	 * 데이터셋이 마지막여부를 확인한다.
	 * @return		boolean 	데이터셋의 마지막 여부
	 */
	public boolean isLast() {
		if(rowpos == size()-1) return true;
		else return false;
	}

	/**
	 * 데이터셋의 처음으로 이동한다.
	 * @return		boolean 	처음 이동 여부
	 */
	public boolean first() {
		rowpos = 0;

		return true;		
	}

	/**
	 * 데이터셋이 처음여부를 확인한다.
	 * @return		boolean 	데이터셋의 처음 여부
	 */
	public boolean isFirst() {
		if(rowpos == 0) return true;
		else return false;
	}

	/**
	 * 데이터셋을 초기화한다(포지션 초기화).
	 * @return		int 	데이터셋의 초기화 위치값(-1 리턴)
	 */
	public int reset() {
		rowpos = -1;

		return rowpos;		
	}

	/**
	 * 호출 SQL문을 리턴한다.  
	 * @return		String		실행 SQL문
	 */
	public String getSQL() { 
		return SQL;
	}

	/**
	 * 호출 SQL문을 설정한다.  
	 * @param		String		실행 SQL문
	 */
	public void setSQL(String SQL) { 
		this.SQL = SQL;
	}

	/**
	 * 호출 COUNT SQL문을 리턴한다.  
	 * @return		String		실행 COUNT SQL문
	 */
	public String getCSQL() { 
		return CSQL;
	}

	/**
	 * 호출 COUNT SQL문을 설정한다.  
	 * @param		String		실행 COUNT SQL문
	 */
	public void setCSQL(String CSQL) { 
		this.CSQL = CSQL;
	}

	/**
	 * 데이터셋에 대한 메타정보를 리턴한다.
	 * @return		Vector		데이터셋에 대한 메타정보			
	 */
	public Vector getMetaInfo() {
		return metainfo;
	}

	/**
	 * 데이터셋에 대한 메타정보를 설정한다.
	 * @param		metainfo	데이터셋에 대한 메타정보
	 * @return		boolean 	설정성공여부
	 */
	public boolean setMetaInfo(Vector metainfo) {
		this.metainfo = metainfo;
		return true;
	}
		

	/**
	 * 리스트 데디터에 대한 전체 정보를 리턴한다.
	 * @return		Vector		리스트 전체 데이터
	 */
	public Vector getListData() {
		return data;		
	}

	/**
	 * 현재 ROW에 대한 COLUMN의 값
	 * @return		String		데이터값
	 */
	public String getData(String colname) {
		return getData(getColPos(colname));
	}

	/**
	 * 현재 ROW에 대한 COLUMN의 값
	 * @param		colpos		컬럼의 포지션(INDEX START : 0)
	 * @return		String		데이터값
	 */
	public String getData(int colpos) {

		if(size()-1 < rowpos || rowpos < 0) return "";				
		if(getColCnt() <= colpos) return "";				

		Vector rowData = (Vector)data.elementAt(rowpos);		
		
		return (String)rowData.elementAt(colpos);
	}

	/**
	 * 특정 ROW에 대한 COLUMN의 값
	 * @param		colname		컬럼명
	 * @param		rowpos		로우의 위치(INDEX STAT : 0)
	 * @return		String		데이터값
	 */
	public String getData(String colname, int rpos) {

		if(size()-1 < rpos || rpos < 0) return "";				

		int colpos = getColPos(colname);
		if(getColCnt() <= colpos) return "";				

		Vector rowData = (Vector)data.elementAt(rpos);		
		
		return (String)rowData.elementAt(colpos);
	}

    /**
	 * 해쉬테이블의 데이터를 읽는다.
	 * <br>테이블의 필드명에 해당하는 데이터를 리턴한다.
	 * (키값 대소문자를 구분하지 않는다.)
	 * @param   dataname    데이터컬럼의 명
  	 * 			dataHash    컬럼명의 해쉬 테이블
	 * @return  String      데이터
     */
     public String getData(String dataname, Hashtable dataHash) {
		  String datanamevar = dataname.toUpperCase();
		  String retStr = "";
		  String key = null;
		  Enumeration e = dataHash.keys();
		  while(e.hasMoreElements()) {
				key = (String)e.nextElement();	
				if(datanamevar.equals(key.toUpperCase())) {
					retStr = (String)dataHash.get(key);
					break;
				}
		  }
		  if(!retStr.equals("")) {
			  retStr = retStr.trim();
		  } 
		  return retStr;
	}

	/**
	 * 한페이지의 로우수를 리턴한다.
	 * @return		int			페이지당 로우수
	 */
	public int getPagerow() {
		return pagerow;	
	}

	/**
	 * 한페이지의 로우수를 설정한다.
	 * @param		pagerow			페이지당 로우수
	 * @return		boolean			설정성공여부
	 */
	public boolean setPagerow(int pagerow) {
		this.pagerow = pagerow;
		return true;
	}

	/**
	 * 전체 데이터 수를 페이지로 계산했을때 전체 페이지 수를 리턴한다.
	 * @return		int			전체 페이지 수
	 */
	public int getTotPage() {
		return totpage;	
	}

	/**
	 * 전체 데이터 수를 페이지로 계산했을때 전체 페이지 수를 설정한다.
	 * @param		totpage			전체 페이지 수
	 * @return		boolean			설정성공여부
	 */
	public boolean setTotPage(int totpage) {
		this.totpage= totpage;
		return true;
	}

	/**
	 * 전체 데이터 수를 리턴한다.
	 * @return		int			전체 데이터 수
	 */
	public int getTotCnt() {
		return totcnt;
	}

	/**
	 * 전체 데이터 수를 설정한다.
	 * @param		totcnt			전체 데이터 수
	 * @return		boolean			설정성공여부
	 */
	public boolean setTotCnt(int totcnt) {
		this.totcnt = totcnt;
		return true;
	}

	/**
	 * 현재 페이지 수를 리턴한다.
	 * @return		int			현재 데이터 수
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 현재 데이터 수를 설정한다.
	 * @param		page			현재 데이터 수
	 * @return		boolean			설정성공여부
	 */
	public boolean setPage(int page) {
		this.page = page;
		return true;
	}

	/**
	 * 컬럼수를 리턴한다.
	 * @return		int			컬럼의 수
	 */
	public int getColCnt() {
		return metainfo.size();
	}

	/**
	 * 컴럼포지션에 대한 컬럼명을 리턴한다.
	 * @param		colpos			컬럼의 포지션		
	 * @return		String			컬럼명
	 */
	public String getColNM(int colpos) {
		String[] colinfo = (String[])metainfo.elementAt(colpos);
	
		if(colinfo == null) return "";

		return colinfo[Code.DB_META_COL_NM];
	}

	/**
	 * 컴럼명에 대한 컬럼포지션을 리턴한다.
	 * @param		colnm			컬럼명
	 * @return		int				컬럼포지션
	 */
	public int getColPos(String colnm) {
		int colsize = getColCnt();
		String[] colinfo = null;
		int colpos = 0;
		int pos = 0;	
		for(colpos = 0; colpos < colsize; colpos++) {		
			colinfo = (String[])metainfo.elementAt(colpos);		
			if(colnm.toUpperCase().equals(colinfo[Code.DB_META_COL_NM].toUpperCase())) break;
			pos++;
		}
		return pos;
	}

	/**
	 * 컴럼포지션에 대한 컬럼유형을 리턴한다.
	 * @param		colpos			컬럼이 포지션
	 * @return		String			컬럼유형
	 */
	public String getColType(int colpos) {
		String[] colinfo = (String[])metainfo.elementAt(colpos);
	
		if(colinfo == null) return "";

		return colinfo[Code.DB_META_COL_TYPE];
	}

	/**
	 * 컴럼포지션에 대한 컬럼유형을 리턴한다(INT형).
	 * @param		colpos			컬럼의 포지션
	 * @return		int				컬럼명
	 */
	public String getColRawType(int colpos) {
		String[] colinfo = (String[])metainfo.elementAt(colpos);
	
		if(colinfo == null) return "";

		return colinfo[Code.DB_META_COL_RAW_TYPE];
	}

	/**
	 * 컴럼포지션에 대한 컬럼길이을 리턴한다.
	 * @param		colpos			컬럼의 포지션
	 * @return		String			컬럼길이
	 */
	public String getColSize(int colpos) {
		String[] colinfo = (String[])metainfo.elementAt(colpos);
	
		if(colinfo == null) return "";

		return colinfo[Code.DB_META_COL_SIZE];
	}

	/**
	 * 컴럼포지션에 대한 테이블명을 리턴한다.
	 * @param		colpos			컬럼의 포지션
	 * @return		String			테이블명
	 */
	public String getColTname(int colpos) {
		String[] colinfo = (String[])metainfo.elementAt(colpos);
	
		if(colinfo == null) return "";

		return colinfo[Code.DB_META_TNAME];
	}

	/**
	 * 컴럼을 추가한다.
	 * 맨마지막으로 저장한다.
	 * @param		colinfo			컬럼정보
	 * @return		boolean			추가 여부
	 */
	public boolean addColumn(String[] colinfo) {
		metainfo.addElement(colinfo);
		return true;
	}

	/**
	 * 컴럼을 추가한다.
	 * @param		colinfo			컬럼정보
	 *				pos				추가할 포지션
	 * @return		boolean			추가 여부
	 */
	public boolean addColumn(int pos, String[] colinfo) {
		metainfo.add(pos,colinfo);			
		return true;
	}

	/**
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * [필드명,데이터] 형식
	 * @return		Hashtable		현재 로우의 정보
	 */
	public Hashtable getRow() { 
		return getRow(rowpos); 
	} 

	/** 
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * [필드명,데이터] 형식
	 * @param		rowpos			데이터셋의 위치
	 * @return		Vector			현재 로우의 정보
	 */
	public Hashtable getRow(int rowpos) {

		if(size()-1 < rowpos || rowpos < 0) return new Hashtable();				

		Vector rowData = (Vector)data.elementAt(rowpos);	
				
		Hashtable rowHash = new Hashtable();
		int colcnt = getColCnt();
		String[] colinfo = null;
		for(int i = 0; i < colcnt; i++) {	
			colinfo = (String[])metainfo.elementAt(i);
			try {
				rowHash.put(colinfo[0],(String)rowData.elementAt(i));	
			} catch (Exception e) {}
		}
		
		return rowHash;
	}
				

	/**
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * 데이터셋의 원본 데이터
	 * @return		Vector			원본로우 데이터
	 */
	public Vector getRawRow() {
		return getRawRow(rowpos);
	}

	/**
	 * 현재 위치의 로우 데이터를 얻어온다.
	 * 데이터셋의 원본 데이터
	 * @param		rowpos			데이터셋의 위치
	 * @return		Vector			원본로우 데이터
	 */
	public Vector getRawRow(int rowpos) {
		if(size()-1 < rowpos || rowpos < 0) return new Vector();				
		
		Vector rowVec = (Vector)data.elementAt(rowpos);

		return rowVec;
		
	}

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(Vector rowdata) {
		boolean retval = true;
		try {
			data.addElement(rowdata);	
		} catch (Exception e) {
			retval = false;	
		}

		return retval;
	}

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(int rowpos, Vector rowdata) {
		boolean retval = true;
		try {
			data.add(rowpos, rowdata);
		} catch (Exception e) {
			retval = false;
		}
		return retval;
	}

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(Hashtable rowdata) {
		return addRow(convRow(rowdata));
	}

	/**
	 * 로우 데이터를 추가한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean addRow(int rowpos, Hashtable rowdata) {
		return addRow(rowpos,convRow(rowdata));

	}

	/**
	 * 로우 데이터를 설정한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean setRow(int rowpos, Vector rowdata) {
		boolean retval = true;
		try {
			data.set(rowpos, rowdata);
		} catch (Exception e) {
			retval = false;
		}
		return retval;

	}

	/**
	 * 로우 데이터를 설정한다.
	 *
	 * @param	    rowdata			로우 데이터
	 *				rowpos			데이터셋의 위치	
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean setRow(int rowpos, Hashtable rowdata) {
		return setRow(rowpos,convRow(rowdata));

	}

	/**
	 * 해당 로우의 해당 컬럼에 데이터를 설정한다.
	 *
	 * @param	    data			설정 데이터
	 * @param	    rowpos 			로우 위치 
	 * @param		columnpos		컬럼 위치
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean setData(String data, int rowpos, int columnpos) {
		getRawRow(rowpos).set(columnpos,data);	
		return true;
	}

	/**
	 * 해당 로우의 해당 컬럼에 데이터를 설정한다.
	 *
	 * @param	    data			설정 데이터
	 * @param	    rowpos 			로우 취치 
	 * @param		column			컬럼명 
	 * @return		boolean			로우 데이터 추가 여부 
	 */
	public boolean setData(String data, int rowpos, String column) {
		getRawRow(rowpos).set(getColPos(column),data);
		return true;
	}

	/**
	 * 해쉬 데이터를 벡터로 변환한다.
	 *
	 * @param	    rowdata			해쉬 데이터
	 * @return		Vector			벡터 데이터
	 */
	public Vector convRow(Hashtable rowdata) {
		Vector rowVec = new Vector();				

		boolean retval = true;

		int colcnt = getColCnt();		
		String[] colinfo = null;
		String colnm = null;
		for(int i = 0; i < colcnt; i++) {
			try {	
				colinfo = (String[])metainfo.elementAt(i);
				colnm = colinfo[0];
				rowVec.addElement(getData(colnm,rowdata));
			} catch (Exception e) {
				retval = false;
				break;
			}
		}

		return rowVec;
	}

	/**
	 * 파라미터 정보를 프린트(STD OUT) 한다.
	 *
	 * @param	    print_yn		STD OUT 여부
	 * @param	    mname			호출 위치
	 * @return		String			프린트 스트링	
	 */
	public String paramPrint(boolean print_yn, String mname) { 

		if(print_yn && param != null) {
			System.out.println("");
			System.out.println("[호출 위치]" + mname);
			System.out.println("[*********************** 파라미터정보(START) ***********************]");
			for(int i = 1; i <= param.size(); i++) {
				System.out.print("[순번:" + i + "]");
				System.out.print("[파라미터유형:" + param.getType(i) + "]");
				System.out.print("[파라미터값:" + param.getValue(i) + "]");
				System.out.println("");
			}
			System.out.println("[*********************** 파라미터정보(END) ***********************]");
			System.out.flush();
		}

		return "";

	}

	/**
	 * 헤더 메타 정보를 프린트(STD OUT)한다.
	 *
	 * @param	    print_yn		STD OUT 여부
	 * @param	    mname			호출 위치
	 * @return		String			프린트 스트링	
	 */
	public String metaPrint(boolean print_yn, String mname) {

		if(print_yn) {
			System.out.println("");
			System.out.println("[호출 위치]" + mname);
			System.out.println("[*********************** 컬럼정보(START) ***********************]");
			for(int i = 0; i < getColCnt(); i++) {
				System.out.print("[컬럼명:" + getColNM(i) + "]");
				System.out.print("[컬럼유형:" + getColType(i) + "]");
				System.out.print("[컬럼유형(INT):" + getColRawType(i) + "]");
				System.out.print("[컬럼사이즈:" + getColSize(i) + "]");
				System.out.print("[컬럼테이블명:" + getColTname(i) + "]");
				System.out.println("");
			}
			System.out.println("[*********************** 컬럼정보(END) ***********************]");
			System.out.flush();
		}

		return "";
	}

	/**
	 * 로우 데이터 정보를 프린트(STD OUT) 한다.
	 *
	 * @param	    print_yn		STD OUT 여부
	 * @param	    mname			호출 위치
	 * @return		String			프린트 스트링	
	 */
	public String rowPrint(boolean print_yn, String mname) {
		if(print_yn) {
			System.out.println("");
			System.out.println("[호출 위치]" + mname);
			System.out.println("[*********************** 데이터정보(START) ***********************]");
			System.out.println("[*********************** 컬  럼  명 ***********************]");
			for(int j = 0; j < getColCnt(); j++) {
				System.out.print("[" + getColNM(j) + "]");
			}
			System.out.println("");
			int tmp_curpos = curPos();	//현재 데이터셋 위치 저장
			reset();	//데이터셋 위치 초기화
			System.out.println("[*********************** 데  이  터 ***********************]");
			while(next()) {
				for(int i = 0; i < getColCnt(); i++) {
					System.out.print("[" + getData(i) + "]");	
				}
				System.out.println("");
			}
			System.out.println("");
			System.out.println("[*********************** 데이터정보(END) ***********************]");
			System.out.flush();
			setCurPos(tmp_curpos);	//현재 위치를 설정한다.				
		}

		return "";
		
	}

	/**
	 * 전체 데이터셋 정보를 프린트(STD OUT) 한다.
	 *
	 * @param	    print_yn		STD OUT 여부
	 * @param	    mname			호출 위치
	 * @return		String			프린트 스트링	
	 */
	public String print(boolean print_yn, String mname) { 

		if(print_yn) {
			System.out.println("[*********************** 데이터셋정보(START) ***********************]");
			System.out.println("[호출 위치]" + mname);
			//데이터셋 정보 출력
			System.out.println("[SQL]:[" + getSQL() + "]");
			System.out.println("[CSQL]:[" + getCSQL() + "]");
			System.out.println("[현재커서위치]:[" + curPos() + "]");
			System.out.println("[페이지로우수]:[" + getPagerow() + "]");
			System.out.println("[전체페이지수]:[" + getTotPage() + "]");
			System.out.println("[전체데이터수]:[" + getTotCnt() + "]");
			System.out.println("[현재페이지위치]:[" + getPage() + "]");
			System.out.flush();
			//파라미터정보 출력
			paramPrint(true,mname);	
			//메타정보 출력
			metaPrint(true,mname);	
			//데이터출력
			rowPrint(true,mname);	
			System.out.println("");
			System.out.println("[*********************** 데이터셋정보(END) ***********************]");
			System.out.flush();
		}

		return "";
	}


	/**
	 * ParamType을 설정한다.
	 *
	 * @param	    param			ParamType
	 */
	public void setParam(ParamType param) { 
		this.param = param;
	}

	/**
	 * ParamType을 리턴한다.
	 *
	 * @return	    ParamType		ParamType	
	 */
	public ParamType getParam() { 
		return  param;
	}

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 첫번째 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       dataStr            	찾을 데이터
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
	 *									(INDEX START : 0)
     */
    public int findData(String colnm, String dataStr) {
		return findData(colnm, dataStr, false);
	}

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 첫번째 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       dataStr            	찾을 데이터
     * @param       flag				대소문자 구분 여부
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
	 *									(INDEX START : 0)
     */
    public int findData(String colnm, String dataStr, boolean flag) {
		//컬럼명에 대한 컬럼 포지션을 읽어온다.		
		int colpos = getColPos(colnm);
		if(getColCnt() <= colpos) return -1;				
		
		//해당 데이터를 LOOP 돌린다(앞에서 부터 돌림).
		int data_size = size();
		int i = 0;
		Vector rowData = null;
		for(i = 0; i < data_size; i++) {	
			rowData = (Vector)data.elementAt(i);		
			if(flag) {
				if((dataStr.toUpperCase()).equals(((String)rowData.elementAt(colpos)).toUpperCase())) break;
			} else {
				if(dataStr.equals((String)rowData.elementAt(colpos))) break;
			}
		}
		if(i >= data_size) return -1;
		else return i;
	}

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 마지막 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       dataStr            	찾을 데이터
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
	 *									(INDEX START : 0)
     */
    public int findLastData(String colnm, String dataStr) {
		return findLastData(colnm, dataStr, false);
	}

    /**
     * 해당 컬럼의 맷칭 데이터를 찾아 포지션을 리턴한다.
     * 마지막 멧칭 데이터의 포지션을 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       dataStr             찾을 데이터
     * @param       flag				대소문자 구분 여부
     * @return      int                 포지션(데이터 존재하지 않으면 -1)
	 *									(INDEX START : 0)
     */
    public int findLastData(String colnm, String dataStr, boolean flag) {
		//컬럼명에 대한 컬럼 포지션을 읽어온다.		
		int colpos = getColPos(colnm);
		if(getColCnt() <= colpos) return -1;				

		//해당 데이터를 LOOP 돌린다(뒤에서 부터 돌림).
		int data_size = size();
		int i = 0;
		Vector rowData = null;
		for(data_size-=1; data_size >= 0; data_size--) {	
			rowData = (Vector)data.elementAt(data_size);		
			if(flag) {
				if((dataStr.toUpperCase()).equals(((String)rowData.elementAt(colpos)).toUpperCase())) break;
			} else {
				if(dataStr.equals((String)rowData.elementAt(colpos))) break;
			}
		}
		return data_size;
	}

    /**
     * 특정 컬럼의 매핑 데이터 전체를 리턴한다.
	 * [AAA] 컬럼의 [aaa] 값을 갖는 모든 로우를 리턴한다.
     *
     * @param       colnm               컬럼명
     * @param       dataStr             매핑 데이터
     * @return      Vector    			데이터 리스트          
	 *
     */
    public Vector getMatchDataList(String colnm, String dataStr) {
		//컬럼의 포지션을 알아낸다.
		int colpos = getColPos(colnm);		
        if(getColCnt() <= colpos) return new Vector();

		Vector retvec = new Vector();
		Vector rowData = null;
		for(int i = 0; i < data.size(); i++) {		
			rowData = (Vector)data.elementAt(i);	
			if(((String)rowData.elementAt(colpos)).equals(dataStr)) {
				retvec.addElement(getRow(i));
			}
		}		
		return retvec;
	}

}
