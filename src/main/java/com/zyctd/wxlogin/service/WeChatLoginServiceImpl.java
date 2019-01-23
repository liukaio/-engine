package com.zyctd.wxlogin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyctd.wxlogin.auth.WXAuthUtil;
import com.zyctd.wxlogin.config.WxConfig;
import com.zyctd.wxlogin.pojo.Oauth2Token;
import com.zyctd.wxlogin.pojo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class WeChatLoginServiceImpl implements WeChatLoginService {

    private static Logger log = LoggerFactory.getLogger(WeChatLoginServiceImpl.class);

    /**
     * 用户同意授权,获取code
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void getCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String backUrl = WxConfig.getInstance().getBackUrl();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WxConfig.getInstance().getAppId() +
                "&redirect_uri=" + URLEncoder.encode(backUrl, "UTF-8") +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=zyctd_wxdenglu#wechat_redirect";
        resp.sendRedirect(url);
    }

    /**
     * 授权登录，获取用户信息以及openId
     * @param request
     * @param response
     * @return userInfo:用户信息
     * @throws IOException
     */
    @Override
    public UserInfo login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        System.out.println("code: "+code);
        System.out.println("state: "+state);

        if (null != code && "zyctd_wxdenglu".equals(state)){
            Oauth2Token oauth2Token = getOauth2AccessToken(code);
            if (null == oauth2Token){
                throw new NullPointerException("获取网页授权token失败!");
            }
            //scope为手动授权方式的时候,可以获取用户信息
            String access_token = checkToken(oauth2Token.getAccessToken(),oauth2Token.getOpenId(),oauth2Token.getRefreshToken());
            return getUserInfo(access_token,oauth2Token.getOpenId());
            /**
             * scope为静默方式的时候,只能获取openID,无法获取用户其他信息
             */
//            UserInfo userInfo = new UserInfo();
//            userInfo.setOpenid(getOpenID(oauth2Token));
//            System.out.println("userinfo : " + userInfo.toString());
//            return userInfo;
        }else {
            throw new NullPointerException("code 获取失败！请检查请求参数!");
        }
    }

    /**
     * 根据code换取access_token
     * @param code
     * @return
     * @throws IOException
     */
    private Oauth2Token getOauth2AccessToken(String code) throws IOException {
        Oauth2Token oauth2Token = null;
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WxConfig.getInstance().getAppId() +
                "&secret=" + WxConfig.getInstance().getAppSecret() +
                "&code=" + code +
                "&grant_type=authorization_code";
        JSONObject jsonObject = WXAuthUtil.doGetJson(url);

        if (null != jsonObject) {
            try {
                oauth2Token = new Oauth2Token();
                oauth2Token.setAccessToken(jsonObject.getString("access_token"));
                oauth2Token.setExpiresIn(jsonObject.getInteger("expires_in"));
                oauth2Token.setRefreshToken(jsonObject.getString("refresh_token"));
                oauth2Token.setOpenId(jsonObject.getString("openid"));
                oauth2Token.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                oauth2Token = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return oauth2Token;
    }

    /**
     * 获取openId
     * @param oauth2Token
     * @return
     */
    public String getOpenID(Oauth2Token oauth2Token){

        if (null != oauth2Token) {
            return oauth2Token.getOpenId();
        }
        return null;
    }

    /**
     * 获取全局token，可用于调用微信其他接口
     * @return
     * @throws IOException
     */
    private String getGlobalToken() throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
                "&appid="+ WxConfig.getInstance().getAppId() +
                "&secret="+ WxConfig.getInstance().getAppSecret();
        JSONObject jsonObject2 = WXAuthUtil.doGetJson(url);

        return jsonObject2.getString("access_token");
    }

    /**
     * 测试获取是否关注的方法
     * @param code
     * @return
     * @throws IOException
     */
    /*private void testgetOauth2AccessToken(String code, HttpServletResponse resp) throws IOException {
        //使用code获取OpenID
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WxConfig.getInstance().getAppId() +
                "&secret=" + WxConfig.getInstance().getAppSecret() +
                "&code=" + code +
                "&grant_type=authorization_code";
        JSONObject jsonObject = WXAuthUtil.doGetJson(url);

        String openid = jsonObject.getString("openid");
        //获取全局Access Token
        String url2 = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ WxConfig.getInstance().getAppId() +"&secret="+ WxConfig.getInstance().getAppSecret();
        JSONObject jsonObject2 = WXAuthUtil.doGetJson(url2);
        String token = jsonObject2.getString("access_token");
        //再使用全局ACCESS_TOKEN获取OpenID的详细信息
        String url3 = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
        JSONObject jsonObject3 = WXAuthUtil.doGetJson(url3);
        //根据subscribe判断用户是否关注公众号,1表示关注,0表示未关注
        Integer subscribe = jsonObject3.getInteger("subscribe");
        System.out.println("subscribe: "+ subscribe);
        if (subscribe != 1){
            String followUrl = "";
            resp.sendRedirect(followUrl);
        }
        System.out.println("nickname"+ jsonObject3.getString("nickname"));
    }*/

    /**
     * 通过网页授权获取用户信息
     * @param access_token 用于网页授权的临时token
     * @param openId
     * @return
     */
    private UserInfo getUserInfo(String access_token, String openId) throws IOException {
        UserInfo userInfo = null;
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token +
                "&openid=" + openId +
                "&lang=zh_CN";
        JSONObject userInfoJson = WXAuthUtil.doGetJson(infoUrl);
        System.out.println("userInfoJson: " + userInfoJson.toString());
        if (null != userInfoJson && null == userInfoJson.getString("errcode")) {
            try {
                userInfo = new UserInfo();
                userInfo.setCity(userInfoJson.getString("city"));
                userInfo.setCountry(userInfoJson.getString("country"));
                userInfo.setHeadimgurl(userInfoJson.getString("headimgurl"));
                userInfo.setNickname(userInfoJson.getString("nickname"));
                userInfo.setOpenid(userInfoJson.getString("openid"));
                List<String> list = JSON.parseArray(userInfoJson.getString("privilege"),String.class);
                userInfo.setPrivilege(list);
                userInfo.setSex(userInfoJson.getString("sex"));
                userInfo.setUnionid(userInfoJson.getString("unionid"));
                userInfo.setProvince(userInfoJson.getString("province"));
            }catch (Exception e){
                userInfo = null;
                int errorCode = userInfoJson.getInteger("errcode");
                String errorMsg = userInfoJson.getString("errmsg");
                log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return userInfo;
    }

    /**
     * 验证access_token是否有效
     * @param access_token
     * @param openId
     * @param refresh_token
     * @return
     * @throws IOException
     */
    private String checkToken(String access_token, String openId, String refresh_token) throws IOException {
        //验证access_token是否有效,errcode为0表示有效，否则无效
        String chickUrl="https://api.weixin.qq.com/sns/auth?access_token="+access_token+"&openid="+openId;
        JSONObject chickInfo = WXAuthUtil.doGetJson(chickUrl);
        if (!"0".equals(chickInfo.getString("errcode"))) {
            String refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+ openId +
                    "&grant_type=refresh_token" +
                    "&refresh_token=" + refresh_token;
            JSONObject refreshInfo = WXAuthUtil.doGetJson(refreshTokenUrl);
            return refreshInfo.getString("access_token");
        } else {
            return access_token;
        }
    }

    /**
     * 判断是否关注公众号
     * @param token 全局的token
     * @param openid
     * @return
     */
    public boolean judgeIsFollow(String token,String openid){
        Integer subscribe = null;
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
        try {
            JSONObject userInfoJson = WXAuthUtil.doGetJson(url);
            subscribe = userInfoJson.getInteger("subscribe");
            System.out.println("subscribe = "+subscribe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1==subscribe?true:false;
    }

}
