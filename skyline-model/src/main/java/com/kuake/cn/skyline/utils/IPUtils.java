package com.kuake.cn.skyline.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 描述：获取本地ip工具类
 * @author: kuake.cn
 * @create: 2021-03-28 14:27
 **/
@Slf4j
public class IPUtils {
    public static String getHost() throws SocketException {
        try {
            Enumeration<NetworkInterface> allNetWorkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetWorkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = allNetWorkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address
                            && !inetAddress.isLoopbackAddress()
                            && !inetAddress.getHostAddress().contains(":")) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            return null;
        }catch (Throwable e) {
            log.error("Get ip error:{}.", e.getMessage());
            throw e;
        }
    }
}
