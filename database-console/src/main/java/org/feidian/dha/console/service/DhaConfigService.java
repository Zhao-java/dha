package org.feidian.dha.console.service;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-17 15:13
 **/
@Service
@Slf4j
public class DhaConfigService {
    private static final String DATA_ID = "org.feidian.dha.properties";
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    private static final long TIME_OUT = 3000L;

    @NacosInjected
    private ConfigService nacosConfigService;

    @SneakyThrows
    synchronized public boolean pushConfig(DhaDataSource dataSource) {
        Map<String, DhaDataSource> map;

        String config = nacosConfigService.getConfig(DATA_ID, DEFAULT_GROUP, TIME_OUT);
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, DhaDataSource>> typeReference = new TypeReference<Map<String, DhaDataSource>>() {};
        if (StringUtils.isBlank(config)) {
            map = Collections.singletonMap(dataSource.getAppName(), dataSource);
        } else {
            map = objectMapper.readValue(config, typeReference);
            DhaDataSource oldDataSource = map.get(dataSource.getAppName());
            log.info("old datasource:{},new datasource:{}", oldDataSource, dataSource);
            map.put(dataSource.getAppName(), dataSource);
        }
        return nacosConfigService.publishConfig(DATA_ID, DEFAULT_GROUP,
            objectMapper.writeValueAsString(map), ConfigType.JSON.getType());
    }

}
