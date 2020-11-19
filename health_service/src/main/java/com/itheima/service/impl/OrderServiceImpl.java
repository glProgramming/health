package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 体检预约方法
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        //{"setmealId":"12","sex":"1","orderDate":"2020-07-16",
        //     "name":"张三","telephone":"13510773927","validateCode":"1234","idCard":"512501197203035172"}

        //获取用户输入的预约日期
        String orderDate = (String)map.get("orderDate");
        String telephone = (String)map.get("telephone");
        String name = (String)map.get("name");
        String sex = (String)map.get("sex");
        String idCard = (String)map.get("idCard");
        String setmealId = (String)map.get("setmealId");
        String orderType = (String)map.get("orderType");

        //将string转date(后续代码要用)
        Date orderDateNew = DateUtils.parseString2Date(orderDate);
        //1.判断当前日期是否可以预约    条件：用户输入日期  查询哪张表 t_ordersetting
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDateNew);
        if(orderSetting ==null){
            return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        // 2.判断当前日期是否预约已满
        int reservations = orderSetting.getReservations();
        int number = orderSetting.getNumber();
        if(reservations >=number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3.判断当前是否是会员  条件：手机号码   查询哪张表 t_member
        Member member = memberDao.findByTelephone(telephone);
        if(member == null){
            //4.自动注册会员
            member = new Member();
            member.setName(name);//姓名
            member.setSex(sex);//性别
            member.setIdCard(idCard);//身份证号码
            member.setPhoneNumber(telephone);//手机号码
            member.setRegTime(new Date());//注册时间
            memberDao.add(member);
        }
        else
        {
            //5.查询是否重复预约  条件：memberId+orderDate+setmealId    查询哪张表 ：Order
            Order order = new Order();
            order.setMemberId(member.getId());//会员id
            order.setOrderDate(orderDateNew);//预约日期
            order.setSetmealId(Integer.parseInt(setmealId));//套餐id
            List<Order> listOrder = orderDao.findByCondition(order);
            if(listOrder != null && listOrder.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }

        //5.进行体检预约t_order
        Order order = new Order();
        order.setMemberId(member.getId());//会员id
        order.setOrderDate(orderDateNew);//预约日期
        order.setOrderType(orderType);//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//默认未到诊状态
        order.setSetmealId(Integer.parseInt(setmealId));//套餐id
        orderDao.add(order);

        //6.修改已经预约人数t_ordersetting 条件：预约日期
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //7.将发送短信的代码 放到这里
        //注意：返回order对象 页面获取预约主键id,根据主键id查询套餐会员信息在成功页面展示
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }
    /**
     * 预约成功页面数据展示
     */
    @Override
    public Map findById4Detail(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
