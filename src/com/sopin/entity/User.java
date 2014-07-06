package com.sopin.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ���û�ģ��---ֻ��Ҫ�û�ID������
 * @author lance
 *
 */
public class User {
	public static final String TAG="User";
	//�û�ID
	private String id;
	//�û�����
	private String password;
	//�û���
	private String username;
	//�û�ͷ��
	private String headpic;
	
	public User(){}
	
	public User(String id,String password){
		this.id=id;
		this.password=password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public User(String jsonString){
		try {
			JSONObject objRoot=new JSONObject(jsonString);
			this.id=objRoot.getString("id");
			this.password=objRoot.getString("password");
			this.username=objRoot.getString("username");
			this.headpic=objRoot.getString("headpic");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		JSONObject objRoot=new JSONObject();
		try {
			objRoot.put("id", this.id);
			objRoot.put("password", this.password);
			objRoot.put("username", this.username);
			objRoot.put("headpic", this.headpic);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return objRoot.toString();
	}
	
}
