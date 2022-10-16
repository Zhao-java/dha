package org.feidian.dha.spring.boot.autoconfigure.route;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-23 15:13
 **/

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        DataSourceRoleEnum threadLocalDataSourceRoleEnum = DynamicDataSourceContextHolder.getThreadLocalDataSourceRole();
        if (threadLocalDataSourceRoleEnum != null) {
            log.info("{}",RegionRoleContextHolder.getCurrentRegionRole());
            log.info("determineCurrentLookupKey:{}",threadLocalDataSourceRoleEnum);

            return threadLocalDataSourceRoleEnum;
        }

        log.error("get thread local data source role null");
        return DynamicDataSourceContextHolder.getGlobalDataSourceRole();
    }
}
