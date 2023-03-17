package com.liangwj.tools2k.beans.system;

import com.liangwj.tools2k.annotation.api.AComment;

@AComment("类加载的信息")
public class ClassLoadingInfo {
	@AComment("当前加载的类的总数")
	private int loadedClassCount;
	@AComment("总共加载的类的总数")
	private long totalLoadedClassCount;
	@AComment("已经卸载的类的总数")
	private long unloadedClassCount;

	public int getLoadedClassCount() {
		return loadedClassCount;
	}

	public void setLoadedClassCount(int loadedClassCount) {
		this.loadedClassCount = loadedClassCount;
	}

	public long getTotalLoadedClassCount() {
		return totalLoadedClassCount;
	}

	public void setTotalLoadedClassCount(long totalLoadedClassCount) {
		this.totalLoadedClassCount = totalLoadedClassCount;
	}

	public long getUnloadedClassCount() {
		return unloadedClassCount;
	}

	public void setUnloadedClassCount(long unloadedClassCount) {
		this.unloadedClassCount = unloadedClassCount;
	}

}
