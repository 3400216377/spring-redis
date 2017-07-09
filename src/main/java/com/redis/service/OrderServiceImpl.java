package com.redis.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.BloomFilter;
import com.redis.lock.DistributedLock;
import com.redis.mapper.UserOrderMapper;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private UserOrderMapper userOrderMapper;
	@Autowired
	private RedisService redisService;
	@Resource
	private DistributedLock lock;

	private BloomFilter<String> bf;

	private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

	@PostConstruct // 执行其他方法前进行初始化
	private void init() {
		
	}

	public Integer getOrderCount(String orderCode) {

		String count = redisService.get(orderCode);

		if (count != null) {
			logger.info("==========get data from cache==========");
			return Integer.valueOf(count);
		}

		if (lock.getLock()) {// 缓存数据过期时可以得到分布式锁，缓存对应的数据存在时则无法得到分布式锁

			logger.info("==========get data from db==========");

			Integer orderCount = userOrderMapper.selectByPrimaryKey(orderCode).getOrdercount();

			redisService.set(orderCode, orderCount.toString(), 1);// 如果只设置两个参数则默认缓存数据为永久不过期

			lock.releaseLock();

			return orderCount;
		} else {
			return 0;// 返回备份缓存数据
		}

	}
	//
	// public Integer getOrderCount(String orderCode) {
	// String count = redisService.get(orderCode);
	//
	// if (count != null) {
	// logger.info("==========get data from cache==========");
	// return Integer.valueOf(count);
	// }
	//
	// synchronized (redisService) {
	//
	// String _count = redisService.get(orderCode);
	//
	// if (_count != null) {
	// logger.info("==========get data from cache2==========");
	// return Integer.valueOf(_count);
	// }
	//
	// logger.info("==========get data from db==========");
	//
	// Integer orderCount =
	// userOrderMapper.selectByPrimaryKey(orderCode).getOrdercount();
	//
	// redisService.set(orderCode, orderCount.toString(), 1);//
	// 如果只设置两个参数则默认缓存数据为永久不过期
	//
	// return orderCount;
	// }
	//
	// }

}
