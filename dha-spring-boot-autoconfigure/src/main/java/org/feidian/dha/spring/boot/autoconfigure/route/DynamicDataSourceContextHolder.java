package org.feidian.dha.spring.boot.autoconfigure.route;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-23 11:17
 **/

@Slf4j
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<DataSourceRoleEnum> THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER = new ThreadLocal<>();
    private static final AtomicReference<DataSourceRoleEnum> GLOBAL_DATA_SOURCE_CONTEXT_HOLDER = new AtomicReference<>();

    private static final ThreadLocal<String> type = new ThreadLocal<>();

    public static DataSourceRoleEnum getGlobalDataSourceRole() {
        log.info("get global data source role:{}", GLOBAL_DATA_SOURCE_CONTEXT_HOLDER.get().name());
        return GLOBAL_DATA_SOURCE_CONTEXT_HOLDER.get();
    }

    public static void setGlobalDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set global data source role:{}", dataSourceRoleEnum.name());
            GLOBAL_DATA_SOURCE_CONTEXT_HOLDER.set(dataSourceRoleEnum);
            return;
        }
        log.error("global data source role null");
    }

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

    public static void setThreadLocalDataSourceRole(DataSourceRoleEnum dataSourceRoleEnum) {
        if (dataSourceRoleEnum != null) {
            log.info("set thread local data source role:{}", dataSourceRoleEnum.name());
            // 如果走到主节点，不允许覆盖，这样会重新回到从节点

            THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.set(dataSourceRoleEnum);
            return;
        }
        log.error("thread local data source role null");
    }

    public static void threadLocalClear() {
        log.info("thread local data source {} is clear", THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.get());
        THREAD_LOCAL_DATA_SOURCE_CONTEXT_HOLDER.remove();
    }

}