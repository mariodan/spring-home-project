package ro.home.project.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import ro.home.project.domain.entity.DocumentDetails;
import ro.home.project.web.filter.RequestResponseLoggingInterceptor;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@EnableAsync
public class BeansConfig {

	@Value("${spring.application.defaultDateTimeFormat:UTC}")
	private String defaultDateTimeFormat;

	@Bean
	public RestTemplate restTemplate(@Value("${config.log-rest-template:false}") boolean isLogEnabled) {
		RestTemplate restTemplate = new RestTemplate();

		if (isLogEnabled) {
			restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
			restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		} else {
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());//utf-8 support
		}

		return restTemplate;
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {

			// formatter
			DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern(defaultDateTimeFormat);

			// deserializers
			builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));

			// serializers
			builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
			builder.serializers(new ZonedDateTimeSerializer(dateTimeFormatter));

		};
	}

	@Bean
	public BlockingQueue<DocumentDetails> documentDetailsProcessingQueue() {
		return new LinkedBlockingQueue<>(100);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		slr.setLocaleAttributeName("session.current.locale");
		slr.setTimeZoneAttributeName("session.current.timezone");
		return slr;
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
