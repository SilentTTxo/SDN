package com.sdn.networkElement;

import com.alibaba.fastjson.JSON;
import com.sdn.core.model.NetworkElementModel;
import com.sdn.core.model.NetworkElementPortModel;
import com.sdn.core.protocol.NetworkElementDataProtocol;
import com.sdn.core.protocol.type.Pattion;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * NE定时发送状态到Controller
 */
public class NESender extends Thread{
    /** 发送数据状态间隔时间 **/
    private static long INTERVAL = 1000;

    private NetworkElementModel cilent;
    private OutputStream out;
    private boolean isContinue;

    public NESender(OutputStream out,NetworkElementModel cilent){
        this.out = out;
        this.cilent = cilent;

        isContinue = true;
    }
    @Override
    public void run() {
        while (isContinue){
            String data = JSON.toJSONString(cilent);
            NetworkElementDataProtocol networkElementDataProtocol = new NetworkElementDataProtocol(Pattion.SEND_DATA,data);
            try {
                out.write(networkElementDataProtocol.getPackData());
                out.flush();
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        isContinue = false;
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
