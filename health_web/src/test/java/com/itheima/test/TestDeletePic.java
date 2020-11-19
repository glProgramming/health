package com.itheima.test;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 删除垃圾图片测试类
 */
//@ContextConfiguration(locations = "classpath:spring-redis.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
public class TestDeletePic {

    //@Autowired
    private JedisPool jedisPool;

    //@Test
    public void testDeletePic(){
        //1.获取集合SETMEAL_PIC_RESOURCES  -  SETMEAL_PIC_DB_RESOURCES=差集
        Set<String> fileNameSet = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        
        //2.调用七牛云接口删除图片 （已经存在）
        for (String fileName : fileNameSet) {
            QiniuUtils.deleteFileFromQiniu(fileName);
            //3.删除SETMEAL_PIC_RESOURCES集合中的垃圾图片
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        }

    }

   // @Test
    public void test(){
        QiniuUtils.deleteFileFromQiniu("a1073e4e-d3ea-4035-ba7b-0a53d668cdc4.jpg");

    }
}
