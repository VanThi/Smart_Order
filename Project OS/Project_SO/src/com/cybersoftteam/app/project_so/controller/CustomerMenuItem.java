package com.cybersoftteam.app.project_so.controller;

import java.util.ArrayList;

import smartorder.common.CartItem;
import smartorder.common.ClientRequest;
import smartorder.common.ServerResponse;



import com.cybersoftteam.app.project_so.R;
import com.cybersoftteam.app.project_so.adapter.MenuItemAdapter;
import com.cybersoftteam.app.project_so.model.MenuItem;
import com.cybersoftteam.app.project_so.network.TCPClient;
import com.cybersoftteam.app.project_so.network.TCPClientConnectionFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerMenuItem extends Fragment{
	private Activity activity;
	private MenuItemAdapter menuItemAdapter;
	private ArrayList<String> menuItems; 
	private ClientRequest clientRequest;
	private TCPClient tcpClient;
	private ConnectTask con;
	private ServerResponse serverResponse;
	private ArrayList<MenuItem> listItem;
	private TextView tvtotal;
	private ListView listview;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i("fragment", "============this is on aScttach fragment customer menu item==============");
		this.activity=activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("van thi", "=======this is oncreateView customer menu item=======");
		View view= inflater.inflate(R.layout.menu_item, container,false);

		//preprocessing menuitem

		listItem =preprocessing(menuItems);
		menuItemAdapter=new MenuItemAdapter(activity,listItem);
		listview= (ListView)view.findViewById(R.id.menu_item);
		listview.setAdapter(menuItemAdapter);

		Button order=(Button)view.findViewById(R.id.order);
		
		Button update=(Button)view.findViewById(R.id.updatebutton);
		
		tvtotal=(TextView)view.findViewById(R.id.total);
		order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("order","============do something order================");
				//show 1 dialog to customer.
				new AlertDialog.Builder(activity)
				.setTitle("Confirm Order")
				.setMessage("Are you sure you want to order this?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						// continue with order
						//set cart item
						Log.i("cart item","start set cart item");
						ArrayList<CartItem> cartItems=new ArrayList<CartItem>();
						for(MenuItem i:listItem){
							if(i.isChoose()){

								CartItem cartItem=new CartItem(i.getId(), i.getQuantity());
								Log.i("cart item", "cart item "+cartItem.getProductID());
								cartItems.add(cartItem);


							}
						}
						if(cartItems.size()!=0){
							//set request object
							clientRequest=new ClientRequest(ClientRequest.ORDER, cartItems);
							//sent order list to server, received from server, and alert to customer
							connectProcessing(clientRequest);
						}else{
							showDialog("Order", "Cart Item is empty");
						}
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						// do nothing
					}
				})
				.show();
			}
		});

		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//set request object
				clientRequest=new ClientRequest(ClientRequest.UPDATE);
				connectProcessing(clientRequest);

			}
		});
		return view;

	}

	public void updateItems(ArrayList<String> menuItems){
		this.menuItems=new ArrayList<String>();
		this.menuItems=menuItems;
	}
	//return 1 array list menu item, then fragment will get menu item to set apapter
	private ArrayList<MenuItem> preprocessing(ArrayList<String> menuItems){
		ArrayList<MenuItem> menu=new ArrayList<MenuItem>();
		for(String s:menuItems){
			String[] subs=s.split(";");
			MenuItem item=new MenuItem();
			item.setId(Integer.parseInt(subs[0]));
			item.setName(subs[1]);
			item.setPrice(Float.parseFloat(subs[2]));

			menu.add(item);
		}
		return menu;
	}

	public ArrayList<String> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(ArrayList<String> menuItems) {
		this.menuItems = menuItems;
	}


	//do in background
	//create a thread, connect to server, and this is doing background
	//after received form server response, this will call onPostExecute to execute
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
				showDialog("Error", "Please check your connect!");
			else
			if(tcpClient.getClientRequest().getRequestID()==ClientRequest.ORDER)
				if (tcpClient.getServerResponse().isOrdered()) 
					showDialog("Order", "Order success!!!");
				else
					showDialog("Order", "Please update menu! Thank You!!");
			//update menu item
			else if(tcpClient.getClientRequest().getRequestID()==ClientRequest.UPDATE){
				Log.i("Update", "upate item "+ tcpClient.getServerResponse().getMenu());
				listItem=preprocessing(tcpClient.getServerResponse().getMenu());
				menuItemAdapter=new MenuItemAdapter(activity, listItem);
				listview.setAdapter(menuItemAdapter);
			}


		}
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

	public void connectProcessing(ClientRequest clientRequest){
		tcpClient =TCPClientConnectionFactory.getTCPClient();
		tcpClient.setClientRequest(clientRequest);
		tcpClient.setServerResponse(serverResponse);
		con=new ConnectTask();
		con.execute(tcpClient);
	}
	
	public void calTotal(String total){
		tvtotal.setText(total);
	}

}
