package com.example.hippo4j.nacos;

import cn.hippo4j.core.executor.support.ThreadFactoryBuilder;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Hippo4jSpringbootNacosApplicationTests {

	@Resource
	private MeterRegistry meterRegistry;

	@Test
	void contextLoads() {
	}

	@Autowired
	@Qualifier("messageProduceDynamicExecutor")
	private ThreadPoolExecutor threadPoolExecutor;

	@Test
	public void addThread() {
		threadPoolExecutor.submit(() -> {
			try {
				TimeUnit.MINUTES.sleep(2);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});

		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
			new Integer(1),
			ThreadFactoryBuilder.builder().daemon(true).prefix("client.scheduled.collect.data").build());

		scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
			System.err.println(meterRegistry.get("dynamic_thread_pool_active_size").gauge().value());
		}, 0, 200, TimeUnit.MILLISECONDS);

	}
}
