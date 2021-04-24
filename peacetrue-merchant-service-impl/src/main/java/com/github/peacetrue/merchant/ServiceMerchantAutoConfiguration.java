package com.github.peacetrue.merchant;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
}
