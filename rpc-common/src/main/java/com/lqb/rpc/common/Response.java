package com.lqb.rpc.common;

import java.io.Serializable;

/**
 * @Author: liqingbin
 * @Date: 2022/3/27 17:52
 */
public class Response implements Serializable {

    /**
     * 成功状态
     */
    private boolean success;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 错误
     */
    private Throwable throwable;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
