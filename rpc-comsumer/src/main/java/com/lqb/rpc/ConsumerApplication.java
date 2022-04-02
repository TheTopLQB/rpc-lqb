package com.lqb.rpc;

import com.lqb.rpc.dao.UserInfo;
import com.lqb.rpc.service.IUserInfoService;

/**
 * @Author: liqingbin
 * @Date: 2022/3/27 18:13
 */
public class ConsumerApplication {
    public static void main(String[] args) {
        IUserInfoService userInfoService = new RpcProxy().getProxy(IUserInfoService.class);
        UserInfo userInfo = userInfoService.get(100L);
        System.out.println(userInfo.getName());
    }
}
