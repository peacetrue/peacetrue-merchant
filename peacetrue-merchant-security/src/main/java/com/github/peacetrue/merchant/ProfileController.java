package com.github.peacetrue.merchant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 当前登陆用户
 *
 * @author : xiayx
 * @since : 2020-12-05 06:34
 **/
@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private MerchantService merchantService;

    @GetMapping(path = {"", "/*"})
    public Mono<MerchantVO> get() {
        log.info("获取当前用户信息");
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> (MerchantVO) context.getAuthentication().getPrincipal());
    }

    @PutMapping(path = {"", "/*"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> modifyByForm(Mono<MerchantModify> mono) {
        log.info("修改当前用户(请求方法+表单参数)");
        return get().zipWith(mono)
                .flatMap(tuple2 -> {
                    tuple2.getT2().setId(tuple2.getT1().getId());
                    return merchantService.modify(tuple2.getT2());
                });
    }

    @PutMapping(path = {"/password", "/*/password"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> modifyPasswordByForm(Mono<MerchantModifyPassword> mono) {
        log.info("修改当前用户密码(请求方法+表单参数)");
        return get().zipWith(mono)
                .flatMap(tuple2 -> {
                    tuple2.getT2().setId(tuple2.getT1().getId());
                    return merchantService.modifyPassword(tuple2.getT2());
                });
    }

    @PutMapping(path = {"", "/*"}, params = "_type=password", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> modifyPasswordByFormForRa(Mono<MerchantModifyPassword> mono) {
        return modifyPasswordByForm(mono);
    }

}
