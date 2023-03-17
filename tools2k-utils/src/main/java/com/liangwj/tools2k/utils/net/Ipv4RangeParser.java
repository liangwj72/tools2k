package com.liangwj.tools2k.utils.net;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.liangwj.tools2k.beans.others.IFilter;

public class Ipv4RangeParser {

	private final boolean debugMode = false;

	public class RangeBean {
		private final int begin; // 第一个ip
		private final int end; // 最后一个ip (包含）
		private final int total;
		private String beginStr;
		private String endStr;

		public RangeBean(int begin, int end) {
			this.begin = begin;
			this.end = end;
			this.total = this.end - this.begin + 1;

			if (debugMode) {
				try {
					this.beginStr = Ipv4Util.intToIpv4(begin).getHostAddress();
					this.endStr = Ipv4Util.intToIpv4(this.end).getHostAddress();
				} catch (Throwable e) {
				}
			}
		}

		@Override
		public String toString() {
			return String.format("IP范围 %s - %s ，合计 %d 个", this.beginStr, this.endStr, this.total);
		}

	}

	/** 不要结尾是255的 */
	IFilter<Integer> no255Filter = new IFilter<Integer>() {

		@Override
		public boolean isMatch(Integer address) {
			int last = (address % 256) & 0xff;
			return last != 255;
		}

	};

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Ipv4RangeParser.class);

	/** 单ip匹配 223.104.93.0 */
	private static final Pattern ipReg = Pattern
			.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");

	/** 掩码匹配 223.104.93.0/24 */
	private static final Pattern ipCidrReg = Pattern
			.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\/(\\d{1,2})");

	/**
	 * CIDR格式:192.168.123.17/28
	 * 
	 * @throws UnknownHostException
	 * 
	 */
	protected RangeBean parserCidr(String line) throws UnknownHostException {
		if (!StringUtils.hasText(line)) {
			return null;
		}

		Matcher m = ipCidrReg.matcher(line);
		if (m.find()) {
			// line是 223.104.93.0/24 你好啊

			String ipStr = m.group(1); // 1是ip: 223.104.93.0
			int bitLen = Integer.parseInt(m.group(0).substring(ipStr.length() + 1)); // 0是223.104.93.0/24

			Inet4Address addr = Ipv4Util.getAddrByName(ipStr);
			// 例如 掩码24的掩码值= 0xff，就是 2的(32-24)次方-1
			int mask = (1 << 32 - bitLen) - 1;

			int start = addr.hashCode() & (0xffffffff - mask); // 第一个地址
			int end = addr.hashCode() + mask - 1; // 最后一个地址
			RangeBean bean = new RangeBean(start, end);

			if (this.debugMode) {
				log.debug("分析[{}] 得到CIDR格式地址范围：{}-{}, 合计 {} 个地址", line,
						Ipv4Util.intToIpv4(start), Ipv4Util.intToIpv4(end),
						bean.total);
			}

			return bean;

		}
		return null;
	}

	public List<Inet4Address> parserLine(String line, String resourceName, int index, IFilter<Integer> filter)
			throws UnknownHostException {

		Assert.notNull(filter, "filter must not be null");

		RangeBean bean;

		// 先看看能否匹配 cidr格式
		bean = this.parserCidr(line);
		if (bean == null) {
			// 如果没有 cidr格式，在看看是否有两个ip
			bean = this.parserRange(line);
		}

		if (bean != null) {

			// 计算总数
			if (bean.total > 65536) {
				log.warn("警告 文件 {} , 第 {} 行 ip范围过大 {}-{} 合计{}个ip", resourceName, index,

						bean.beginStr, bean.endStr,
						bean.total);
			}

			if (bean.total > 20000000) {
				log.warn("文件 {} , 第 {} 行 ip范围过大，忽略", resourceName, index);
			}

			List<Inet4Address> list = new LinkedList<>();

			for (int address = bean.begin; address <= bean.end; address++) {
				if (filter.isMatch(address)) {
					// 只要过滤器匹配的
					Inet4Address ip = Ipv4Util.intToIpv4(address);
					list.add(ip);
				}
			}
			return list;
		}

		return null;
	}

	/**
	 * 从资源文件的一行中分析ip范围
	 */
	public List<Inet4Address> parserLine(String line, String resourceName, int index) throws UnknownHostException {
		return this.parserLine(line, resourceName, index, this.no255Filter);
	}

	/**
	 * 单ip或者多ip
	 * 
	 * @throws UnknownHostException
	 */
	protected RangeBean parserRange(String line) throws UnknownHostException {
		if (!StringUtils.hasText(line)) {
			return null;
		}

		Matcher m = ipReg.matcher(line);
		if (m.find()) {
			// 先找到第一个ip
			String beginStr = m.group(1);
			String endStr = beginStr;

			if (m.find()) {
				// 在找第二个ip，如果能找到，说明是一个范围，否则就是单个ip
				endStr = m.group(1);
			}

			// 获得范围
			int begin = Ipv4Util.getAddrByName(beginStr).hashCode();
			int end = Ipv4Util.getAddrByName(endStr).hashCode();

			return new RangeBean(begin, end);
		}

		return null;
	}

}
