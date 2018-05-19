package com.sdn.controller.Web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdn.controller.Context;
import com.sdn.controller.DB.Po.NeInfoPo;
import com.sdn.controller.NE.ResponseWorker;
import com.sdn.controller.Web.Vo.NeInfoVo;
import com.sdn.core.model.NetworkElementModel;
import com.sdn.core.model.NetworkElementPortModel;
import com.sdn.core.protocol.NetworkElementDataProtocol;
import com.sdn.core.protocol.type.Pattion;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

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
            server.createContext("/ne/list", new NEListHandler());
            server.createContext("/ne/history", new NEHistroyHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer = server;
    }

    public void start(){
        this.httpServer.start();
    }

    /**
     * 修改网元设备信息接口
     */
    static  class UpdateHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            boolean isFind = false;
            String rs = "";
            if(exchange.getRequestMethod().equals("POST")){
                String jsonStr = new String(exchange.getRequestBody().readAllBytes());
                NetworkElementModel networkElementModel = JSON.parseObject(jsonStr,NetworkElementModel.class);

                for(Object key : Context.socketMap.keySet()){
                    String keyStr = (String) key;
                    if (keyStr.contains("-cache")){
                        NetworkElementModel temp = (NetworkElementModel) Context.socketMap.get(keyStr);
                        if(temp.getIpAddress() == networkElementModel.getIpAddress()){
                            // 找到了要修改的设备
                            String resKey  = keyStr.replace("-cache","-rsp");
                            ResponseWorker responseWorker = (ResponseWorker)Context.socketMap.get(resKey);
                            responseWorker.SendMsg(new NetworkElementDataProtocol(Pattion.UPDATE_CILENT_DATA,jsonStr));
                            rs = "{\"code\":200,\"msg\":\"ok\"}";
                            isFind = true;
                            break;
                        }
                    }
                }
                if (!isFind){
                    rs = "{\"code\":500,\"msg\":\"can not find the device\"}";
                }
            }else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                rs = "{\"code\":500,\"msg\":\"what happend? I dont know\"}";
            }


            String response = rs;
            exchange.getResponseHeaders().add("Content-Type","application/json");
            exchange.getResponseHeaders().add("Access-control-Allow-Origin","*");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    /**
     * 获取当前网元状态列表
     */
    static  class NEListHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Context.socketMap.keySet();
            ArrayList<NetworkElementModel> rsList = new ArrayList<>();
            for(Object key : Context.socketMap.keySet()){
                String keyStr = (String) key;
                if(keyStr.contains("-cache")){
                    NetworkElementModel networkElementModel = (NetworkElementModel)Context.socketMap.get(keyStr);
                    rsList.add(networkElementModel);
                }
            }
            String response = JSON.toJSONString(rsList);
            exchange.getResponseHeaders().add("Content-Type","application/json");
            exchange.getResponseHeaders().add("Access-control-Allow-Origin","*");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    /**
     * 获取网元历史状态
     */
    static  class NEHistroyHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if(exchange.getRequestMethod().equals("POST")){
                String jsonStr = new String(exchange.getRequestBody().readAllBytes());
                JSONObject param = JSONObject.parseObject(jsonStr);
                long ip = param.getLong("ip");

                List<NeInfoPo> infoiList = Context.neInfoDao.getNeinfo(ip);
                List<NeInfoVo> rsList = new ArrayList<>();
                for(NeInfoPo neInfoPo : infoiList){
                    NeInfoVo temp = new NeInfoVo();
                    temp.setCreateTime(neInfoPo.getCreateTime() / 1000);
                    temp.setInfo(JSON.parseArray(neInfoPo.getInfo(),NetworkElementPortModel.class));
                    rsList.add(temp);
                }
                String response = JSON.toJSONString(rsList);
                exchange.getResponseHeaders().add("Content-Type","application/json");
                exchange.getResponseHeaders().add("Access-control-Allow-Origin","*");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}