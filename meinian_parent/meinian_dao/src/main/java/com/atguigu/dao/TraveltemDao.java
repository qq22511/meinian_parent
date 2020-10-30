package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface TraveltemDao {
    void add(TravelItem travelItem);

    Page< TravelItem > findPage(String queryString);

    void deleteById(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    Long findCountByTravelItemItemId(Integer id);

    List< TravelItem > findAll();

    List< TravelItem > findTravelItemListById(Integer id);

}
