package com.github.peacetrue.merchant;

import com.github.peacetrue.spring.core.io.support.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * @author xiayx
 */
@Configuration
@EnableConfigurationProperties(ControllerMerchantProperties.class)
@ComponentScan(basePackageClasses = ControllerMerchantAutoConfiguration.class)
@PropertySource(value = "classpath:/application-merchant-controller.yml", factory = YamlPropertySourceFactory.class)
public class ControllerMerchantAutoConfiguration {


}
