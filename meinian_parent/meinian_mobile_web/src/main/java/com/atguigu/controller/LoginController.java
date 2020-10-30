package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {


    @Reference
    public LoginService loginService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        String telephone = (String) map.get("telephone");
        Object validateCode = map.get("validateCode");
        String code = jedisPool.getResource().get(telephone);
        //比较验证码是否输入正确
        if (!validateCode.equals(code)) {
            return  new Result(false , MessageConstant.VALIDATECODE_ERROR);
        }
        Member count  = loginService.findByTelephone(telephone);

        //如果该账号不存在，则注册一个账号
        if (count == null){
            Member member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            loginService.add(member);
        }

        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);
        response.addCookie(cookie);

        return new Result(true, MessageConstant.LOGIN_SUCCESS);


    }



}
