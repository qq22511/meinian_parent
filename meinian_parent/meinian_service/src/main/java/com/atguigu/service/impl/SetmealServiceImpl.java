package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;


    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] travelgroupId) {
        // 新增套餐
        setmealDao.add(setmeal);
        // 2：向套餐和跟团游的中间表中插入数据
        if (travelgroupId != null && travelgroupId.length > 0) {
            //绑定套餐和跟团游的多对多关系
            setSetmealAndTravelGroup(setmeal.getId(), travelgroupId);
        }
        savePic2Redis(setmeal.getImg());
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page< Setmeal > page = setmealDao.findByPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(), page.getResult());


    }

    @Override
    public List< Setmeal > findAll() {
        return setmealDao.findAll();
    }


    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Setmeal findAllById(Integer id) {
        return setmealDao.findAllById(id);
    }

    @Override
    public List< Map< String, Object > > findSetmealCount() {
        return setmealDao.findSetmealCount();
    }


    //绑定套餐和跟团游的多对多关系
    private void setSetmealAndTravelGroup(Integer id, Integer[] travelgroupId) {
        for (Integer checkgroupId : travelgroupId) {
            Map< String, Integer > map = new HashMap<>();
            map.put("travelgroup_id", checkgroupId);
            map.put("setmeal_id", id);
            setmealDao.setSetmealAndTravelGroup(map);
        }
    }


    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }
}
