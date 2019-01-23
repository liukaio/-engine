package com.zyctd.wxlogin.service;

import com.zyctd.wxlogin.pojo.RedPack;
import com.zyctd.wxlogin.pojo.SendResult;

import javax.servlet.http.HttpServletRequest;

public interface SendRedPackService {

    SendResult sendRedPack(HttpServletRequest request, RedPack redPack) throws Exception;
}
