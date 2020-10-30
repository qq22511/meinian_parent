package com.atguigu.job;

import com.atguigu.constant.RedisConstant;
import com.atguigu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    //定期清理图片
    public void clearImg(){
        //使用redis中的sdiff方法，返回于第一个集合和其他集合之间的差异
        //返回一个Set集合
        Set< String > set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,
                RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        Iterator< String > iterator = set.iterator();
        while (iterator.hasNext()){
            String pic = iterator.next();
            System.out.println("差异图片"+pic);

            QiniuUtils.deleteFileFromQiniu(pic);

            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }

    }
}
