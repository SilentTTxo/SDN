package com.sdn.controller.Cilent;

import com.sdn.controller.Context;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * 小型HttpServer，为客户端提供http接口
 */
public class MiniHttpServer {
    private HttpServer httpServer;

    public MiniHttpServer(int port){
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/ne/update", new UpdateHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer = server;
    }

    public void start(){
        this.httpServer.start();
    }

    static  class UpdateHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String rs = "";
            Context.socketMap.keySet();
            for(Object key : Context.socketMap.keySet()){
                String keyStr = (String) key;
                rs += keyStr;
            }
            String response = rs;
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}