package com.lqb.rpc;
import com.lqb.rpc.common.Request;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: liqingbin
 * @Date: 2022/3/27 18:16
 */
public class RpcProxy{
    public <T> T getProxy(Class<T> clazz) {
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Request request = new Request();
                        request.setClassName(clazz.getPackage().getName() + "." + clazz.getSimpleName());
                        request.setMethodName(method.getName());
                        request.setParams(args);
                        request.setParamTypes(method.getParameterTypes());
                        Consumer consumer = new Consumer("127.0.0.1", 8888);
                        Object result = consumer.send(request);
                        return result;
                    }
                });
    }
}
