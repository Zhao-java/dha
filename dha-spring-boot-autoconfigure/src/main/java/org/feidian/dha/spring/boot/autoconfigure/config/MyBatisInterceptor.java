package org.feidian.dha.spring.boot.autoconfigure.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.feidian.dha.spring.boot.autoconfigure.domain.RegionRoleEnum;
import org.feidian.dha.spring.boot.autoconfigure.route.DynamicDataSourceContextHolder;
import org.feidian.dha.spring.boot.autoconfigure.route.RegionRoleContextHolder;

import java.util.Arrays;
import java.util.Properties;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-20 19:27
 **/

@Slf4j
@Intercepts({
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,
            BoundSql.class}),
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MyBatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.warn("into mybatis interceptor: {}", invocation);
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement)args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        log.info("type:{}", sqlCommandType);
        DataSourceRoleEnum dataSourceRole = null;
        try {
            boolean canBeStandby = SqlCommandType.SELECT == sqlCommandType;
            log.info("canBeStandby:{}",canBeStandby);
            // ??????????????????????????????
            DataSourceRoleEnum master = DynamicDataSourceContextHolder.getGlobalDataSourceRole();
            DataSourceRoleEnum standBy = Arrays.stream(DataSourceRoleEnum.values())
                .filter(item -> !item.equals(master)).findFirst()
                .orElseThrow(() -> new RuntimeException("error get data source role"));

            dataSourceRole = canBeStandby ? standBy : master;
            log.info("can be standby:{}", canBeStandby);
            // ???????????????????????????????????? ??? ?????????????????????????????????
            // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (canBeStandby) {
                RegionRoleEnum currentRegionRole = RegionRoleContextHolder.getCurrentRegionRole();
                // ???????????? region ??? master region ?????????????????????
                if (currentRegionRole.name().equals(master.name())) {
                    dataSourceRole = master;
                    // ???????????? region ??? master region ????????????????????????
                } else if (currentRegionRole.name().equals(standBy.name())) {
                    dataSourceRole = standBy;
                } else {
                    log.error("current region role is null");
                }
            }
            DynamicDataSourceContextHolder.setThreadLocalDataSourceRole(dataSourceRole);
            log.info("global data source:{},threadLocal data source:{}", master, dataSourceRole);
            return invocation.proceed();
        } finally {
            if (dataSourceRole == null) {
                log.error("set threadLocal data source error,data source role is null");
            }
            log.info("mybatis interceptor clear thread local data source");
            DynamicDataSourceContextHolder.threadLocalClear();
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
