package org.feidian.dha.spring.boot.autoconfigure.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-22 21:46
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DhaDataSource {
    /**
     * 当前角色
     */
    private DataSourceRoleEnum currentRole;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 主库（读写）jdbc 连接串
     */
    private String masterDBConnection;

    /**
     * 备库（只读）jdbc 连接串
     */
    private String standbyDBConnection;

//    public DhaDataSource(String currentRole, String appName, String masterDBConnection, String standbyDBConnection) {
//        if(currentRole.equals("MASTER")){
//            this.currentRole = DataSourceRoleEnum.MASTER;
//        }else{
//            this.currentRole = DataSourceRoleEnum.STAND_BY;
//        }
//        this.appName = appName;
//        this.masterDBConnection = masterDBConnection;
//        this.standbyDBConnection = standbyDBConnection;
//    }
//
//    public DhaDataSource() {
//    }
}
