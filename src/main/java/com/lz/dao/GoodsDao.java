package com.lz.dao;

import com.lz.entity.Goods;
import com.lz.entity.MiaoshaGoods;
import com.lz.entity.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Goods queryById(Long id);

    /**
     * 查询所有商品
     * @return GoodsVO
     */
    List<GoodsVO> listGoodsVo();

    /**
     * 根据商品id查询 商品
     * @param id
     * @return GoodsVO
     */
    GoodsVO getGoodsVoByGoodsId(@Param("id") long id);

    /**
     * 减库存并更新库存
     * @param goods
     * @return
     */
    int reduceStock(MiaoshaGoods goods);


}