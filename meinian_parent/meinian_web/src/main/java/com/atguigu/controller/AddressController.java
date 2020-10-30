package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    @RequestMapping("/findAllMaps")
    public Map findAllMaps(){

        /*
        * 需要的数据类型
        * success: function (data) {
		*		for(var x=0;x<data.gridnMaps.length;x++){
		*			adds.push(new BMap.Point(data.gridnMaps[x].lng,data.gridnMaps[x].lat));
		*			addNames.push(data.nameMaps[x].addressName);
		*		}
		*	}
        *
        * */

        //查询数据
        List< Address > list = addressService.findAllMaps();
        List< Map > gridnMaps = new ArrayList<>();
        List< Map > nameMaps = new ArrayList<>();

        //组装成需要的数据格式
        for (Address address : list) {
            Map gridnMap = new HashMap();
            gridnMap.put("lng",address.getLng());
            gridnMap.put("lat",address.getLat());
            gridnMaps.add(gridnMap);

            Map nameMap = new HashMap<>();
            nameMap.put("addressName",address.getAddressName());
            nameMaps.add(nameMap);
        }

        //将数据放在map中
        Map<String , Object> map = new HashMap<>();
        map.put("addNames",nameMaps);
        map.put("gridnMaps",gridnMaps);
        return map;
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult page = addressService.findPage(queryPageBean);
        return page;
    }

    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){

        addressService.addAddress(address);
        return new Result(true,"添加地址成功");
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        addressService.deleteById(id);
        return new Result(true,"删除成功");
    }
}
