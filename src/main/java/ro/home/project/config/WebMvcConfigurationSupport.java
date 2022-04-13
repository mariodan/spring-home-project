package ro.home.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ro.home.project.web.filter.LoggerRequestInterceptorAdapter;

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
