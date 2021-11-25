package ro.home.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ro.home.project.web.filter.LoggerRequestInterceptorAdapter;
import ro.home.project.web.filter.RequestResponseLoggingInterceptor;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfigurationSupport implements WebMvcConfigurer {

    private final LoggerRequestInterceptorAdapter loggerRequestInterceptorAdapter;

    @Override
    public void addInterceptors(final InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(loggerRequestInterceptorAdapter);
    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.parseMediaType("application/json"));
    }

}
