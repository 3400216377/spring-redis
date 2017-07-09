package spring;

import java.util.concurrent.CountDownLatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.redis.service.OrderService;
import com.redis.service.RedisService;

@ContextConfiguration(locations = { "/spring-redis.xml", "/beans.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisServiceTest {
	@Autowired
	private OrderService orderService;
	@Autowired
	private RedisService redisService;

	private static final String ORDER_CODE = "orderCode_01";
	private static final int threadNum = 100;

	private static final CountDownLatch cd = new CountDownLatch(threadNum);

	@Before
	public void init() {
		//redisService.set(ORDER_CODE, "100", 2);
	}

	@SuppressWarnings("static-access")
	@Test
	public void test() {

		for (int i = 0; i < threadNum; i++) {

			new Thread(new RedisTest()).start();

			cd.countDown();

		}

		try {
			Thread.currentThread().sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private class RedisTest implements Runnable {

		public void run() {
			try {
				cd.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(Thread.currentThread().getName() + "=======>" + orderService.getOrderCount(ORDER_CODE));
		}

	}

}
