package com.sdn.controller;

import com.sdn.controller.Cilent.MiniHttpServer;
import com.sdn.controller.NE.ReceiveWorker;
import com.sdn.controller.NE.ResponseWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Controller入口类
 */
public class Service {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private int port;
    private int httpPort;
    private MiniHttpServer miniHttpServer;

    Service(int port,int httpPort){
        this.port = port;
        this.httpPort = httpPort;
    }

    public void start(){
        try {
            ServerSocket serverScoket = new ServerSocket(port);
            Socket socket = null;
            logger.info("Socket Start at port : "+ port);

            miniHttpServer = new MiniHttpServer(httpPort);
            miniHttpServer.start();
            logger.info("HttpServer start ,listen at " + httpPort);

            while (true) {
                socket=serverScoket.accept();
                ReceiveWorker receiveWorker = new ReceiveWorker(socket);
                receiveWorker.start();
                ResponseWorker responseWorker = new ResponseWorker(socket);

                Context.socketMap.put(socket.toString() + "-res",receiveWorker);
                Context.socketMap.put(socket.toString() + "-rsp",responseWorker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Service service = new Service(10880,8080);

        service.start();
    }
}
