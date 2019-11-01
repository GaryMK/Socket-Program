package com.company;

public class HexConverter {
    public static String main(String ipAddr, String subnetMask) {
        HexConverter obj = new HexConverter();
        long a = obj.ipToLong2(ipAddr), b = obj.ipToLong2(subnetMask);
        System.out.println("网络号：" + obj.longToIp2((a & b)));
        return obj.longToIp2((a & b));
    }

    //ip地址转换为数字
    public long ipToLong1(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        }
        return result;
    }

    //ip地址转换为数字（位运算）
    public long ipToLong2(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << (i * 8);
        }
        return result;
    }

    //逆向转换
    public String longToIp1(long ip) {
        return ((ip >> 24) & 0xFF) +
                "." + ((ip >> 16) & 0xFF) +
                "." + ((ip >> 8) & 0xFF) +
                "." + (ip & 0xFF);
    }

    //逆向转换（位运算）
    public String longToIp2(long ip) {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(ip & 0xff));
            if (i < 3) {
                sb.insert(0, '.');
            }
            ip = ip >> 8;
        }
        return sb.toString();
    }

    public String ipToBinary(String ipAddress) {
        return  Long.toBinaryString(ipToLong2(ipAddress));
    }
}
