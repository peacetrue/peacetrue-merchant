package com.github.peacetrue.merchant;

import com.github.peacetrue.spring.core.io.support.YamlPropertySourceFactory;
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
@PropertySource(value = "classpath:/application-merchant-service.yml", factory = YamlPropertySourceFactory.class)
public class ServiceMerchantAutoConfiguration {

    private ServiceMerchantProperties properties;

    public ServiceMerchantAutoConfiguration(ServiceMerchantProperties properties) {
        this.properties = Objects.requireNonNull(properties);
    }
}
