package com.atguigu.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.pojo.Permission;
import com.atguigu.pojo.Role;
import com.atguigu.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //通过名字查询user
        com.atguigu.pojo.User user = userService.findUserByUserName(username);
        //判断是否注册
        if (user == null) {
            return null;
        }

        //创建集合用来添加所有的权限
        List< GrantedAuthority > lists  = new ArrayList<>();

        //获取当前登录用户所有角色
        Set< Role > roles = user.getRoles();
        for (Role role : roles) {
            //获取用户权限
            Set< Permission > permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //进行授权
                lists.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        String password = user.getPassword();
        return new User(username,password,lists);
    }
}
