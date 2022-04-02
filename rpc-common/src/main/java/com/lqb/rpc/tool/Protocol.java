package com.lqb.rpc.tool;

/**
 * @Author: liqingbin
 * @Date: 2022/3/30 22:08
 */
public class Protocol<T> {

    private byte[] packBytes; // 数据包长度 4字节
    private byte[] headerBytes; // 协议头长度 2字节
    private byte[] sequenceIdBytes; // sequenceId 4字节
    private byte operationByte; // 消息类型，比如心跳包、ACK包等 1字节
    private byte versionByte; // 消息

    private T body;

    public Protocol(byte[] bytes) {
        this.packBytes = new byte[4];
        this.headerBytes = new byte[2];
        this.sequenceIdBytes = new byte[4];
        System.arraycopy(bytes, 0, this.packBytes, 0, 4);
        System.arraycopy(bytes, 4, this.headerBytes, 0, 2);
        System.arraycopy(bytes, 6, this.sequenceIdBytes, 0, 4);
        this.operationByte = bytes[10];
        this.versionByte = bytes[11];


    }

}
