package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;


    @Override
    public Result order(Map map) throws Exception {
        /*
        *   1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约

            2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约

            3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约

            4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约

            5、预约成功，更新当日的已预约人数
        * */

        // 使用预约时间查询预约设置表，看看是否可以 进行预约
        //（1）使用预约时间，查询预约设置表，判断是否有该记录
        String date1 = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(date1);

        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //查询是否满人
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (number <= reservations) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }


        //（2）使用手机号，查询会员表，判断当前预约人是否是会员
        String telephone = (String) map.get("telephone");
        // 根据手机号，查询会员表，判断当前预约人是否是会员
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {

            Integer id = member.getId();
            // 获取套餐id
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            // 根据预约信息查询是否已经预约
            Order order = new Order(id, date, null, null, setmealId);
            List< Order > list = orderDao.findByCondition(order);
            // 判断是否已经预约list不等于null，说明已经预约，不能重复预约
            if (list != null && list.size() > 0) {
                //已经预约过
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {

            //该会员尚未注册，需要注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setName((String)map.get("name"));
            member.setSex((String)map.get("sex"));
            member.setPhoneNumber((String)map.get("telephone"));
            member.setIdCard((String)map.get("idCard"));
            member.setRegTime(new Date()); // 会员注册时间，当前时间
            memberDao.add(member);
        }

        //（3）可以进行预约，更新预约设置表中预约人数的值，使其的值+1
        //可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);


        //（4）可以进行预约，向预约表中添加1条数据
        //保存预约信息到预约表
        Order order = new Order();
        order.setMemberId(member.getId()); //会员id
        order.setOrderDate(date); // 预约时间
        order.setOrderStatus(Order.ORDERSTATUS_NO); // 预约状态（已到诊/未到诊）
        order.setOrderType((String)map.get("orderType"));
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);
        //返回添加成功的信息
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);

    }

    @Override
    public Map findById(Integer id) {
        return orderDao.findById(id);
    }
}
