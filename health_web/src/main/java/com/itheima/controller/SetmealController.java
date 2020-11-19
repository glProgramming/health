package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐控制层
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //注入jedisPool连接池对象
    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询所有检查组
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(MultipartFile imgFile) {
        try {
            //1.获取原始文件名
            String oldFileName = imgFile.getOriginalFilename();
            //2.文件名称处理 最终得到新的文件名（保证唯一）
            String suffix = oldFileName.substring(oldFileName.indexOf("."));
            String fileName = UUID.randomUUID().toString();
            String newFileName = fileName+suffix;
            //3.调用七牛云上传文件  字节数组，文件名
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),newFileName);
            //1.上传图片成功后，记录操作到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 新增套餐
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal,checkgroupIds);
            //新增套餐成功后，记录到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 套餐分页查询
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }


    /**
     * 回显套餐
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result findById(Integer setmealId) {
        try {
            Setmeal setmeal  = setmealService.findById(setmealId);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }


    /**
     * 根据套餐id 查询套餐ids
     */
    @RequestMapping(value = "/findCheckGroupIdsBySetmealId", method = RequestMethod.GET)
    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId) {
        return setmealService.findCheckGroupIdsBySetmealId(setmealId);
    }


    /**
     * 编辑套餐
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try {
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除套餐
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    public Result deleteById(Integer id) {
        try {
            setmealService.deleteById(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS2);
        }catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL2);
        }
    }

}
