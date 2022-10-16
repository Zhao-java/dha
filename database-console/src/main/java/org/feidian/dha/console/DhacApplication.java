package org.feidian.dha.console;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-14 9:34
 **/
@SpringBootApplication
@EnableNacosConfig
@NacosPropertySource(dataId = "org.feidian.dha.properties", autoRefreshed = true)
public class DhacApplication {


    public static void main(String[] args) {
        SpringApplication.run(DhacApplication.class, args);
    }


}
