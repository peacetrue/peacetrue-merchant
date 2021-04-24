package com.github.peacetrue.merchant;

import com.github.peacetrue.core.IdCapable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author xiayx
 */
@SpringBootApplication
public class MerchantApplication {

    public static void main(String[] args) {
        SpringApplication.run(MerchantApplication.class, args);
    }

    @Getter
    @Setter
    public static class IdUser extends User implements IdCapable<Long> {

        private Long id;

        public IdUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
            this.id = id;
        }

        public IdUser(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
            this.id = id;
        }
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return new ReactiveUserDetailsService() {
            @Autowired
            private MerchantService userService;

            @Override
            public Mono<UserDetails> findByUsername(String username) {
                return userService.get(new MerchantGet(null, username))
                        .map(user -> {
                            Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
                            return new IdUser(user.getId(), user.getUsername(), user.getPassword(), authorities);
                        });
            }
        };
    }


}
