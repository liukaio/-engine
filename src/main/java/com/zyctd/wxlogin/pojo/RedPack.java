package com.zyctd.wxlogin.pojo;

public class RedPack {

    /**
     * 随机字符串，用于保证签名不可预测
     * 随机字符串，不长于32位
     */
    private String nonce_str;
    /**
     * 签名
     */
    private String sign;
    /**
     * 商户订单号
     * 商户订单号（每个订单号必须唯一。取值范围：0~9，a~z，A~Z）接口根据商户订单号支持重入，如出现超时可再调用。
     */
    private String mch_billno;
    /**
     * 商户号
     * 微信支付分配的商户号
     */
    private String mch_id;
    /**
     * 公众账号
     * 微信分配的公众账号ID（企业号corpid即为此appId）
     */
    private String wxappid;
    /**
     * 商户名称
     * 红包发送者名称
     */
    private String send_name;
    /**
     * 用户openid
     * 接受红包的用户
     * 用户在wxappid下的openid
     */
    private String re_openid;
    /**
     * 付款金额 单位：分
     * 100 == 1元钱 ，也就是说 这里的 1 相当于1分钱
     * 微信发送红包不能少于1元钱
     */
    private int total_amount;
    /**
     * 红包发放总人数
     */
    private int total_num;
    /**
     * 红包祝福语
     */
    private String wishing;
    /**
     * Ip地址
     * 调用接的机器Ip地口址
     */
    private String client_ip;
    /**
     * 活动名称
     */
    private String act_name;
    /**
     * 备注
     */
    private String remark;
    public String getNonce_str() {
        return nonce_str;
    }
    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getMch_billno() {
        return mch_billno;
    }
    public void setMch_billno(String mch_billno) {
        this.mch_billno = mch_billno;
    }
    public String getMch_id() {
        return mch_id;
    }
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }
    public String getWxappid() {
        return wxappid;
    }
    public void setWxappid(String wxappid) {
        this.wxappid = wxappid;
    }
    public String getSend_name() {
        return send_name;
    }
    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }
    public String getRe_openid() {
        return re_openid;
    }
    public void setRe_openid(String re_openid) {
        this.re_openid = re_openid;
    }
    public int getTotal_amount() {
        return total_amount;
    }
    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }
    public int getTotal_num() {
        return total_num;
    }
    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }
    public String getWishing() {
        return wishing;
    }
    public void setWishing(String wishing) {
        this.wishing = wishing;
    }
    public String getClient_ip() {
        return client_ip;
    }
    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }
    public String getAct_name() {
        return act_name;
    }
    public void setAct_name(String act_name) {
        this.act_name = act_name;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
