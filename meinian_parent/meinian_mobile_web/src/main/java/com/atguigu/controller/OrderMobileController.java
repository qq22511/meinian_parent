package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import com.atguigu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/orderInfo")
public class OrderMobileController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) throws Exception {

        /*
        *   1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约

            2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约

            3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约

            4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约

            5、预约成功，更新当日的已预约人数
        * */

        //获取手机号码、验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从redis中拿验证码
        String code = jedisPool.getResource().get(telephone);
        //输入验证码和redis中的验证码做比较
        if (code == null || !code.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //进入方法内看是否有相同的手机号码，以防再次报该旅行团
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        Result result = orderService.order(map);


        //最后发送短信通知用户，报名旅行团成功

        return result;
    }
}
