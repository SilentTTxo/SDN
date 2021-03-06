package com.sdn.controller;

import com.sdn.controller.DB.MysqlConnector;
import com.sdn.controller.DB.NeInfoDao;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多线程共享环境
 */
public class Context {
    // 线程安全的socketMap
    public static ConcurrentHashMap socketMap = new ConcurrentHashMap();

    public static NeInfoDao neInfoDao = new NeInfoDao();

    public static void closeSocket(Socket socket) {
        try {
            if(socket.getInputStream() != null){
                socket.getInputStream().close();
            }
            if(socket.getOutputStream() != null){
                socket.getOutputStream().close();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        socketMap.remove(socket + "-res");
        socketMap.remove(socket + "-rsp");
        socketMap.remove(socket + "-cache");
    }

    public static String DB_URL = null;
    public static String DB_USER = null;
    public static String DB_PASSWORD = null;
    public static MysqlConnector mysqlConnector = null;
}
