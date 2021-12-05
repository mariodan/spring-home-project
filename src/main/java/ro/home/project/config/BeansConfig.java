package ro.home.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import ro.home.project.domain.entity.DocumentDetails;
import ro.home.project.web.filter.RequestResponseLoggingInterceptor;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@EnableAsync
public class BeansConfig {

	@Bean
	public RestTemplate restTemplate(@Value("${config.log-rest-template:false}") boolean isLogEnabled) {
		RestTemplate restTemplate = new RestTemplate();

		if (isLogEnabled) {
			restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		}

		return restTemplate;
	}

	@Bean
	public BlockingQueue<DocumentDetails> documentDetailsProcessingQueue() {
		return new LinkedBlockingQueue<>(100);
	}

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("executor-");
		executor.initialize();
		return executor;
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
		return threadPoolTaskScheduler;
	}
}
