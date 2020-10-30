package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {


    void add(Setmeal setmeal);

    void setSetmealAndTravelGroup(Map< String, Integer> map);

    Page< Setmeal > findByPage(String queryString);

    List< Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal findAllById(Integer id);

    List< Map< String, Object>> findSetmealCount();

}
