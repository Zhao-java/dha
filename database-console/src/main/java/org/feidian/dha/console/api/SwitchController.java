package org.feidian.dha.console.api;

import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.console.response.Response;
import org.feidian.dha.console.response.Responses;
import org.feidian.dha.console.service.DhaConfigService;
import org.feidian.dha.spring.boot.autoconfigure.domain.DataSourceRoleEnum;
import org.feidian.dha.spring.boot.autoconfigure.domain.DhaDataSource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-16 20:15
 **/
@RestController
@RequestMapping("/switch")
@Slf4j
public class SwitchController {
    @Resource
    private DhaConfigService dhaConfigService;

    @GetMapping()
    public Response getDataSource() {
        
        return Responses.successResponse();
    }

    @PostMapping
    public Response switchDataSource(@RequestParam("appName") String appName,
        @RequestParam("role") DataSourceRoleEnum role) {
        log.info("switch app:{},role:{}", appName, role);
        DhaDataSource datasource = DhaDataSource.builder()
            .appName(appName)
            .currentRole(role).build();
        boolean success = dhaConfigService.pushConfig(datasource);
        log.info("push result:{}", success);
        if (!success) {
            return Responses.errorResponse();
        }
        return Responses.successResponse();
    }

}
