package com.lqb.rpc.tool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @Author: liqingbin
 * @Date: 2022/3/30 21:28
 */
public class DecodeMessage<T> {
    // byte数组转为request或response
    public T decode(byte[] bytes, Class<T> clazz) {
        // 注意下大小端问题，这里改为大端
        ByteOrder order = ByteOrder.BIG_ENDIAN;
        byte[] packageLengthBytes = new byte[4];
        System.arraycopy(bytes, 0, packageLengthBytes, 0, 4);
        int packageLength = ByteBuffer.wrap(packageLengthBytes).order(order).getInt();

        // header长度
        byte[] headerLengthBytes = new byte[2];
        System.arraycopy(bytes, 4, headerLengthBytes, 0, 2);
        short headerLength = ByteBuffer.wrap(headerLengthBytes).order(order).getShort();

        //Sequence Id
        byte[] sequenceIdBytes = new byte[4];
        System.arraycopy(bytes, 6, sequenceIdBytes, 0, 4);
        int sequenceId = ByteBuffer.wrap(sequenceIdBytes).order(order).getInt();

        // Operation
        byte operationByte = bytes[10];
        int operation = operationByte & 0xFF;

        // protolVersion(暂时不需要)
        byte protocolVersionByte = bytes[11];;
        int protocolVersion = (int)protocolVersionByte & 0xFF;

        // body长度
        int bodyLength = packageLength - headerLength;
        byte[] messageBody = new byte[bodyLength];
        System.out.println("收到的bodyLength：" + bodyLength);
        System.arraycopy(bytes, headerLength, messageBody, 0, bodyLength);

        String str = new String(messageBody);
        return JSON.parseObject(str, clazz);
    }
}
