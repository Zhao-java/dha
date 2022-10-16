package org.feidian.dha.spring.boot.autoconfigure.domain;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-22 20:13
 **/

public enum DataSourceRoleEnum {
    /**
     * 主数据源(默认读写)
     */
    MASTER,
    /**
     * 备数据源(默认只读)
     */
    STAND_BY,

}
