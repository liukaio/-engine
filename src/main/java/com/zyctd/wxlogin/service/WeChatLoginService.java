package com.zyctd.wxlogin.service;


import com.zyctd.wxlogin.pojo.UserInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WeChatLoginService {

    /**
     * 用户同意授权，获取code
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    void getCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    /**
     * 授权登录，获取用户信息以及openId
     * @param request
     * @param response
     * @return userInfo:用户信息
     * @throws IOException
     */
    UserInfo login(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
