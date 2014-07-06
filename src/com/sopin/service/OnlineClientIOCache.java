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
 * ���߿ͻ��˵�IO��������
 * @author lance
 *
 */
public class OnlineClientIOCache {
	
	private String userId;//���ﱣ��󶨵��û�ID;����ǿ�ƶϿ���������Ƴ��û�
	private Socket socket;
	
	private InputStream is;//�����
	private OutputStream os;//������
	
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
	//���������Ӧ
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
