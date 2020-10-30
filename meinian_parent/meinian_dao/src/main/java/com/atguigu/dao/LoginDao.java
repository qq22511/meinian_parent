package com.atguigu.dao;

import com.atguigu.pojo.Member;

public interface LoginDao {

    Member findByTelephone(String telephone);

    void add(Member member);
}
