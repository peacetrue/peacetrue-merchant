package com.github.peacetrue.merchant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 商家控制器
 *
 * @author xiayx
 */
@Slf4j
@RestController
@RequestMapping(value = "/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<MerchantVO> addByForm(MerchantAdd params) {
        log.info("新增商家信息(请求方法+表单参数)[{}]", params);
        return merchantService.add(params);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MerchantVO> addByJson(@RequestBody MerchantAdd params) {
        log.info("新增商家信息(请求方法+JSON参数)[{}]", params);
        return merchantService.add(params);
    }

    @GetMapping(params = "page")
    public Mono<Page<MerchantVO>> query(MerchantQuery params, Pageable pageable, String... projection) {
        log.info("分页查询商家信息(请求方法+参数变量)[{}]", params);
        return merchantService.query(params, pageable, projection);
    }

    @GetMapping
    public Flux<MerchantVO> query(MerchantQuery params, Sort sort, String... projection) {
        log.info("全量查询商家信息(请求方法+参数变量)[{}]", params);
        return merchantService.query(params, sort, projection);
    }

    @GetMapping("/{id}")
    public Mono<MerchantVO> getByUrlPathVariable(@PathVariable Long id, String... projection) {
        log.info("获取商家信息(请求方法+路径变量)详情[{}]", id);
        return merchantService.get(new MerchantGet(id), projection);
    }

    @RequestMapping("/get")
    public Mono<MerchantVO> getByPath(MerchantGet params, String... projection) {
        log.info("获取商家信息(请求路径+参数变量)详情[{}]", params);
        return merchantService.get(params, projection);
    }

    @RequestMapping("/exists")
    public Mono<Boolean> existsByPath(MerchantGet params) {
        log.info("检查商家(请求路径+参数变量)[{}]是否存在", params);
        return merchantService.exists(params);
    }

    @PutMapping(value = {"", "/*"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> modifyByForm(MerchantModify params) {
        log.info("修改商家信息(请求方法+表单参数)[{}]", params);
        return merchantService.modify(params);
    }

    @PutMapping(value = {"", "/*"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> modifyByJson(@RequestBody MerchantModify params) {
        log.info("修改商家信息(请求方法+JSON参数)[{}]", params);
        return merchantService.modify(params);
    }

    @PutMapping(value = {"/password", "/*/password"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> modifyPasswordByForm(MerchantModifyPassword params) {
        log.info("修改商家密码(请求方法+表单参数)[{}]", params);
        return merchantService.modifyPassword(params);
    }

    @PutMapping(value = {"/password", "/*/password"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> modifyPasswordByJson(@RequestBody MerchantModifyPassword params) {
        log.info("修改商家密码(请求方法+JSON参数)[{}]", params);
        return merchantService.modifyPassword(params);
    }

    @PutMapping(value = {"/password/reset", "/*/password/reset"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Integer> resetPasswordByForm(MerchantResetPassword params) {
        log.info("重置商家密码(请求方法+表单参数)[{}]", params);
        return merchantService.resetPassword(params);
    }

    @PutMapping(value = {"/password/reset", "/*/password/reset"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Integer> resetPasswordByJson(@RequestBody MerchantResetPassword params) {
        log.info("重置商家密码(请求方法+JSON参数)[{}]", params);
        return merchantService.resetPassword(params);
    }

    @DeleteMapping("/{id}")
    public Mono<Integer> deleteByUrlPathVariable(@PathVariable Long id) {
        log.info("删除商家信息(请求方法+URL路径变量)[{}]", id);
        return merchantService.delete(new MerchantDelete(id));
    }

    @DeleteMapping(params = "id")
    public Mono<Integer> deleteByUrlParamVariable(MerchantDelete params) {
        log.info("删除商家信息(请求方法+URL参数变量)[{}]", params);
        return merchantService.delete(params);
    }

    @RequestMapping(value = "/delete")
    public Mono<Integer> deleteByPath(MerchantDelete params) {
        log.info("删除商家信息(请求路径+URL参数变量)[{}]", params);
        return merchantService.delete(params);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<MerchantVO> registerByForm(MerchantRegister params) {
        log.info("注册商家信息(请求方法+表单参数)[{}]", params);
        params.setOperatorId(1L);
        return merchantService.register(params);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MerchantVO> registerByJson(@RequestBody MerchantRegister params) {
        log.info("注册商家信息(请求方法+JSON参数)[{}]", params);
        params.setOperatorId(1L);
        return merchantService.register(params);
    }

}
