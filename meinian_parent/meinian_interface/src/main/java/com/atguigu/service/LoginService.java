package com.atguigu.service;

import com.atguigu.pojo.Member;

public interface LoginService {
    Member findByTelephone(String telephone);

    void add(Member member);
}
