package com.sopin.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 请求对象
 * @author lance
 *
 */
public class Request {
	
	/** 请求动作 */
	private String action;
	/** 请求域中得数据 name-value 属性集合 */
	private Map<String,Object> attributeMap;
	
	public Request(){
		attributeMap=new HashMap<String, Object>();
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	public Object getAttribute(String name) {
		return this.attributeMap.get(name);
	}

	public void setAttribute(String name,Object value){
		this.attributeMap.put(name, value);
	}
	
	public void removeAttribute(String name){
		this.attributeMap.remove(name);
	}
	
	public void clearAttribute(){
		this.attributeMap.clear();
	}
	
	//根据属性值构造请求字符串--需要微信对接确认
	public String toString(){
		JSONObject objRoot=new JSONObject();
		try {
			objRoot.put(ActionType.ACTION_TYPE_KEY, action);
			JSONObject obj=new JSONObject();
			for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
				String key=entry.getKey();
				if(key.equals(Message.TAG)){
					Message msg=(Message)entry.getValue();
					obj.put(key, msg.toString());
				}else if(key.equals(User.TAG)){
					User user=(User)entry.getValue();
					obj.put(key, user.toString());
				}
				
			}
			objRoot.put(ActionType.ACTION_DATA, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return objRoot.toString()+ActionType.EOF;
	}
	
	/** 根据字符串构造请求对象 以对应IOS为主 */
	public Request (String json){
		try {
			JSONObject objRoot=new JSONObject(json);
			this.action=objRoot.getString(ActionType.ACTION_TYPE_KEY);
			JSONObject obj=new JSONObject(objRoot.getString(ActionType.ACTION_DATA));//
			this.attributeMap=new HashMap<String, Object>();
			for (Iterator iter = obj.keys(); iter.hasNext();) {
				String key = (String)iter.next();
				if(key.equals(User.TAG)){
					setAttribute(key, new User(obj.getString(key)));
				}else if(key.equals(Message.TAG)){
					setAttribute(key, new Message(obj.getString(key)));
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
