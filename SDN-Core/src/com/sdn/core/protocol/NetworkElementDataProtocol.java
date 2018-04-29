package com.sdn.core.protocol;

import com.sdn.core.util.SocketUtils;

import java.io.*;
import java.net.SocketException;
import java.util.Arrays;

/**
 * Controller与网元设备之间的数据通信协议
 */
public class NetworkElementDataProtocol {

    /** 协议长度存储区域容量 单位：byte **/
    private static final int LENGTH_LEN = 4;

    /** 协议类型存储区域容量 单位：byte **/
    private static final int PATTION_LEN = 4;

    /**
     * 协议类型
     * 0:定时传输状态信息 1:修改网元设备设置
     **/
    private int pattion;

    /** 协议数据 json **/
    private String data ;

    public int getPattion() {
        return pattion;
    }

    public void setPattion(int pattion) {
        this.pattion = pattion;
    }

    public String getData() {
        return data;
    }

    public byte[] getDataByte() {
        try {
            return data.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("协议内容编码失败");
            e.printStackTrace();
        }
        return new byte[0];
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLength(){
        return LENGTH_LEN + PATTION_LEN + getDataByte().length;
    }

    /** 获取数据包 **/
    public byte[] getPackData(){
        byte[] length = SocketUtils.int2ByteArrays(getLength());
        byte[] pation = SocketUtils.int2ByteArrays(this.pattion);
        byte[] data = getDataByte();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(getLength());
        baos.write(length,0,LENGTH_LEN);
        baos.write(pation,0,PATTION_LEN);
        baos.write(data,0,getDataByte().length);
        return  baos.toByteArray();
    }

    public NetworkElementDataProtocol(int pattion,String data){
        this.pattion = pattion;
        this.data = data;
    }

    /**
     * 从输入流中读取数据包并按照协议解析
     * @param is
     * @return
     */
    public static NetworkElementDataProtocol getDataFromStream(InputStream is){
        NetworkElementDataProtocol rs = null;
        byte[] length = new byte[NetworkElementDataProtocol.LENGTH_LEN];

        int offset = 0;
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            // 读取数据包总长度
            int read_length = 0;
            while (offset < NetworkElementDataProtocol.LENGTH_LEN){
                read_length = bis.read(length,offset,NetworkElementDataProtocol.LENGTH_LEN - offset);
                if(read_length > 0 ){
                    offset += read_length;
                }else if(read_length == -1){
                    bis.close();
                    return null;
                }
            }

            // 读取全部数据
            int len = 0;
            int totalLength = SocketUtils.byteArrayToInt(length);
            totalLength -= NetworkElementDataProtocol.LENGTH_LEN;
            byte[] totalData = new byte[totalLength];

            while(len < totalLength){
                read_length = bis.read(totalData,len,totalLength - len);
                if(read_length > 0){
                    len += read_length;
                }
            }

            // 解析成协议包
            rs = parse(totalData);
        } catch (Exception e) {
            return null;
        }

        return rs;
    }

    public static NetworkElementDataProtocol parse(byte[] content){
        int pattion = parsePattion(content);
        String data = parseData(content);

        return new NetworkElementDataProtocol(pattion,data);
    }

    public static int parsePattion(byte[] content){
        byte[] pattionByte =  Arrays.copyOfRange(content,0,NetworkElementDataProtocol.PATTION_LEN - 1);
        return SocketUtils.byteArrayToInt(pattionByte);
    }

    public static String parseData(byte[] content){
        byte[] dataByte =  Arrays.copyOfRange(content,NetworkElementDataProtocol.PATTION_LEN ,content.length);
        try {
            return new String(dataByte,0,dataByte.length,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "NetworkElementDataProtocol{" +
                "pattion=" + pattion +
                ", data='" + data + '\'' +
                '}';
    }
}
