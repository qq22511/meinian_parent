package com.atguigu.service;

import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.Map;

public interface OrderService {




    Result order(Map map) throws Exception;

    Map findById(Integer id);
}
