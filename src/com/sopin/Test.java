package com.sopin;

import java.util.Date;
import java.util.Map;

import com.sopin.entity.ActionType;
import com.sopin.entity.Message;
import com.sopin.entity.Request;
import com.sopin.entity.Response;
import com.sopin.entity.ResponseStatus;
import com.sopin.entity.ResponseType;
import com.sopin.entity.User;

public class Test {
	
	public static void main(String[] args) {
			
	}
	
	//测试json序列化
	public static void testAction(){
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
		
		Response resp=new Response();
		resp.setStatus(ResponseStatus.OK);
		resp.setType(ResponseType.CHAT);
		
		resp.setData(Message.TAG,msg);
		
		System.out.println("请求:"+req);
		System.out.println("响应:"+resp);
		
		
		//解析服务器请求及响应
		Request req2=new Request(req.toString());
		Response resp2=new Response(resp.toString());
		
		System.out.println("请求动作--->"+req2.getAction());
		Message msg2=(Message)req2.getAttribute(Message.TAG);
		System.out.println("请求消息--->"+msg2);
		User fromUsr2=msg2.getSendUser();
		System.out.println("消息来自用户:"+fromUsr2+"\n"+fromUsr2.getId()+"--->"+fromUsr2.getPassword());
		User toUsr2=msg2.getReceiveUser();
		System.out.println("消息接收者:"+toUsr2+"\n"+toUsr2.getId()+"--->"+toUsr2.getPassword());
		System.out.println("消息内容:"+msg2.getMessage());
		System.out.println("消息时间:"+msg2.getSendTime().getTime());
		
		System.out.println("读取到得响应状态:"+resp2.getStatus());
		System.out.println("响应类型:"+resp2.getType());
		Message respMsg=(Message)resp2.getData(Message.TAG);
		System.out.println("响应消息:"+respMsg.getMessage()+"--->"+respMsg.getSendTime().getTime());
		User respFromUsr=respMsg.getSendUser();
		User respToUsr=respMsg.getReceiveUser();
		
		System.out.println("消息来自:"+respFromUsr.getId()+"--->"+respFromUsr.getPassword());
		System.out.println("消息接收:"+respToUsr.getId()+"--->"+respToUsr.getPassword());
	}

}
