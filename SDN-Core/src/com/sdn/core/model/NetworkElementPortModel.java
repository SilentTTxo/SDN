package com.sdn.core.model;

/**
 * 网元端口信息
 */
public class NetworkElementPortModel {
    /** 端口是否打开 **/
    private boolean isOpen;

    /** 端口网络流量 **/
    private double networkFlux;

    /** 端口网络流量限制最大值 **/
    private double maxNetworkFlux;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getNetworkFlux() {
        return networkFlux;
    }

    public void setNetworkFlux(double networkFlux) {
        this.networkFlux = networkFlux;
    }

    public double getMaxNetworkFlux() {
        return maxNetworkFlux;
    }

    public void setMaxNetworkFlux(double maxNetworkFlux) {
        this.maxNetworkFlux = maxNetworkFlux;
    }

    public NetworkElementPortModel() {
        this.isOpen = true;
        this.networkFlux = 0;
        this.maxNetworkFlux = Integer.MAX_VALUE;
    }
}
