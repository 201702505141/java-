package com.lz.service;

import com.lz.dao.MiaoshaDao;
import com.lz.entity.MiaoshaUser;
import com.lz.entity.OrderInfo;
import com.lz.entity.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class MiaoshaService {

    @Resource
    private MiaoshaDao miaoshaDao;

    @Resource
    private GoodsService goodsService;

    @Resource
    private OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goodsVO) {
        //减库存下订单 写入秒杀订单
        goodsService.reduceStock(goodsVO);
        //
        return orderService.createOrder(user,goodsVO);
    }

}
