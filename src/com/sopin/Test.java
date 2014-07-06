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
	
	//����json���л�
	public static void testAction(){
		User fromUsr=new User();
		fromUsr.setId("M_"+2L);
		fromUsr.setPassword("32434435fdghah����");
		
		User toUsr=new User();
		toUsr.setId("M_"+7897L);
		toUsr.setPassword("3454dfsd�ǵ���ȵ�");
		
		
		Message msg=new Message();
		msg.setSendUser(fromUsr);
		msg.setReceiveUser(toUsr);
		msg.setMessage("��Ϣ֪ͨ,sdfsd");
		msg.setSendTime(new Date());
		
		Request req=new Request();
		req.setAction(ActionType.ACTION_TYPE_CHAT);
		req.setAttribute(Message.TAG, msg);
		
		Response resp=new Response();
		resp.setStatus(ResponseStatus.OK);
		resp.setType(ResponseType.CHAT);
		
		resp.setData(Message.TAG,msg);
		
		System.out.println("����:"+req);
		System.out.println("��Ӧ:"+resp);
		
		
		//����������������Ӧ
		Request req2=new Request(req.toString());
		Response resp2=new Response(resp.toString());
		
		System.out.println("������--->"+req2.getAction());
		Message msg2=(Message)req2.getAttribute(Message.TAG);
		System.out.println("������Ϣ--->"+msg2);
		User fromUsr2=msg2.getSendUser();
		System.out.println("��Ϣ�����û�:"+fromUsr2+"\n"+fromUsr2.getId()+"--->"+fromUsr2.getPassword());
		User toUsr2=msg2.getReceiveUser();
		System.out.println("��Ϣ������:"+toUsr2+"\n"+toUsr2.getId()+"--->"+toUsr2.getPassword());
		System.out.println("��Ϣ����:"+msg2.getMessage());
		System.out.println("��Ϣʱ��:"+msg2.getSendTime().getTime());
		
		System.out.println("��ȡ������Ӧ״̬:"+resp2.getStatus());
		System.out.println("��Ӧ����:"+resp2.getType());
		Message respMsg=(Message)resp2.getData(Message.TAG);
		System.out.println("��Ӧ��Ϣ:"+respMsg.getMessage()+"--->"+respMsg.getSendTime().getTime());
		User respFromUsr=respMsg.getSendUser();
		User respToUsr=respMsg.getReceiveUser();
		
		System.out.println("��Ϣ����:"+respFromUsr.getId()+"--->"+respFromUsr.getPassword());
		System.out.println("��Ϣ����:"+respToUsr.getId()+"--->"+respToUsr.getPassword());
	}

}
