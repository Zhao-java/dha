package org.feidian.dha.console.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-17 10:23
 **/

@AllArgsConstructor
@Data
public class Meta {
    private Integer code;
    private String message;
}
