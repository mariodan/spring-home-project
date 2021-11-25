package ro.home.project.config;

import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Instant;

@Configuration
@ConditionalOnExpression("'${spring.liquibase.enabled}' == 'true'")
public class SpringLiquibaseConfig {

	@Bean
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase springLiquibase = new SpringLiquibase() {

			@Override
			protected void performUpdate(Liquibase liquibase) throws LiquibaseException {
				final String dbTagName = Instant.now().toString();
				try {
					liquibase.forceReleaseLocks();
					liquibase.tag(dbTagName);
					super.performUpdate(liquibase);
				} catch (LiquibaseException e) {
					liquibase.rollback(dbTagName, "");
					throw e;
				}
			}
		};

		springLiquibase.setChangeLog("classpath:db/db.changelog-master.yaml");
		springLiquibase.setDataSource(dataSource);
		return springLiquibase;
	}
}
