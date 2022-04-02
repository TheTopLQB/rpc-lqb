package com.lqb.rpc;
import com.lqb.rpc.common.Request;
import com.lqb.rpc.common.Response;
import com.lqb.rpc.tool.DecodeMessage;
import com.lqb.rpc.tool.EncodeMessage;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @Author: liqingbin
 * @Date: 2022/3/27 18:12
 */
public class Consumer {
    private String host;
    private int port;
    public Consumer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object send(Request request) {
        try (
                final Socket socket = new Socket(host, port);
                OutputStream os = new ObjectOutputStream(socket.getOutputStream());
                InputStream is = new ObjectInputStream(socket.getInputStream());
        ){

            byte[] packBytes = new EncodeMessage<Request>().encode(request);
            os.write(packBytes);
            os.flush();
            int len = 0;
            byte[] buf = new byte[1024];

            byte[] tempBuf = new byte[1024];
            int temBufLength = 0;
            StringBuilder sb = new StringBuilder();
            ByteOrder order = ByteOrder.BIG_ENDIAN;// 处理下大小端
            while ((len = is.read(buf)) != -1) {
                System.arraycopy(buf, 0, tempBuf, temBufLength, len);
                temBufLength += len;
                System.out.println("收到消息长度len：" + len);
                // 处理协议
                boolean hasNext = true;
                while (hasNext) {
                    if (temBufLength > 4) {
                        byte[] packageLengthBytes = new byte[4];
                        System.arraycopy(tempBuf, 0, packageLengthBytes, 0, 4);
                        int packageLength = ByteBuffer.wrap(packageLengthBytes).order(order).getInt();

                        byte[] messageBytes = new byte[packageLength];
                        if (temBufLength >= packageLength) {
                            System.arraycopy(tempBuf, 0, messageBytes, 0, packageLength);
                            temBufLength -= packageLength;
                            System.arraycopy(tempBuf, packageLength, tempBuf, 0, packageLength);
                            return decodeMessage(messageBytes, os);
                        } else {
                            hasNext = false;
                        }
                    } else {
                        hasNext = false;
                    }
                }
            }
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object decodeMessage(final byte[] bytes, final OutputStream os) {
        Response response = new DecodeMessage<Response>().decode(bytes, Response.class);
        return response.getResult();
    }
}
