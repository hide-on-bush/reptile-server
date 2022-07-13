package com.xsx.jsoup.controller;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public abstract class BaseController {
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    @Getter
    private HttpServletResponse response;

    @Getter
    private HttpServletRequest request;


    @Autowired
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }



    @SuppressWarnings("unchecked")
    public <T> T getRequestAttr(String key) {
        return (T)request.getAttribute(key);
    }

    public void setRequestAttr(String key, Object obj) {
        request.setAttribute(key, obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T getSessionAttr(String key) {
        if(request.getSession() != null) {
            return (T)request.getSession().getAttribute(key);
        }
        return null;
    }

    public void setSessionAttr(String key, Object obj) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(key, obj);
        }
    }

    protected HttpSession getSession() {
        return request.getSession();
    }

    protected HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    public void removeSession(String key) {
        request.getSession(true).removeAttribute(key);
    }

    public String getRequestParameter(String name) {
        return request.getParameter(name);
    }

    protected void setResponseHeader(String userInfo, HttpServletResponse response) {
        response.addHeader("Authorization", userInfo);
    }
}
