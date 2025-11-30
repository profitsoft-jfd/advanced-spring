package dev.profitsoft.fd.springadvanced.conf;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

/**
 * Configuration for scheduled tasks with distributed locking using ShedLock.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(
    name = "scheduling.enabled",
    havingValue = "true",
    matchIfMissing = true
)
@EnableSchedulerLock(defaultLockAtMostFor = "PT1M")
public class SchedulingConfig {

  /**
   * Creates JDBC-based lock provider for distributed task coordination.
   *
   * @param dataSource data source
   * @return lock provider
   */
  @Bean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }

}
