package com.redis.entity;

public class UserOrder {
    private String ordercode;

    private Integer ordercount;

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public Integer getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(Integer ordercount) {
        this.ordercount = ordercount;
    }
}