package com.sopin.server;

import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import com.sopin.entity.User;
import com.sopin.service.OnlineClientIOCache;

/**
 * 服务器数据缓存
 * @author lance
 *
 */
public class ServerDataBuffer {
	//服务器套接字
	public static ServerSocket serverSocket;
	//在线用户的缓存
	public static Map<String,OnlineClientIOCache> onlineUserIOCacheMap;
	//在线用户缓存
	public static Map<String,User> onlineUsersMap;
	
	static{
		onlineUserIOCacheMap=new ConcurrentSkipListMap<String,OnlineClientIOCache>();
		onlineUsersMap=new ConcurrentSkipListMap<String, User>();
	}
	
}
