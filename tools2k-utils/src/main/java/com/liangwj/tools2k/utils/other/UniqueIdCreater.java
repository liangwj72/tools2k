package com.liangwj.tools2k.utils.other;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * 生成唯一id
 * 
 * 使用系统启动时间作为id的开始值
 * 
 * 每获取一次，就+1
 * </pre>
 * 
 * @author rock
 * 
 */
public class UniqueIdCreater {

	private static final UniqueIdCreater instance = new UniqueIdCreater();

	/** 系统启动时间 */
	private long startTime = System.nanoTime();

	/** 计数器 */
	private AtomicLong counter = new AtomicLong();

	private long create() {
		long i = this.counter.getAndIncrement();
		return this.startTime + i;
	}

	/** 获取long型的唯一id */
	public static long createIdInLong() {
		return instance.create();
	}

	/** 获取String型的唯一id */
	public static String createIdInStr() {
		return String.valueOf(createIdInLong());
	}

	/** 获取uuid格式的唯一id */
	public static String createUUID() {
		String str = UUID.randomUUID().toString();
		return str.replace("-", "");
	}

}
