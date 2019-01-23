package com.zyctd.wxlogin.config;


import com.zyctd.wxlogin.exception.WxErrorException;
import com.zyctd.wxlogin.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WxConfig {
    private static final String configFile = "/wxconfig.properties";
    private static WxConfig config = null;

    private volatile String appId;
    private volatile String appSecret;
    private volatile String backUrl;

    private volatile String mch_id;
    private volatile String send_name;
    private volatile String key;
    private volatile String certPath;

    public WxConfig() {
        Properties p = new Properties();
        InputStream inStream = this.getClass().getResourceAsStream(configFile);
        if(inStream == null){
            try {
                throw new WxErrorException("Can't find file");
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        try {
            p.load(inStream);
            this.appId = p.getProperty("wx.appId");
            if(StringUtils.isNotBlank(this.appId)) this.appId = this.appId.trim();
            this.appSecret = p.getProperty("wx.appSecret");
            if(StringUtils.isNotBlank(this.appSecret)) this.appSecret = this.appSecret.trim();
            this.backUrl = p.getProperty("wx.backUrl");
            if(StringUtils.isNotBlank(this.backUrl)) this.backUrl = this.backUrl.trim();

            this.mch_id = p.getProperty("wx.mch_id");
            if(StringUtils.isNotBlank(this.mch_id)) this.mch_id = this.mch_id.trim();
            this.send_name = p.getProperty("wx.send_name");
            if(StringUtils.isNotBlank(this.send_name)) this.send_name = this.send_name.trim();
            this.key = p.getProperty("wx.key");
            if(StringUtils.isNotBlank(this.key)) this.key = this.key.trim();
            this.certPath = p.getProperty("wx.certPath");
            if(StringUtils.isNotBlank(this.certPath)) this.certPath = this.certPath.trim();
            inStream.close();
        } catch (IOException e) {
            try {
                throw new WxErrorException("load wxconfig.properties error,class, can't find wxconfig.properties");
            } catch (WxErrorException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("load wxconfig.properties success");
    }

    public static synchronized WxConfig getInstance(){
        if(config == null){
            config = new WxConfig();
        }
        return config;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public String getMch_id() {
        return mch_id;
    }

    public String getSend_name() {
        return send_name;
    }

    public String getKey() {
        return key;
    }

    public String getCertPath() {
        return certPath;
    }
}
