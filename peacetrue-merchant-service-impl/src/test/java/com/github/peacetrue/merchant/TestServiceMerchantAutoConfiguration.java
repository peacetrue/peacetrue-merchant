package com.github.peacetrue.merchant;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author xiayx
 */
@Configuration(proxyBeanMethods = false)
@ImportAutoConfiguration({
        R2dbcAutoConfiguration.class,
        R2dbcDataAutoConfiguration.class,
        R2dbcRepositoriesAutoConfiguration.class,
        R2dbcTransactionManagerAutoConfiguration.class,
})
@EnableAutoConfiguration
@ActiveProfiles("merchant-service-test")
public class TestServiceMerchantAutoConfiguration {

    @Autowired
    public void initializeDatabase(ConnectionFactory connectionFactory) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource[] scripts = new Resource[]{
                resourceLoader.getResource("classpath:/schema-merchant-h2.sql"),
//                resourceLoader.getResource("classpath:data.sql")
        };
        new ResourceDatabasePopulator(scripts).execute(connectionFactory).block();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
