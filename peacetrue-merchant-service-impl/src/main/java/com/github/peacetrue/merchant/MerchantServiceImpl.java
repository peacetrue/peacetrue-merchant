package com.github.peacetrue.merchant;

import com.github.peacetrue.core.Range;
import com.github.peacetrue.result.ResultType;
import com.github.peacetrue.result.exception.ResultException;
import com.github.peacetrue.spring.data.relational.core.query.CriteriaUtils;
import com.github.peacetrue.spring.data.relational.core.query.UpdateUtils;
import com.github.peacetrue.spring.util.BeanUtils;
import com.github.peacetrue.util.DateUtils;
import com.github.peacetrue.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.data.domain.*;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 商家服务实现
 *
 * @author xiayx
 */
@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private R2dbcEntityTemplate entityTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ServiceMerchantProperties properties;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Mono<Boolean> checkUsername(String username) {
        return this.exists(new MerchantGet(null, username))
                .checkpoint(String.format("检查商家[%s]是否已存在", username), true)
                .flatMap(exists -> {
                    if (exists) {
                        String message = String.format("商家[%s]已存在", username);
                        return Mono.error(new ResultException(ResultType.failure.name(), message));
                    }
                    return Mono.just(false);
                });
    }

    @Override
    @Transactional
    public Mono<MerchantVO> add(MerchantAdd params) {
        log.info("新增商家信息[{}]", params);
        String username = params.getUsername();
        return checkUsername(username)
                .flatMap(exists -> {
                    Merchant entity = BeanUtils.map(params, Merchant.class);
                    entity.setPassword(passwordEncoder.encode(params.getPassword()));
                    entity.setCreatorId(params.getOperatorId());
                    entity.setCreatedTime(LocalDateTime.now());
                    entity.setModifierId(entity.getCreatorId());
                    entity.setModifiedTime(entity.getCreatedTime());
                    return entityTemplate.insert(entity)
                            .map(item -> BeanUtils.map(item, MerchantVO.class))
                            .doOnNext(item -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(item, params)));
                });
    }

    public static Criteria buildCriteria(MerchantQuery params) {
        return CriteriaUtils.and(
                CriteriaUtils.nullableCriteria(CriteriaUtils.smartIn("id"), params::getId),
                CriteriaUtils.nullableCriteria(Criteria.where("username")::like, value -> "%" + value + "%", params::getUsername),
                CriteriaUtils.nullableCriteria(Criteria.where("creatorId")::is, params::getCreatorId),
                CriteriaUtils.nullableCriteria(Criteria.where("createdTime")::greaterThanOrEquals, params.getCreatedTime()::getLowerBound),
                CriteriaUtils.nullableCriteria(Criteria.where("createdTime")::lessThan, DateUtils.DATE_CELL_EXCLUDE, params.getCreatedTime()::getUpperBound),
                CriteriaUtils.nullableCriteria(Criteria.where("modifierId")::is, params::getModifierId),
                CriteriaUtils.nullableCriteria(Criteria.where("modifiedTime")::greaterThanOrEquals, params.getModifiedTime()::getLowerBound),
                CriteriaUtils.nullableCriteria(Criteria.where("modifiedTime")::lessThan, DateUtils.DATE_CELL_EXCLUDE, params.getModifiedTime()::getUpperBound)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<MerchantVO>> query(@Nullable MerchantQuery params, @Nullable Pageable pageable, String... projection) {
        log.info("分页查询商家信息[{}]", params);
        if (params == null) params = MerchantQuery.DEFAULT;
        if (params.getCreatedTime() == null) params.setCreatedTime(Range.LocalDateTime.DEFAULT);
        if (params.getModifiedTime() == null) params.setModifiedTime(Range.LocalDateTime.DEFAULT);
        Pageable finalPageable = pageable == null ? PageRequest.of(0, 10) : pageable;
        Criteria where = buildCriteria(params);

        return entityTemplate.count(Query.query(where), Merchant.class)
                .flatMap(total -> total == 0L ? Mono.empty() : Mono.just(total))
                .<Page<MerchantVO>>flatMap(total -> {
                    Query query = Query.query(where).with(finalPageable)
                            .sort(finalPageable.getSortOr(Sort.by("createdTime").descending()));
                    return entityTemplate.select(query, Merchant.class)
                            .map(item -> BeanUtils.map(item, MerchantVO.class))
                            .reduce(new ArrayList<>(), StreamUtils.reduceToCollection())
                            .map(item -> new PageImpl<>(item, finalPageable, total));
                })
                .switchIfEmpty(Mono.just(new PageImpl<>(Collections.emptyList())));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MerchantVO> query(@Nullable MerchantQuery params, @Nullable Sort sort, String... projection) {
        log.info("全量查询商家信息[{}]", params);
        if (params == null) params = MerchantQuery.DEFAULT;
        if (params.getCreatedTime() == null) params.setCreatedTime(Range.LocalDateTime.DEFAULT);
        if (params.getModifiedTime() == null) params.setModifiedTime(Range.LocalDateTime.DEFAULT);
        if (sort == null) sort = Sort.by("createdTime").descending();
        Criteria where = buildCriteria(params);
        Query query = Query.query(where).sort(sort).limit(properties.getMaxCountOfQuery());
        return entityTemplate.select(query, Merchant.class)
                .map(item -> BeanUtils.map(item, MerchantVO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MerchantVO> get(MerchantGet params, String... projection) {
        log.info("获取商家信息[{}]", params);
        Criteria where = CriteriaUtils.and(
                CriteriaUtils.nullableId(params::getId),
                CriteriaUtils.nullableCriteria(Criteria.where("username")::is, params::getUsername)
        );
        return entityTemplate.selectOne(Query.query(where), Merchant.class)
                .map(item -> BeanUtils.map(item, MerchantVO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Boolean> exists(MerchantGet params) {
        log.info("检查商家[{}]是否存在", params);
        Criteria where = CriteriaUtils.and(
                CriteriaUtils.nullableId(params::getId),
                CriteriaUtils.nullableCriteria(Criteria.where("username")::is, params::getUsername)
        );
        return entityTemplate.exists(Query.query(where), Merchant.class);
    }

    @Override
    @Transactional
    public Mono<Integer> modify(MerchantModify params) {
        log.info("修改商家信息[{}]", params);
        Criteria where = CriteriaUtils.id(params::getId);
        Query idQuery = Query.query(where);
        return entityTemplate.selectOne(idQuery, Merchant.class)
                .map(entity -> BeanUtils.map(entity, MerchantVO.class))
                .zipWhen(vo -> {
                    boolean usernameChanged = !vo.getUsername().equals(params.getUsername());
                    //商家名变了，检查是否存在
                    if (usernameChanged) return this.checkUsername(params.getUsername()).thenReturn(usernameChanged);
                    return Mono.just(usernameChanged);
                })
                .flatMap(tuple2 -> {
                    Merchant modify = BeanUtils.map(params, Merchant.class);
                    //商家名没变，不需要修改商家名
                    if (!tuple2.getT2()) modify.setUsername(null);
                    modify.setModifierId(params.getOperatorId());
                    modify.setModifiedTime(LocalDateTime.now());
                    Update update = UpdateUtils.selectiveUpdateFromExample(modify);
                    return entityTemplate.update(idQuery, update, Merchant.class)
                            .map(count -> Tuples.of(tuple2.getT1(), count));
                })
                .doOnNext(tuple2 -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(tuple2.getT1(), params)))
                .map(Tuple2::getT2)
                .switchIfEmpty(Mono.just(0));
    }

    @Override
    @Transactional
    public Mono<Integer> modifyPassword(MerchantModifyPassword params) {
        log.info("修改商家[{}]的密码", params);
        Criteria where = CriteriaUtils.id(params::getId);
        Query idQuery = Query.query(where);
        return entityTemplate.selectOne(idQuery, Merchant.class)
                .map(entity -> BeanUtils.map(entity, MerchantVO.class))
                .doOnNext(vo -> {
                    if (!passwordEncoder.matches(params.getOldPassword(), vo.getPassword())) {
                        throw new ResultException(ResultType.failure.name(),
                                String.format("商家[%s]原密码错误", vo.getUsername()));
                    }
                })
                .zipWhen(vo -> {
                    if (params.getOldPassword().equals(params.getNewPassword())) {
                        return Mono.just(1);
                    }

                    return entityTemplate.update(
                            idQuery,
                            Update.update("password", passwordEncoder.encode(params.getNewPassword()))
                                    .set("modifierId", params.getOperatorId())
                                    .set("modifiedTime", LocalDateTime.now()),
                            Merchant.class
                    );
                })
                .doOnNext(tuple2 -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(tuple2.getT1(), params)))
                .map(Tuple2::getT2)
                .switchIfEmpty(Mono.just(0));
    }

    @Override
    @Transactional
    public Mono<Integer> resetPassword(MerchantResetPassword params) {
        log.info("重置商家[{}]的密码", params);
        Criteria where = CriteriaUtils.id(params::getId);
        Query idQuery = Query.query(where);
        return entityTemplate.selectOne(idQuery, Merchant.class)
                .map(entity -> BeanUtils.map(entity, MerchantVO.class))
                .zipWhen(vo -> {
                    if (passwordEncoder.matches(properties.getDefaultPassword(), vo.getPassword())) {
                        return Mono.just(1);
                    }

                    return entityTemplate.update(
                            idQuery,
                            Update.update("password", passwordEncoder.encode(properties.getDefaultPassword()))
                                    .set("modifierId", params.getOperatorId())
                                    .set("modifiedTime", LocalDateTime.now()),
                            Merchant.class
                    );
                })
                .doOnNext(tuple2 -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(tuple2.getT1(), params)))
                .map(Tuple2::getT2)
                .switchIfEmpty(Mono.just(0));
    }

    @Override
    @Transactional
    public Mono<Integer> delete(MerchantDelete params) {
        log.info("删除商家信息[{}]", params);
        Criteria where = CriteriaUtils.id(params::getId);
        Query idQuery = Query.query(where);
        return entityTemplate.selectOne(idQuery, Merchant.class)
                .doOnNext(entity -> log.debug("取得商家[{}]", entity))
                .doOnNext(entity -> {
                    if (properties.getBuildInUsers().contains(entity.getUsername())) {
                        throw new ResultException(ResultType.failure.name(), "禁止删除内置用户[" + entity.getUsername() + "]");
                    }
                })
                .zipWhen(entity -> entityTemplate.delete(idQuery, Merchant.class)
                        .doOnNext(count -> log.debug("删除商家[{}]影响[{}]行", params.getId(), count))
                        .thenReturn(entity)
                )
                .map(item -> BeanUtils.map(item, MerchantVO.class))
                .doOnNext(vo -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(vo, params)))
                .map(vo -> 1)
                .switchIfEmpty(Mono.just(0));
    }

    @Override
    @Transactional
    public Mono<MerchantVO> register(MerchantRegister params) {
        log.info("注册商家[{}]", params);
        return checkUsername(params.getUsername())
                .flatMap(exists -> {
                    Merchant entity = BeanUtils.map(params, Merchant.class);
                    entity.setPassword(passwordEncoder.encode(params.getPassword()));
                    entity.setCreatorId(params.getOperatorId());
                    entity.setCreatedTime(LocalDateTime.now());
                    entity.setModifierId(entity.getCreatorId());
                    entity.setModifiedTime(entity.getCreatedTime());
                    return entityTemplate.insert(entity)
                            .map(item -> BeanUtils.map(item, MerchantVO.class))
                            .doOnNext(item -> eventPublisher.publishEvent(new PayloadApplicationEvent<>(item, params)));
                });
    }
}
