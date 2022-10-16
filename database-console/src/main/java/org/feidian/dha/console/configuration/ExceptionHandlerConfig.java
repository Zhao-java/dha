package org.feidian.dha.console.configuration;

import org.feidian.dha.console.response.Response;
import org.feidian.dha.console.response.Responses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-18 18:23
 **/
@ControllerAdvice
@RestController
public class ExceptionHandlerConfig {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public Response defaultErrorHandler(Exception e, HttpServletRequest request) {
        logger.info("请求地址：" + request.getRequestURL());
        logger.error("异常信息：", e);
        logger.error("============");
        return Responses.errorResponse("异常类型:" + e.getClass() + "\n异常信息:" + e.getMessage());
    }
}
