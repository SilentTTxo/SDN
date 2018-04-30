package com.sdn.controller.NE;

import com.alibaba.fastjson.JSON;
import com.sdn.controller.Context;
import com.sdn.controller.DB.NeInfoDao;
import com.sdn.controller.DB.Po.NeInfoPo;
import com.sdn.core.model.NetworkElementModel;
import com.sdn.core.protocol.NetworkElementDataProtocol;
import com.sdn.core.protocol.type.Pattion;
import com.sdn.core.util.IPUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Controller接收NE发送的数据包的线程
 */
public class ReceiveWorker extends Thread {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private Socket socket;
    private InputStream is;
    private long Ip;

    public ReceiveWorker(Socket socket){
        this.socket = socket;
        try {
            this.is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //接收服务器数据
        while (is != null) {
            NetworkElementDataProtocol networkElementDataProtocol = NetworkElementDataProtocol.getDataFromStream(is);
            if(networkElementDataProtocol == null){
                logger.info("socket disconnect IP:" + IPUtils.num2ip(Ip));
                Context.closeSocket(socket);
                return;
            }
            switch (networkElementDataProtocol.getPattion()){
                case Pattion.SEND_DATA:{
                    String json = networkElementDataProtocol.getData();
                    NetworkElementModel networkElementModel = JSON.parseObject(json,NetworkElementModel.class);
                    this.Ip = networkElementModel.getIpAddress();
                    Context.neInfoDao.addNeInfo(new NeInfoPo(networkElementModel));
                    logger.info("recive status data from : " + networkElementModel.getIpAddressStr());

                    // 加入查询缓存
                    Context.socketMap.put(socket + "-cache",networkElementModel);
                }
            }
        }
    }
}
