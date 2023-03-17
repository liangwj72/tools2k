
package com.liangwj.tools2k.apiServer.dict;

import java.util.ArrayList;
import java.util.List;

public class DictXml {

	protected List<DictXml.DictXmlRow> dictXmlRow;
	protected List<DictXml.DictAttachmentRow> dictAttachmentRow;

	public List<DictXml.DictXmlRow> getDictXmlRow() {
		if (dictXmlRow == null) {
			dictXmlRow = new ArrayList<DictXml.DictXmlRow>();
		}
		return this.dictXmlRow;
	}

	public List<DictXml.DictAttachmentRow> getDictAttachmentRow() {
		if (dictAttachmentRow == null) {
			dictAttachmentRow = new ArrayList<DictXml.DictAttachmentRow>();
		}
		return this.dictAttachmentRow;
	}

	public static class DictAttachmentRow {

		protected String pathPrefix;
		protected String extName;
		protected String memo;
		protected String key;
		protected boolean imageFile;
		protected int imageWidth;
		protected int imageHeight;

		/**
		 * 获取pathPrefix属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getPathPrefix() {
			return pathPrefix;
		}

		/**
		 * 设置pathPrefix属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setPathPrefix(String value) {
			this.pathPrefix = value;
		}

		/**
		 * 获取extName属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getExtName() {
			return extName;
		}

		/**
		 * 设置extName属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setExtName(String value) {
			this.extName = value;
		}

		/**
		 * 获取memo属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getMemo() {
			return memo;
		}

		/**
		 * 设置memo属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setMemo(String value) {
			this.memo = value;
		}

		/**
		 * 获取key属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getKey() {
			return key;
		}

		/**
		 * 设置key属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setKey(String value) {
			this.key = value;
		}

		/**
		 * 获取imageFile属性的值。
		 * 
		 */
		public boolean isImageFile() {
			return imageFile;
		}

		/**
		 * 设置imageFile属性的值。
		 * 
		 */
		public void setImageFile(boolean value) {
			this.imageFile = value;
		}

		/**
		 * 获取imageWidth属性的值。
		 * 
		 */
		public int getImageWidth() {
			return imageWidth;
		}

		/**
		 * 设置imageWidth属性的值。
		 * 
		 */
		public void setImageWidth(int value) {
			this.imageWidth = value;
		}

		/**
		 * 获取imageHeight属性的值。
		 * 
		 */
		public int getImageHeight() {
			return imageHeight;
		}

		/**
		 * 设置imageHeight属性的值。
		 * 
		 */
		public void setImageHeight(int value) {
			this.imageHeight = value;
		}

	}

	public static class DictXmlRow {

		protected String value;
		protected String memo;
		protected boolean todo;
		protected int usedCount;
		protected boolean html;
		protected int type;
		protected String key;

		/**
		 * 获取value属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 设置value属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * 获取memo属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getMemo() {
			return memo;
		}

		/**
		 * 设置memo属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setMemo(String value) {
			this.memo = value;
		}

		/**
		 * 获取todo属性的值。
		 * 
		 */
		public boolean isTodo() {
			return todo;
		}

		/**
		 * 设置todo属性的值。
		 * 
		 */
		public void setTodo(boolean value) {
			this.todo = value;
		}

		/**
		 * 获取usedCount属性的值。
		 * 
		 */
		public int getUsedCount() {
			return usedCount;
		}

		/**
		 * 设置usedCount属性的值。
		 * 
		 */
		public void setUsedCount(int value) {
			this.usedCount = value;
		}

		/**
		 * 获取html属性的值。
		 * 
		 */
		public boolean isHtml() {
			return html;
		}

		/**
		 * 设置html属性的值。
		 * 
		 */
		public void setHtml(boolean value) {
			this.html = value;
		}

		/**
		 * 获取type属性的值。
		 * 
		 */
		public int getType() {
			return type;
		}

		/**
		 * 设置type属性的值。
		 * 
		 */
		public void setType(int value) {
			this.type = value;
		}

		/**
		 * 获取key属性的值。
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getKey() {
			return key;
		}

		/**
		 * 设置key属性的值。
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setKey(String value) {
			this.key = value;
		}

	}

}
