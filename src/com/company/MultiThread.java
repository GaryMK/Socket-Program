package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.SimpleFormatter;

public class MultiThread {

    private List<Map<String, String>> ipList; // 需验证的IP+端口集合
    private int threadNum = 0; // 线程数

    public MultiThread() {

    }

    public MultiThread(List<Map<String, String>> ipList, int threadNum) {
        this.ipList = ipList;
        this.threadNum = threadNum;
    }

    public void startConnect() {
        // 创建一个线程池，多个线程同步执行
        final CountDownLatch cdAnswer = new CountDownLatch(threadNum);
        ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < ipList.size(); i++) {
            pool.execute(new ConnectRunner(ipList.get(i), cdAnswer));
        }

        try {
            System.out.println("线程等待开始=" + cdAnswer.getCount());
            cdAnswer.await();
            System.out.println("线程等待结束=" + cdAnswer.getCount());
        } catch (InterruptedException e) {
            System.out.println("线程" + e);
            e.printStackTrace();
        }

        pool.shutdown();
        try {
            while (!pool.isTerminated()) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ConnectRunner implements Runnable {
        private String host = null;
        private int port = 0;
        private Map<String, String> hostList; // 需验证的IP
        private CountDownLatch cdAnswer;

        public ConnectRunner(Map<String, String> hostList, CountDownLatch cdAnswer) {
            this.hostList = hostList;
            this.cdAnswer = cdAnswer;
        }

        Socket s = new Socket();

        @Override
        public void run() {
            host = hostList.get("addr");
            port = Integer.valueOf(hostList.get("port"));
            SocketAddress add = new InetSocketAddress(host, port);
            try {
                s.connect(add,1000); //超时3秒
                System.out.println("IP:" + host + ":" + port + "正常");
            } catch (IOException e) {
                System.out.println("IP:" + host + ":" + port + "超时");
            } finally {
                try {
                    s.close();
                    cdAnswer.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
