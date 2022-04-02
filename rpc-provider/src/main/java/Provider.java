import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lqb.rpc.common.Request;
import com.lqb.rpc.common.Response;
import com.lqb.rpc.tool.DecodeMessage;
import com.lqb.rpc.tool.EncodeMessage;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * @Author: liqingbin
 * @Date: 2022/3/27 17:57
 */
public class Provider {

    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("waiting consumer connect");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("客户端连进来了");
                try (
                        InputStream is = new ObjectInputStream(socket.getInputStream());
                        OutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ) {

                    int len = 0;
                    byte[] buf = new byte[1024];
                    byte[] tempBuf = new byte[1024];
                    int temBufLength = 0;
                    StringBuilder sb = new StringBuilder();
                    while ((len = is.read(buf)) != -1) {
                        System.arraycopy(buf, 0, tempBuf, temBufLength, len);
                        temBufLength += len;
                        // 处理协议
                        ByteOrder order = ByteOrder.BIG_ENDIAN;// 处理下大小端
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
                                    decodeMessage(messageBytes, os);
                                }else {
                                    hasNext = false;
                                }
                            }else {
                                hasNext = false;
                            }
                        }
                        sb.append(new String(buf, 0, len));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decodeMessage(final byte[] bytes, final OutputStream os) {
        Request request = new DecodeMessage<Request>().decode(bytes, Request.class);
        doRequest(request, os);
    }

    public static void doRequest(Request request, final OutputStream os) {
        try {
            final String className = request.getClassName();
            final String methodName = request.getMethodName();
            final Class<?>[] paramTypes = request.getParamTypes();
            final Object[] params = request.getParams();


            final Class<?> clazz = Class.forName(className);

            Object object = null;
            Set<Class<?>> classes = ClassUtil.getClasses(clazz.getPackage().getName());
            for (Class<?> c : classes) {
                if (!c.isInterface()) {
                    Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(c.getInterfaces()));
                    if (interfaces.contains(clazz)) {
                        object = c.newInstance();
                    }
                }
            }

            Method method = null;
            Object result = null;
            if (params == null || params.length == 0) {
                method = clazz.getMethod(methodName);
                result = method.invoke(object);
            } else {
                method = clazz.getMethod(methodName, paramTypes);
                System.out.println(new Date().getTime());
                result = method.invoke(object, params);
            }

            final Response response = new Response();
            String resultStr = JSON.toJSONString(result, SerializerFeature.WriteClassName);
            response.setResult(resultStr);
            response.setSuccess(true);
            String s = JSON.toJSONString(response, SerializerFeature.WriteClassName);
            System.out.println("response = " + response);

            byte[] responseBytes = new EncodeMessage<Response>().encode(response);
            os.write(responseBytes);
            os.flush();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
