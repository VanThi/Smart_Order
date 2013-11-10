package com.cybersoftteam.app.project_so.network;
/**
 * create singleton for tcp client
 * @author Van Thi
 *
 */
public class TCPClientConnectionFactory {
	private static TCPClient tcpClient;
	
	private TCPClientConnectionFactory(){
		
	}
	
	public static TCPClient getTCPClient(){
		if(tcpClient==null){
			tcpClient=new TCPClient();
		}
		return tcpClient;
	}
	
}
