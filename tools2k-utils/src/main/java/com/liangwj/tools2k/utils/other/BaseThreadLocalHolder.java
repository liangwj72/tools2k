package com.liangwj.tools2k.utils.other;

/**
 * <pre>
 * 用线程保存对象的基类
 * </pre>
 * 
 * @author rock
 * 
 */
public abstract class BaseThreadLocalHolder<T> {

	private final ThreadLocal<T> threadLocal = new ThreadLocal<>();

	protected abstract T createObj();

	public T getObj() {
		T obj = this.threadLocal.get();
		if (obj == null) {
			obj = this.createObj();
			this.threadLocal.set(obj);
		}
		return obj;
	}

}
