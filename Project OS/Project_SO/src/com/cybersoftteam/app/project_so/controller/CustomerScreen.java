package com.cybersoftteam.app.project_so.controller;

import java.util.ArrayList;

import smartorder.common.ClientRequest;
import smartorder.common.ServerResponse;

import com.cybersoftteam.app.project_so.R;
import com.cybersoftteam.app.project_so.adapter.MenuItemAdapter.autoUpdateTotal;
import com.cybersoftteam.app.project_so.controller.CustomerViewOrder.OnViewOrderItemSelectListener;
import com.cybersoftteam.app.project_so.network.TCPClient;
import com.cybersoftteam.app.project_so.network.TCPClientConnectionFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerScreen extends CustomerBasedAcitivy implements OnClickListener,
OnViewOrderItemSelectListener, autoUpdateTotal{
	private View lastView;
	private CustomerMenuItem customerMenuItem;
	private ArrayList<String> menuItems;

	private ClientRequest clientRequest;
	private TCPClient tcpClient;
	private ConnectTask con;
	private ServerResponse serverResponse;
	private CustomerViewOrder fragmentcCustomerViewOrder;
	
	private Activity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("activity", "=====this is customer screen======");
		this.activity=this;
		//setContentView(R.layout.customer_screen);
		setLayoutIds(R.layout.customer_menu, R.layout.customer_screen);
		setAnimationDuration(300);
		setAnimationType(MENU_TYPE_SLIDEOVER);
		super.onCreate(savedInstanceState);

		// Set on clicking listener object
		lastView = findViewById(R.id.customer_menu_item);
		lastView.setOnClickListener(this);
		findViewById(R.id.configuration).setOnClickListener(this);
		findViewById(R.id.viewOrder).setOnClickListener(this);
		findViewById(R.id.logout).setOnClickListener(this);
		findViewById(R.id.logout).setOnClickListener(this);
		findViewById(R.id.exit).setOnClickListener(this);
		Log.i("van thi", "===========before init customer menu item===========");
		customerMenuItem =new CustomerMenuItem();
		Log.i("van thi", "===========after init customer menu item===========");

		Bundle extras=getIntent().getExtras();
		menuItems=extras.getStringArrayList("menuitem");

		customerMenuItem.updateItems(menuItems);

		// Create content fragment
		if (savedInstanceState == null) {

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,customerMenuItem ).commit();
		} else {
			viewContent(R.id.fragment_container, customerMenuItem);
		}	
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.customer_menu_item){
			showMenuItem();
		}else if(id==R.id.configuration){
			showConfiguration();
		}else if(id==R.id.viewOrder){
			showViewOrder();
		}else if(id==R.id.logout){
			//close connect, clear data and ......
			logout();

		}else{
			showExitConfirm();
		}
		// Set menu item background
		if (this.lastView != null) {
			this.lastView.setBackgroundResource(R.drawable.menu_bg);
		}

		lastView = v;
		v.setBackgroundResource(R.drawable.menu_bg_selected);
	}

	private void logout() {
		clientRequest =new ClientRequest(ClientRequest.LOGOUT);
		connectProcessing(clientRequest);
	}

	public void showMenuItem(){
		toggleMenu();
		//show menu item
		CustomerMenuItem fragment=new CustomerMenuItem();
		fragment.updateItems(menuItems);
		viewContent(R.id.fragment_container, fragment);
	}

	@Override
	public void showMenu(View view) {
		toggleMenu();
		//viewContent(R.id.fragment_container, customerMenuItem);

	}

	public void showConfiguration(){
		toggleMenu();

		CustomerDetail fragment=new CustomerDetail();
		viewContent(R.id.fragment_container, fragment);
	}

	public void showViewOrder(){
		toggleMenu();

		fragmentcCustomerViewOrder=new CustomerViewOrder();
		//set request object

		clientRequest =new ClientRequest(ClientRequest.VIEWORDERS);

		connectProcessing(clientRequest);
	}


	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onViewOrderItemSelected(String orderDetail) {

		//the customer selected the view order item of customer view order
		//capture the list view order item from the customer screen

		ViewOrderDeital listViewOrderItem=new ViewOrderDeital();
		viewContent(R.id.fragment_container, listViewOrderItem);
		Log.i("call back in customer screen", "order detail "+orderDetail);
		listViewOrderItem.updateListViewOrder(orderDetail);

	}
	
	@Override
	public void updateTotal(String total) {
		customerMenuItem.calTotal(total);
	}

	//do in background
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
			if(tcpClient.getServerResponse()==null)
				//show dialog
				showDialog("Error!", "Please check your connect!!");
			else if(tcpClient.getClientRequest().getRequestID()==ClientRequest.VIEWORDERS){
				fragmentcCustomerViewOrder.setOrderList(tcpClient.getServerResponse().getOrderIDs());
				viewContent(R.id.fragment_container, fragmentcCustomerViewOrder);
			}
			else if(tcpClient.getClientRequest().getRequestID()==ClientRequest.LOGOUT){
				Intent logoutIntent=new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(logoutIntent);
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
	
	public void showExitConfirm() {
		// create dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setMessage("You are sure exit app");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

							Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("Exit", true);
							startActivity(intent);

					}
				});
		builder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
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
}
