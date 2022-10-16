package org.feidian.dha.spring.boot.autoconfigure.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.feidian.dha.spring.boot.autoconfigure.DhaService;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaProperties;
import org.feidian.dha.spring.boot.autoconfigure.domain.RegionRoleEnum;
import org.feidian.dha.spring.boot.autoconfigure.route.RegionRoleContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: dha-spring-boot-autoconfigure
 * @description:
 * @author: zys
 * @create: 2022-08-20 17:38
 **/

@Configuration
@ConditionalOnClass(DhaService.class)
@EnableConfigurationProperties(DhaProperties.class)
@Slf4j
public class DhaAutoConfiguration {
    private final DhaProperties dhaProperties;

    public DhaAutoConfiguration(DhaProperties dhaProperties) {this.dhaProperties = dhaProperties;}

    @Bean
    @ConditionalOnMissingBean(DhaService.class)
    @ConditionalOnProperty(prefix = "dha.config", value = "enable", havingValue = "true")
    DhaService dhaService() {
        log.info("dha properties:{}", dhaProperties);
        String masterRegion = dhaProperties.getMaster().getRegion();
        String standbyRegion = dhaProperties.getStandby().getRegion();
        String currentRegion = dhaProperties.getCurrentRegion();
        if (StringUtils.isAnyBlank(standbyRegion, masterRegion, currentRegion)) {
            throw new RuntimeException("region message cannot be empty");
        }
        if (masterRegion.equals(standbyRegion)) {
            throw new RuntimeException("Master region and standby region cannot be the same");
        }
        if (!masterRegion.equals(currentRegion) && !standbyRegion.equals(currentRegion)) {
            throw new RuntimeException("current region must be the same as the master or standby region");
        }
        RegionRoleEnum currentRegionRole;
        if (dhaProperties.getCurrentRegion().equals(masterRegion)) {
            currentRegionRole = RegionRoleEnum.MASTER;
        } else {
            currentRegionRole = RegionRoleEnum.STAND_BY;
        }
        RegionRoleContextHolder.setCurrentRegionRole(currentRegionRole);
        return new DhaService(dhaProperties);
    }
}