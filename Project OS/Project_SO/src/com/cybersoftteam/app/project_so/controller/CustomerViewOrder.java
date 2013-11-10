package com.cybersoftteam.app.project_so.controller;

import java.util.ArrayList;

import smartorder.common.ClientRequest;
import smartorder.common.ServerResponse;


import com.cybersoftteam.app.project_so.R;
import com.cybersoftteam.app.project_so.adapter.CustomerViewOrderAdapter;
import com.cybersoftteam.app.project_so.network.TCPClient;
import com.cybersoftteam.app.project_so.network.TCPClientConnectionFactory;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerViewOrder extends Fragment{
	private Activity act;
	private CustomerViewOrderAdapter customerViewOrderAdapter; 
	private OnViewOrderItemSelectListener myCallBack;

	private ClientRequest clientRequest;
	private TCPClient tcpClient;
	private ConnectTask con;
	private ServerResponse serverResponse;
	private ArrayList<String> orderList;

	// The container Activity must implement this interface so the frag can deliver messages
	public interface OnViewOrderItemSelectListener{
		public void onViewOrderItemSelected(String orderDetail);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.act=activity;
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			myCallBack=(OnViewOrderItemSelectListener)activity;
		} catch (Exception e) {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("fragment","=============this is fragment view order================");
		View view=inflater.inflate(R.layout.customer_vieworder, container, false);


		//Log.i("list", orderList.toString());
		customerViewOrderAdapter=new CustomerViewOrderAdapter(act,orderList);
		final ListView listview=(ListView)view.findViewById(R.id.customer_vieworder_item);
		listview.setAdapter(customerViewOrderAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i("click on item", "select tiem");
				String id= (String)listview.getItemAtPosition(arg2);
				//set request object
				clientRequest= new ClientRequest(ClientRequest.VIEWDETAIL,id);
				//sent request get list order item to sever
				//received list order item from sever
				connectProcessing(clientRequest);
			}
		});
		return view;
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
			if(tcpClient.getServerResponse()==null){
				//show dialog. please check your connect!
				showDialog("Error","Please check your connect!!");
			}else{
				// Notify the parent activity of selected item
				myCallBack.onViewOrderItemSelected(tcpClient.getServerResponse().getDetail());
			}
			
		}
	}


	public ArrayList<String> getOrderList() {
		return orderList;
	}

	public void setOrderList(ArrayList<String> orderList) {
		this.orderList = orderList;
	}

	public void connectProcessing(ClientRequest clientRequest){
		tcpClient =TCPClientConnectionFactory.getTCPClient();
		tcpClient.setClientRequest(clientRequest);
		tcpClient.setServerResponse(serverResponse);
		con=new ConnectTask();
		con.execute(tcpClient);
	}

	//We will detached show dialog in to own packet
	public void showDialog(String tiltle, String message){
		final Dialog dialog = new Dialog(act);
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
}
