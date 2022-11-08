# DHA

## DHA

借鉴思想
[读多写少业务场景实践](https://help.aliyun.com/document_detail/197293.html?utm_content=g_1000230851&amp;spm=5176.20966629.toubu.3.f2991ddcpxxvD1#title-gk5-hgi-a2l )

## 使用文档

[文档说明](https://github.com/Zhao-java/dha/blob/master/docs/DHA%E6%95%B0%E6%8D%AE%E5%BA%93Starter.md)

[备用说明文档](https://www.yuque.com/docs/share/cab5a749-bd56-4ab3-9e1f-e8b08daa639b?#Wc86D )

## 说点什么？

首先我目前本人只是一名计科的大三学生，也不是什么大佬，只是在大学期间接触的计算机。我写这个项目只是单纯出于兴趣，比不上Spring、Mybatis这些成熟的框架，而且我还使用了这些框架的功能，但是只要有一点创新或者我在这个过程中思考过，我认为这就足够了

## 代码示例

### 判断读写操作逻辑

```java
// 查询全局主备节点状态
DataSourceRoleEnum master = DynamicDataSourceContextHolder.getGlobalDataSourceRole();
DataSourceRoleEnum standBy = Arrays.stream(DataSourceRoleEnum.values())
    .filter(item -> !item.equals(master)).findFirst()
    .orElseThrow(() -> new RuntimeException("error get data source role"));

dataSourceRole = canBeStandby ? standBy : master;
log.info("can be standby:{}", canBeStandby);
// 如果可以路由到备节点，即 读 操作，再判断同地域优先
// 此操作好处在于，第一减少跨地域读，提高读的性能；第二如果备库地域故障，路由到备库读，那么所有读操作都会失败
if (canBeStandby) {
    RegionRoleEnum currentRegionRole = RegionRoleContextHolder.getCurrentRegionRole();
    // 如果当前 region 和 master region 相同，则本地读
    if (currentRegionRole.name().equals(master.name())) {
        dataSourceRole = master;
        // 如果当前 region 和 master region 不同，还是本地读
    } else if (currentRegionRole.name().equals(standBy.name())) {
        dataSourceRole = standBy;
    } else {
        log.error("current region role is null");
    }
}
DynamicDataSourceContextHolder.setThreadLocalDataSourceRole(dataSourceRole);
log.info("global data source:{},threadLocal data source:{}", master, dataSourceRole);
```


### 设置当前线程需要访问的数据库

```java
public static DataSourceRoleEnum getThreadLocalDataSourceRole() {
    DataSourceRoleEnum dataSourceRoleEnum = THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.get();
    if (dataSourceRoleEnum != null) {
        log.info("get thread local data source role:{}", dataSourceRoleEnum.name());
        /**
         * 用过之后立刻删除，防止线程复用，导致最终核心线程都路由到 master 节点
         * 主要解决 MyBatisInterceptor中 如果有是插入操作则将会让当前线程的数据库操作走向MASTER，操作完毕之后若不清楚线程元素
         *  会导致所有的操作都流向MASTER
         */
        THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.remove();
        return dataSourceRoleEnum;
    }
    return null;
}
```

### 数据库Canal同步

```java
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
            }catch (InterruptedException e) {
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
```

**_以上只是简单的示例，详细使用请务必查看使用文档_**
