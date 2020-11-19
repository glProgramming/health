package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 任务类 清理垃圾图片
 */
@Component
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //1.获取集合SETMEAL_PIC_RESOURCES  -  SETMEAL_PIC_DB_RESOURCES=差集
        Set<String> fileNameSet = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        //2.调用七牛云接口删除图片 （已经存在）
        for (String fileName : fileNameSet) {
            QiniuUtils.deleteFileFromQiniu(fileName);
            //3.删除SETMEAL_PIC_RESOURCES集合中的垃圾图片
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        }
    }
}
