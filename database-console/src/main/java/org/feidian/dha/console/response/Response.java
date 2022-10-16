package org.feidian.dha.console.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-17 10:23
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response {
    private Meta meta;
    private Map<String, Object> data;
}
