package com.redis.mapper;

import com.redis.entity.UserOrder;

public interface UserOrderMapper {
    int deleteByPrimaryKey(String ordercode);

    int insert(UserOrder record);

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(String ordercode);

    int updateByPrimaryKeySelective(UserOrder record);

    int updateByPrimaryKey(UserOrder record);
}