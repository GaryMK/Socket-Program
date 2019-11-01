package com.company;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class GetSubnetMask {
    /**
     * 获取绑定了指定IP地址的网络接口的子网掩码
     */
    public static int getMask() {
        try {
            // 获得本机的所有网络接口
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();

                //筛选出无线WIFI的网络接口
                if (netInterface.getName().startsWith("wlan")) {
                    // 获取此网络接口的全部或部分InterfaceAddresses所组成的列表
                    List<InterfaceAddress> addresses = netInterface.getInterfaceAddresses();
                    if (addresses.size() > 0) {
                        int mask = addresses.get(0).getNetworkPrefixLength(); // 子网掩码的二进制1的个数
                        System.out.println("WIFI网络接口的掩码位：" + mask);
                        return mask;
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error when getting host ip address" + e.getMessage());
        }
        return 0;
    }

    public static String getSubnetMask(int mask) {
        StringBuilder maskStr = new StringBuilder();
        int[] maskIp = new int[4];
        for (int i = 0; i < maskIp.length; i++) {
            maskIp[i] = (mask >= 8) ? 255 : (mask > 0 ? (255 - ((int)Math.pow(2,8 - (double)mask) - 1)) : 0);
            mask -= 8;
            maskStr.append(maskIp[i]);
            if (i < maskIp.length - 1) {
                maskStr.append(".");
            }
        }
        System.out.println("WIFI网卡接口子网掩码：" + maskStr);
        return maskStr.toString();
    }
}
