package com.sopin.server;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import com.sopin.entity.User;
import com.sopin.service.OnlineClientIOCache;

/**
 * ���������ݻ���
 * @author lance
 *
 */
public class ServerDataBuffer {
	//�������׽���
	public static ServerSocket serverSocket;
	//�����û��Ļ���
	public static Map<String,OnlineClientIOCache> onlineUserIOCacheMap;
	//�����û�����
	public static Map<String,User> onlineUsersMap;
	
	static{
		onlineUserIOCacheMap=new ConcurrentSkipListMap<String,OnlineClientIOCache>();
		onlineUsersMap=new ConcurrentSkipListMap<String, User>();
	}
	
}
