package com.liangwj.tools2k.utils.other;

import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;

import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.SimpleApiException;

/**
 * <pre>
 * 根据时间生成校验码
 * </pre>
 * 
 * @author rock
 * 
 */
public class TimeChecksumUtil {

	private final String key;

	/** 默认有效期为10分钟 */
	private long expireLimit = TimeUnit.MINUTES.toMillis(10);

	public TimeChecksumUtil(String key) {
		Assert.hasText(key, "key不能为空");
		this.key = key;
	}

	public long getExpireLimit() {
		return expireLimit;
	}

	public void setExpireLimit(long expireLimit) {
		this.expireLimit = expireLimit;
	}

	/** 生成校验码 */
	public String createCheckSum(long time) {
		StringBuilder sb = new StringBuilder();
		sb.append(time);
		sb.append('\t');
		sb.append(this.key);
		return EncryptUtil.md5(sb.toString());
	}

	/**
	 * 检查这个校验码是否有效
	 * 
	 * @param time
	 *            校验码生成的时间
	 * @param checkSum
	 *            要检查的校验码
	 * @throws BaseApiException
	 */
	public void verifyCheckSum(long time, String checkSum) throws BaseApiException {
		long diff = System.currentTimeMillis() - time;
		if (diff > this.expireLimit) {
			throw new SimpleApiException("该校验码超时了");
		}

		String right = this.createCheckSum(time);
		if (!right.equals(checkSum)) {
			throw new SimpleApiException("该校验码非法");
		}
	}

}
