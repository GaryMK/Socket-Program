package com.company;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetRealLocalIP {
    /**
     * 获取本地真正的IP地址，即获得有线或者无线WIFI地址
     * 过滤虚拟机、蓝牙等地址。
     */
    public static String getRealIP() {
        try {
            // 获得本机的所有网络接口
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();

                //筛选出无线WIFI的网络接口
                if (netInterface.getName().startsWith("wlan")) {
                    // 获得与该网络接口绑定的 IP 地址，一般只有一个
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (addr instanceof Inet4Address) { // 只关心 IPv4 地址
                            System.out.println("WIFI网卡接口名称：" + netInterface.getName());
                            System.out.println("WIFI网卡接口地址：" + addr.getHostAddress());
                            //System.out.println();
                            return  addr.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error when getting host ip address" + e.getMessage());
        }
        return null;
    }
}
