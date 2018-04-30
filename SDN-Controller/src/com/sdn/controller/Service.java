package com.sdn.controller;

import com.sdn.controller.DB.MysqlConnector;
import com.sdn.controller.Web.MiniHttpServer;
import com.sdn.controller.NE.ReceiveWorker;
import com.sdn.controller.NE.ResponseWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.logging.Logger;

/**
 * Controller入口类
 */
public class Service {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private int port;
    private int httpPort;
    private MiniHttpServer miniHttpServer;

    Service(int port,int httpPort, String db_url, String db_user, String db_password){
        this.port = port;
        this.httpPort = httpPort;

        Context.DB_URL = db_url;
        Context.DB_USER = db_user;
        Context.DB_PASSWORD = db_password;
        Context.mysqlConnector = new MysqlConnector();
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
        if(args.length == 0){
//            Service service = new Service(10880,8080,"jdbc:mysql://HOST:PORT/SDN","USERNAME","PASSWORD");
//            service.start();
        } else{
            int port = Integer.parseInt(args[0]);
            int httpPort = Integer.parseInt(args[1]);
            String db_url = args[2];
            String db_user = args[3];
            String db_password = args[4];

            Service service = new Service(port,httpPort,db_url,db_user,db_password);
            service.start();
        }
    }
}
