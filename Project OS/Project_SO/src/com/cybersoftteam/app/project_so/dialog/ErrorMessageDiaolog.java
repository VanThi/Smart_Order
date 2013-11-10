package com.cybersoftteam.app.project_so.dialog;

import android.app.Activity;

public class ErrorMessageDiaolog {
	private String title;
	private String message;
	private Activity act;
	public ErrorMessageDiaolog(String title, String message) {
		this.title = title;
		this.message = message;
		this.act=(Activity)act;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Activity getAct() {
		return act;
	}
	public void setAct(Activity act) {
		this.act = act;
	}
	

	//We will detached show dialog in to own packet
	

	
}
