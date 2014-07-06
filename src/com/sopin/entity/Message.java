package com.sopin.entity;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息对象
 * @author lance
 *
 */
public class Message {
	public static final String TAG = "Message";
	
	/** 消息发送者 */
	private User sendUser;
	/** 消息接收者 */
	private User receiveUser;
	/** 消息内容 */
	private String message;
	/** 发送时间 */
	private Date sendTime;
	
	public User getSendUser() {
		return sendUser;
	}
	public void setSendUser(User sendUser) {
		this.sendUser = sendUser;
	}
	public User getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public Message(){}
	
	public Message(String jsonString){
		try {
			JSONObject jsonObj=new JSONObject(jsonString);
			this.sendUser=new User(jsonObj.getString("sendUser"));
			this.receiveUser=new User(jsonObj.getString("receiveUser"));
			this.message=jsonObj.getString("message");
			this.sendTime=new Date(jsonObj.getLong("sendTime"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		JSONObject obj=new JSONObject();
		try {
			obj.put("sendUser", this.sendUser.toString());
			obj.put("receiveUser", this.receiveUser.toString());
			obj.put("message", this.message);
			obj.put("sendTime", this.sendTime.getTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	
}
