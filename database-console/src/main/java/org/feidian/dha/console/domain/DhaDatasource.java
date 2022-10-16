package org.feidian.dha.console.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zys
 * @date 2022/8/17 16:00
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DhaDatasource implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键 id
     */
    private Long id;
    /**
     * 数据源名称
     */
    private String datasourceName;
    /**
     * 数据库 ip 或域名
     */
    private String ip;
    /**
     * 数据库端口
     */
    private Integer port;
    /**
     * 数据库名
     */
    private String dbName;
    /**
     * 业务应用连接数据库用户名
     */
    private String username;
    /**
     * 业务应用连接数据库密码
     */
    private String password;
    /**
     * 权限
     */
    private String privilege;
}