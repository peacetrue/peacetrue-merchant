package com.github.peacetrue.merchant;

import com.github.peacetrue.spring.util.BeanUtils;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import reactor.test.StepVerifier;

import java.io.Serializable;


/**
 * @author : xiayx
 * @since : 2020-05-22 16:43
 **/
@SpringBootTest(classes = TestServiceMerchantAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MerchantServiceImplTest {

    public static final EasyRandom EASY_RANDOM = new EasyRandom(new EasyRandomParameters().randomize(Serializable.class, () -> "operatorName"));
    public static final MerchantAdd ADD = EASY_RANDOM.nextObject(MerchantAdd.class);
    public static final MerchantModify MODIFY = EASY_RANDOM.nextObject(MerchantModify.class);
    public static final MerchantRegister REGISTER = EASY_RANDOM.nextObject(MerchantRegister.class);
    public static MerchantVO vo;

    static {
        ADD.setOperatorId(EASY_RANDOM.nextObject(Long.class));
        MODIFY.setOperatorId(EASY_RANDOM.nextObject(Long.class));
        REGISTER.setOperatorId(EASY_RANDOM.nextObject(Long.class));
    }

    @Autowired
    private MerchantServiceImpl service;

    @Test
    @Order(10)
    void add() {
        service.add(ADD)
                .as(StepVerifier::create)
                .assertNext(data -> {
                    Assertions.assertEquals(data.getCreatorId(), ADD.getOperatorId());
                    vo = data;
                })
                .verifyComplete();
    }

    @Test
    @Order(20)
    void queryForPage() {
        MerchantQuery params = BeanUtils.map(vo, MerchantQuery.class);
        service.query(params, PageRequest.of(0, 10))
                .as(StepVerifier::create)
                .assertNext(page -> Assertions.assertEquals(1, page.getTotalElements()))
                .verifyComplete();
    }

    @Test
    @Order(30)
    void queryForList() {
        MerchantQuery params = BeanUtils.map(vo, MerchantQuery.class);
        service.query(params)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(40)
    void get() {
        MerchantGet params = BeanUtils.map(vo, MerchantGet.class);
        service.get(params)
                .as(StepVerifier::create)
                .assertNext(item -> Assertions.assertEquals(vo.getUsername(), item.getUsername()))
                .verifyComplete();
    }

    @Test
    @Order(45)
    void exists() {
        MerchantGet params = BeanUtils.map(vo, MerchantGet.class);
        service.exists(params)
                .as(StepVerifier::create)
                .assertNext(item -> Assertions.assertEquals(true, item))
                .verifyComplete();
    }

    @Test
    @Order(50)
    void modify() {
        MerchantModify params = MODIFY;
        params.setId(vo.getId());
        service.modify(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    @Order(55)
    void modifyPassword() {
        MerchantModifyPassword params = EASY_RANDOM.nextObject(MerchantModifyPassword.class);
        params.setId(vo.getId());
        params.setOldPassword(ADD.getPassword());
        params.setOperatorId(vo.getId());
        service.modifyPassword(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    @Order(56)
    void resetPassword() {
        MerchantResetPassword params = new MerchantResetPassword();
        params.setId(vo.getId());
        params.setOperatorId(vo.getId());
        service.resetPassword(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    @Order(60)
    void delete() {
        MerchantDelete params = new MerchantDelete(vo.getId());
        service.delete(params)
                .as(StepVerifier::create)
                .expectNext(1)
                .verifyComplete();
    }

    @Test
    @Order(70)
    void register() {
        service.register(REGISTER)
                .as(StepVerifier::create)
                .assertNext(data -> {
                    Assertions.assertEquals(data.getCreatorId(), REGISTER.getOperatorId());
                    vo = data;
                })
                .verifyComplete();
    }
}
