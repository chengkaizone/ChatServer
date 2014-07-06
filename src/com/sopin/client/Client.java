package com.sopin.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import com.sopin.entity.ActionType;
import com.sopin.entity.Message;
import com.sopin.entity.Request;
import com.sopin.entity.Response;
import com.sopin.entity.User;

public class Client {
	
	private InputStream is;
	private OutputStream os;
	BufferedReader in = null;
	PrintStream ps = null;
	
	public Client() { 
		Socket sc = null;
		try {
			sc = new Socket("localhost", 8000);
			System.out.println("come to server..");
			is = sc.getInputStream();
			os=sc.getOutputStream();
			in = new BufferedReader(new InputStreamReader(is));
			ps = new PrintStream(os);
			new Thread(){
				public void run(){
					try {
						while (true) {
							String str = in.readLine();
							//Response resp=new Response(str);
							System.out.println("server 传来消息" + str);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
			new Thread(){
				public void run(){
					try {
						byte bt[] = new byte[1024];
						while (true) {
							System.in.read(bt);
							User fromUsr=new User();
							fromUsr.setId("M_"+2L);
							fromUsr.setPassword("32434435fdghah");
							
							Request req=new Request();
							req.setAction(ActionType.ACTION_TYPE_LOGIN);
							req.setAttribute(User.TAG, fromUsr);
							//String msg = new String(bt, "UTF-8").trim();
							String reqStr=req.toString();
							ps.println(req.toString());
							System.out.println("读取控制台--->"+req.toString());
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client();
	}
	
	private String chatMesg(){
		User fromUsr=new User();
		fromUsr.setId("M_"+2L);
		fromUsr.setPassword("32434435fdghah哈哈");
		
		User toUsr=new User();
		toUsr.setId("M_"+7897L);
		toUsr.setPassword("3454dfsd是电风扇的");
		
		
		Message msg=new Message();
		msg.setSendUser(fromUsr);
		msg.setReceiveUser(toUsr);
		msg.setMessage("消息通知,sdfsd");
		msg.setSendTime(new Date());
		Request req=new Request();
		req.setAction(ActionType.ACTION_TYPE_CHAT);
		req.setAttribute(Message.TAG, msg);
		return req.toString();
	}
}
