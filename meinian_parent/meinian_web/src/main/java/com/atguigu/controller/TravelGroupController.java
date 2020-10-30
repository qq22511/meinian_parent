package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelgroup")
public class TravelGroupController {

    @Reference
    private TravelGroupService travelGroupService;

    //@RequestBody 主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)
    @RequestMapping("/add")
    public Result add(@RequestBody TravelGroup travelGroup, Integer[] travelItemIds) {
        travelGroupService.add(travelGroup, travelItemIds);
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = travelGroupService.findPage(queryPageBean);

        return pageResult;

    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {

        TravelGroup travelGroup = travelGroupService.findById(id);

        return new Result(true, MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroup);
    }

    /*
     * axios.get("/travelgroup/findTravelItemIdByTravelgroupId.do?id=" + row.id).then((res) => {

     * */
    //查询报团旅行团相关的自由旅行团
    @RequestMapping("/findTravelItemIdByTravelgroupId")
    public List< Integer > findTravelItemIdByTravelgroupId(Integer id){
        List< Integer > list = travelGroupService.findTravelItemIdByTravelgroupId(id);
        return list;
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody TravelGroup travelGroup , Integer[] travelItemIds){

        travelGroupService.edit(travelGroup,travelItemIds);

        return new Result(true,MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        List< TravelGroup > list = travelGroupService.findAll();

        return  new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
    }
}
