package com.liangwj.tools2k.utils.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 * 网络工具，例如获得本机IP
 * </pre>
 * 
 * @author rock 2015年5月15日
 */
public class NetUtil {
	/**
	 * 地址转换为字符串
	 * 
	 * @param inetAddress
	 * @return
	 */
	public static String address2String(InetAddress inetAddress) {
		if (inetAddress == null || inetAddress == null) {
			return null;
		}
		byte[] address = inetAddress.getAddress();
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < address.length; i++) {
			if (i > 0)
				res.append(".");
			res.append(String.valueOf(address[i] & 0xff));
		}
		return res.toString();
	}

	/**
	 * 获得本地IP
	 * 
	 * @return
	 */
	public static String getMyIP() {
		// 先获取所有ip
		List<String> ipList = findAllIpAddress();

		// 如果一个ip都找不到
		if (!ipList.isEmpty()) {
			// 如果至少有一个ip，就直接返回这个ip
			return ipList.get(0);
		} else {
			// 如果实在是一个ip都没有，就返回127
			return "127.0.0.1";
		}
	}

	/** 获取所有的ip地址，外网ip在前，内网在后 */
	public static List<String> findAllIpAddress() {
		LinkedList<String> list = new LinkedList<>();

		LinkedList<String> inetList = new LinkedList<>();

		try {
			// 获取所有的网卡
			Enumeration<NetworkInterface> networkInterfacesEnum = NetworkInterface.getNetworkInterfaces();
			while (networkInterfacesEnum.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfacesEnum.nextElement();

				// 获取这块网卡上的所有ip
				Enumeration<InetAddress> inetAddressEnum = networkInterface.getInetAddresses();
				while (inetAddressEnum.hasMoreElements()) {
					InetAddress inetAddress = inetAddressEnum.nextElement();

					if (!(inetAddress instanceof Inet6Address) && !inetAddress.isLoopbackAddress()) {
						// 排除inet6的地址, 排除127.0.0.1的地址
						String addrStr = address2String(inetAddress);

						if (addrStr.startsWith("192.168") || addrStr.startsWith("10.") || addrStr.startsWith("172")) {
							// 内网ip放到队伍后面
							if (addrStr.endsWith(".0.1")) {
								// 网关类的添加在后面
								inetList.addLast(addrStr);
							} else {
								// 非网关类的放在前面
								inetList.addFirst(addrStr);
							}
						} else {
							// 外网ip放在前面
							list.addFirst(addrStr);
						}
					}
				}
			}

		} catch (SocketException e) {
			throw new RuntimeException("getLocalHost ip address error", e);
		}

		// 将内网地址添加在后面
		list.addAll(inetList);

		return list;

	}

	/**
	 * 是否在同一子网中
	 * 
	 * @param ip1
	 * @param ip2
	 * @return
	 */
	public static boolean isSameSubnet(String ip1, String ip2) {
		if (!isVaildInetAddress(ip1) || !isVaildInetAddress(ip2))
			return false;

		String s1 = ip1.substring(0, ip1.lastIndexOf("."));
		String s2 = ip2.substring(0, ip2.lastIndexOf("."));

		return s1.equals(s2);
	}

	/**
	 * 判断是否合法的网络IP地址
	 * 
	 * @param ipStr
	 * @return
	 */
	public static boolean isVaildInetAddress(String ipStr) {
		if (ipStr == null || ipStr.trim().length() == 0)
			return false;

		String[] na = ipStr.split("\\x2e");
		if (na.length != 4)
			return false;

		for (int i = 0; i < 4; i++) {
			int addr = 0;
			try {
				addr = Integer.parseInt(na[i]);
			} catch (Exception e) {
				return false;
			}

			if (addr < 0 || addr > 254)
				return false;

			if (i == 0 && addr == 0)
				return false;

			if (i == 3 && addr == 0)
				return false;

		}

		return true;
	}

}
