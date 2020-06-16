package com.lz.service;

import com.lz.dao.GoodsDao;
import com.lz.entity.MiaoshaGoods;
import com.lz.entity.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVO> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVO getGoodsVoByGoodsId(long id) {
        return goodsDao.getGoodsVoByGoodsId(id);
    }

    public void reduceStock(GoodsVO goodsVO) {
        MiaoshaGoods goods = new MiaoshaGoods();
        goods.setGoodsId(goodsVO.getId());
        goodsDao.reduceStock(goods);
    }
}
