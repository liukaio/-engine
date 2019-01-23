package com.zyctd.wxlogin.controller;

import com.alibaba.fastjson.JSONObject;
import com.zyctd.wxlogin.cache.CacheService;
import com.zyctd.wxlogin.pojo.UserInfo;
import com.zyctd.wxlogin.service.WeChatLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/wx")
public class WXLoginContoller {

    @Autowired
    CacheService cacheService;

    @Autowired
    WeChatLoginService weChatLoginService;

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public String wxlogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (null == code || "".equals(code)){
            weChatLoginService.getCode(request,response);
            return "";
        }
        UserInfo userInfo = weChatLoginService.login(request,response);
        System.out.println("openID: " + userInfo.getOpenid());
        cacheService.set(userInfo.getOpenid(),JSONObject.toJSONString(userInfo));

        return "/load.html";
    }
}
