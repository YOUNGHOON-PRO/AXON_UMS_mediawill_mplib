package com.mp.util;

import java.io.*;
import java.util.*;
import java.net.*;

public class DNSLookup {
	public DNSLook look;
	private String inDomain;
	private String realDomain = "";
	private String DNS_Server_IP = null;	

	public DNSLookup(String inDomain, String DNS_Server_IP) {
		this.inDomain = inDomain;
		this.DNS_Server_IP = DNS_Server_IP;
	}

	public Vector lookupList() {
		int mxPreference = 70000;
		int currentMxPreference = 0;
		String currentDomain = "";
		Vector mxDomainVector = new Vector();
		Hashtable mxLowDomainHash = new Hashtable();

		look = new DNSLook();
		look.setMessage(inDomain);
	 	int c = look.sendMessage(DNS_Server_IP);

		if( c == 0 ) {
	  	c = look.receiveMessage();

	    if( c == 0) {
	    	if( look.getReturnCode() == 3 ) {
	      	return null;
	      }

	      int num = look.getNumberOfAnswers();
	      if( num == 0 ) {  //Not MX rcord
	   			return null;
	      }

				for(int i=0; i < num; i++) {
					String answer = look.getAnswer(i);
					if(answer==null) {
						return null;
					}
					StringBuffer sb = new StringBuffer(answer);
					
					int sbLength = sb.length();

					try {
						if( sbLength-1 > 0 ) {
							if(sb.charAt( sbLength-1 ) == '.' )
								sb.setCharAt( sbLength-1,' ' );

							currentDomain = new String(sb);
							currentMxPreference =  look.getMXPreference(i);

							if( mxPreference > currentMxPreference )
							{
								mxPreference = currentMxPreference;
							}

							mxLowDomainHash.put( currentDomain , new Integer( currentMxPreference ) );
						}
					} catch(java.lang.StringIndexOutOfBoundsException ex) { }
				}

				if ( mxLowDomainHash.size() == 0 ) {
					return null;
				} else {
					for( Enumeration en = mxLowDomainHash.keys() ;en.hasMoreElements();) {
						String addMxDomain = (String)en.nextElement();
						if ( mxPreference == ((Integer)mxLowDomainHash.get(addMxDomain)).intValue() ) {
							mxDomainVector.add(addMxDomain.trim());
						}
					}

					return mxDomainVector;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String lookupAll() {
		int mxPreference = 70000;
		int currentMxPreference = 0;
		String currentDomain = "";

		look = new DNSLook();
		look.setMessage( inDomain );
		int c = look.sendMessage(DNS_Server_IP );

		if( c == 0 ) {
			c = look.receiveMessage();

			if( c == 0 ) {
				if( look.getReturnCode() == 3 ) {
					return "4"; //Notexist
				}

				int num = look.getNumberOfAnswers();
				if( num == 0 ) { //Not MX record
					return this.inDomain;
				}

				for(int i=0; i < num; i++) {
					String answer = look.getAnswer(i);
					
					if(answer==null) {
						return "7";
					}
					StringBuffer sb = new StringBuffer(answer);
					int sbLength = sb.length();

					try {
						if( sbLength-1 > 0 ) {
							if(sb.charAt( sbLength-1 ) == '.' )
								sb.setCharAt( sbLength-1,' ' );

							currentDomain = new String(sb);
							currentMxPreference =  look.getMXPreference(i);

							if( mxPreference > currentMxPreference ) {
								mxPreference = currentMxPreference;
								realDomain = currentDomain;
							}
						}
					} catch(java.lang.StringIndexOutOfBoundsException ex) { }
				}
				return realDomain;
			} else { // DNS Server Error || Response Error
				return "7"; //UnclassifiedHost
			}
		} else {
			return "7"; //UnclassifiedHost
		}
	}
}
