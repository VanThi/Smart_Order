package com.cybersoftteam.app.project_so.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

import smartorder.common.ClientRequest;
import smartorder.common.ServerResponse;

public class TCPClient  {
	public static final String SERVERIP="192.168.3.101"; 
	public static final int SERVERPORT=4444;

	private ServerResponse serverResponse;
	private ClientRequest clientRequest;

	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	private static Socket soc;

	public TCPClient(ServerResponse serverResponse, ClientRequest clientRequest){
		this.serverResponse=serverResponse;
		this.clientRequest=clientRequest;

	}


	public TCPClient() {
	}
	public void run()throws Exception{

		Log.i("TCP client", "Connecting......");

		Log.i("TCP client", "request"+ clientRequest.toString());

		if(soc==null){

			InetAddress serAddress = InetAddress.getByName(SERVERIP);
			soc = new Socket(serAddress, SERVERPORT);
			OutputStream os = soc.getOutputStream();
			oos = new ObjectOutputStream(os);
			InputStream is = soc.getInputStream();
			ois = new ObjectInputStream(is);
		}

		Log.i("TCP client", "create success socket");

		//sent request to server

		oos.writeObject(clientRequest);
		oos.flush();
		Log.i("TCP CLient", "Sent.........");
		//receive the response which the server sends back

		//in this while the client listens for the response sent by the server
		Object obj = ois.readObject();
		serverResponse = (ServerResponse) obj;
		Log.i("TCPClient","----------------------received from server "+ (serverResponse!=null));

	}

	public ServerResponse getServerResponse() {
		return serverResponse;
	}

	public void setServerResponse(ServerResponse serverResponse) {
		this.serverResponse = serverResponse;
	}

	public ClientRequest getClientRequest() {
		return clientRequest;
	}

	public void setClientRequest(ClientRequest clientRequest) {
		this.clientRequest = clientRequest;
	}

	public void closeConnect(){
		try {
			soc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
