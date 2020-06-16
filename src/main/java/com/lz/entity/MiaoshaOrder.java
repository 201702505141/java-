package com.lz.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "miaosha_order",schema="book")
public class MiaoshaOrder implements Serializable {
    private static final long serialVersionUID = -28885863078842832L;

    @Column(name= "id")
    private Long id;

    /**
     * 用户ID
     */
    @Column(name= "user_id")
    private Long userId;

    /**
     * 订单ID
     */
    @Column(name= "order_id")
    private Long orderId;

    /**
     * 商品ID
     */
    @Column(name= "goods_id")
    private Long goodsId;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

}