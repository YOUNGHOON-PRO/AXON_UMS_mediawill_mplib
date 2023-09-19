/**
 * 작성자 : 김상진
 * 작성일시 : 2021.07.06
 * 설명 : 문자열을 암호화 처리
 */
package com.mp.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.apache.poi.util.SystemOutLogger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

//import messager.center.creator.FetchException;

public class EncryptUtil {
	//public  Logger logger = Logger.getLogger(EncryptUtil.class);
	
	/**
	 * 문자열을 SHA256으로 암호화(해싱)한다.
	 * @param str
	 * @return
	 */
	public  String getEncryptedSHA256(String str) {
		String result = "";
		if(str == null) {
			return "";
		} else {
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				digest.reset();
				digest.update(str.getBytes());
				byte[] hash = digest.digest();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < hash.length; i++) {
					sb.append(Integer.toString((hash[i]&0xff) + 0x100, 16).substring(1));
				}
				result = sb.toString();
			} catch (NoSuchAlgorithmException nsae) {
				result = str;
			}
			return result;
		}
	}
	
	/**
	 * 문자열을 Jasypt library로 암호화한다.
	 * @param algorithm
	 * @param password
	 * @param str
	 * @return 
	 * @return
	 */
	public   String getJasyptEncryptedString(String algorithm, String password, String str) {
		try {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setAlgorithm(algorithm);
			encryptor.setPassword(password);
			return encryptor.encrypt(str);
		} catch(Exception e) {
			//logger.error("getJasyptEncryptedString error = " + e);
			return str;
		}
	}

	/**
	 * 문자열을 Jasypt library로 복호화한다.
	 * @param algorithm
	 * @param password
	 * @param str
	 * @return
	 * @throws FetchException 
	 */
	public   String getJasyptDecryptedString(String algorithm, String password, String str)  {
		try {
			
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setAlgorithm(algorithm);
			encryptor.setPassword(password);
			return encryptor.decrypt(str);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("e"+e);
			//logger.error("getJasyptDecryptedString error = " + e);
			return str;
		}
		
	}
	
	/**
	 * 문자열을 Base64로 인코딩한다.
	 * @param str
	 * @return
	 */
	public  String getBase64EncodedString(String str) {
		try {
			Encoder encoder = Base64.getEncoder();
			return new String(encoder.encode(str.getBytes()));
		} catch(Exception e) {
			//logger.error("getBase64EncodedString Error = " + e.getMessage());
			return str;
		}
	}
	
	/**
	 * 문자열을 Base64로 디코딩한다.
	 * @param str
	 * @return
	 */
	public  String getBase64DecodedString(String str) {
		try {
			Decoder decoder = Base64.getDecoder();
			return new String(decoder.decode(str.getBytes()));
		} catch(Exception e) {
			//logger.error("getBase64DecodedString Error = " + e.getMessage());
			return str;			
		}
	}
	
	
	public static void main(String args[]) throws Exception  {
		
	
//		bank.jdbc.driver.name=oracle.jdbc.driver.OracleDriver
//		bank.jdbc.url=jdbc:oracle:thin:@127.0.0.1:1521:xe		Qj2JBvf/s0e065aiToYXTwwMQKmirZQEpJBm2PSXe6F1qXmUixcA0ol+r0PWjsTK
//		bank.db.user=ums
//		bank.db.password=amway11!
//		bank.db.table=TEMP_BANK
//		bank.db.mkcolumn=RESP_YN
//		bank.db.mktid=ID
//
//
//		#카드정보
//		card.jdbc.driver.name=oracle.jdbc.driver.OracleDriver
//		card.jdbc.url=jdbc:oracle:thin:@127.0.0.1:1521:xe
//		card.db.user=ums
//		card.db.password=amway11!
//		card.db.table=TEMP_CARD
//		card.db.mkcolumn=RESP_YN
//		card.db.mktid=ID
		
		String ALGORITHM = "PBEWithMD5AndDES";
		String KEYSTRING = "ENDERSUMS";
		//String KEYSTRING = "NOT_RNNO";
		
		String msg1 ="jdbc:oracle:thin:@127.0.0.1:1521:xe";
		String msg2 ="ums";
		String msg3 ="enders1!";
		
		
		EncryptUtil enc =  new EncryptUtil();
		String enc_data1 = enc.getJasyptEncryptedString(ALGORITHM, KEYSTRING, msg1);
//		String enc_data2 = enc.getJasyptEncryptedString(ALGORITHM, KEYSTRING, msg2);
//		String enc_data3 = enc.getJasyptEncryptedString(ALGORITHM, KEYSTRING, msg3);
//		
//		String enc_data4 = enc.getJasyptEncryptedString(ALGORITHM, KEYSTRING, "hun1110@enders.co.kr");
//		String enc_data5 = enc.getJasyptEncryptedString(ALGORITHM, KEYSTRING, "hun1010616@naver.com");
//		String enc_data6 = enc.getJasyptEncryptedString(ALGORITHM, KEYSTRING, "hun1110@hanmail.net");
		String enc_data7 = enc.getJasyptEncryptedString(ALGORITHM, KEYSTRING, "amway11!");
//		
//		
		System.out.println("enc_data1 : " +enc_data1);
//		System.out.println("enc_data2 : " +enc_data2);
//		System.out.println("enc_data3 : " +enc_data3);
//		System.out.println("enc_data4 : " +enc_data4);
//		System.out.println("enc_data5 : " +enc_data5);
//		System.out.println("enc_data6 : " +enc_data6);
		System.out.println("enc_data7 : " +enc_data7);
//		System.out.println("");
//		
//		
//		String dec_data1 = enc.getJasyptDecryptedString(ALGORITHM, KEYSTRING, enc_data1);
//		String dec_data2 = enc.getJasyptDecryptedString(ALGORITHM, KEYSTRING, enc_data2);
		String dec_data3 = enc.getJasyptDecryptedString(ALGORITHM, KEYSTRING, "3amS4jZkdpZkCZUHakUpSI9ucKyDSHI4");
//		System.out.println("enc_data1 : " +dec_data1);
//		System.out.println("enc_data2 : " +dec_data2);
		System.out.println("enc_data3 : " +dec_data3);
		

				
	}

}
