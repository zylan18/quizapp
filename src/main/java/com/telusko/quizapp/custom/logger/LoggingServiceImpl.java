package com.telusko.quizapp.custom.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;

@Service
public class LoggingServiceImpl implements LoggingService{

    Logger logger = LoggerFactory.getLogger("LoggingServiceImpl");

    @Override
    public void displayReq(HttpServletRequest request, Object body) {
        StringBuilder reqMessage = new StringBuilder();
        Map<String,String> parameters = getParameters(request);

        reqMessage.append("REQUEST ");
        reqMessage.append("method = [").append(request.getMethod()).append("]");
        reqMessage.append(" path = [").append(request.getRequestURI()).append("] ");

        if(!parameters.isEmpty()) {
            reqMessage.append(" parameters = [").append(parameters).append("] ");
        }
        Map<String,String> headers = getHeadersRequest(request);
        
        if(!headers.isEmpty()) {
        	reqMessage.append(" RequestHeaders = [").append(headers).append("]");
        }
        System.out.println(headers.get("authorization"));
        StringBuilder requestString=new StringBuilder();
        requestString.append(headers.get("authorization")).append(request.getRequestURI());
        

        if(!Objects.isNull(body)) {
            reqMessage.append(" body = [").append(body).append("]");
        }

        logger.info("log Request: {}", reqMessage);
        System.out.println();
    }

    @Override
    public void displayResp(HttpServletRequest request, HttpServletResponse response, Object body, MethodParameter returnType) {
        StringBuilder respMessage = new StringBuilder();
        Map<String,String> headers = getHeaders(response);
        respMessage.append("RESPONSE ");
        respMessage.append(" method = [").append(request.getMethod()).append("]");
        if(!headers.isEmpty()) {
            respMessage.append(" ResponseHeaders = [").append(headers).append("]");
        }
        respMessage.append(" responseBody = [").append(body).append("]");

        logger.info("logResponse: {}",respMessage);
        System.out.println(respMessage);
    }

    private Map<String,String> getHeaders(HttpServletResponse response) {
        Map<String,String> headers = new HashMap<>();
        Collection<String> headerMap = response.getHeaderNames();
        for(String str : headerMap) {
            headers.put(str,response.getHeader(str));
        }
        return headers;
    }

    private Map<String,String> getParameters(HttpServletRequest request) {
        Map<String,String> parameters = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            parameters.put(paramName,paramValue);
        }
        return parameters;
    }
    
    private Map<String,String> getHeadersRequest(HttpServletRequest request) {
        Map<String,String> headers = new HashMap<>();
        Enumeration<String> headerMapEnum = request.getHeaderNames();
        headerMapEnum.asIterator().forEachRemaining(str->headers.put(str,request.getHeader(str)));
//        for(String str : headerMap) {
//            headers.put(str,request.getHeader(str));
//        }
        return headers;
    }


}