package com.liangwj.tools2k.utils.net;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;

import com.liangwj.tools2k.utils.other.NumberUtils;

public class Ipv4Util {

	/**
	 * 根据名字找到ipv4地址
	 */
	public static Inet4Address getAddrByName(String host) throws UnknownHostException {
		final InetAddress addr = InetAddress.getByName(host);
		if (addr instanceof Inet4Address) {
			return (Inet4Address) addr;
		} else {
			throw new UnknownHostException("无法找到 " + host + "的 ipv4地址");
		}
	}

	/**
	 * 获得域名的所有的ipv4的地址
	 */
	public static List<Inet4Address> getAllAddrByName(String host) throws UnknownHostException {
		Assert.notNull(host, "host must not be null");

		InetAddress[] addrs = InetAddress.getAllByName(host);

		List<Inet4Address> list = new LinkedList<Inet4Address>();
		for (InetAddress addr : addrs) {
			if (addr instanceof Inet4Address) {
				list.add((Inet4Address) addr);
			}
		}
		return list;
	}

	/** 根据byte数组生成ipv4地址 */
	public static Inet4Address getByAddress(byte[] bytes) throws UnknownHostException {
		final InetAddress addr = InetAddress.getByAddress(bytes);
		if (addr instanceof Inet4Address) {
			return (Inet4Address) addr;
		} else {
			throw new UnknownHostException();
		}
	}

	/** 比较两个ipv4地址的大小，用于排序 */
	public static int compare(Inet4Address a1, Inet4Address a2) {
		Assert.notNull(a1, "addr must not be null");
		Assert.notNull(a2, "addr must not be null");

		byte[] b1 = a1.getAddress();
		byte[] b2 = a2.getAddress();

		int res = 0;
		for (int i = 0; i < 4; i++) {
			res = NumberUtils.byteToInt(b1[i]) - NumberUtils.byteToInt(b2[i]);
			if (res != 0) {
				// 随便一个不等于0，就可以退出了
				break;
			}
		}
		return res;
	}

	public static Inet4Address intToIpv4(int address) throws UnknownHostException {
		byte[] addr = new byte[4];

		addr[0] = (byte) ((address >>> 24) & 0xFF);
		addr[1] = (byte) ((address >>> 16) & 0xFF);
		addr[2] = (byte) ((address >>> 8) & 0xFF);
		addr[3] = (byte) (address & 0xFF);
		return (Inet4Address) Inet4Address.getByAddress(addr);
	}

	public static long ipv4ToLong(Inet4Address addr) {
		Assert.notNull(addr, "addr must not be null");
		return NumberUtils.bytesToLong(addr.getAddress(), 0, 4);
	}

	public static String ip4ByteToString(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return null;
		}

		try {
			InetAddress addr = InetAddress.getByAddress(bytes);
			return addr.getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static Inet4Address getByAddressNoEx(byte[] bytes) {
		try {
			if (bytes != null && bytes.length == 4) {
				InetAddress addr = InetAddress.getByAddress(bytes);
				if (addr instanceof Inet4Address) {
					return (Inet4Address) addr;
				}
			}
		} catch (UnknownHostException e) {
		}
		return null;
	}

	/** 是否是内网ip */
	public static boolean isLanIp(String ip) {
		Assert.notNull(ip, "ip must not be null");

		return ip.startsWith("192.168")
				|| ip.startsWith("127.0")
				|| ip.startsWith("0.0")
				|| ip.startsWith("169.254.")
				|| ip.startsWith("172.16")
				|| ip.startsWith("10.");

	}

	/** 是否是内网ip */
	public static boolean isLanIp(Inet4Address addr) {
		int code = addr.hashCode();
		return (code >= 2130706432 && code <= 2147483647) // 127.
				|| (code >= -1062731776 && code <= -1062666241) // 192.168.
				|| (code >= -1408237568 && code <= -1407188993) // 172.16
				|| (code >= 167772160 && code <= 184549375) // 10.
				|| (code >= -1442971648 && code <= -1442906113) // 169.254
		;
	}

	/** 在联网状态寻找默认路由网卡的IP */
	public static InetAddress findMyAddr() throws IOException {
		InetAddress addr = null;
		Socket s = null;
		try {
			s = new Socket("www.baidu.com", 80);
			addr = s.getLocalAddress();
		} finally {
			if (s != null) {
				s.close();
			}
		}
		return addr;
	}

}
