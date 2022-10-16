package org.feidian.dha.spring.boot.autoconfigure.canal.zys;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @program: summer
 * @description: canal服务类
 * @author: zys
 * @create: 2022-08-20 12:36
 **/


//@Component
@Slf4j
//@Configuration
public class Canal {

    //sql队列
    private final Queue<String> SQL_QUEUE = new ConcurrentLinkedQueue<>();

    Connection con = null;

    /**
     * canal入库方法
     */
    public void run() {

        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("localhost",
                11111), "example", "root", "123456!");
        int batchSize = 1000;
        try {
            connector.connect();
            log.info("连接成功");
            connector.subscribe("DHA\\..*");
            connector.rollback();
            try {
                while (true) {
                    //尝试从master那边拉去数据batchSize条记录，有多少取多少
                    Message message = connector.getWithoutAck(batchSize);
                   // log.info("获取主数据库的数据：{}",message);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        Thread.sleep(1000);
                    } else {
                        dataHandle(message.getEntries());
                    }
                    connector.ack(batchId);
                  //  log.info("SQL_QUEUE.size(): {}" ,SQL_QUEUE.size());
                    //当队列里面堆积的sql大于一定数值的时候就模拟执行
                    if (SQL_QUEUE.size() >= 1) {
                        executeQueueSql();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } finally {
            connector.disconnect();
        }
    }

    /**
     * 模拟执行队列里面的sql语句
     */
    public void executeQueueSql() throws ClassNotFoundException {
        int size = SQL_QUEUE.size();
        for (int i = 0; i < size; i++) {
            String sql = SQL_QUEUE.poll();
            log.info("sql :{} " + sql);
            this.execute(sql);
        }
    }

    /**
     * 数据处理
     *
     * @param entrys
     */
    private void dataHandle(List<Entry> entrys) throws InvalidProtocolBufferException {
        log.info("dataHandle init");
        for (Entry entry : entrys) {
            if (EntryType.ROWDATA == entry.getEntryType()) {
                RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
                EventType eventType = rowChange.getEventType();
                if (eventType == EventType.DELETE) {
                    saveDeleteSql(entry);
                } else if (eventType == EventType.UPDATE) {
                    saveUpdateSql(entry);
                } else if (eventType == EventType.INSERT) {
                    saveInsertSql(entry);
                }
            }
        }
    }

    /**
     * 保存更新语句
     *
     * @param entry
     */
    private void saveUpdateSql(Entry entry) {
        log.info("saveUpdateSql init");
        try {
            RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
            String sql = rowChange.getSql();
            log.info("sql : {}",sql);
            System.out.println(sql);
            SQL_QUEUE.add(sql);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存删除语句
     *
     * @param entry
     */
    private void saveDeleteSql(Entry entry) {
        log.info("saveDeleteSql init");
        try {
            RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
            String sql = rowChange.getSql();
            log.info("sql : {}",sql);
            SQL_QUEUE.add(sql);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存插入语句
     *
     * @param entry
     */
    private void saveInsertSql(Entry entry) {
        log.info("saveInsertSql init");
        try {
            RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
            log.info("rowChange:{}",rowChange);
            String sql = rowChange.getSql();
            log.info("sql : {}",sql);
            SQL_QUEUE.add(sql);
//            List<RowData> rowDatasList = rowChange.getRowDatasList();
//            log.info("rowDatasList:{}",rowDatasList);
//            for (RowData rowData : rowDatasList) {
//                List<Column> columnList = rowData.getAfterColumnsList();
//                StringBuffer sql = new StringBuffer("insert into " + entry.getHeader().getTableName() + " (");
//                for (int i = 0; i < columnList.size(); i++) {
//                    sql.append(columnList.get(i).getName());
//                    if (i != columnList.size() - 1) {
//                        sql.append(",");
//                    }
//                }
//                sql.append(") VALUES (");
//                for (int i = 0; i < columnList.size(); i++) {
//                    sql.append("'" + columnList.get(i).getValue() + "'");
//                    if (i != columnList.size() - 1) {
//                        sql.append(",");
//                    }
//                }
//                sql.append(")");
//                log.info("SQL_QUEUE.add(sql.toString()): {}" ,sql.toString());
//
//                SQL_QUEUE.add(sql.toString());
//            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 入库
     * @param sql
     */
    public void execute(String sql) throws ClassNotFoundException {
        log.info("execute init");
        Class.forName("com.mysql.cj.jdbc.Driver") ;
        try {
            if(null == sql) return;
            con = DriverManager.getConnection("jdbc:mysql://120.55.190.249:3306/DHA?characterEncoding=UTF-8" , "root" , "20020806zhang" ) ;
            QueryRunner qr = new QueryRunner();
            int row = qr.execute(con, sql);
            System.out.println("update: "+ row);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
    }


}
