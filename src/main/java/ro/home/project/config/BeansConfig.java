package ro.home.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ro.home.project.web.filter.RequestResponseLoggingInterceptor;

import java.util.Collections;

@Configuration
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
}
