package com.liangwj.tools2k.utils.threadPool;

/**
 * 用于异步执行的任务
 * 
 * @author rock
 * 
 */
public interface IMyTask extends Runnable {

	/**
	 * 是否需要在线程池添加任务的时候检查唯一性，如果返回的是null就不需要,否则就按这个key作为主键来检查
	 * 
	 * @return
	 */
	String getUniqueId();

}
