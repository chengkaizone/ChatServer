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
 * 服务器端请求处理器
 * @author lance
 *
 */
public class RequestProcessor implements Runnable {
	
	private OnlineClientIOCache currentClientIO;//当前IO通道
	private boolean flag=true;//是否循环监听端口
	
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
					logger.debug("读取到的数据--->"+readJson);
					Request request=null;
					try {
						readJson=readJson.substring(0, readJson.length()-ActionType.EOF.length());
						request=new Request(readJson);
						readJson="";//重置接收到得字符串
						logger.debug("客户端发送了请求动作:"+request.getAction());
					} catch (Exception e) {
						e.printStackTrace();
						logger.debug("已经强制客户端与服务器连接断开!!");
					}
					if(currentClientIO.getSocket().isConnected()){
						if(request.getAction()!=null){
							System.out.println("Server读取了客户端得请求:"+request.getAction());
							String actionName=request.getAction();
							if(ActionType.ACTION_TYPE_LOGIN.equals(actionName)){
								//用户登录
								login(currentClientIO,request);
							}else if(ActionType.ACTION_TYPE_LOGOUT.equals(actionName)){
								//请求退出--这里就会退出循环
								flag=logout(currentClientIO,request);
							}else if(ActionType.ACTION_TYPE_CHAT.equals(actionName)){
								//聊天
								chat(request);
							}
						}
					}else{
						logger.debug("Server未读取到客户端得请求!!");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			//这里需要移除当前对应的用户ID
			logger.debug("已经强制客户端与服务器连接断开!");
			String curUserId=currentClientIO.getUserId();
			if(curUserId!=null){
				logger.debug("会话中断,移除用户->"+currentClientIO.getUserId());
				ServerDataBuffer.onlineUsersMap.remove(currentClientIO.getUserId());
				//把当前上线客户端得IO从map中删除
				ServerDataBuffer.onlineUserIOCacheMap.remove(currentClientIO.getUserId());
			}
		}
		logger.debug("运行结束!!");
		String curUserId=currentClientIO.getUserId();
		if(curUserId!=null){//有可能是客户端断开了连接
			logger.debug("会话结束,移除用户->"+currentClientIO.getUserId());
			ServerDataBuffer.onlineUsersMap.remove(currentClientIO.getUserId());
			//把当前上线客户端得IO从map中删除
			ServerDataBuffer.onlineUserIOCacheMap.remove(currentClientIO.getUserId());
		}
	}
	
	//客户端请求登录
	public void login(OnlineClientIOCache currentClientIO,Request request) throws IOException{
		User usrTmp=(User)request.getAttribute(User.TAG);
		
		//验证登录账号和密码
		User user=UserService.login(usrTmp.getId(), usrTmp.getPassword());
		Response resp=new Response();//创建响应对象返回到客户端
		if(null!=user){
			if(ServerDataBuffer.onlineUsersMap.containsKey(user.getId())){
				logger.debug("该用户已经在别处上线了!");
				//用户已经登录过了
				resp.setStatus(ResponseStatus.OK);
				resp.setType(ResponseType.LOGIN);
				resp.setData(ActionType.ACTION_TYPE_MSG, "用户已经在别处登录了!");
				currentClientIO.writeResp(resp.toString());
			}else{//正确登录
				//添加到在线用户中
				ServerDataBuffer.onlineUsersMap.put(user.getId(), user);
				currentClientIO.setUserId(user.getId());
				ServerDataBuffer.onlineUserIOCacheMap.put(user.getId(),
						currentClientIO);
				logger.debug("正确登录用户数-->"+ServerDataBuffer.onlineUsersMap.size()+" id="+user.getId());
				//设置在线用户---这里用于返回在线用户给客户端
				resp.setStatus(ResponseStatus.OK);
				resp.setData(User.TAG, user);
				// TODO
				//这里还要返回相关得在线用户到客户端
				
				logger.debug("服务器响应客户端-->"+resp.toString());
				//输出响应
				currentClientIO.writeResp(resp.toString());
				
				//通知其他用户有人已经上线了
				Response respAll=new Response();
				respAll.setType(ResponseType.LOGIN);
				respAll.setData(User.TAG, user);
				iteratorResponse(respAll);
				
				//把当前上线用户添加到OnlineUserTableModel--暂时不处理这里用于后台的监控显示
			}
		}else{//登录失败!
			logger.debug("登录失败-->"+resp.toString());
			
			resp.setStatus(ResponseStatus.OK);
			resp.setData(ActionType.ACTION_TYPE_MSG, "用户名或密码不正确!");

			currentClientIO.writeResp(resp.toString());
		}
		
	}
	
	/** 聊天---由指定用户的通道发送消息--不需要当前连接的通道 */
	public void chat(Request req) throws IOException {
		logger.debug("发送聊天信息!");
		//封装成消息对象
		Message msg=(Message)req.getAttribute(Message.TAG);
		Response resp=new Response();
		resp.setStatus(ResponseStatus.OK);
		resp.setType(ResponseType.CHAT);
		resp.setData(Message.TAG,msg);
		
		User receiveUser=msg.getReceiveUser();
		User sendUser=msg.getSendUser();
		
		logger.debug(receiveUser.getId()+"--->"+receiveUser.getPassword()+"--->"+sendUser.getId()+"--->"+sendUser.getPassword());
		//服务器可处理显示
		if(receiveUser.getId()!=null){//私聊
			logger.debug("私聊!");
			OnlineClientIOCache io=ServerDataBuffer.onlineUserIOCacheMap.get(receiveUser.getId());
			sendResponse(io,resp);//发送响应
		}else{
			logger.debug("群聊!");
			//群聊--除了自己以外的人都要发
			for(String id:ServerDataBuffer.onlineUserIOCacheMap.keySet()){
				if(sendUser.getId().equals(id)){
					continue;
				}
				sendResponse(ServerDataBuffer.onlineUserIOCacheMap.get(id), resp);
			}
			logger.debug("发送完成!");
		}
	}
	
	public boolean logout(OnlineClientIOCache oio,Request req) throws IOException {
		logger.debug(currentClientIO.getSocket().getInetAddress().getHostAddress() + ":"
				+ currentClientIO.getSocket().getPort() + "  断开连接...");
		User user=(User)req.getAttribute(User.TAG);
		if(null!=user){
			//从在线用户缓存中删除当前用户
			ServerDataBuffer.onlineUsersMap.remove(user.getId());
			//把当前上线客户端得IO从map中删除
			ServerDataBuffer.onlineUserIOCacheMap.remove(user.getId());
			
			//创建响应
			Response resp=new Response();
			resp.setType(ResponseType.LOGOUT);
			resp.setData(User.TAG, user);
			
			oio.writeResp(resp.toString());
			
			currentClientIO.getSocket().close();
			//从在线用户表中删除---这里是处理后台的显示
			//ServerDataBuffer.onlineUserIOCacheMap
			//通知其他在线客户端
			iteratorResponse(resp);
			//断开连接
		}
		return false;
	}
	
	//给所有在线客户都发送响应--通知所有用户新人上线了
	private void iteratorResponse(Response resp) throws IOException{
		logger.debug("开始通知所有用户!");
		for(OnlineClientIOCache oio:ServerDataBuffer.onlineUserIOCacheMap.values()){
			oio.writeResp(resp.toString());
			logger.debug("用户--->");
		}
		logger.debug("通知完成!");
	}
	
	//向指定客户端IO的输出流输出指定的响应
	private void sendResponse(OnlineClientIOCache oio,Response resp) throws IOException {
		if(oio!=null){
			logger.debug("开始发送!\n"+resp.toString());			
			oio.writeResp(resp.toString());
			logger.debug("发送完成!");
		}
	}
	
}
