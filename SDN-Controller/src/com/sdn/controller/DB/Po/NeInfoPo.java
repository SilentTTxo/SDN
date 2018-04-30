package com.sdn.controller.DB.Po;

import com.alibaba.fastjson.JSON;
import com.sdn.core.model.NetworkElementModel;

public class NeInfoPo {
    private int id;
    private long ip;
    private String info;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getIp() {
        return ip;
    }

    public void setIp(long ip) {
        this.ip = ip;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public NeInfoPo(int id, long ip, String info, long createTime) {
        this.id = id;
        this.ip = ip;
        this.info = info;
        this.createTime = createTime;
    }

    public NeInfoPo(NetworkElementModel networkElementModel){
        this.ip = networkElementModel.getIpAddress();
        this.info = JSON.toJSONString(networkElementModel.getNetworkElementPortModels());
    }
}
