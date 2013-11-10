package com.cybersoftteam.app.project_so.controller;


import java.util.ArrayList;

import smartorder.common.ClientRequest;
import smartorder.common.ServerResponse;

import com.cybersoftteam.app.project_so.R;
import com.cybersoftteam.app.project_so.network.TCPClient;
import com.cybersoftteam.app.project_so.network.TCPClientConnectionFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity  {
	private EditText userName;
	private EditText password;
	private Button register;
	private Button login;
	private ClientRequest clientRequest;
	private ServerResponse serverResponse;
	private ConnectTask con;
	private TCPClient tcpClient;
	private Activity activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity=this;
		setContentView(R.layout.activity_login);
		//finish when press exit button
		if (getIntent().getBooleanExtra("Exit", false)) {
			finish();
		}
		userName =(EditText) findViewById(R.id.et_userName_login);
		password= (EditText) findViewById(R.id.et_pass_login);
		register=(Button) findViewById(R.id.register);
		login=(Button) findViewById(R.id.login);

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent regesterIntent= new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(regesterIntent);
			}
		});

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//set client request
				clientRequest=new ClientRequest(ClientRequest.LOGIN,
						userName.getText().toString(), password.getText().toString());
				Log.i("van thi",">>>>>>>>>>>>request<<<<<<<<<<<<<"+clientRequest.toString());
				//sent sent request and received response sever;
				connectProcessing(clientRequest);
			}


		});
	}
	public ClientRequest getClientRequest() {
		return clientRequest;
	}

	public void setClientRequest(ClientRequest clientRequest) {
		this.clientRequest = clientRequest;
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//We will detached show dialog in to own packet
	public void showDialog(String tiltle, String message){
		final Dialog dialog = new Dialog(activity);
		dialog.setContentView(R.layout.dialog_show_error);
		dialog.setTitle(tiltle);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(message);
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.error);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	@Override
	public void onBackPressed() {
		return;
	}

	//do in background
	//create a thread, connect to server, and this is doing background
	//after received form server response, this will call onPostExecute

	public class ConnectTask extends AsyncTask<TCPClient, ServerResponse, ServerResponse>{

		@Override
		protected ServerResponse doInBackground(TCPClient... tcpClients) {
			//we create a TCPClient object and
			Log.i("asynctask", "doing backgroud---------------------");
			try {
				tcpClient.run();
			} catch (Exception e) {
				return null;
			}
			return serverResponse;
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			if (tcpClient.getServerResponse()==null) {
				showDialog("Error", "Please connect network!!!");
			}
			else if(tcpClient.getServerResponse().getMenu()!=null){
				Intent customerScreenIntent=new Intent(getApplicationContext(), CustomerScreen.class);
				//received menu item
				customerScreenIntent.putStringArrayListExtra("menuitem", tcpClient.getServerResponse().getMenu());

				//received configuration of customer, we save it to shared preference
				SharedPreferences share=PreferenceManager.getDefaultSharedPreferences(activity);
				SharedPreferences.Editor editor = share.edit();
				ArrayList<String> userDetails=tcpClient.getServerResponse().getUserDetail();
				editor.putString("userName",userName.getText().toString());
				editor.putString("fullName",userDetails.get(0));
				editor.putString("address",userDetails.get(1));
				editor.putString("phoneNumber",userDetails.get(2));
				editor.putString("email",userDetails.get(3));
				editor.commit();

				startActivity(customerScreenIntent);
			}else{
				showDialog("Login", "User name or password not variable");
			}
		}
	}

	public void connectProcessing(ClientRequest clientRequest){
		tcpClient =TCPClientConnectionFactory.getTCPClient();
		tcpClient.setClientRequest(clientRequest);
		tcpClient.setServerResponse(serverResponse);
		con=new ConnectTask();
		con.execute(tcpClient);
	}

}
