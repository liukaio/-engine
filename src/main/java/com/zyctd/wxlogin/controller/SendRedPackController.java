package com.zyctd.wxlogin.controller;

import com.zyctd.wxlogin.pojo.RedPack;
import com.zyctd.wxlogin.pojo.SendResult;
import com.zyctd.wxlogin.service.SendRedPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@Controller
@RestController
@RequestMapping("/redpack")
public class SendRedPackController {

    @Autowired
    SendRedPackService sendRedPackService;

    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public SendResult sendRedPack(HttpServletRequest request, @RequestBody List<RedPack> redPacks){

        SendResult sendResult = null;
        for (RedPack redPack : redPacks) {
            try {
                sendResult = sendRedPackService.sendRedPack(request, redPack);
                if ("SUCCESS".equals(sendResult.getReturn_code())){
                    //将发送成功的记录写入数据库
                }else {
                    //红包发送失败的处理，重发？
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sendResult;
    }
}
