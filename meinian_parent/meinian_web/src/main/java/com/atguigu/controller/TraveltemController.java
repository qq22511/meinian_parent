package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TraveltemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelItem")
public class TraveltemController {

    @Reference
    private TraveltemService traveltemService;

    //分页
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = traveltemService.findPage(queryPageBean);
        return pageResult;
    }

    //添加单人旅游项目
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
    public Result add (@RequestBody TravelItem travelItem){
        traveltemService.add(travelItem);
        return new Result(true , MessageConstant.ADD_TRAVELITEM_SUCCESS);
    }

    //根据ID删除单人旅游项目
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")//权限校验，使用TRAVELITEM_DELETE123测试
    public Result delete(Integer id){

        try {
            traveltemService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    //查询单人旅游项目
    @RequestMapping("/findById")
    public Result findById(Integer id){

        TravelItem travelItem = traveltemService.findById(id);
        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result edit(@RequestBody TravelItem travelItem){
        traveltemService.edit(travelItem);
        return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
    }


    //查询所有单人旅游项目
    @RequestMapping("/findAll")
    public Result findAll(){

        List<TravelItem> lists =  traveltemService.findAll();

        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,lists);
    }
}
