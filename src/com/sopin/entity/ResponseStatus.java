package com.sopin.entity;

/**
 * 响应状态
 * @author lance
 *
 */
public class ResponseStatus {

	/** 请求处理成功 */
	public static final int OK=0;
	/** 服务器内部出错 */
	public static final int SERVER_ERROR=1;
	/** 请求资源未找到 */
	public static final int NOT_FOUND=2;
	/** 错误请求 */
	public static final int BAD_REQUEST=3;
	
}
