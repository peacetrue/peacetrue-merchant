package com.github.peacetrue.merchant;

import com.github.peacetrue.spring.data.relational.TableSchemaInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import java.util.Objects;

/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ServiceMerchantProperties.class)
@ComponentScan(basePackageClasses = ServiceMerchantAutoConfiguration.class)
@PropertySource("classpath:/application-merchant-service.yml")
public class ServiceMerchantAutoConfiguration {

    private ServiceMerchantProperties properties;

    public ServiceMerchantAutoConfiguration(ServiceMerchantProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public R2dbcEntityTemplate r2dbcEntityTemplate(DatabaseClient databaseClient) {
        return new R2dbcEntityTemplate(databaseClient);
    }

    @Bean(name = "memberTableSchemaInitializer")
    public TableSchemaInitializer tableSchemaInitializer() {
        return new TableSchemaInitializer(Merchant.class, "/schema-merchant-mysql.sql");
    }

}
