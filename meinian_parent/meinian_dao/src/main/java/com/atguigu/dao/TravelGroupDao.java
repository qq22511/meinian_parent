package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void setCheckGroupAndCheckItem(Map< String, Integer > hashMap);

    Page< TravelGroup > findPage(String queryString);

    TravelGroup findById(Integer id);

    List< Integer > findTravelItemIdByTravelgroupId(Integer id);


    void edit(TravelGroup travelGroup);

    void delete(Integer id);


    List< TravelGroup > findAll();

    List< TravelGroup > findTravelGroupListById(Integer id);

}
