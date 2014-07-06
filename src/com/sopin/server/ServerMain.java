package com.sopin.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.sopin.controller.RequestProcessor;

/**
 * ���������
 * @author lance
 *
 */
public class ServerMain {
	
	final static int SERVER_PORT=8000;//�������˿�
	private static final Logger logger = Logger.getLogger(ServerMain.class);
	
	//ͨ�ŷ������
	public static void main(String[] args){
		
		try {
			ServerDataBuffer.serverSocket=new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			logger.debug("����������������...");
			e.printStackTrace();
		}
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				logger.debug("������������...");
				
				try {
					while(true){
						Socket socket=ServerDataBuffer.serverSocket.accept();
						logger.debug("�ͻ����ӳɹ�..:"
								+ socket.getInetAddress().getHostAddress()
								+ ":" + socket.getPort());
						//�������߳�����������
						new Thread(new RequestProcessor(socket)).start();
					}
				} catch (IOException e) {
					e.printStackTrace();
					logger.debug("���������д���...");
				}
				logger.debug("���н���...");
			}
		}).start();
	}
	
}
