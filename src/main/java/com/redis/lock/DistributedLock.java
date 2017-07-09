package com.redis.lock;

public interface DistributedLock {
	
	public boolean getLock();

	public void releaseLock();
	
}
