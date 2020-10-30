package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TraveltemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TraveltemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TraveltemService.class)
@Transactional
public class TraveltemServiceImpl implements TraveltemService {

    @Autowired
    private TraveltemDao traveltemDao;

    //添加旅行团
    @Override
    public void add(TravelItem travelItem) {
        traveltemDao.add(travelItem);
    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //获取page
        Page< TravelItem > page = traveltemDao.findPage(queryPageBean.getQueryString());
        System.out.println(page);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {

        Long count = traveltemDao.findCountByTravelItemItemId(id);
        if (count > 0){
            throw  new RuntimeException("不允许删除");
        }
        traveltemDao.deleteById(id);


    }
    //以id查找旅行团信息
    @Override
    public TravelItem findById(Integer id) {
        return traveltemDao.findById(id);
    }
    //修改旅行团信息
    @Override
    public void edit(TravelItem travelItem) {
        traveltemDao.edit(travelItem);
    }

    @Override
    public List< TravelItem > findAll() {
        return traveltemDao.findAll();
    }
}
