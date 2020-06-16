package com.lz.controller;


import com.lz.entity.MiaoshaOrder;
import com.lz.entity.MiaoshaUser;
import com.lz.entity.OrderInfo;
import com.lz.entity.vo.GoodsVO;
import com.lz.service.GoodsService;
import com.lz.service.MiaoshaService;
import com.lz.service.OrderService;
import com.lz.service.RedisService;
import com.lz.utils.result.CodeMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author m
 * @className MiaoshaController
 * @description MiaoshaController
 * @date 2020/5/30
 */

@Controller
@Api(value = "秒杀" ,description = "秒杀接口")
@RequestMapping(value = "miaosha/")
public class MiaoshaController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @ApiOperation(httpMethod = "POST",value = "秒杀",notes = "立即秒杀")
    @ApiImplicitParam(name = "id",value = "商品id",required = true ,dataType = "Long")
    @RequestMapping("do_miaosha")
    public String doSha(Model model, MiaoshaUser user,
                        @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        //判断库存
        GoodsVO goodsVO = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVO.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_REPET.getMsg());
            return "miaosha_fail";
        }
        //减少库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods",goodsVO);
        logger.info("goods:"+goodsVO+"\t"+"orderInfo:"+orderInfo);
        return "order_detail";
    }
}
