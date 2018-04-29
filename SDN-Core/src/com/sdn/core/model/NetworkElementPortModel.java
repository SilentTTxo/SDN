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
}
