package com.liangwj.tools2k.utils.other;

/**
 * 
 * <pre>
 * 和数字相关的工具
 * </pre>
 *
 */
public class NumberUtils {

	/** 16进制方式显示byte数组 */
	public static String printHexString(byte[] b) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			hex = hex.replaceAll(":", " ");
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex + " ");
		}
		return sb.toString();
	}

	/** byte转int */
	public static int byteToInt(byte b) {
		return b & 0xff;
	}

	public static int shortToInt(short s) {
		return s & 0xffff;
	}

	public static int byteToInt(byte b, int shift) {
		int i = b & 0xff;
		return i << shift;
	}

	public static long bytesToLong(byte[] bytes, int start, int len) {
		if (bytes == null || start < 0 || start + len > bytes.length) {
			return 0;
		}

		long res = 0;
		for (int i = 0; i < len; i++) {
			long v = byteToInt(bytes[start + i]);
			res += v << ((len - i - 1) * 8);
		}
		return res;
	}

	public static int bytesToInt(byte[] bytes, int start, int len) {
		if (bytes == null || start < 0 || start + len > bytes.length) {
			return 0;
		}

		int res=0;
		for (int i = 0; i < len; i++) {
			int v = byteToInt(bytes[start + i]);
			res += v << ((len - i - 1) * 8);
		}
		return res;
	}

}
