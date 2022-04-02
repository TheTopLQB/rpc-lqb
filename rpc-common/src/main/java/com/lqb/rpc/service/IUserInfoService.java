package com.lqb.rpc.service;

import com.lqb.rpc.dao.UserInfo;

/**
 * @Author: liqingbin
 * @Date: 2022/3/27 17:46
 */
public interface IUserInfoService {
    void save(UserInfo userInfo);

    void deleteById(Long id);

    void update(UserInfo userInfo);

    UserInfo get(Long id);
}
