package com.github.peacetrue.merchant;

import com.github.peacetrue.spring.core.io.support.YamlPropertySourceFactory;
import com.github.peacetrue.spring.util.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

/**
 * @author : xiayx
 * @since : 2020-12-05 07:18
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ReactiveUserDetailsService.class)
@PropertySource(value = "classpath:/application-merchant-security.yml", factory = YamlPropertySourceFactory.class)
public class MerchantReactiveSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ReactiveUserDetailsService.class)
    public ReactiveUserDetailsService userDetailsService(MerchantService merchantService) {
        return username -> {
            MerchantGet userGet = new MerchantGet(null, username);
            //调用接口时会自动注入当前用户，而获取当前用户需要通过此方法
            //所以该方法必须手动设置操作者标识，防止循环调用
            userGet.setOperatorId(MerchantService.MERCHANT_SYSTEM_ID);
            return merchantService.get(userGet)
                    .map(user -> {
                        SecurityMerchantVO securityMerchant = BeanUtils.map(user, SecurityMerchantVO.class);
                        securityMerchant.setAccountNonExpired(true);
                        securityMerchant.setAccountNonLocked(true);
                        //系统用户不允许登陆
                        securityMerchant.setEnabled(!MerchantService.MERCHANT_SYSTEM_ID.equals(user.getId()));
                        securityMerchant.setCredentialsNonExpired(true);
                        return securityMerchant;
                    })
                    ;
        };
    }
}
