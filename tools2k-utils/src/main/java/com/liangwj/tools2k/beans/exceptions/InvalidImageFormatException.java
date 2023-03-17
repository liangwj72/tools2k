package com.liangwj.tools2k.beans.exceptions;

/**
 * <pre>
 * 图片类型非法
 * </pre>
 * 
 * @author rock
 *  2016年8月3日
 */
public class InvalidImageFormatException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getErrorMsg() {
		return "图片文件格式错误";
	}

}
