package org.feidian.dha.spring.boot.autoconfigure.config;

import com.alibaba.nacos.api.config.convert.NacosConfigConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSource;

import java.util.Map;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-20 15:13
 **/

@Slf4j
public class DatasourceMapConverter implements NacosConfigConverter<Map<String, DhaDataSource>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canConvert(Class<Map<String, DhaDataSource>> targetClass) {

        boolean canConvert = objectMapper.canSerialize(targetClass);
        if (canConvert) {
            log.info("can convert");
        } else {
            log.error("can not convert");
            System.out.println("can not convert");
        }
        return canConvert;
    }

    @SneakyThrows
    @Override
    public Map<String, DhaDataSource> convert(String config) {
        return objectMapper.readValue(config, new TypeReference<Map<String, DhaDataSource>>() {});
    }

}
