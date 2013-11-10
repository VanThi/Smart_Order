package com.cybersoftteam.app.project_so.controller;


import com.cybersoftteam.app.project_so.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class CustomerDetail extends Fragment{
	private Activity act;
	private EditText username; 
	private EditText fullname;
	private EditText email;
	private EditText phoneNumber;
	private EditText address;
	//private Button clear;
	//private ClientRequest clientRequest;
	//private TCPClient tcpClient;
	//private ConnectTask con;
	//private ServerResponse serverResponse;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.act=activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("fragment", "====this is fragmenr configuration======");
		View view=inflater.inflate(R.layout.customer_detail,container, false);

		username =(EditText)view.findViewById(R.id.et_userName_con);
		fullname =(EditText)view.findViewById(R.id.et_fullname_con);
		email =(EditText)view.findViewById(R.id.et_email_con);
		phoneNumber =(EditText)view.findViewById(R.id.et_phoneNumber_con);
		address =(EditText)view.findViewById(R.id.et_address_con);
		
		username.setEnabled(false);
		fullname.setEnabled(false);
		email.setEnabled(false);
		phoneNumber.setEnabled(false);
		address.setEnabled(false);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(act);
		
		username.setText(preferences.getString("userName",""));
		fullname.setText(preferences.getString("fullName",""));
		email.setText(preferences.getString("email",""));
		phoneNumber.setText(preferences.getString("phoneNumber",""));
		address.setText(preferences.getString("address",""));

		return view;
	}

}