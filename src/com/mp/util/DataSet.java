package com.mp.util; 

import java.util.*;
import java.lang.*;

/**
 * <pre>
 * ÇÁ·Î±×·¥À¯Çü : DataSet(java)
 * ÇÁ·Î±×·¥¸í   : DataSet.java
 * Version      : 1.0
 * ÀÛ¼ºÀÏ       : 2003/12/07
 * ÀÛ¼ºÀÚ       : ¿À¹ü¼®
 * ¼öÁ¤ÀÏ       : 2004/01/01
 * ¼öÁ¤ÀÚ       : ¿À¹ü¼®
 * ¼öÁ¤¼³¸í		: ParamType ÀúÀå Ãß°¡
 * ¼öÁ¤ÀÏ       : 2004/06/22
 * ¼öÁ¤ÀÚ       : ¿À¹ü¼®
 * ¼öÁ¤¼³¸í		: µ¥ÀÌÅÍ Á¶È¸ ¸Ş¼Òµå Ãß°¡ [find ¸Ş¼Òµå]
 *
 * ¼³¸í         : Ã³¸® µ¥ÀÌÅÍ¼ÂÀÇ ÀÎÅÍÆäÀÌ½º¸¦ ±¸ÇöÇÑ´Ù.
 *
 * ÇÁ·ÎÁ§Æ®¸í   : Ç¥ÁØÄÄÆ÷³ÍÆ®
 * Copyright	: (ÁÖ)´ÙÄÚ½Ã½ºÅÛ
 * </pre>
 */
public interface DataSet {
	
	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ µ¥ÀÌÅÍÀÇ ¼ö
	 * @return		int		µ¥ÀÌÅÍÀÇ ¼ö(-1 ÀÌ¸é ¿¡·¯)
	 */
	public int size(); 

	/**
	 * ÇöÀç µ¥ÀÌÅÍ¼ÂÀÇ µ¥ÀÌÅÍ¼Â À§Ä¡
	 * @return		int		µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡(-1 ÀÌ¸é ¿¡·¯)
	 */
	public int curPos(); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡ ¼³Á¤
	 * @param		rowpos	¼³Á¤ À§Ä¡
	 * @return		int		µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡(-1 ÀÌ¸é ¿¡·¯)
	 */
	public int setCurPos(int rowpos); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ ÇöÀç À§Ä¡¸¦ ÀÌÀüÀ¸·Î ÀÌµ¿ÇÑ´Ù.
	 * @return		boolean 	ÀÌµ¿¿©ºÎ
	 */
	public boolean prev(); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ ÇöÀç À§Ä¡¸¦ ÀÌÀüÀ¸·Î ÀÌµ¿ÇÑ´Ù.
	 * @param		cnt			ÀÌµ¿ÇÒ µ¥ÀÌÅÍ ¼ö
	 * @return		boolean  	ÀÌµ¿¿©ºÎ	
	 */
	public boolean prev(int cnt); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ ÇöÀç À§Ä¡¸¦ ÀÌÈÄ·Î ÀÌµ¿ÇÑ´Ù.
	 * @return		boolean 	ÀÌµ¿ ¿©ºÎ
	 */
	public boolean next(); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ ÇöÀç À§Ä¡¸¦ ÀÌÈÄ·Î ÀÌµ¿ÇÑ´Ù.
	 * @param		cnt			ÀÌµ¿ÇÒ µ¥ÀÌÅÍ ¼ö
	 * @return		boolean 	ÀÌµ¿¿©ºÎ	
	 */
	public boolean next(int cnt); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ ¸¶Áö¸·À¸·Î ÀÌµ¿
	 * @return		boolean 	ÀÌµ¿¿©ºÎ
	 */
	public boolean last(); 
	
	/**
	 * µ¥ÀÌÅÍ¼ÂÀÌ ¸¶Áö¸·¿©ºÎ¸¦ È®ÀÎÇÑ´Ù.
	 * @return		boolean 	µ¥ÀÌÅÍ¼ÂÀÇ ¸¶Áö¸· ¿©ºÎ
	 */
	public boolean isLast(); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÇ Ã³À½À¸·Î ÀÌµ¿
	 * @return		boolean 	ÀÌµ¿¿©ºÎ	
	 */
	public boolean first(); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀÌ Ã³À½ÀÎÁö¸¦ È®ÀÎÇÑ´Ù.
	 * @return		boolean 	µ¥ÀÌÅÍ¼ÂÀÇ Ã³À½ ¿©ºÎ
	 */
	public boolean isFirst(); 

	/**
	 * µ¥ÀÌÅÍ¼ÂÀ» ÃÊ±âÈ­ÇÑ´Ù(Æ÷Áö¼Ç ÃÊ±âÈ­).
	 * @return		int 	ÃÊ±âÈ­ Ä¿¼­ À§Ä¡
	 */
	public int reset(); 

	/**
	 * È£Ãâ SQL¹®À» ¸®ÅÏÇÑ´Ù.  
	 * @return		String		½ÇÇà SQL¹®
	 */
	public String getSQL(); 

	/**
	 * È£Ãâ SQL¹®À» ¼³Á¤ÇÑ´Ù.  
	 * @param		String		½ÇÇà SQL¹®
	 */
	public void setSQL(String SQL); 

	/**
	 * È£Ãâ COUNT SQL¹®À» ¸®ÅÏÇÑ´Ù.  
	 * @return		String		½ÇÇà COUNT SQL¹®
	 */
	public String getCSQL(); 

	/**
	 * È£Ãâ COUNT SQL¹®À» ¼³Á¤ÇÑ´Ù.  
	 * @return		String		½ÇÇà COUNT SQL¹®
	 */
	public void setCSQL(String CSQL); 

	/**
	 * µ¥ÀÌÅÍ¼Â¿¡ ´ëÇÑ ¸ŞÅ¸Á¤º¸¸¦ ¸®ÅÏÇÑ´Ù.
	 * @return		Vector		µ¥ÀÌÅÍ¼Â¿¡ ´ëÇÑ ¸ŞÅ¸Á¤º¸			
	 */
	public Vector getMetaInfo(); 

	/**
	 * µ¥ÀÌÅÍ¼Â¿¡ ´ëÇÑ ¸ŞÅ¸Á¤º¸¸¦ ¼³Á¤ÇÑ´Ù.
	 * @param		metainfo	µ¥ÀÌÅÍ¼Â¿¡ ´ëÇÑ ¸ŞÅ¸Á¤º¸
	 * @return		boolean 	¼³Á¤¼º°ø¿©ºÎ
	 */
	public boolean setMetaInfo(Vector metainfo); 

	/**
	 * ¸®½ºÆ® µ¥µğÅÍ¿¡ ´ëÇÑ ÀüÃ¼ Á¤º¸¸¦ ¸®ÅÏÇÑ´Ù.
	 * @return		Vector		¸®½ºÆ® ÀüÃ¼ µ¥ÀÌÅÍ
	 */
	public Vector getListData();

	/**
	 * ÇöÀç ROW¿¡ ´ëÇÑ COLUMNÀÇ °ª
	 * @param		colname		ÄÃ·³¸í
	 * @return		String		µ¥ÀÌÅÍ°ª
	 */
	public String getData(String colname);

	/**
	 * ÇöÀç ROW¿¡ ´ëÇÑ COLUMNÀÇ °ª
	 * @param		colname		ÄÃ·³¸í
	 *				dataHash	µ¥ÀÌÅÍ¼Â
	 * @return		String		µ¥ÀÌÅÍ°ª
	 */
	public String getData(String colname, Hashtable dataHash);

	/**
	 * ÇöÀç ROW¿¡ ´ëÇÑ COLUMNÀÇ °ª
	 * @param		colpos			ÄÃ·³ÀÇ Æ÷Áö¼Ç
	 * @return		String			µ¥ÀÌÅÍ°ª
	 */
	public String getData(int colpos);

	/**
	 * Æ¯Á¤ ROW¿¡ ´ëÇÑ COLUMNÀÇ °ª
	 * @param		colnm			ÄÃ·³¸í
	 * @param		rowpos			·Î¿ìÀÇ À§Ä¡
	 * @return		String			µ¥ÀÌÅÍ°ª
	 */
	public String getData(String colnm, int rowpos);

	/**
	 * ÆäÀÌÁö´ç ·Î¿ìÀÇ ¼ö¸¦ ¸®ÅÏÇÑ´Ù.
	 * @return		int			ÆäÀÌÁö´ç ·Î¿ì ¼ö
	 */
	public int getPagerow();

	/**
	 * ÆäÀÌÁö´ç ·Î¿ìÀÇ ¼ö¸¦ ¼³Á¤ÇÑ´Ù.
	 * @param		pagerow			ÆäÀÌÁö´ç ·Î¿ì ¼ö
	 * @return		boolean			¼³Á¤ ¼³°ø ¿©ºÎ
	 */
	public boolean setPagerow(int pagerow);

	/**
	 * ÀüÃ¼ µ¥ÀÌÅÍ ¼ö¸¦ ÆäÀÌÁö·Î °è»êÇßÀ»¶§ ÀüÃ¼ ÆäÀÌÁö ¼ö¸¦ ¸®ÅÏÇÑ´Ù.
	 * @return		int			ÀüÃ¼ ÆäÀÌÁö ¼ö
	 */
	public int getTotPage();

	/**
	 * ÀüÃ¼ µ¥ÀÌÅÍ ¼ö¸¦ ÆäÀÌÁö·Î °è»êÇßÀ»¶§ ÀüÃ¼ ÆäÀÌÁö ¼ö¸¦ ¼³Á¤ÇÑ´Ù.
	 * @param		pagerow			ÀüÃ¼ ÆäÀÌÁö ¼ö
	 * @return		boolean			¼³Á¤ ¼³°ø ¿©ºÎ
	 */
	public boolean setTotPage(int totpage);

	/**
	 * ÀüÃ¼ µ¥ÀÌÅÍ ¼ö¸¦ ¸®ÅÏÇÑ´Ù.
	 * @return		int			ÀüÃ¼ µ¥ÀÌÅÍ ¼ö
	 */
	public int getTotCnt();

	/**
	 * ÀüÃ¼ µ¥ÀÌÅÍ ¼ö¸¦ ¼³Á¤ÇÑ´Ù.
	 * @param		totcnt			ÀüÃ¼ µ¥ÀÌÅÍ ¼ö
	 * @return		boolean			¼³Á¤ ¼³°ø ¿©ºÎ
	 */
	public boolean setTotCnt(int totcnt);

	/**
	 * ÇöÀç ÆäÀÌÁö ¼ö¸¦ ¸®ÅÏÇÑ´Ù.
	 * @return		int			ÇöÀç µ¥ÀÌÅÍ ¼ö
	 */
	public int getPage();

	/**
	 * ÇöÀç µ¥ÀÌÅÍ ¼ö¸¦ ¼³Á¤ÇÑ´Ù.
	 * @param		page			ÇöÀç µ¥ÀÌÅÍ ¼ö
	 * @return		boolean			¼³Á¤ ¼³°ø ¿©ºÎ
	 */
	public boolean setPage(int page);

	/**
	 * ÄÃ·³¼ö¸¦ ¸®ÅÏÇÑ´Ù.
	 * @return		int			ÄÃ·³ÀÇ ¼ö
	 */
	public int getColCnt();

	/**
	 * ÄÄ·³Æ÷Áö¼Ç¿¡ ´ëÇÑ ÄÃ·³¸íÀ» ¸®ÅÏÇÑ´Ù.
	 * @param		colpos			ÄÃ·³ÀÇ Æ÷Áö¼Ç		
	 * @return		String			ÄÃ·³¸í
	 */
	public String getColNM(int colpos);

	/**
	 * ÄÄ·³¸í¿¡ ´ëÇÑ ÄÃ·³Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
	 * @param		colnm			ÄÃ·³¸í
	 * @return		int				ÄÃ·³Æ÷Áö¼Ç
	 */
	public int getColPos(String colnm);

	/**
	 * ÄÄ·³Æ÷Áö¼Ç¿¡ ´ëÇÑ ÄÃ·³À¯ÇüÀ» ¸®ÅÏÇÑ´Ù.
	 * @param		colpos			ÄÃ·³ÀÇ Æ÷Áö¼Ç		
	 * @return		String			ÄÃ·³À¯Çü
	 */
	public String getColType(int colpos);

	/**
	 * ÄÄ·³Æ÷Áö¼Ç¿¡ ´ëÇÑ ÄÃ·³À¯ÇüÀ» ¸®ÅÏÇÑ´Ù(INTÇü).
	 * @param		colpos			ÄÃ·³ÀÇ Æ÷Áö¼Ç		
	 * @return		int				ÄÃ·³À¯ÇüÌ
	 */
	public String getColRawType(int colpos);

	/**
	 * ÄÄ·³Æ÷Áö¼Ç¿¡ ´ëÇÑ ÄÃ·³ÀÇ ±æÀÌ¸¦ ¸®ÅÏÇÑ´Ù.
	 * @param		colpos			ÄÃ·³ÀÇ Æ÷Áö¼Ç		
	 * @return		String			ÄÃ·³±æÀÌ
	 */
	public String getColSize(int colpos);

	/**
	 * ÄÄ·³Æ÷Áö¼Ç¿¡ ´ëÇÑ ÄÃ·³ÀÇ Å×ÀÌºí¸íÀ» ¸®ÅÏÇÑ´Ù.
	 * @param		colpos			ÄÃ·³ÀÇ Æ÷Áö¼Ç		
	 * @return		String			ÄÃ·³ÀÇ Å×ÀÌºí¸í
	 */
	public String getColTname(int colpos);

	/**
	 * ÄÄ·³À» Ãß°¡ÇÑ´Ù.
	 * ¸Ç¸¶Áö¸·À¸·Î ÀúÀåÇÑ´Ù.
	 * @param		colinfo			ÄÃ·³Á¤º¸
	 * @return		boolean			Ãß°¡ ¿©ºÎ
	 */
	public boolean addColumn(String[] colinfo);

	/**
	 * ÄÄ·³À» Ãß°¡ÇÑ´Ù.
	 * @param		colinfo			ÄÃ·³Á¤º¸
	 *				pos				Ãß°¡ÇÒ Æ÷Áö¼Ç
	 * @return		boolean			Ãß°¡ ¿©ºÎ
	 */
	public boolean addColumn(int pos, String[] colinfo);

	/**
	 * ÇöÀç À§Ä¡ÀÇ ·Î¿ì µ¥ÀÌÅÍ¸¦ ¾ò¾î¿Â´Ù.
	 * [ÇÊµå¸í,µ¥ÀÌÅÍ] Çü½Ä
	 * @return		Hashtable		ÇöÀç ·Î¿ìÀÇ Á¤º¸
	 */
	public Hashtable getRow();

	/**
	 * ÇöÀç À§Ä¡ÀÇ ·Î¿ì µ¥ÀÌÅÍ¸¦ ¾ò¾î¿Â´Ù.
	 * [ÇÊµå¸í,µ¥ÀÌÅÍ] Çü½Ä
	 * @param		rowpos			µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡
	 * @return		Vector			ÇöÀç ·Î¿ìÀÇ Á¤º¸
	 */
	public Hashtable getRow(int rowpos);

	/**
	 * ÇöÀç À§Ä¡ÀÇ ·Î¿ì µ¥ÀÌÅÍ¸¦ ¾ò¾î¿Â´Ù.
	 * µ¥ÀÌÅÍ¼ÂÀÇ ¿øº» µ¥ÀÌÅÍ
	 * @return		Vector			¿øº»·Î¿ì µ¥ÀÌÅÍ
	 */
	public Vector getRawRow();

	/**
	 * ÇöÀç À§Ä¡ÀÇ ·Î¿ì µ¥ÀÌÅÍ¸¦ ¾ò¾î¿Â´Ù.
	 * µ¥ÀÌÅÍ¼ÂÀÇ ¿øº» µ¥ÀÌÅÍ
	 * @param		rowpos			µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡
	 * @return		Vector			¿øº»·Î¿ì µ¥ÀÌÅÍ
	 */
	public Vector getRawRow(int rowpos);

	/**
	 * ·Î¿ì µ¥ÀÌÅÍ¸¦ Ãß°¡ÇÑ´Ù.
	 *
	 * @param	    rowdata			·Î¿ì µ¥ÀÌÅÍ
	 * @return		boolean			·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ 
	 */
	public boolean addRow(Vector rowdata);

	/**
	 * ·Î¿ì µ¥ÀÌÅÍ¸¦ Ãß°¡ÇÑ´Ù.
	 *
	 * @param	    rowdata			·Î¿ì µ¥ÀÌÅÍ
	 *				rowpos			µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡	
	 * @return		boolean			·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ 
	 */
	public boolean addRow(int rowpos, Vector rowdata);

	/**
	 * ·Î¿ì µ¥ÀÌÅÍ¸¦ Ãß°¡ÇÑ´Ù.
	 *
	 * @param	    rowdata			·Î¿ì µ¥ÀÌÅÍ
	 * @return		boolean			·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ 
	 */
	public boolean addRow(Hashtable rowdata);

	/**
	 * ·Î¿ì µ¥ÀÌÅÍ¸¦ Ãß°¡ÇÑ´Ù.
	 *
	 * @param	    rowdata			·Î¿ì µ¥ÀÌÅÍ
	 *				rowpos			µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡	
	 * @return		boolean			·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ 
	 */
	public boolean addRow(int rowpos, Hashtable rowdata);

	/**
	 * ·Î¿ì µ¥ÀÌÅÍ¸¦ ¼³Á¤ÇÑ´Ù.
	 *
	 * @param	    rowdata			·Î¿ì µ¥ÀÌÅÍ
	 *				rowpos			µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡	
	 * @return		boolean			·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ 
	 */
	public boolean setRow(int rowpos, Vector rowdata);

	/**
	 * ·Î¿ì µ¥ÀÌÅÍ¸¦ ¼³Á¤ÇÑ´Ù.
	 *
	 * @param	    rowdata			·Î¿ì µ¥ÀÌÅÍ
	 *				rowpos			µ¥ÀÌÅÍ¼ÂÀÇ À§Ä¡	
	 * @return		boolean			·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ 
	 */
	public boolean setRow(int rowpos, Hashtable rowdata);

    /**
     * ÇØ´ç ·Î¿ìÀÇ ÇØ´ç ÄÃ·³¿¡ µ¥ÀÌÅÍ¸¦ ¼³Á¤ÇÑ´Ù.
     *
     * @param       data            ¼³Á¤ µ¥ÀÌÅÍ
     * @param       rowpos          ·Î¿ì ÃëÄ¡
     * @param       columnpos       ÄÃ·³ À§Ä¡
     * @return      boolean         ·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ
     */
    public boolean setData(String data, int rowpos, int columnpos); 

    /**
     * ÇØ´ç ·Î¿ìÀÇ ÇØ´ç ÄÃ·³¿¡ µ¥ÀÌÅÍ¸¦ ¼³Á¤ÇÑ´Ù.
     *
     * @param       data            ¼³Á¤ µ¥ÀÌÅÍ
     * @param       rowpos          ·Î¿ì ÃëÄ¡
     * @param       column          ÄÃ·³¸í
     * @return      boolean         ·Î¿ì µ¥ÀÌÅÍ Ãß°¡ ¿©ºÎ
     */
    public boolean setData(String data, int rowpos, String column); 


	/**
	 * Çì´õ ¸ŞÅ¸ Á¤º¸¸¦ ÇÁ¸°Æ®(STD OUT)ÇÑ´Ù.
	 *
	 * @param	    print_yn		STD OUT ¿©ºÎ
	 * @param	    mname			È£Ãâ ¸ğµâ À§Ä¡
	 * @return		String			ÇÁ¸°Æ® ½ºÆ®¸µ	
	 */
	public String metaPrint(boolean print_yn, String mname);

	/**
	 * ·Î¿ì µ¥ÀÌÅÍ Á¤º¸¸¦ ÇÁ¸°Æ®(STD OUT) ÇÑ´Ù.
	 *
	 * @param	    print_yn		STD OUT ¿©ºÎ
	 * @param	    mname			È£Ãâ ¸ğµâ À§Ä¡
	 * @return		String			ÇÁ¸°Æ® ½ºÆ®¸µ	
	 */
	public String rowPrint(boolean print_yn, String mname);

	/**
	 * ÀüÃ¼ µ¥ÀÌÅÍ¼Â Á¤º¸¸¦ ÇÁ¸°Æ®(STD OUT) ÇÑ´Ù.
	 *
	 * @param	    print_yn		STD OUT ¿©ºÎ
	 * @param	    mname			È£Ãâ ¸ğµâ À§Ä¡
	 * @return		String			ÇÁ¸°Æ® ½ºÆ®¸µ	
	 */
	public String print(boolean print_yn, String mname);

    /**
     * ÆÄ¶ó¹ÌÅÍ Á¤º¸¸¦ ÇÁ¸°Æ®(STD OUT) ÇÑ´Ù.
     *
     * @param       print_yn        STD OUT ¿©ºÎ
     * @param       mname           È£Ãâ ¸ğµâ À§Ä¡
     * @return      String          ÇÁ¸°Æ® ½ºÆ®¸µ
     */
    public String paramPrint(boolean print_yn, String mname);

    /**
     * ParamTypeÀ» ¼³Á¤ÇÑ´Ù.
     *
     * @param       param           ParamType
     */
    public void setParam(ParamType param);

    /**
     * ParamTypeÀ» ¸®ÅÏÇÑ´Ù.
     *
     * @return      ParamType           ParamType
     */
    public ParamType getParam();

    /**
     * ÇØ´ç ÄÃ·³ÀÇ ¸ËÄª µ¥ÀÌÅÍ¸¦ Ã£¾Æ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     * Ã¹¹øÂ° ¸äÄª µ¥ÀÌÅÍÀÇ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     *
     * @param       colnm               ÄÃ·³¸í
     * @param       data                Ã£À» µ¥ÀÌÅÍ
     * @return      int                 Æ÷Áö¼Ç(µ¥ÀÌÅÍ Á¸ÀçÇÏÁö ¾ÊÀ¸¸é -1)
     */
    public int findData(String colnm, String data); 

    /**
     * ÇØ´ç ÄÃ·³ÀÇ ¸ËÄª µ¥ÀÌÅÍ¸¦ Ã£¾Æ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     * Ã¹¹øÂ° ¸äÄª µ¥ÀÌÅÍÀÇ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     *
     * @param       colnm               ÄÃ·³¸í
     * @param       data                Ã£À» µ¥ÀÌÅÍ
     * @param       flag                ´ë¼Ò¹®ÀÚ ±¸ºĞ ¿©ºÎ
     * @return      int                 Æ÷Áö¼Ç(µ¥ÀÌÅÍ Á¸ÀçÇÏÁö ¾ÊÀ¸¸é -1)
     */
    public int findData(String colnm, String data, boolean flag); 

    /**
     * ÇØ´ç ÄÃ·³ÀÇ ¸ËÄª µ¥ÀÌÅÍ¸¦ Ã£¾Æ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     * ¸¶Áö¸· ¸äÄª µ¥ÀÌÅÍÀÇ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     *
     * @param       colnm               ÄÃ·³¸í
     * @param       data                Ã£À» µ¥ÀÌÅÍ
     * @return      int                 Æ÷Áö¼Ç(µ¥ÀÌÅÍ Á¸ÀçÇÏÁö ¾ÊÀ¸¸é -1)
     */
    public int findLastData(String colnm, String data); 

    /**
     * ÇØ´ç ÄÃ·³ÀÇ ¸ËÄª µ¥ÀÌÅÍ¸¦ Ã£¾Æ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     * ¸¶Áö¸· ¸äÄª µ¥ÀÌÅÍÀÇ Æ÷Áö¼ÇÀ» ¸®ÅÏÇÑ´Ù.
     *
     * @param       colnm               ÄÃ·³¸í
     * @param       data                Ã£À» µ¥ÀÌÅÍ
     * @param       flag                ´ë¼Ò¹®ÀÚ ±¸ºĞ ¿©ºÎ
     * @return      int                 Æ÷Áö¼Ç(µ¥ÀÌÅÍ Á¸ÀçÇÏÁö ¾ÊÀ¸¸é -1)
     */
    public int findLastData(String colnm, String data, boolean flag); 

    /**
     * Æ¯Á¤ ÄÃ·³ÀÇ ¸ÅÇÎ µ¥ÀÌÅÍ ÀüÃ¼¸¦ ¸®ÅÏÇÑ´Ù.
     * [AAA] ÄÃ·³ÀÇ [aaa] °ªÀ» °®´Â ¸ğµç ·Î¿ì¸¦ ¸®ÅÏÇÑ´Ù.
     *
     * @param       colnm               ÄÃ·³¸í
     * @param       dataStr             ¸ÅÇÎ µ¥ÀÌÅÍ
     * @return      Vector              µ¥ÀÌÅÍ ¸®½ºÆ®
     *
     */
    public Vector getMatchDataList(String colnm, String dataStr); 

}
