package com.sdn.networkElement;

import com.alibaba.fastjson.JSON;
import com.sdn.core.model.NetworkElementModel;
import com.sdn.core.model.NetworkElementPortModel;
import com.sdn.core.protocol.NetworkElementDataProtocol;
import com.sdn.core.protocol.type.Pattion;
import com.sdn.core.util.IPUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * NE启动类
 */
public class NECilent {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private NetworkElementModel baseInfo;

    private Socket socket;
    private InputStream is;

    /**
     * 初始化cilent
     * @param ControllerIp  管理层ip地址
     * @param port          监听端口号
     * @param CilentIp      客户端ip地址
     * @param portNum       网元设备端口数量
     */
    public NECilent(String ControllerIp,int port,String CilentIp ,int portNum){

        ArrayList<NetworkElementPortModel> portList = new ArrayList<>();
        IntStream.range(1,portNum).forEach(i ->{
            portList.add(new NetworkElementPortModel());
        });

        baseInfo = new NetworkElementModel(CilentIp,portNum,portList.toArray(new NetworkElementPortModel[0]));

        this.startConnect(ControllerIp,port);
    }

    public void startConnect(String ControllerIp,int port) {
        logger.info("start connect ip:" + ControllerIp);
        try {
            //1.建立客户端socket连接，指定服务器位置及端口
            this.socket = new Socket(ControllerIp, port);

            //输入输出流
            this.is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            logger.info("connect success");

            // 打开发送数据线程
            NESender neSender = new NESender(os,baseInfo);
            neSender.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //接收服务器数据
            while (is != null) {
                NetworkElementDataProtocol networkElementDataProtocol = NetworkElementDataProtocol.getDataFromStream(is);
                if(networkElementDataProtocol == null){
                    close();
                    logger.info("socket disconnect IP:" + IPUtils.num2ip(baseInfo.getIpAddress()));
                    return;
                }
                logger.info("recive data : "+ networkElementDataProtocol);
                switch (networkElementDataProtocol.getPattion()){
                    case Pattion.UPDATE_CILENT_DATA:{
                        logger.info("start update data");
                        String json = networkElementDataProtocol.getData();
                        NetworkElementModel networkElementModel = JSON.parseObject(json,NetworkElementModel.class);
                        NetworkElementPortModel[] networkElementPortModels = networkElementModel.getNetworkElementPortModels();
                        for(int i=0;i<networkElementPortModels.length;i++){
                            baseInfo.getNetworkElementPortModels()[i].setMaxNetworkFlux(networkElementPortModels[i].getMaxNetworkFlux());
                        }
                        logger.info("update end");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        //关闭资源
        try {
            is.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NECilent neCilent = new NECilent("192.168.2.161",10880,"192.168.2.161",10);
    }
}
