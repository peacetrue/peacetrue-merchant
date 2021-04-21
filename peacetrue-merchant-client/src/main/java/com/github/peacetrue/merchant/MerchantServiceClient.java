package com.github.peacetrue.merchant;

import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * 商家客户端
 *
 * @author xiayx
 */
@ReactiveFeignClient(name = "peacetrue-merchant")
public interface MerchantServiceClient extends MerchantService {

    @PostMapping(value = "/merchants")
    Mono<MerchantVO> add(MerchantAdd params);

    @GetMapping(value = "/merchants", params = "page")
    Mono<Page<MerchantVO>> query(@Nullable @SpringQueryMap MerchantQuery params, @Nullable Pageable pageable, @SpringQueryMap String... projection);

    @GetMapping(value = "/merchants", params = "sort")
    Flux<MerchantVO> query(@SpringQueryMap MerchantQuery params, Sort sort, @SpringQueryMap String... projection);

    @GetMapping(value = "/merchants")
    Flux<MerchantVO> query(@SpringQueryMap MerchantQuery params, @SpringQueryMap String... projection);

    @GetMapping(value = "/merchants/get")
    Mono<MerchantVO> get(@SpringQueryMap MerchantGet params, @SpringQueryMap String... projection);

    @GetMapping(value = "/merchants/get")
    Mono<Boolean> exists(@SpringQueryMap MerchantGet params);

    @PutMapping(value = "/merchants")
    Mono<Integer> modify(MerchantModify params);

    @PutMapping(value = "/merchants/password")
    Mono<Integer> modifyPassword(MerchantModifyPassword params);

    @PutMapping(value = "/merchants/password/reset")
    Mono<Integer> resetPassword(MerchantResetPassword params);

    @GetMapping(value = "/merchants/delete")
    Mono<Integer> delete(@SpringQueryMap MerchantDelete params);

    @GetMapping(value = "/merchants/register")
    Mono<MerchantVO> register(@SpringQueryMap MerchantRegister params);

}
