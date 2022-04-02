package com.lqb.rpc.tool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @Author: liqingbin
 * @Date: 2022/3/30 21:27
 */
public class EncodeMessage<T> {
    // request或response 转为byte数组
    public byte[] encode(T t) {
        String s = JSON.toJSONString(t, SerializerFeature.WriteClassName);
        int bodyLen = s.getBytes().length;
        byte[] packBytes = new byte[12 + bodyLen];
        int packLen = 12 + bodyLen;
        System.out.println(packLen);
        byte[] packLenBytes = new byte[4];
        packLenBytes[0] = (byte)((packLen >> 24) & 0xFF);
        packLenBytes[1] = (byte)((packLen >> 16) & 0xFF);
        packLenBytes[2] = (byte)((packLen >> 8) & 0xFF);
        packLenBytes[3] = (byte)(packLen & 0xFF);

        byte[] headerBytes = new byte[2];
        headerBytes[1] = (byte) 0b0001100;

        System.arraycopy(packLenBytes, 0, packBytes, 0, 4);
        System.arraycopy(headerBytes, 0, packBytes, 4, 2);
        System.arraycopy(s.getBytes(), 0, packBytes, 12, bodyLen);

        return packBytes;
    }
}
