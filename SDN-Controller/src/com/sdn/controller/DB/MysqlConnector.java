package com.sdn.controller.DB;

import com.sdn.controller.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class MysqlConnector {
        private Connection connection;

        public MysqlConnector(){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(Context.DB_URL,Context.DB_USER,Context.DB_PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    public Statement getStatement(){
        Logger logger = Logger.getLogger("MysqlConnector");
        try {
            Statement statement = connection.createStatement();
            return  statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  null;
    }

}
