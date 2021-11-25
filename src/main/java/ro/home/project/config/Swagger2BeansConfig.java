package ro.home.project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@ConditionalOnExpression("${springfox.documentation.swagger.enabled:true} == true")
@EnableSwagger2
@Configuration
public class Swagger2BeansConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(Swagger2BeansConfig.class);

	public Swagger2BeansConfig() {
		LOGGER.debug("Swagger documentation is active.");
	}

	@Bean
	public Docket api(final Environment environment, final VersionInfoContributor versionInfoContributor) {

		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any()).build().pathMapping("/")
				.ignoredParameterTypes(BindingResult.class, HttpServletRequest.class, HttpServletResponse.class)
				.globalOperationParameters(Collections.emptyList())
				.apiInfo(apiInfo(environment, versionInfoContributor))
				.useDefaultResponseMessages(false);
	}

	@Bean
	public ApiInfo apiInfo(final Environment environment, final VersionInfoContributor versionInfoContributor) {
		final var versionInfo = versionInfoContributor.extractVersionInfo();
		var builder = new ApiInfoBuilder();
		builder.title(environment.getProperty("spring.application.name") + " API")
				.version(versionInfo.getVersion() == null ? null : versionInfo.getVersion().split("-")[0])
				.license("(C) Marian Home");
		return builder.build();
	}

}

