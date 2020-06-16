package com.lz.dao;

import com.lz.entity.MiaoshaOrder;
import com.lz.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface OrderDao {

    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId") long goodsId);

    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "SELECT LAST_INSERT_ID()")
    long insert(OrderInfo orderInfo);

    int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
