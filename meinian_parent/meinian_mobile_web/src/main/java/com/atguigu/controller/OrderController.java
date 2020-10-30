package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {



    @Reference
    private OrderService orderService;

    @RequestMapping("/findById")
    public Result findById(Integer id){
        Map map = orderService.findById(id);
        return  new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
    }
}
