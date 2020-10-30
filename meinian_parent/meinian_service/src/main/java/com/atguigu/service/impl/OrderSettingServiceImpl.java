package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List< OrderSetting > orderSettings) {
        for (OrderSetting orderSetting : orderSettings) {
            Long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if (count > 0){
                orderSettingDao.editNumberByOrderDate(orderSetting);
            }else {

                orderSettingDao.add(orderSetting);
            }

        }

    }

    @Override
    public List< Map > getOrderSettingByMonth(String date) {

        //获取符合条件的OrderSetting集合，并将其修为所需要格式
        String dateStart = date + "-1";
        String dateEnd = date + "-31";
        Map<String , Object> mapByMonth = new HashMap<>();
        mapByMonth.put("dateStart",dateStart);
        mapByMonth.put("dateEnd",dateEnd);
        List<OrderSetting> temp = orderSettingDao.getOrderSettingByMonth(mapByMonth);

        List<Map> list = new ArrayList<>();
        for (OrderSetting orderSetting : temp) {
            Map<String , Object> map = new HashMap<>();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            list.add(map);
        }
        return list;


    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        orderSettingDao.editNumberByOrderDate(orderSetting);
    }
}
