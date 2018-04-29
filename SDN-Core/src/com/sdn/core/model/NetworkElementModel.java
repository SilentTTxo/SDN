package com.sdn.core.model;

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
}
