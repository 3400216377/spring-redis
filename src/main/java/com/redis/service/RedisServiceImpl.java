package com.redis.service;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 封装redis 缓存服务器服务接口
 * 
 * @author yu
 * 
 *         2017-6-25
 */
@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final String redisCode = "utf-8";


	/**
	 * @param key
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long del(final String... keys) {
		return (long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void set(final byte[] key, final byte[] value, final long liveTime) {
		redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public void set(String key, String value, long liveTime) {
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void set(byte[] key, byte[] value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String get(final String key) {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					byte[] bytes = connection.get(key.getBytes());
					if(bytes==null || bytes.length<=0){
						return null;
					}
					return new String(bytes, redisCode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	/**
	 * @param pattern
	 * @return
	 * @return
	 */
	public Set<String> Setkeys(String pattern) {
		return redisTemplate.keys(pattern);

	}

	/**
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean exists(final String key) {
		return (boolean) redisTemplate.execute(new RedisCallback() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
	}

	/**
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String flushDB() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long dbSize() {
		return (long) redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String ping() {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.ping();
			}
		});
	}

}
