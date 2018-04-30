package com.sdn.controller.DB;

import com.sdn.controller.Context;
import com.sdn.controller.DB.Po.NeInfoPo;

import java.sql.SQLException;

public class NeInfoDao {
    public void addNeInfo(NeInfoPo infoPo){
        String sql = String.format("INSERT INTO T_NE_INFO VALUES(NULL,'%s',%d,NOW())",infoPo.getInfo(),infoPo.getIp());
        try {
            Context.mysqlConnector.getStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
