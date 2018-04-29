package com.sdn.core.model;

import com.sdn.core.util.IPUtils;
import com.sdn.core.util.SocketUtils;

/**
 * 网元设备信息
 */
public class NetworkElementModel {
    /** 网元设备ip地址 **/
    private long ipAddress;

    /** 端口数 **/
    private int portNum;

    /** 端口列表 **/
    private NetworkElementPortModel[] networkElementPortModels;

    public long getIpAddress() {
        return ipAddress;
    }

    public String getIpAddressStr() {
        return IPUtils.num2ip(ipAddress);
    }

    public void setIpAddress(long ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    public NetworkElementPortModel[] getNetworkElementPortModels() {
        return networkElementPortModels;
    }

    public void setNetworkElementPortModels(NetworkElementPortModel[] networkElementPortModels) {
        this.networkElementPortModels = networkElementPortModels;
    }

    public NetworkElementModel(long ipAddress, int portNum, NetworkElementPortModel[] networkElementPortModels) {
        this.ipAddress = ipAddress;
        this.portNum = portNum;
        this.networkElementPortModels = networkElementPortModels;
    }

    public NetworkElementModel(String ipAddress, int portNum, NetworkElementPortModel[] networkElementPortModels) {
        this.ipAddress = IPUtils.ip2num(ipAddress);
        this.portNum = portNum;
        this.networkElementPortModels = networkElementPortModels;
    }

    public NetworkElementModel() { }
}
