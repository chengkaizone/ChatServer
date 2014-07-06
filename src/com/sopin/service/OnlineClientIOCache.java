package com.sopin.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.sopin.entity.ActionType;

/** 
 * 在线客户端得IO流缓存类
 * @author lance
 *
 */
public class OnlineClientIOCache {
	
	private String userId;//这里保存绑定的用户ID;用于强制断开的情况下移除用户
	private Socket socket;
	
	private InputStream is;//输出流
	private OutputStream os;//输入流
	
	public OnlineClientIOCache(Socket socket) throws IOException {
		this.socket=socket;
		this.is=socket.getInputStream();
		this.os=socket.getOutputStream();
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}
	//这里输出响应
	public void writeResp(String resp) throws IOException{
		os.write(resp.getBytes(ActionType.CHARSET));
		os.flush();
	}

	public void closeIOCache(){
		try {
			getIs().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			getOs().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
