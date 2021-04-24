package com.github.peacetrue.merchant;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiayx
 */
@Configuration
@ImportAutoConfiguration(classes = {
        TestServiceMerchantAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        ControllerMerchantAutoConfiguration.class,
        SpringDataWebAutoConfiguration.class,
})
@EnableAutoConfiguration
public class TestControllerMerchantAutoConfiguration {

}
