/*
 * Copyright &copy; <a href="http://www.zsteel.cc">zsteel</a> All rights reserved.
 */

package com.platform.framework.exception;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理<br>
 * 不必在Controller中对异常进行处理，抛出即可，由此异常解析器统一控制。<br>
 * ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。<br>
 * 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。<br>
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 *
 * @author lufengc
 * @date 2016/1/22 9:48
 */
public class HandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

    private String defaultErrorView;

    public String getDefaultErrorView() {
        return defaultErrorView;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    /**
     * 处理异常
     *
     * @param request       HttpServletRequest
     * @param response      HttpServletResponse
     * @param handlerMethod HandlerMethod
     * @param exception     异常信息
     * @return ModelAndView
     */
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
                                                           HandlerMethod handlerMethod, Exception exception) {
        if (handlerMethod == null) {
            return null;
        }
        Method method = handlerMethod.getMethod();
        if (method == null) {
            return null;
        }
        ModelAndView returnValue = super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
        ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
        if (responseBodyAnn != null) {
            try {
                ResponseStatus responseStatusAnn = AnnotationUtils.findAnnotation(method, ResponseStatus.class);
                if (responseStatusAnn != null) {
                    HttpStatus responseStatus = responseStatusAnn.value();
                    String reason = responseStatusAnn.reason();
                    if (!StringUtils.hasText(reason)) {
                        response.setStatus(responseStatus.value());
                    } else {
                        try {
                            response.sendError(responseStatus.value(), reason);
                        } catch (IOException ignored) {
                        }
                    }
                }
                return handleResponseBody(returnValue, request, response);
            } catch (Exception e) {
                return null;
            }
        }

        if (returnValue.getViewName() == null) {
            returnValue.setViewName(defaultErrorView);
        }
        return returnValue;

    }

    /**
     * 处理被@ResponseBody注解的方法异常
     *
     * @param returnValue ModelAndView
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @return ModelAndView
     * @throws ServletException
     * @throws IOException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private ModelAndView handleResponseBody(ModelAndView returnValue, HttpServletRequest request,
                                            HttpServletResponse response) throws ServletException, IOException {
        Map value = returnValue.getModelMap();
        HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        Class<?> returnValueType = value.getClass();
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        if (messageConverters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : messageConverters) {
                    if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                        messageConverter.write(value, acceptedMediaType, outputMessage);
                        return new ModelAndView();
                    }
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
        }
        return null;
    }
}
