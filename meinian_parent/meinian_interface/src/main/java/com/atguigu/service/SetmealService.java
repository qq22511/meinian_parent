package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] travelgroupIds);

    PageResult findByPage(QueryPageBean queryPageBean);

    List< Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal findAllById(Integer id);

    List< Map< String, Object>> findSetmealCount();

}
