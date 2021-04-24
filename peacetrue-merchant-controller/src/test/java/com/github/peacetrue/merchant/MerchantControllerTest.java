package com.github.peacetrue.merchant;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

import static com.github.peacetrue.merchant.MerchantServiceImplTest.EASY_RANDOM;

/**
 * @author xiayx
 */
@SpringBootTest(classes = TestControllerMerchantAutoConfiguration.class)
@AutoConfigureWebTestClient
@ActiveProfiles({"member-controller-test", "merchant-service-test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MerchantControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    @Order(10)
    void add() {
        this.client.post().uri("/merchants")
                .bodyValue(MerchantServiceImplTest.ADD)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MerchantVO.class).value((Consumer<MerchantVO>) vo -> MerchantServiceImplTest.vo = vo);
    }

    @Test
    @Order(20)
    void queryForPage() {
        this.client.get()
                .uri("/merchants?page=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.totalElements").isEqualTo(1);
    }

    @Test
    @Order(30)
    void queryForList() {
        this.client.get()
                .uri("/merchants")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.size()").isEqualTo(1);
    }

    @Test
    @Order(40)
    void get() {
        this.client.get()
                .uri("/merchants/{0}", MerchantServiceImplTest.vo.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MerchantVO.class).isEqualTo(MerchantServiceImplTest.vo);
    }


    @Test
    @Order(45)
    void exists() {
        this.client.get()
                .uri("/merchants/exists?id=", MerchantServiceImplTest.vo.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Boolean.class).isEqualTo(true);
    }


    @Test
    @Order(50)
    void modify() {
        MerchantModify modify = MerchantServiceImplTest.MODIFY;
        modify.setId(MerchantServiceImplTest.vo.getId());
        this.client.put()
                .uri("/merchants/{id}", MerchantServiceImplTest.vo.getId())
                .bodyValue(modify)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Integer.class).isEqualTo(1);
    }

    @Test
    @Order(55)
    void modifyPassword() {
        MerchantModifyPassword modify = EASY_RANDOM.nextObject(MerchantModifyPassword.class);
        modify.setId(MerchantServiceImplTest.vo.getId());
        modify.setOldPassword(MerchantServiceImplTest.ADD.getPassword());
        modify.setOperatorId(MerchantServiceImplTest.vo.getId());
        this.client.put()
                .uri("/merchants/{id}/password", MerchantServiceImplTest.vo.getId())
                .bodyValue(modify)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Integer.class).isEqualTo(1);
    }

    @Test
    @Order(56)
    void resetPassword() {
        MerchantResetPassword modify = new MerchantResetPassword();
        modify.setId(MerchantServiceImplTest.vo.getId());
        modify.setOperatorId(MerchantServiceImplTest.vo.getId());
        this.client.put()
                .uri("/merchants/{id}/password/reset", MerchantServiceImplTest.vo.getId())
                .bodyValue(modify)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Integer.class).isEqualTo(1);
    }

    @Test
    @Order(60)
    void delete() {
        this.client.delete()
                .uri("/merchants/{0}", MerchantServiceImplTest.vo.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Integer.class).isEqualTo(1);
    }

    @Test
    @Order(70)
    void register() {
        this.client.post().uri("/merchants/register")
                .bodyValue(MerchantServiceImplTest.REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MerchantVO.class).value((Consumer<MerchantVO>) vo -> MerchantServiceImplTest.vo = vo);
    }

}
