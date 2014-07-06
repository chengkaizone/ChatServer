package com.sopin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * ��ֹ�ܵ���Ӧ���г��ֵó�������
 * @author lance
 *
 */
public class LoopedStreams {
	
	private PipedOutputStream pipedOS=new PipedOutputStream();
	//����߳��Ƿ�
	private boolean keepRunning=true;
	
	public LoopedStreams(){
		//pipedOS.connect(pipedIS);
		
	}
	
	private ByteArrayOutputStream byteArrayOS=new ByteArrayOutputStream(){

		@Override
		public void close() {
			keepRunning=false;
			try {
				super.close();
				pipedOS.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private PipedInputStream pipedIS=new PipedInputStream(){

		@Override
		public void close() {
			keepRunning=false;
			try {
				super.close();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	};
	
	public InputStream getInputStream(){
		return pipedIS;
	}
	public OutputStream getOutputStream(){
		return byteArrayOS;
	}
	
	/** ����һ���������߳�ִ��д��PipedOutputStream�Ĳ��� */
	private void startByteArrayReaderThread(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				//�̻߳ѭ�����ʹ˰�����ֹ����
				while(keepRunning){
					//���ܵ��е��ֽ���
					if(byteArrayOS.size()>0){
						byte[] buffer=null;
						synchronized (byteArrayOS) {
							//��ȡ����
							buffer=byteArrayOS.toByteArray();
							//��ջ�����
							byteArrayOS.reset();
						}
						try {
							//����ȡ�������ݷ��͸�pipedOutputStream
							pipedOS.write(buffer,0,buffer.length);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
						
					}else{//û�����ݿ���,�߳̽���˯��״̬
						try {
							//ÿ��1��鿴ByteArrayOutputStream���������
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}).start();
	}
	
	
}
