package com.sdn.controller.Web.Vo;

import com.sdn.core.model.NetworkElementPortModel;

import java.util.List;

public class NeInfoVo {
    private long createTime;
    private List<NetworkElementPortModel> info;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<NetworkElementPortModel> getInfo() {
        return info;
    }

    public void setInfo(List<NetworkElementPortModel> info) {
        this.info = info;
    }
}
