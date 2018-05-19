package com.sdn.controller.DB;

import com.sdn.controller.Context;
import com.sdn.controller.DB.Po.NeInfoPo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NeInfoDao {
    public void addNeInfo(NeInfoPo infoPo){
        String sql = String.format("INSERT INTO T_NE_INFO VALUES(NULL,'%s',%d,NOW())",infoPo.getInfo(),infoPo.getIp());
        try {
            Context.mysqlConnector.getStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<NeInfoPo> getNeinfo(long ip){
        List<NeInfoPo> infoList = new ArrayList<>();
        String sql = String.format("SELECT info,updateTime FROM `T_NE_INFO` WHERE ip = %d limit 1000;",ip);
        try {
             ResultSet data =  Context.mysqlConnector.getStatement().executeQuery(sql);

             while (data.next()){
                 String info = data.getString(1);
                 long createTime = data.getTimestamp(2).getTime();

                 NeInfoPo temp = new NeInfoPo();
                 temp.setInfo(info);
                 temp.setCreateTime(createTime);

                 infoList.add(temp);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return infoList;
    }
}
