package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.LoginDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = LoginService.class)
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    public LoginDao loginDao;

    @Override
    public Member findByTelephone(String telephone) {
        return loginDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        loginDao.add(member);
    }
}
