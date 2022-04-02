package com.lqb.rpc.service.impl;

import com.lqb.rpc.dao.UserInfo;
import com.lqb.rpc.service.IUserInfoService;

/**
 * @Author: liqingbin
 * @Date: 2022/3/27 17:56
 */


public class UserInfoServiceImpl implements IUserInfoService {
    @Override
    public void save(UserInfo userInfo) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void update(UserInfo userInfo) {

    }

    @Override
    public UserInfo get(Long id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("孙悟空");
        userInfo.setAge(500);
        userInfo.setHeight(1.3f);
        userInfo.setId(1L);
        return userInfo;
    }
}
