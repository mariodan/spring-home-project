package ro.home.project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class Application {

	@Value("${spring.application.defaultTimeZone:UTC}")
	private String defaultTimeZone;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	protected static ConfigurableApplicationContext run(final Class springAppClass, final String[] arg) {
		return run(springAppClass, null, arg);
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZone));
	}

	protected static ConfigurableApplicationContext run(final Class springAppClass,
														final List<String> appConfigFiles,
														final String[] args) {

		final List<String> configFiles = new ArrayList<>();
		configFiles.add("classpath:/core-application.yml");
		configFiles.add("file:/config/");
		if (appConfigFiles != null) {
			configFiles.addAll(appConfigFiles);
		}
		configFiles.add("classpath:/application.yml");

		final String configLocation = configFiles
				.stream()
				.reduce((s1, s2) -> s1 + ", " + s2)
				.orElseThrow(() -> new IllegalStateException("Invalid config files!!"));

		System.setProperty("spring.config.location", configLocation);

		return new SpringApplicationBuilder(springAppClass)
				.web(WebApplicationType.SERVLET)
				.run(args);
	}
}
