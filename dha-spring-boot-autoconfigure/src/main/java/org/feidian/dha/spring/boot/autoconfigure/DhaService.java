package org.feidian.dha.spring.boot.autoconfigure;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.config.DatasourceMapConverter;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSource;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.feidian.dha.spring.boot.autoconfigure.route.DynamicDataSourceContextHolder;

import java.util.Map;

import static org.feidian.dha.spring.boot.autoconfigure.config.Constant.DATA_ID;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-21 8:11
 **/

@Slf4j
public class DhaService {
    private final DhaProperties dhaProperties;

    public DhaService(DhaProperties dhaProperties) {
        this.dhaProperties = dhaProperties;
    }

    @NacosConfigListener(dataId = DATA_ID, converter = DatasourceMapConverter.class)
    public void getRoleAndSetDataSource(Map<String, DhaDataSource> dataSourceMap) {
        log.info("map:{}", dataSourceMap);
        DhaDataSource dhaDataSource = dataSourceMap.get(dhaProperties.getAppName());
        if (dhaDataSource != null && dhaDataSource.getCurrentRole() != null) {
            log.info("data source changed,new data:{}", dhaDataSource);

            DynamicDataSourceContextHolder.setGlobalDataSourceRole(dhaDataSource.getCurrentRole());
            DynamicDataSourceContextHolder.threadLocalClear();
            return;
        }
        log.error("data source changed null map:{}", dataSourceMap);
    }
}
