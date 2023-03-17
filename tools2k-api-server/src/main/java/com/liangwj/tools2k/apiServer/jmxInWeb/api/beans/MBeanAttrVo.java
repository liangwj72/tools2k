package com.liangwj.tools2k.apiServer.jmxInWeb.api.beans;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.liangwj.tools2k.utils.other.LogUtil;
import com.liangwj.tools2k.utils.other.OpenTypeUtil;

/**
 * <pre>
 * 对 MBeanAttributeInfo 的包装
 * </pre>
 * 
 * @author<a href="https://github.com/liangwj72">Alex (梁韦江)</a> 2015年10月15日
 */
public class MBeanAttrVo implements Comparable<MBeanAttrVo> {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MBeanAttrVo.class);

	private final MBeanAttributeInfo info;

	private boolean openType;

	private Object attributeValue;

	private final boolean inputable;

	private String value;

	public MBeanAttrVo(MBeanAttributeInfo mbeanAttributeInfo) {
		super();
		this.info = mbeanAttributeInfo;

		this.inputable = OpenTypeUtil.isOpenType(info.getType());
	}

	public String getAccessMode() {
		StringBuffer sb = new StringBuffer();
		if (this.info.isReadable()) {
			sb.append("R");
		}

		if (this.info.isWritable()) {
			sb.append("W");
		}

		return sb.toString();
	}

	public String toDebugString() {

		StringBuffer sb = new StringBuffer();

		// read write mode
		sb.append("(");
		if (this.info.isReadable()) {
			sb.append("R");
		}

		if (this.info.isWritable()) {
			sb.append("W");
		}
		sb.append(")");

		sb.append(String.format("\tdesc=%s", this.info.getDescription()));

		sb.append(String.format("\ttype=%s", this.info.getType()));

		sb.append(String.format("\tvalue=%s", this.getValue()));

		return sb.toString();
	}

	public String getValue() {
		if (this.value == null) {
			if (this.attributeValue == null || this.info == null) {
				this.value = "";
			} else {
				if (this.openType) {
					this.value = OpenTypeUtil.toString(this.attributeValue, this.info.getType());
				} else {
					// 如果不是简单类型，就变成json格式
					this.value = JSON.toJSONString(attributeValue,
							SerializerFeature.PrettyFormat,
							SerializerFeature.UseSingleQuotes,
							SerializerFeature.MapSortField);
				}
			}
		}
		return this.value;
	}

	/**
	 * 是否json格式的返回值，如果能找到回车，就算是
	 * 
	 * @return
	 */
	public boolean isJsonValue() {
		return !this.openType;
	}

	/**
	 * info中的太长了，所以做多一个方法
	 * 
	 * @return
	 */
	public String getDesc() {
		return this.info.getDescription();
	}

	public MBeanAttributeInfo getInfo() {
		return info;
	}

	/**
	 * 是否可通过界面输入修改值
	 * 
	 * @return
	 */
	public boolean isInputable() {
		return this.inputable;
	}

	protected void setValueFromMBeanServer(ObjectName objectName, MBeanServer server) throws JMException {
		if (this.info.isReadable()) {
			try {
			attributeValue = server.getAttribute(objectName, this.info.getName());
			} catch (Throwable ex) {
				log.warn("在获取 {} 属性 {} 的值 时，发生了错误", objectName, this.info.getName());
				LogUtil.traceError(log, ex);
			}
			if (attributeValue != null) {
				this.openType = OpenTypeUtil.isOpenType(attributeValue.getClass());
			} else {
				this.openType = true;
			}
		}
	}

	@Override
	public int compareTo(MBeanAttrVo o) {
		return this.info.getName().compareToIgnoreCase(o.info.getName());
	}

}
