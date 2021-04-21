package com.github.peacetrue.merchant;

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
@PropertySource("classpath:/application-merchant-controller.yml")
public class ControllerMerchantAutoConfiguration {


}
