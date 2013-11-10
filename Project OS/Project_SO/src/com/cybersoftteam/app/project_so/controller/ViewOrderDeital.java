package com.cybersoftteam.app.project_so.controller;


import com.cybersoftteam.app.project_so.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewOrderDeital extends Fragment{
	private Activity act;
	private TextView twOrderDetail;
	private String orderDetail;
	
	@Override
	public void onAttach(Activity activity) {
		Log.i("fragment", "*********=======this is onattach fragmet list view order item===========");
		super.onAttach(activity);
		this.act=activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("fragment", "*******=======this is oncreateview fragmet list view order item===========");
		View view=inflater.inflate(R.layout.vieworder_detail, container,false);
		twOrderDetail=(TextView)view.findViewById(R.id.order_detail);
		twOrderDetail.setText(Html.fromHtml(orderDetail));
		return view;
	}
	
	public void updateListViewOrder(String orderDetail){
		Log.i("van thi", "update list view order"+orderDetail);
		this.orderDetail=orderDetail;
		
	}

	public Activity getAct() {
		return act;
	}

	public void setAct(Activity act) {
		this.act = act;
	}
	
	
	
}
