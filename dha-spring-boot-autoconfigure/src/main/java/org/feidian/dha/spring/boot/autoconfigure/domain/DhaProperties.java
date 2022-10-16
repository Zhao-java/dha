package org.feidian.dha.spring.boot.autoconfigure.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-22 23:11
 **/

@Data
@ConfigurationProperties("dha.config")
public class DhaProperties {
    /**
     * 中心数据源配置
     */
    private DhaDataSourceProperties master;
    /**
     * 灾备数据源配置
     */
    private DhaDataSourceProperties standby;
    /**
     * 应用名称（唯一）
     */
    private String appName;
    /**
     * 当前节点地域
     */
    private String currentRegion;
}
