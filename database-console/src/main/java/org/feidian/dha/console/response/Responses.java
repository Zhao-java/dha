package org.feidian.dha.console.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-17 10:23
 **/

@Data
@AllArgsConstructor
public class Responses {

    /**
     * @param operationMessage 操作执行后的信息
     * @return 操作执行后的信息
     */
    public static Response successResponse(String operationMessage) {
        Response response = new Response();
        Meta meta = new Meta(0, null);
        response.setMeta(meta);
        Map<String, Object> data = new HashMap<>(1);
        data.put("operation_message", operationMessage);
        response.setData(data);
        return response;
    }

    /**
     * @return 操作成功返回默认信息
     */
    public static Response successResponse() {
        Response response = new Response();
        Meta meta = new Meta(0, null);
        response.setMeta(meta);
        Map<String, Object> data = new HashMap<>(1);
        data.put("operation_message", "操作成功");
        response.setData(data);
        return response;
    }

    /**
     * @param data 操作执行的数据信息
     * @return 返回操作的数据信息
     */
    public static Response successResponse(Map data) {
        Response response = new Response();
        Meta meta = new Meta(0, null);
        response.setMeta(meta);
        response.setData(data);
        return response;
    }

    /**
     * @param errorMessage 操作的状态码和错误信息
     * @return 操作的状态码和错误信息
     */
    public static Response errorResponse(String errorMessage) {
        Response response = new Response();
        Meta meta = new Meta(1, errorMessage);
        response.setMeta(meta);
        response.setData(null);
        return response;
    }

    public static Response errorResponse() {
        Response response = new Response();
        Meta meta = new Meta(1, "操作失败");
        response.setMeta(meta);
        response.setData(null);
        return response;
    }
}
