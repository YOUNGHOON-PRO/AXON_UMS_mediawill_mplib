
package com.mp.exception;

import java.util.*;
import java.rmi.RemoteException;


/**
 * <pre>
 * ���α׷����� : Exception(java)
 * ���α׷���   : Err.java
 * Version      : 1.0
 * �ۼ���       : 2002/04/06
 * �ۼ���       : ������
 * ������       : 
 * ������       : 
 * ����         : ������ ó���Ѵ�.
 *				  Err_CD1	==> ��з�_���������ڵ�(���ڰ�)
 *				  Err_CD2 	==> �ߺз�_���������ڵ�(���ڰ�)
 *				  Err_CD3   ==> �Һз�_���������ڵ�(���ڰ�)
 *				  Err_Msg 	==> ����� ���� �޽��� (���ڰ�)
 *				  Err_Raw_Msg 	==> �ý��� ���� �޽��� (���ڰ�)
 * ������Ʈ��   : ǥ�ذ���
 * Copyright	: BUMSUK OH
 * </pre>
 */

public class Err extends RemoteException {

	/** ������ ������ ���� */
	private Hashtable errHash = null;

	/**
	 * �Լ��� : getErrValue()<br>
	 * ��  �� : ���� ���� �����Ѵ�.
	 * @return	Hashtable 	������
	 */	 
	public Hashtable getErrValue() {
		return errHash;
	}

	/**
	 * �Լ��� : getMsg()<br>
	 * ��  �� : ���� �޼����� �����Ѵ�.
	 * @return	String		���� �޼���(����� �޼���)
	 */
	public String getMsg() {
		return (String)errHash.get("Err_Msg");		
	}

	/**
	 * �Լ��� : getCode()<br>
	 * ��  �� : ���� �ڵ带 �����Ѵ�.
	 * @return	String		�����ڵ�(�����ڿ�)
	 */
	public String getCode() {
		String excode = null;
		excode = (String)errHash.get("Err_CD1") + 
			   	 (String)errHash.get("Err_CD2") + 
			   	 (String)errHash.get("Err_CD3");  
	
		return excode;
	}
	
	/**
	 * �Լ��� : getEXStr()<br>
	 * ��  �� : ��ü  ���� �޼����� �����Ѵ�.
	 * @return	String		��ü ���� �޼���(���� �ڵ� ����)
	 */
	public String getEXStr() {
		String exstr = null;
		exstr = "[�����ڵ�] : " + getCode() + "\n" +
				"[�����޼���] : " + getMsg(); 

		String errpos = getPos();
		if(!errpos.equals("")) exstr += "\n[������ġ] : " + errpos;
	
		return exstr;
	}

	/**
	 * �Լ��� : getDBErrorCode()<br>
	 * ��  �� : �����ͺ��̽� ���� �ڵ带 �����Ѵ�.
	 * @return	int		�����ͺ��̽� ���� �ڵ�
	 */
	public String getDBErrorCode() {
		String dbcode = (String)errHash.get("DBErrorCode");
		if(dbcode == null || dbcode.equals("")) {
			dbcode = "";
		}
		return dbcode;
	}

	/**
	 * �Լ��� : getUserMsg()<br>
	 * ��  �� : ����� ���� ���� �޼����� �����Ѵ�.
	 * @return	Hashtable		����� ���� ���� �޼���
	 */
	public Hashtable getUserMsg() {
		if(errHash.containsKey("usermsg")) {
			return (Hashtable)errHash.get("usermsg");
		} else {
			return new Hashtable();
		}
	}

	/**
	 * �Լ��� : getPos()<br>
	 * ��  �� : ������ �߻��� ������ �����Ѵ�.
	 * @return	Hashtable		����� ���� ���� �޼���
	 */
	public String getPos() {
		String errpos = (String)errHash.get("Err_Pos");
		if(errpos == null || errpos.equals("")) {
			errpos = "";
		}
		return errpos;
	}

	
	/**
	 * �Լ��� : Err()<br>
	 * ��  �� : ������ ����
	 */
	public Err() {
		errHash = new Hashtable();
	}
	

	/**
	 * �Լ��� : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_CD1		��з�_���������ڵ�
	 * 			Err_CD2		�ߺз�_���������ڵ�	
	 * 			Err_CD3		�Һз�_���������ڵ� 
	 * 			Err_Msg		�����_�����޼���
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
	}


/* ============================================================================= */
	/**
	 * �Լ��� : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_CD1		��з�_���������ڵ�
	 * 			Err_CD2		�ߺз�_���������ڵ�	
	 * 			Err_CD3		�Һз�_���������ڵ� 
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos		�����߻�����
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("Err_Pos", Err_Pos);
	}

	/**
	 * �Լ��� : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, int DBErrorCode)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_CD1		��з�_���������ڵ�
	 * 			Err_CD2		�ߺз�_���������ڵ�	
	 * 			Err_CD3		�Һз�_���������ڵ� 
	 * 			Err_Msg		�����_�����޼���
	 * 			DBErrorCode	�����ͺ��̽� �����ڵ�
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, int DBErrorCode) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
	}

/* ============================================================================ */
	/**
	 * �Լ��� : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, int DBErrorCode)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_CD1		��з�_���������ڵ�
	 * 			Err_CD2		�ߺз�_���������ڵ�	
	 * 			Err_CD3		�Һз�_���������ڵ� 
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos		�����߻�����
	 * 			DBErrorCode	�����ͺ��̽� �����ڵ�
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, int DBErrorCode) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("Err_Pos", Err_Pos);
		errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
	}

	/**
	 * �Լ��� : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, Hashtable msghash)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_CD1		��з�_���������ڵ�
	 * 			Err_CD2		�ߺз�_���������ڵ�	
	 * 			Err_CD3		�Һз�_���������ڵ� 
	 * 			Err_Msg		�����_�����޼���
	 * 			msghash		����� ���� ���� �޼���
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, Hashtable msghash) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("usermsg",msghash);
	}

/* ============================================================================ */
	/**
	 * �Լ��� : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, Hashtable msghash)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_CD1		��з�_���������ڵ�
	 * 			Err_CD2		�ߺз�_���������ڵ�	
	 * 			Err_CD3		�Һз�_���������ڵ� 
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos		�����߻�����
	 * 			msghash		����� ���� ���� �޼���
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, Hashtable msghash) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("usermsg",msghash);
		errHash.put("Err_Pos",Err_Pos);
	}

	/**
	 * �Լ��� : Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, Hashtable msghash)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_CD1		��з�_���������ڵ�
	 * 			Err_CD2		�ߺз�_���������ڵ�	
	 * 			Err_CD3		�Һз�_���������ڵ� 
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos		�����߻�����
	 * 			DBErrorCode	�����ͺ��̽� �����ڵ�
	 * 			msghash		����� ���� ���� �޼���
	 */
	public Err(String Err_CD1, String Err_CD2, String Err_CD3, String Err_Msg, String Err_Pos, int DBErrorCode, Hashtable msghash) {
		errHash = new Hashtable();
		errHash.put("Err_CD1", Err_CD1);
		errHash.put("Err_CD2", Err_CD2);
		errHash.put("Err_CD3", Err_CD3);
		errHash.put("Err_Msg", Err_Msg);
		errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		errHash.put("usermsg",msghash);
		errHash.put("Err_Pos",Err_Pos);
	}


	/**
	 * �Լ��� : Err(String Err_Code, String Err_Msg)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_Code	�����ڵ�
	 * 			Err_Msg		�����_�����޼���
	 */
	public Err(String Err_Code, String Err_Msg) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
		}
		
	}


/* ======================================================================== */
	/**
	 * �Լ��� : Err(String Err_Code, String Err_Msg, String Err_Pos)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_Code	�����ڵ�
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos		���� �߻� ����
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
		}
		
	}

	/**
	 * �Լ��� : Err(String Err_Code, String Err_Msg, String Err_Pos,Hashtable msghash)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_Code	�����ڵ�
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos		���� �߻� ����
	 * 			msghash 	����� ���� ���� �޼���
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos, Hashtable msghash) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("usermsg",msghash);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("usermsg",msghash);
		}
		
	}


	/**
	 * �Լ��� : Err(String Err_Code, String Err_Msg, int DBErrorCode)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_Code	�����ڵ�
	 * 			Err_Msg		�����_�����޼���
	 * 			DBErrorCode	�����ͺ��̽� �����ڵ�
	 */
	public Err(String Err_Code, String Err_Msg, int DBErrorCode) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		}
		
	}

	/**
	 * �Լ��� : Err(String Err_Code, String Err_Msg, int DBErrorCode, Hashtable msghash)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_Code	�����ڵ�
	 * 			Err_Msg		�����_�����޼���
	 * 			DBErrorCode	�����ͺ��̽� �����ڵ�
	 * 			msghash 	����� ���� ���� �޼���
	 */
	public Err(String Err_Code, String Err_Msg, int DBErrorCode, Hashtable msghash) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		}
		
	}

/* =========================================================================== */
	/**
	 * �Լ��� : Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_Code	�����ڵ�
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos 	�����߻���ġ
	 * 			DBErrorCode	�����ͺ��̽� �����ڵ�
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
		}
		
	}

	/**
	 * �Լ��� : Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode, Hashtable msghash)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	Err_Code	�����ڵ�
	 * 			Err_Msg		�����_�����޼���
	 * 			Err_Pos 	�����߻���ġ
	 * 			DBErrorCode	�����ͺ��̽� �����ڵ�
	 * 			msghash 	����� ���� ���� �޼���
	 */
	public Err(String Err_Code, String Err_Msg, String Err_Pos, int DBErrorCode, Hashtable msghash) {
		errHash = new Hashtable();
		if(Err_Code.length() == 3) {
			errHash.put("Err_CD1",Err_Code.substring(0,1));	
			errHash.put("Err_CD2",Err_Code.substring(1,2));	
			errHash.put("Err_CD3",Err_Code.substring(2,3));	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		} else {
			errHash.put("Err_CD1","0");	
			errHash.put("Err_CD2","0");	
			errHash.put("Err_CD3","0");	
			errHash.put("Err_Msg",Err_Msg);
			errHash.put("Err_Pos",Err_Pos);
			errHash.put("DBErrorCode",Integer.toString(DBErrorCode));
			errHash.put("usermsg",msghash);
		}
		
	}
	
	/**
	 * �Լ��� : Err(Hashtable ierHash)<br>
	 * ��  �� : �������� �����Ѵ�.
	 * @param	ierrHash		������
	 */
	public Err(Hashtable ierrHash) {
		errHash = ierrHash;
	}

	/**
	 * �Լ��� : Throwable�� toString() �������̵�
	 * ��  �� : ������ ���
	 * @return		String 		������
	 */
	public String toString() {
		return getEXStr();
	}	

	/**
	 * �Լ��� : Throwable�� getMessage() �������̵�
	 * ��  �� : ������ ���
	 * @return		String 		������
	 */
	public String getMessage() {
		return getEXStr();
	}		

	/**
	 * �Լ��� : setPos() 
	 * ��  �� : ������ġ�� �����Ѵ�.
	 * @param	errpos			������ġ
	 */
	public void setPos(String errpos) {
		errHash.put("Err_Pos",errpos);	
	}		
	
}
			
