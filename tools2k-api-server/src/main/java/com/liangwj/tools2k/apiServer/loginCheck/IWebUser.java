package com.liangwj.tools2k.apiServer.loginCheck;

import java.io.Serializable;

/**
 * web项目中，要从session检查的用户对象的基类
 * 
 * @author rock
 * 
 */
public interface IWebUser extends Serializable {

	/**
	 * 用于检查权限
	 * 
	 * @param optId
	 * @return
	 */
	boolean checkRights(String optId);

	/**
	 * 返回用户登录账号
	 * 
	 */
	String getAccount();

	/**
	 * 返回用户显示用的名字
	 * 
	 */
	String getName();

	/**
	 * 返回用于生成加密后的密码，不需要是明文
	 * 
	 */
	String getEncryptedPassword();

}