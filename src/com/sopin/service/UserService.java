package com.sopin.service;

import com.sopin.entity.User;

public class UserService {

	/** ��֤��¼---Ӧ�ò�ѯ���ݿ� */
	public static User login(String id, String password) {
		User usr = new User();
		usr.setId(id);
		usr.setPassword(password);
		return usr;
	}
}
