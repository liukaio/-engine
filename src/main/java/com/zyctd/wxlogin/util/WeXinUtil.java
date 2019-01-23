package com.zyctd.wxlogin.util;

import com.zyctd.wxlogin.config.WxConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeXinUtil {

    /**
     * 获取用户IP
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request){
        String ipAddress = null;
        if (request.getHeader("x-forwarded-for") == null) {
            ipAddress = request.getRemoteAddr();
        }else{
            if(request.getHeader("x-forwarded-for").length()  > 15){
                String [] aStr = request.getHeader("x-forwarded-for").split(",");
                ipAddress = aStr[0];
            } else{
                ipAddress = request.getHeader("x-forwarded-for");
            }
        }
        return ipAddress;
    }

    /**
     * 拼接生成sign 签名
     * @param object
     * @param KEY
     * @return
     * @throws Exception
     */
    public static String createUnifiedOrderSign(Object object,String KEY) throws Exception{
        StringBuffer sign = new StringBuffer();
        Map<String, String> map = getSortMap(object);

        boolean isNotFirst = false;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(isNotFirst == true){
                sign.append("&");
            }else{
                isNotFirst = true;
            }

            sign.append(entry.getKey()).append("=").append(entry.getValue());
        }
        sign.append("&key=").append(KEY);

        return DigestUtils.md5Hex(sign.toString()).toUpperCase();

    }

    /**
     * 使用反射机制，动态获取对象的属性和参数值，排除值为null的情况，并按字典序排序
     * @param object
     * @return
     * @throws Exception
     */
    private static Map<String, String> getSortMap(Object object) throws Exception{
        Field[] fields = object.getClass().getDeclaredFields();
        Map<String, String> map = new HashMap<String, String>();

        for(Field field : fields){
            String name = field.getName();
            //String methodName = "get" + name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
            //        .toUpperCase());
            // 调用getter方法获取属性值
//                 Method getter = object.getClass().getMethod(methodName);
//                 String value =  getter.invoke(object)+"";
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null){
                map.put(name, value.toString());
            }
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String arg0, String arg1) {

                        return arg0.compareTo(arg1);
                    }
                });
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 获取SSLContext实例
     */
    public static SSLContext getSSL() throws KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, java.security.cert.CertificateException {
        //指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //证书位置(证书放在项目目录下存在风险，有可能被远程获取)
        //Resource resource = new ClassPathResource("apiclient_cert.p12");
        //InputStream instream = resource.getInputStream();
        File certPath = new File(WxConfig.getInstance().getCertPath());
        InputStream instream = new FileInputStream(certPath);
        //证书密码,默认是商户号
        String certKey = WxConfig.getInstance().getMch_id();
        try {
            keyStore.load(instream, certKey.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, certKey.toCharArray())
                .build();
        return sslcontext;
    }

    /**
     * 生成订单号
     * 商户号加上当前时间和随机字符串来保证订单号唯一性
     * 订单号总长28位，后面补随机字符串
     */
    public static String generateOrderId(){
        String mch_orderId = null;
        String mch_id = WxConfig.getInstance().getMch_id();
        if (null != mch_id && mch_id.equals("")){
            //String userId = openId.substring(openId.length()-5);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String nowTime = df.format(new Date());
            Integer len = 28 - mch_id.length() - nowTime.length();
            mch_orderId = mch_id + nowTime + StringUtils.randomStr(len);
        }
        return mch_orderId;
    }

}
