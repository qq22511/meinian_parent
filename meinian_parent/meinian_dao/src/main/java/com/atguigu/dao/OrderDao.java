package com.atguigu.dao;

import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {


    List< Order> findByCondition(Order order);

    void add(Order order);

    Map findById(Integer id);

    /*。。。。*/

    int getTodayOrderNumber(String today);

    int getTodayVisitsNumber(String today);

    int getThisWeekAndMonthOrderNumber(Map< String, Object> paramWeek);

    int getThisWeekAndMonthVisitsNumber(Map< String, Object> paramWeekVisit);

    List< Map< String, Object>> findHotSetmeal();

}
