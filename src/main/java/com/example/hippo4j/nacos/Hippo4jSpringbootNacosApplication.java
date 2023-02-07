package com.example.hippo4j.nacos;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableDynamicThreadPool
@RestController
public class Hippo4jSpringbootNacosApplication implements InitializingBean {

	@Autowired
	@Qualifier("messageConsumeDynamicExecutor")
	private ThreadPoolTaskExecutor normalThreadPoolExecutor;

	@Autowired
	@Qualifier("messageProduceDynamicExecutor")
	private ExecutorService messageProduceDynamicExecutor;

	private ExecutorService ttlThreadPoolExecutor;

	public static void main(String[] args) {
		SpringApplication.run(Hippo4jSpringbootNacosApplication.class, args);
	}

	/**
	 * 有变化
	 */
	@GetMapping("add1")
	public void addNormalThread() {
		normalThreadPoolExecutor.execute(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * 无变化
	 */
	@GetMapping("add2")
	public void addThread() {
		ttlThreadPoolExecutor.execute(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ttlThreadPoolExecutor = TtlExecutors.getTtlExecutorService(messageProduceDynamicExecutor);
	}
}
