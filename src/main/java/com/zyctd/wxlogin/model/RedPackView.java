package com.zyctd.wxlogin.model;

import com.zyctd.wxlogin.pojo.RedPack;

import java.util.List;

public class RedPackView extends RedPack {
    List<RedPack> redPackList;

    public List<RedPack> getRedPackList() {
        return redPackList;
    }

    public void setRedPackList(List<RedPack> redPackList) {
        this.redPackList = redPackList;
    }
}
