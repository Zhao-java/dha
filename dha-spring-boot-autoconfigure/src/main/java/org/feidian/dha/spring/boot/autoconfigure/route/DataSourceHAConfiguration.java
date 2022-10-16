package org.feidian.dha.spring.boot.autoconfigure.route;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.feidian.dha.spring.boot.autoconfigure.config.MyBatisInterceptor;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSource;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSourceProperties;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.nacos.api.common.Constants.DEFAULT_GROUP;
import static org.feidian.dha.spring.boot.autoconfigure.config.Constant.DATA_ID;


/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-23 10:07
 **/
@Configuration
@Slf4j
public class DataSourceHAConfiguration {

    @Resource
    private DhaProperties dhaProperties;

    @NacosInjected
    private ConfigService configService;

    @Bean(name = "standbyDataSource")
    public DataSource standbyDataSource() {
        DhaDataSourceProperties standby = dhaProperties.getStandby();
        log.info("standby == "+ standby);
        if (standby == null) {
            return null;
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        log.info("config properties:{}", dhaProperties.getStandby());
        dataSource.setUrl(standby.getJdbcUrl());
        dataSource.setUsername(standby.getUserName());
        dataSource.setPassword(standby.getPassword());
        log.info("standby datasource:{}", dataSource);
        return dataSource;
    }

    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() {
        DhaDataSourceProperties master = dhaProperties.getMaster();
        log.info("master ==  "+ master);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        log.info("config properties:{}", dhaProperties.getMaster());
        dataSource.setUrl(master.getJdbcUrl());
        dataSource.setUsername(master.getUserName());
        dataSource.setPassword(master.getPassword());
        log.info("master datasource:{}", dataSource);
        return dataSource;
    }

    @SneakyThrows
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
        @Qualifier("standbyDataSource") DataSource standbyDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        log.info("param:master:{},standby:{}", masterDataSource, standbyDataSource);
        if (standbyDataSource == null) {
            standbyDataSource = masterDataSource;
        }
        HashMap<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceRoleEnum.MASTER, masterDataSource);
        targetDataSources.put(DataSourceRoleEnum.STAND_BY, standbyDataSource);
        String config = null;
        config = configService.getConfig(DATA_ID, DEFAULT_GROUP, 1000L);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, DhaDataSource> map = null;
        map = objectMapper.readValue(config, new TypeReference<Map<String, DhaDataSource>>() {});
        log.info("dhaProperties===="+dhaProperties);
        DhaDataSource appDataSource = map.get(dhaProperties.getAppName());
        DataSource defaultDataSource;
        log.info("init appDataSource:{}", appDataSource);
        if (appDataSource != null && DataSourceRoleEnum.STAND_BY.equals(appDataSource.getCurrentRole())) {
            log.info("init dha standby");
            defaultDataSource = standbyDataSource;
            DynamicDataSourceContextHolder.setGlobalDataSourceRole(DataSourceRoleEnum.STAND_BY);
        } else {
            log.info("init dha master");
            defaultDataSource = masterDataSource;
            DynamicDataSourceContextHolder.setGlobalDataSourceRole(DataSourceRoleEnum.MASTER);
        }
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
        routingDataSource.setTargetDataSources(targetDataSources);
        return routingDataSource;
    }

    @Bean
    SqlSessionFactoryBeanCustomizer sqlSessionFactoryBeanCustomizer(
        @Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return factoryBean -> {
            factoryBean.setDataSource(dynamicDataSource);
        };
    }

    @Bean
    Interceptor mybatisReadWriteSplit() {
        return new MyBatisInterceptor();
    }

}