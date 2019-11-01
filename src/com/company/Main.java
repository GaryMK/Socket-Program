package com.company;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        String ipAddr,subnetMask,networkNum,hostNum;
        int mask;
        long num;
        HexConverter tool = new HexConverter();
        ipAddr = GetRealLocalIP.getRealIP();
        mask = GetSubnetMask.getMask();
        subnetMask = GetSubnetMask.getSubnetMask(mask);
        networkNum = tool.main(ipAddr, subnetMask);
        num = (long)Math.pow(2, 32-mask);
        System.out.println("局域网内主机个数：" + num);
        System.out.println("扫描起始地址：" + networkNum);
        System.out.println("扫描结束地址：" + tool.longToIp2(tool.ipToLong2(networkNum) + num -1));

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HH时mm分ss秒"); //格式化时间
        Date date = new Date(currentTime);

        System.out.println("多线程测试：Connect开始时间：" + formatter.format(date));
        List<Map<String, String>> allIP = new ArrayList<Map<String, String>>();

        long networkNumLong = tool.ipToLong2(networkNum),finalNetwork = tool.ipToLong2(networkNum) + num -1;
        for (; networkNumLong <= finalNetwork; networkNumLong++)
        {
            Map<String, String> m = new HashMap<String, String>();
            m.put("addr", tool.longToIp2(networkNumLong));
            m.put("port", "135");
            allIP.add(m);
        }
        MultiThread batchConnectThread = new MultiThread(allIP, allIP.size());
        //多线程测试Connect IP+端口时间
        batchConnectThread.startConnect();

        long current = System.currentTimeMillis();
        Date end = new Date(current);
        System.out.println("多线程测试：Connect结束时间：" + formatter.format(end));
    }
}
