package com.sopin.entity;

/**
 * 请求的动作类型---目前发送都需要通过动作来告诉服务器需要作什么处理
 * @author lance
 *
 */
public class ActionType {
	//数据流结束符--12个字符--一般在平时使用中无意义的字符
	public static final String EOF="#$*&~@~@&*$#";
	//定义数据编码解码字符集
	public static final String CHARSET="UTF-8";
	
	// TODO 用于请求,响应的数据存取
	//取出发送的数据的key
	public final static String ACTION_DATA="action_data";
	//取出响应类型key
	public final static String ACTION_RESP_TYPE="action_resp_type";
	//取出响应的状态
	public final static String ACTION_RESP_STATUS="action_resp_status";
	
	//取出动作的key
	public final static String ACTION_TYPE_KEY="action_type_key";
	//定义的动作类型
	public final static String ACTION_TYPE_LOGIN = "login";
	public final static String ACTION_TYPE_LOGOUT = "logout";
	public final static String ACTION_TYPE_CHAT = "chat";
	
	//取出消息提示
	public final static String ACTION_TYPE_MSG = "msg";
	
	
	
}
