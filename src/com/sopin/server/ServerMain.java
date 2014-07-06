package com.sopin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.sopin.controller.RequestProcessor;

/**
 * 服务器入口
 * @author lance
 *
 */
public class ServerMain {
	
	final static int SERVER_PORT=8000;//服务器端口
	private static final Logger logger = Logger.getLogger(ServerMain.class);
	
	//通信服务入口
	public static void main(String[] args){
		
		try {
			ServerDataBuffer.serverSocket=new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			logger.debug("服务器正在运行中...");
			e.printStackTrace();
		}
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				logger.debug("服务器运行中...");
				
				try {
					while(true){
						Socket socket=ServerDataBuffer.serverSocket.accept();
						logger.debug("客户连接成功..:"
								+ socket.getInetAddress().getHostAddress()
								+ ":" + socket.getPort());
						//开启新线程来处理连接
						new Thread(new RequestProcessor(socket)).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
					logger.debug("服务器运行错误...");
				}
				logger.debug("运行结束...");
			}
		}).start();
	}
	
}
