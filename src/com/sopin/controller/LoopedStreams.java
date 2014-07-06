package com.sopin.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * 防止管道流应用中出现得常见问题
 * @author lance
 *
 */
public class LoopedStreams {
	
	private PipedOutputStream pipedOS=new PipedOutputStream();
	//检测线程是否活动
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
	
	/** 创建一个独立的线程执行写入PipedOutputStream的操作 */
	private void startByteArrayReaderThread(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				//线程活动循环发送此案成终止发送
				while(keepRunning){
					//检查管道中得字节数
					if(byteArrayOS.size()>0){
						byte[] buffer=null;
						synchronized (byteArrayOS) {
							//提取数据
							buffer=byteArrayOS.toByteArray();
							//清空缓冲区
							byteArrayOS.reset();
						}
						try {
							//把提取到得数据发送给pipedOutputStream
							pipedOS.write(buffer,0,buffer.length);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
						
					}else{//没有数据可用,线程进入睡眠状态
						try {
							//每隔1秒查看ByteArrayOutputStream检查新数据
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
