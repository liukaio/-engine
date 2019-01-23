package com.zyctd.wxlogin.service;

import com.zyctd.wxlogin.config.WxConfig;
import com.zyctd.wxlogin.pojo.RedPack;
import com.zyctd.wxlogin.pojo.SendResult;
import com.zyctd.wxlogin.util.WeXinUtil;
import com.zyctd.wxlogin.util.XmlUtil;
import okhttp3.*;
import okhttp3.internal.platform.Platform;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@Service
public class SendRedPackServiceImpl implements SendRedPackService {

    private XmlUtil xmlUtil = new XmlUtil();

    /**
     * 发送红包
     * @param request
     * @param redPack：红包参数
     * @return 发红包结果信息，return_code为SUCCESS表示发送成功，FAIL发送失败
     * @throws Exception
     */
    @Override
    public SendResult sendRedPack(HttpServletRequest request, RedPack redPack) throws Exception{

        /**
         * 构造红包实体
         */
        RedPack pack = new RedPack();

        pack.setRe_openid(redPack.getRe_openid());
        pack.setAct_name(redPack.getAct_name());
        pack.setRemark(redPack.getRemark());
        pack.setTotal_amount(redPack.getTotal_amount());
        pack.setTotal_num(redPack.getTotal_num());
        pack.setWishing(redPack.getWishing());

        pack.setWxappid(WxConfig.getInstance().getAppId());
        pack.setMch_id(WxConfig.getInstance().getMch_id());
        pack.setSend_name(WxConfig.getInstance().getSend_name());

        pack.setClient_ip(WeXinUtil.getIp(request));
        pack.setMch_billno(WeXinUtil.generateOrderId());
        String nonce = UUID.randomUUID().toString().replaceAll("-", "");
        pack.setNonce_str(nonce);
        String sign = WeXinUtil.createUnifiedOrderSign(pack,WxConfig.getInstance().getKey());
        pack.setSign(sign);

        /**
         * 转成XML格式 微信可接受的格式
         */
        xmlUtil.getXstreamInclueUnderline().alias("xml", pack.getClass());
        String xml = xmlUtil.getXstreamInclueUnderline().toXML(pack);

        //发起请求前准备
        RequestBody body = RequestBody.create(MediaType.parse("text/xml;charset=UTF-8"), xml);
        Request req = new Request.Builder()
                .url("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack")
                .post(body)
                .build();
        //为http请求设置证书
        SSLSocketFactory socketFactory = WeXinUtil.getSSL().getSocketFactory();
        X509TrustManager x509TrustManager = Platform.get().trustManager(socketFactory);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().sslSocketFactory(socketFactory, x509TrustManager).build();
        /**
         * 发送红包,解析结果，判断是否红包发送成功
         */
        Response response = okHttpClient.newCall(req).execute();
        String content = response.body().string();
        Map<String, String> responseMap = xmlUtil.parseXML(content);
        //微信返回结果写入实体类
        SendResult sendResult = new SendResult();
        sendResult.setReturn_code(responseMap.get("return_code"));
        sendResult.setErr_code(responseMap.get("err_code"));
        sendResult.setErr_code_des(responseMap.get("err_code_des"));
        sendResult.setMch_billno(responseMap.get("mch_billno"));
        sendResult.setMch_id(responseMap.get("mch_id"));
        sendResult.setRe_openid(responseMap.get("re_openid"));
        sendResult.setResult_code(responseMap.get("result_code"));
        sendResult.setReturn_msg(responseMap.get("return_msg"));
        sendResult.setTotal_amount(responseMap.get("total_amount"));
        sendResult.setWxappid(responseMap.get("wxappid"));
        sendResult.setSend_listid(responseMap.get("send_listid"));

        if ("SUCCESS".equals(responseMap.get("return_code"))){
            System.out.println("红包发送成功");
        }else {
            System.out.println("红包发送失败");
        }
        System.out.println(sendResult.toString());
        return sendResult;

    }

}
