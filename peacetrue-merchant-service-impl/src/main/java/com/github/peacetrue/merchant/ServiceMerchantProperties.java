package com.github.peacetrue.merchant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author xiayx
 */
@Data
@ConfigurationProperties(prefix = "peacetrue.merchant")
public class ServiceMerchantProperties {

    /** 忘记密码后，重置时使用的默认密码 */
    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "[0-9a-zA-Z.\\-*]+")
    private String defaultPassword = "123456";

    /** 查询的最大记录条数 */
    @NotNull
    @Min(1)
    private Integer maxCountOfQuery = 100;

    /** 初始化 SQL 位置 */
    private String sqlLocation = "/schema-merchant-mysql.sql";
}
