package org.feidian.dha.console.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.config.DatasourceMapConverter;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-17 14:11
 **/

@Component
@Slf4j
public class DhaServiceListener {
    private static final String DATA_ID = "org.feidian.dha.properties";
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    @NacosInjected
    private ConfigService configService;

    @NacosConfigListener(dataId = DATA_ID, type = ConfigType.JSON, converter = DatasourceMapConverter.class)
    public void getRoleAndSetDataSource(Map<String, DhaDataSource> dataSourceMap) {
        DhaDataSource dataSource = dataSourceMap.get("short_url");
        log.info("json data:{},map:{}", dataSource, dataSourceMap);
    }

}