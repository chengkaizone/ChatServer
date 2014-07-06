package com.sopin.service;

import com.sopin.entity.User;

public class UserService {

	/** 验证登录---应该查询数据库 */
	public static User login(String id, String password) {
		User usr = new User();
		usr.setId(id);
		usr.setPassword(password);
		return usr;
	}
}
