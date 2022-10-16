package org.feidian.dha.spring.boot.autoconfigure.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-22 22:13
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DhaDataSourceProperties {
    /**
     * jdbc 连接串
     */
    private String jdbcUrl;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * region 信息
     */
    private String region;
}
