package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐服务接口实现类
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;
    //通过@Value注解获取生成静态页面目录
    @Value("${out_put_path}")
    private String outPutPath;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 新增套餐
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //第一步：保存套餐表
        setmealDao.add(setmeal);
        //第二步：获取检查组id
        Integer setmealId = setmeal.getId();
        //第三步：往检查组检查项中间表 遍历插入关系数据
        setCheckGroupAndCheckSetmeal(setmealId,checkgroupIds);
        //1.生成静态页面（套餐列表页面+套餐详情页面）
        generateMobileStaticHtml();

    }

    /**
     * 生成静态页面（套餐列表页面+套餐详情页面）
     */
    private void generateMobileStaticHtml() {
        //2.查询所有套餐列表数据
        List<Setmeal> setmealList = setmealDao.getSetmeal();
        //3.生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //4.生成套餐详情静态页面
        generateMobileSetmealDetailHtml(setmealList);
    }

    /**
     * 4.生成套餐列表静态页面
     * @param setmealList
     */
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        //5.封装模板中需要的数据格式
        Map map = new HashMap();
        map.put("setmealList",setmealList);
        //模板 mobile_setmeal.ftl  ==> m_setmeal.html
        //6.生成静态页面（套餐列表页面）
        generateHtml(map,"mobile_setmeal.ftl","m_setmeal.html");
    }

    /**
     * 3.生成套餐详情静态页面
     * @param setmealList
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        //7.遍历套餐列表
        for (Setmeal setmeal : setmealList) {
            Integer setmealId = setmeal.getId();
            //获取套餐详情（数据）
            Setmeal setmealDeail = setmealDao.findById(setmealId);
            Map map = new HashMap();
            map.put("setmeal",setmealDeail);
            //8.生成静态页面（套餐列表页面）
            generateHtml(map,"mobile_setmeal_detail.ftl","setmeal_detail_"+setmealId+".html");
        }
    }

    /**
     * 公共方法 生成静态页面
     * @param map  数据
     * @param templateFile 模板
     * @param outHtmlFile 输出文件名
     */
    private void generateHtml(Map map, String templateFile, String outHtmlFile) {
        try {
            //配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //获取模板对象
            Template template = configuration.getTemplate(templateFile);
            //生成文件名（包括生成目录）
            File file = new File(outPutPath+"\\"+outHtmlFile);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));//FileWriter(直接操作磁盘) BuffedWriter:（操作缓存） 性能好
            //填充数据
            template.process(map,out);
            //释放资源
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 套餐分页
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //第一步：设置分页参数
        PageHelper.startPage(currentPage,pageSize);
        //第二步：查询数据库（代码一定要紧跟设置分页代码  没有手动分页 select * from table where name = 'xx'  ）
        Page<Setmeal> setmealPage = setmealDao.selectByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }
    /**
     * 回显套餐
     */
    @Override
    public Setmeal findById(Integer setmealId) {
        return setmealDao.findById(setmealId);
    }
    /**
     * 根据套餐id 查询套餐ids
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId) {
        return setmealDao.findCheckGroupIdsBySetmealId(setmealId);
    }
    /**
     * 编辑套餐
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.先根据套餐id从检查组套餐中间表 删除关系数据
        setmealDao.deleteRelBySetmealId(setmeal.getId());
        //2.根据页面传入的检查组ids 和 套餐重新建立关系
        setCheckGroupAndCheckSetmeal(setmeal.getId(),checkgroupIds);
        //3根据套餐id 更新套餐数据
        setmealDao.edit(setmeal);
    }
    /**
     * 删除套餐
     */
    @Override
    public void deleteById(Integer id) {

       //1.	根据套餐id查询中间表是否存在关联关系
        int count = setmealDao.findCountGroupBySetmealId(id);
        if(count>0){
            throw new RuntimeException(MessageConstant.DELETE_SETMEAL_FAIL);
        }
        //2.	先根据id查询套餐数据
        Setmeal setmeal = setmealDao.findById(id);
        //3.	如果不存在，可以删除
        setmealDao.deleteById(id);
        //4.	获取图片名称，调用七牛云删除图片方法
        String img = setmeal.getImg();
        if(!StringUtils.isEmpty(setmeal)){
            QiniuUtils.deleteFileFromQiniu(img);
        }
    }
    /**
     * 套餐列表
     */
    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }
    /**
     * 套餐预约占比饼形图
     */
    @Override
    public Map<String, Object> getSetmealReport() {
        Map<String, Object> rsMap = new HashMap<>();

        //1.获取List<Map> 套餐名称、套餐预约数量
        List<Map> mapList = setmealDao.findSetmealNameCount();
        rsMap.put("setmealCount",mapList);

        //3.获取List<String> 套餐名称
        List<String> setmealNames = new ArrayList<>();
        if(mapList !=null && mapList.size()>0){
            for (Map map : mapList) {
                String name = (String)map.get("name");
                setmealNames.add(name);
            }
            rsMap.put("setmealNames",setmealNames);
        }
        return rsMap;
    }

    /**
     * 往检查组和套餐表插入数据
     * @param setmealId
     * @param checkgroupIds
     */
    private void setCheckGroupAndCheckSetmeal(Integer setmealId, Integer[] checkgroupIds) {
        if(checkgroupIds != null && checkgroupIds.length>0){
            for (Integer groupId : checkgroupIds) {
                Map<String,Object> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("setmealId",setmealId);
                setmealDao.setCheckGroupAndCheckSetmeal(map);
            }
        }
    }
}
