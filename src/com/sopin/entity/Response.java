package com.sopin.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ��Ӧ����
 * @author lance
 *
 */
public class Response {
	
	//��Ӧ��״̬
	private int status;
	//��Ӧ����
	private int type;
	//��Ӧ������---
	private Map<String,Object> dataMap;
	
	public Response(){
		this.status=ResponseStatus.OK;
		this.type=ResponseType.TEXT;
		this.dataMap=new HashMap<String, Object>();
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(HashMap<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	
	public void setData(String name,Object value){
		this.dataMap.put(name, value);
	}
	
	public Object getData(String name){
		return this.dataMap.get(name);
	}
	
	public void removeData(String name){
		this.dataMap.remove(name);
	}
	
	public void clearData(){
		this.dataMap.clear();
	}

	/** ��������ֵ������Ӧ�ַ���---��IOSΪ�� */
	public String toString(){
		JSONObject objRoot=new JSONObject();
		try {
			objRoot.put(ActionType.ACTION_RESP_STATUS, status);
			objRoot.put(ActionType.ACTION_RESP_TYPE, type);
			JSONObject obj=new JSONObject();
			for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
				String key=entry.getKey();
				if(key.equals(User.TAG)){
					User user=(User)entry.getValue();
					obj.put(key, user.toString());
				}else if(key.equals(Message.TAG)){
					Message msg=(Message)entry.getValue();
					obj.put(key, msg.toString());
				}else if(key.equals(ActionType.ACTION_TYPE_MSG)){
					obj.put(key, entry.getValue());
				}
			}
			objRoot.put(ActionType.ACTION_DATA, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return objRoot.toString()+ActionType.EOF;
	}
	
	/** ������Ӧ�ַ���������Ӧ����---�÷����ɿͻ���ʹ�� */
	public Response(String json){
		try {
			JSONObject objRoot=new JSONObject(json);
			this.status=objRoot.getInt(ActionType.ACTION_RESP_STATUS);
			this.type=objRoot.getInt(ActionType.ACTION_RESP_TYPE);
			JSONObject obj=objRoot.getJSONObject(ActionType.ACTION_DATA);//
			this.dataMap=new HashMap<String, Object>();
			for (Iterator iter = obj.keys(); iter.hasNext();) {
				String key = (String)iter.next();
				if(key.equals(User.TAG)){
					setData(key,new User(obj.getString(key)));
				}else if(key.equals(Message.TAG)){
					setData(key,new Message(obj.getString(key)));
				}else if(key.equals(ActionType.ACTION_TYPE_MSG)){
					setData(key,obj.getString(key));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
