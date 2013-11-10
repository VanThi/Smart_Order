package com.cybersoftteam.app.project_so.adapter;

import java.util.ArrayList;

import com.cybersoftteam.app.project_so.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomerViewOrderAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> listViewOrder=new ArrayList<String>();
	
	public CustomerViewOrderAdapter() {
	}
	
	public CustomerViewOrderAdapter(Activity act,ArrayList<String> listViewOder){
		Log.i("adapter", "================this is  view order adapter============");
		this.context=act;
		this.listViewOrder=listViewOder;
	}
	@Override
	public int getCount() {
		return listViewOrder.size();
	}
	
	@Override
	public Object getItem(int position) {
		return listViewOrder.get(position);
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("adapter", "================this is View view order adapter============");
		LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView=inflater.inflate(R.layout.customer_vieworder_item, null);
		Log.d("loi", "get conver view");
		TextView viewOrderItem= (TextView)convertView.findViewById(R.id.customer_ViewOrderItem);
		viewOrderItem.setText(listViewOrder.get(position));
		//can set event handles on item in there or in Customer View Order class
		//but when set in there, We can't replace fragment in customer screen
		/*listViewOrderItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		*/
		return convertView;
	}
	
}
