package com.mp.util; 

import java.util.*;
import java.lang.*;

/**
 * <pre>
 * ���α׷����� : DataSet(java)
 * ���α׷���   : DataSet.java
 * Version      : 1.0
 * �ۼ���       : 2003/12/07
 * �ۼ���       : ������
 * ������       : 2004/01/01
 * ������       : ������
 * ��������		: ParamType ���� �߰�
 * ������       : 2004/06/22
 * ������       : ������
 * ��������		: ������ ��ȸ �޼ҵ� �߰� [find �޼ҵ�]
 *
 * ����         : ó�� �����ͼ��� �������̽��� �����Ѵ�.
 *
 * ������Ʈ��   : ǥ��������Ʈ
 * Copyright	: (��)���ڽý���
 * </pre>
 */
public interface DataSet {
	
	/**
	 * �����ͼ��� �������� ��
	 * @return		int		�������� ��(-1 �̸� ����)
	 */
	public int size(); 

	/**
	 * ���� �����ͼ��� �����ͼ� ��ġ
	 * @return		int		�����ͼ��� ��ġ(-1 �̸� ����)
	 */
	public int curPos(); 

	/**
	 * �����ͼ��� ��ġ ����
	 * @param		rowpos	���� ��ġ
	 * @return		int		�����ͼ��� ��ġ(-1 �̸� ����)
	 */
	public int setCurPos(int rowpos); 

	/**
	 * �����ͼ��� ���� ��ġ�� �������� �̵��Ѵ�.
	 * @return		boolean 	�̵�����
	 */
	public boolean prev(); 

	/**
	 * �����ͼ��� ���� ��ġ�� �������� �̵��Ѵ�.
	 * @param		cnt			�̵��� ������ ��
	 * @return		boolean  	�̵�����	
	 */
	public boolean prev(int cnt); 

	/**
	 * �����ͼ��� ���� ��ġ�� ���ķ� �̵��Ѵ�.
	 * @return		boolean 	�̵� ����
	 */
	public boolean next(); 

	/**
	 * �����ͼ��� ���� ��ġ�� ���ķ� �̵��Ѵ�.
	 * @param		cnt			�̵��� ������ ��
	 * @return		boolean 	�̵�����	
	 */
	public boolean next(int cnt); 

	/**
	 * �����ͼ��� ���������� �̵�
	 * @return		boolean 	�̵�����
	 */
	public boolean last(); 
	
	/**
	 * �����ͼ��� ���������θ� Ȯ���Ѵ�.
	 * @return		boolean 	�����ͼ��� ������ ����
	 */
	public boolean isLast(); 

	/**
	 * �����ͼ��� ó������ �̵�
	 * @return		boolean 	�̵�����	
	 */
	public boolean first(); 

	/**
	 * �����ͼ��� ó�������� Ȯ���Ѵ�.
	 * @return		boolean 	�����ͼ��� ó�� ����
	 */
	public boolean isFirst(); 

	/**
	 * �����ͼ��� �ʱ�ȭ�Ѵ�(������ �ʱ�ȭ).
	 * @return		int 	�ʱ�ȭ Ŀ�� ��ġ
	 */
	public int reset(); 

	/**
	 * ȣ�� SQL���� �����Ѵ�.  
	 * @return		String		���� SQL��
	 */
	public String getSQL(); 

	/**
	 * ȣ�� SQL���� �����Ѵ�.  
	 * @param		String		���� SQL��
	 */
	public void setSQL(String SQL); 

	/**
	 * ȣ�� COUNT SQL���� �����Ѵ�.  
	 * @return		String		���� COUNT SQL��
	 */
	public String getCSQL(); 

	/**
	 * ȣ�� COUNT SQL���� �����Ѵ�.  
	 * @return		String		���� COUNT SQL��
	 */
	public void setCSQL(String CSQL); 

	/**
	 * �����ͼ¿� ���� ��Ÿ������ �����Ѵ�.
	 * @return		Vector		�����ͼ¿� ���� ��Ÿ����			
	 */
	public Vector getMetaInfo(); 

	/**
	 * �����ͼ¿� ���� ��Ÿ������ �����Ѵ�.
	 * @param		metainfo	�����ͼ¿� ���� ��Ÿ����
	 * @return		boolean 	������������
	 */
	public boolean setMetaInfo(Vector metainfo); 

	/**
	 * ����Ʈ �����Ϳ� ���� ��ü ������ �����Ѵ�.
	 * @return		Vector		����Ʈ ��ü ������
	 */
	public Vector getListData();

	/**
	 * ���� ROW�� ���� COLUMN�� ��
	 * @param		colname		�÷���
	 * @return		String		�����Ͱ�
	 */
	public String getData(String colname);

	/**
	 * ���� ROW�� ���� COLUMN�� ��
	 * @param		colname		�÷���
	 *				dataHash	�����ͼ�
	 * @return		String		�����Ͱ�
	 */
	public String getData(String colname, Hashtable dataHash);

	/**
	 * ���� ROW�� ���� COLUMN�� ��
	 * @param		colpos			�÷��� ������
	 * @return		String			�����Ͱ�
	 */
	public String getData(int colpos);

	/**
	 * Ư�� ROW�� ���� COLUMN�� ��
	 * @param		colnm			�÷���
	 * @param		rowpos			�ο��� ��ġ
	 * @return		String			�����Ͱ�
	 */
	public String getData(String colnm, int rowpos);

	/**
	 * �������� �ο��� ���� �����Ѵ�.
	 * @return		int			�������� �ο� ��
	 */
	public int getPagerow();

	/**
	 * �������� �ο��� ���� �����Ѵ�.
	 * @param		pagerow			�������� �ο� ��
	 * @return		boolean			���� ���� ����
	 */
	public boolean setPagerow(int pagerow);

	/**
	 * ��ü ������ ���� �������� ��������� ��ü ������ ���� �����Ѵ�.
	 * @return		int			��ü ������ ��
	 */
	public int getTotPage();

	/**
	 * ��ü ������ ���� �������� ��������� ��ü ������ ���� �����Ѵ�.
	 * @param		pagerow			��ü ������ ��
	 * @return		boolean			���� ���� ����
	 */
	public boolean setTotPage(int totpage);

	/**
	 * ��ü ������ ���� �����Ѵ�.
	 * @return		int			��ü ������ ��
	 */
	public int getTotCnt();

	/**
	 * ��ü ������ ���� �����Ѵ�.
	 * @param		totcnt			��ü ������ ��
	 * @return		boolean			���� ���� ����
	 */
	public boolean setTotCnt(int totcnt);

	/**
	 * ���� ������ ���� �����Ѵ�.
	 * @return		int			���� ������ ��
	 */
	public int getPage();

	/**
	 * ���� ������ ���� �����Ѵ�.
	 * @param		page			���� ������ ��
	 * @return		boolean			���� ���� ����
	 */
	public boolean setPage(int page);

	/**
	 * �÷����� �����Ѵ�.
	 * @return		int			�÷��� ��
	 */
	public int getColCnt();

	/**
	 * �ķ������ǿ� ���� �÷����� �����Ѵ�.
	 * @param		colpos			�÷��� ������		
	 * @return		String			�÷���
	 */
	public String getColNM(int colpos);

	/**
	 * �ķ��� ���� �÷��������� �����Ѵ�.
	 * @param		colnm			�÷���
	 * @return		int				�÷�������
	 */
	public int getColPos(String colnm);

	/**
	 * �ķ������ǿ� ���� �÷������� �����Ѵ�.
	 * @param		colpos			�÷��� ������		
	 * @return		String			�÷�����
	 */
	public String getColType(int colpos);

	/**
	 * �ķ������ǿ� ���� �÷������� �����Ѵ�(INT��).
	 * @param		colpos			�÷��� ������		
	 * @return		int				�÷������
	 */
	public String getColRawType(int colpos);

	/**
	 * �ķ������ǿ� ���� �÷��� ���̸� �����Ѵ�.
	 * @param		colpos			�÷��� ������		
	 * @return		String			�÷�����
	 */
	public String getColSize(int colpos);

	/**
	 * �ķ������ǿ� ���� �÷��� ���̺���� �����Ѵ�.
	 * @param		colpos			�÷��� ������		
	 * @return		String			�÷��� ���̺��
	 */
	public String getColTname(int colpos);

	/**
	 * �ķ��� �߰��Ѵ�.
	 * �Ǹ��������� �����Ѵ�.
	 * @param		colinfo			�÷�����
	 * @return		boolean			�߰� ����
	 */
	public boolean addColumn(String[] colinfo);

	/**
	 * �ķ��� �߰��Ѵ�.
	 * @param		colinfo			�÷�����
	 *				pos				�߰��� ������
	 * @return		boolean			�߰� ����
	 */
	public boolean addColumn(int pos, String[] colinfo);

	/**
	 * ���� ��ġ�� �ο� �����͸� ���´�.
	 * [�ʵ��,������] ����
	 * @return		Hashtable		���� �ο��� ����
	 */
	public Hashtable getRow();

	/**
	 * ���� ��ġ�� �ο� �����͸� ���´�.
	 * [�ʵ��,������] ����
	 * @param		rowpos			�����ͼ��� ��ġ
	 * @return		Vector			���� �ο��� ����
	 */
	public Hashtable getRow(int rowpos);

	/**
	 * ���� ��ġ�� �ο� �����͸� ���´�.
	 * �����ͼ��� ���� ������
	 * @return		Vector			�����ο� ������
	 */
	public Vector getRawRow();

	/**
	 * ���� ��ġ�� �ο� �����͸� ���´�.
	 * �����ͼ��� ���� ������
	 * @param		rowpos			�����ͼ��� ��ġ
	 * @return		Vector			�����ο� ������
	 */
	public Vector getRawRow(int rowpos);

	/**
	 * �ο� �����͸� �߰��Ѵ�.
	 *
	 * @param	    rowdata			�ο� ������
	 * @return		boolean			�ο� ������ �߰� ���� 
	 */
	public boolean addRow(Vector rowdata);

	/**
	 * �ο� �����͸� �߰��Ѵ�.
	 *
	 * @param	    rowdata			�ο� ������
	 *				rowpos			�����ͼ��� ��ġ	
	 * @return		boolean			�ο� ������ �߰� ���� 
	 */
	public boolean addRow(int rowpos, Vector rowdata);

	/**
	 * �ο� �����͸� �߰��Ѵ�.
	 *
	 * @param	    rowdata			�ο� ������
	 * @return		boolean			�ο� ������ �߰� ���� 
	 */
	public boolean addRow(Hashtable rowdata);

	/**
	 * �ο� �����͸� �߰��Ѵ�.
	 *
	 * @param	    rowdata			�ο� ������
	 *				rowpos			�����ͼ��� ��ġ	
	 * @return		boolean			�ο� ������ �߰� ���� 
	 */
	public boolean addRow(int rowpos, Hashtable rowdata);

	/**
	 * �ο� �����͸� �����Ѵ�.
	 *
	 * @param	    rowdata			�ο� ������
	 *				rowpos			�����ͼ��� ��ġ	
	 * @return		boolean			�ο� ������ �߰� ���� 
	 */
	public boolean setRow(int rowpos, Vector rowdata);

	/**
	 * �ο� �����͸� �����Ѵ�.
	 *
	 * @param	    rowdata			�ο� ������
	 *				rowpos			�����ͼ��� ��ġ	
	 * @return		boolean			�ο� ������ �߰� ���� 
	 */
	public boolean setRow(int rowpos, Hashtable rowdata);

    /**
     * �ش� �ο��� �ش� �÷��� �����͸� �����Ѵ�.
     *
     * @param       data            ���� ������
     * @param       rowpos          �ο� ��ġ
     * @param       columnpos       �÷� ��ġ
     * @return      boolean         �ο� ������ �߰� ����
     */
    public boolean setData(String data, int rowpos, int columnpos); 

    /**
     * �ش� �ο��� �ش� �÷��� �����͸� �����Ѵ�.
     *
     * @param       data            ���� ������
     * @param       rowpos          �ο� ��ġ
     * @param       column          �÷���
     * @return      boolean         �ο� ������ �߰� ����
     */
    public boolean setData(String data, int rowpos, String column); 


	/**
	 * ��� ��Ÿ ������ ����Ʈ(STD OUT)�Ѵ�.
	 *
	 * @param	    print_yn		STD OUT ����
	 * @param	    mname			ȣ�� ��� ��ġ
	 * @return		String			����Ʈ ��Ʈ��	
	 */
	public String metaPrint(boolean print_yn, String mname);

	/**
	 * �ο� ������ ������ ����Ʈ(STD OUT) �Ѵ�.
	 *
	 * @param	    print_yn		STD OUT ����
	 * @param	    mname			ȣ�� ��� ��ġ
	 * @return		String			����Ʈ ��Ʈ��	
	 */
	public String rowPrint(boolean print_yn, String mname);

	/**
	 * ��ü �����ͼ� ������ ����Ʈ(STD OUT) �Ѵ�.
	 *
	 * @param	    print_yn		STD OUT ����
	 * @param	    mname			ȣ�� ��� ��ġ
	 * @return		String			����Ʈ ��Ʈ��	
	 */
	public String print(boolean print_yn, String mname);

    /**
     * �Ķ���� ������ ����Ʈ(STD OUT) �Ѵ�.
     *
     * @param       print_yn        STD OUT ����
     * @param       mname           ȣ�� ��� ��ġ
     * @return      String          ����Ʈ ��Ʈ��
     */
    public String paramPrint(boolean print_yn, String mname);

    /**
     * ParamType�� �����Ѵ�.
     *
     * @param       param           ParamType
     */
    public void setParam(ParamType param);

    /**
     * ParamType�� �����Ѵ�.
     *
     * @return      ParamType           ParamType
     */
    public ParamType getParam();

    /**
     * �ش� �÷��� ��Ī �����͸� ã�� �������� �����Ѵ�.
     * ù��° ��Ī �������� �������� �����Ѵ�.
     *
     * @param       colnm               �÷���
     * @param       data                ã�� ������
     * @return      int                 ������(������ �������� ������ -1)
     */
    public int findData(String colnm, String data); 

    /**
     * �ش� �÷��� ��Ī �����͸� ã�� �������� �����Ѵ�.
     * ù��° ��Ī �������� �������� �����Ѵ�.
     *
     * @param       colnm               �÷���
     * @param       data                ã�� ������
     * @param       flag                ��ҹ��� ���� ����
     * @return      int                 ������(������ �������� ������ -1)
     */
    public int findData(String colnm, String data, boolean flag); 

    /**
     * �ش� �÷��� ��Ī �����͸� ã�� �������� �����Ѵ�.
     * ������ ��Ī �������� �������� �����Ѵ�.
     *
     * @param       colnm               �÷���
     * @param       data                ã�� ������
     * @return      int                 ������(������ �������� ������ -1)
     */
    public int findLastData(String colnm, String data); 

    /**
     * �ش� �÷��� ��Ī �����͸� ã�� �������� �����Ѵ�.
     * ������ ��Ī �������� �������� �����Ѵ�.
     *
     * @param       colnm               �÷���
     * @param       data                ã�� ������
     * @param       flag                ��ҹ��� ���� ����
     * @return      int                 ������(������ �������� ������ -1)
     */
    public int findLastData(String colnm, String data, boolean flag); 

    /**
     * Ư�� �÷��� ���� ������ ��ü�� �����Ѵ�.
     * [AAA] �÷��� [aaa] ���� ���� ��� �ο츦 �����Ѵ�.
     *
     * @param       colnm               �÷���
     * @param       dataStr             ���� ������
     * @return      Vector              ������ ����Ʈ
     *
     */
    public Vector getMatchDataList(String colnm, String dataStr); 

}
