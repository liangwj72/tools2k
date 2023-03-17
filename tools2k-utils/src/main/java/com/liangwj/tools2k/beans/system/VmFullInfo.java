package com.liangwj.tools2k.beans.system;

import com.liangwj.tools2k.annotation.api.AComment;

public class VmFullInfo extends VmInfo {

	private static final long serialVersionUID = 350357583922719983L;

	@AComment("类路径")
	private java.lang.String classPath;
	@AComment("引导类路径")
	private java.lang.String bootClassPath;
	@AComment("库路径")
	private java.lang.String libraryPath;

	public java.lang.String getClassPath() {
		return classPath;
	}

	public void setClassPath(java.lang.String classPath) {
		this.classPath = classPath;
	}

	public java.lang.String getBootClassPath() {
		return bootClassPath;
	}

	public void setBootClassPath(java.lang.String bootClassPath) {
		this.bootClassPath = bootClassPath;
	}

	public java.lang.String getLibraryPath() {
		return libraryPath;
	}

	public void setLibraryPath(java.lang.String libraryPath) {
		this.libraryPath = libraryPath;
	}

}
