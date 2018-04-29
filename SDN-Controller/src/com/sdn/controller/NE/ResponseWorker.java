package com.sdn.controller.NE;

import com.alibaba.fastjson.JSON;
import com.sdn.controller.Context;
import com.sdn.core.protocol.NetworkElementDataProtocol;
import com.sdn.core.protocol.type.Pattion;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Controller向NE发出数据包的线程
 */
public class ResponseWorker {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private Socket socket;

    public ResponseWorker(Socket socket){
        this.socket = socket;
    }

    public void SendMsg(NetworkElementDataProtocol networkElementDataProtocol){
        try {
            OutputStream os =  socket.getOutputStream();
            os.write(networkElementDataProtocol.getPackData());

            logger.info("send ok");
        } catch (IOException e) {
            e.printStackTrace();
            Context.closeSocket(socket);
        }
    }
}
