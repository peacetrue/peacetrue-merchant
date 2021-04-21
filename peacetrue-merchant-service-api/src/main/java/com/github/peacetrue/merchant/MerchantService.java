package com.github.peacetrue.merchant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;

/**
 * 商家服务接口
 *
 * @author xiayx
 */
public interface MerchantService {

    /** 系统商家 id */
    Long MEMBER_SYSTEM_ID = 0L;
    /** 管理员商家 id */
    Long MEMBER_ADMIN_ID = 1L;

    /** 新增 */
    Mono<MerchantVO> add(MerchantAdd params);

    /** 分页查询 */
    Mono<Page<MerchantVO>> query(@Nullable MerchantQuery params, @Nullable Pageable pageable, String... projection);

    /** 全量查询 */
    Flux<MerchantVO> query(MerchantQuery params, @Nullable Sort sort, String... projection);

    /** 全量查询 */
    default Flux<MerchantVO> query(MerchantQuery params, String... projection) {
        return this.query(params, (Sort) null, projection);
    }

    /** 获取 */
    Mono<MerchantVO> get(MerchantGet params, String... projection);

    /** 是否存在 */
    Mono<Boolean> exists(MerchantGet params);

    /** 修改 */
    Mono<Integer> modify(MerchantModify params);

    /** 修改密码 */
    Mono<Integer> modifyPassword(MerchantModifyPassword params);

    /** 重置密码 */
    Mono<Integer> resetPassword(MerchantResetPassword params);

    /** 删除 */
    Mono<Integer> delete(MerchantDelete params);

    /** 注册 */
    Mono<MerchantVO> register(MerchantRegister params);
}
