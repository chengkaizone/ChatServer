package com.sopin.controller;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.sopin.entity.ActionType;
import com.sopin.entity.Message;
import com.sopin.entity.Request;
import com.sopin.entity.Response;
import com.sopin.entity.ResponseStatus;
import com.sopin.entity.ResponseType;
import com.sopin.entity.User;
import com.sopin.server.ServerDataBuffer;
import com.sopin.server.ServerMain;
import com.sopin.service.OnlineClientIOCache;
import com.sopin.service.UserService;

/**
 * ����������������
 * @author lance
 *
 */
public class RequestProcessor implements Runnable {
	
	private OnlineClientIOCache currentClientIO;//��ǰIOͨ��
	private boolean flag=true;//�Ƿ�ѭ�������˿�
	
	private static final Logger logger = Logger.getLogger(ServerMain.class);
	
	public RequestProcessor(Socket socket){
		try {
			currentClientIO=new OnlineClientIOCache(socket);
		} catch (IOException e) {
			e.printStackTrace();
			flag=false;
		}
	}
	
	@Override
	public void run() {
		try {
			byte[] brr=new byte[1024];
			String readTmp="";
			String readJson="";
			int len=0;
			while(flag&&(((len=currentClientIO.getIs().read(brr))!=-1))){
				readTmp = new String(brr,0,len,ActionType.CHARSET);
				readJson+=readTmp;
				readTmp=readJson.substring(readJson.length()-ActionType.EOF.length(), readJson.length());
				if(readTmp.equals(ActionType.EOF)){
					logger.debug("��ȡ��������--->"+readJson);
					Request request=null;
					try {
						readJson=readJson.substring(0, readJson.length()-ActionType.EOF.length());
						request=new Request(readJson);
						readJson="";//���ý��յ����ַ���
						logger.debug("�ͻ��˷�����������:"+request.getAction());
					} catch (Exception e) {
						e.printStackTrace();
						logger.debug("�Ѿ�ǿ�ƿͻ�������������ӶϿ�!!");
					}
					if(currentClientIO.getSocket().isConnected()){
						if(request.getAction()!=null){
							System.out.println("Server��ȡ�˿ͻ��˵�����:"+request.getAction());
							String actionName=request.getAction();
							if(ActionType.ACTION_TYPE_LOGIN.equals(actionName)){
								//�û���¼
								login(currentClientIO,request);
							}else if(ActionType.ACTION_TYPE_LOGOUT.equals(actionName)){
								//�����˳�--����ͻ��˳�ѭ��
								flag=logout(currentClientIO,request);
							}else if(ActionType.ACTION_TYPE_CHAT.equals(actionName)){
								//����
								chat(request);
							}
						}
					}else{
						logger.debug("Serverδ��ȡ���ͻ��˵�����!!");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			//������Ҫ�Ƴ���ǰ��Ӧ���û�ID
			logger.debug("�Ѿ�ǿ�ƿͻ�������������ӶϿ�!");
			String curUserId=currentClientIO.getUserId();
			if(curUserId!=null){
				logger.debug("�Ự�ж�,�Ƴ��û�->"+currentClientIO.getUserId());
				ServerDataBuffer.onlineUsersMap.remove(currentClientIO.getUserId());
				//�ѵ�ǰ���߿ͻ��˵�IO��map��ɾ��
				ServerDataBuffer.onlineUserIOCacheMap.remove(currentClientIO.getUserId());
			}
		}
		logger.debug("���н���!!");
		String curUserId=currentClientIO.getUserId();
		if(curUserId!=null){//�п����ǿͻ��˶Ͽ�������
			logger.debug("�Ự����,�Ƴ��û�->"+currentClientIO.getUserId());
			ServerDataBuffer.onlineUsersMap.remove(currentClientIO.getUserId());
			//�ѵ�ǰ���߿ͻ��˵�IO��map��ɾ��
			ServerDataBuffer.onlineUserIOCacheMap.remove(currentClientIO.getUserId());
		}
	}
	
	//�ͻ��������¼
	public void login(OnlineClientIOCache currentClientIO,Request request) throws IOException{
		User usrTmp=(User)request.getAttribute(User.TAG);
		
		//��֤��¼�˺ź�����
		User user=UserService.login(usrTmp.getId(), usrTmp.getPassword());
		Response resp=new Response();//������Ӧ���󷵻ص��ͻ���
		if(null!=user){
			if(ServerDataBuffer.onlineUsersMap.containsKey(user.getId())){
				logger.debug("���û��Ѿ��ڱ�������!");
				//�û��Ѿ���¼����
				resp.setStatus(ResponseStatus.OK);
				resp.setType(ResponseType.LOGIN);
				resp.setData(ActionType.ACTION_TYPE_MSG, "�û��Ѿ��ڱ𴦵�¼��!");
				currentClientIO.writeResp(resp.toString());
			}else{//��ȷ��¼
				//��ӵ������û���
				ServerDataBuffer.onlineUsersMap.put(user.getId(), user);
				currentClientIO.setUserId(user.getId());
				ServerDataBuffer.onlineUserIOCacheMap.put(user.getId(),
						currentClientIO);
				logger.debug("��ȷ��¼�û���-->"+ServerDataBuffer.onlineUsersMap.size()+" id="+user.getId());
				//���������û�---�������ڷ��������û����ͻ���
				resp.setStatus(ResponseStatus.OK);
				resp.setData(User.TAG, user);
				// TODO
				//���ﻹҪ������ص������û����ͻ���
				
				logger.debug("��������Ӧ�ͻ���-->"+resp.toString());
				//�����Ӧ
				currentClientIO.writeResp(resp.toString());
				
				//֪ͨ�����û������Ѿ�������
				Response respAll=new Response();
				respAll.setType(ResponseType.LOGIN);
				respAll.setData(User.TAG, user);
				iteratorResponse(respAll);
				
				//�ѵ�ǰ�����û���ӵ�OnlineUserTableModel--��ʱ�������������ں�̨�ļ����ʾ
			}
		}else{//��¼ʧ��!
			logger.debug("��¼ʧ��-->"+resp.toString());
			
			resp.setStatus(ResponseStatus.OK);
			resp.setData(ActionType.ACTION_TYPE_MSG, "�û��������벻��ȷ!");

			currentClientIO.writeResp(resp.toString());
		}
		
	}
	
	/** ����---��ָ���û���ͨ��������Ϣ--����Ҫ��ǰ���ӵ�ͨ�� */
	public void chat(Request req) throws IOException {
		logger.debug("����������Ϣ!");
		//��װ����Ϣ����
		Message msg=(Message)req.getAttribute(Message.TAG);
		Response resp=new Response();
		resp.setStatus(ResponseStatus.OK);
		resp.setType(ResponseType.CHAT);
		resp.setData(Message.TAG,msg);
		
		User receiveUser=msg.getReceiveUser();
		User sendUser=msg.getSendUser();
		
		logger.debug(receiveUser.getId()+"--->"+receiveUser.getPassword()+"--->"+sendUser.getId()+"--->"+sendUser.getPassword());
		//�������ɴ�����ʾ
		if(receiveUser.getId()!=null){//˽��
			logger.debug("˽��!");
			OnlineClientIOCache io=ServerDataBuffer.onlineUserIOCacheMap.get(receiveUser.getId());
			sendResponse(io,resp);//������Ӧ
		}else{
			logger.debug("Ⱥ��!");
			//Ⱥ��--�����Լ�������˶�Ҫ��
			for(String id:ServerDataBuffer.onlineUserIOCacheMap.keySet()){
				if(sendUser.getId().equals(id)){
					continue;
				}
				sendResponse(ServerDataBuffer.onlineUserIOCacheMap.get(id), resp);
			}
			logger.debug("�������!");
		}
	}
	
	public boolean logout(OnlineClientIOCache oio,Request req) throws IOException {
		logger.debug(currentClientIO.getSocket().getInetAddress().getHostAddress() + ":"
				+ currentClientIO.getSocket().getPort() + "  �Ͽ�����...");
		User user=(User)req.getAttribute(User.TAG);
		if(null!=user){
			//�������û�������ɾ����ǰ�û�
			ServerDataBuffer.onlineUsersMap.remove(user.getId());
			//�ѵ�ǰ���߿ͻ��˵�IO��map��ɾ��
			ServerDataBuffer.onlineUserIOCacheMap.remove(user.getId());
			
			//������Ӧ
			Response resp=new Response();
			resp.setType(ResponseType.LOGOUT);
			resp.setData(User.TAG, user);
			
			oio.writeResp(resp.toString());
			
			currentClientIO.getSocket().close();
			//�������û�����ɾ��---�����Ǵ����̨����ʾ
			//ServerDataBuffer.onlineUserIOCacheMap
			//֪ͨ�������߿ͻ���
			iteratorResponse(resp);
			//�Ͽ�����
		}
		return false;
	}
	
	//���������߿ͻ���������Ӧ--֪ͨ�����û�����������
	private void iteratorResponse(Response resp) throws IOException{
		logger.debug("��ʼ֪ͨ�����û�!");
		for(OnlineClientIOCache oio:ServerDataBuffer.onlineUserIOCacheMap.values()){
			oio.writeResp(resp.toString());
			logger.debug("�û�--->");
		}
		logger.debug("֪ͨ���!");
	}
	
	//��ָ���ͻ���IO����������ָ������Ӧ
	private void sendResponse(OnlineClientIOCache oio,Response resp) throws IOException {
		if(oio!=null){
			logger.debug("��ʼ����!\n"+resp.toString());			
			oio.writeResp(resp.toString());
			logger.debug("�������!");
		}
	}
	
}
