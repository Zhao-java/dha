package org.feidian.dha.console.canal.yc;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;

import java.net.InetSocketAddress;

/**
 * 单机模式的测试例子
 *
 * @author yc 2013-4-15 下午04:19:20
 * @version 1.0.4
 */
public class SimpleCanalClientTest extends AbstractCanalClientTest {

    public SimpleCanalClientTest(String destination) {
        super(destination);
    }

    public static void main(String[] args) {
        // 根据ip，直接创建链接，无HA的功能
        String destination = "example";
//        String ip = AddressUtils.getHostIp();
//        System.out.println(ip);
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("101.35.139.41", 11111),
                destination,
                "root",
                "Yongan123456789!");

        final SimpleCanalClientTest clientTest = new SimpleCanalClientTest(destination);
        clientTest.setConnector(connector);
        clientTest.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                logger.info("## stop the canal client");
                clientTest.stop();
            } catch (Throwable e) {
                logger.warn("##something goes wrong when stopping canal:", e);
            } finally {
                logger.info("## canal client is down.");
            }
        }));
    }

}
