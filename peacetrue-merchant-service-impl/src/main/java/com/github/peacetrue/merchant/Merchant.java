package com.github.peacetrue.merchant;

import com.github.peacetrue.core.CreateModify;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商家实体类
 *
 * @author xiayx
 */
@Getter
@Setter
@ToString(exclude = "password")
@Table("merchant")
public class Merchant implements Serializable, com.github.peacetrue.core.Id<Long>, CreateModify<Long> {

    private static final long serialVersionUID = 0L;

    /** 主键 */
    @Id
    private Long id;
    /** 商家名 */
    private String username;
    /** 密码 */
    private String password;
    /** 创建者主键 */
    private Long creatorId;
    /** 创建时间 */
    private LocalDateTime createdTime;
    /** 修改者主键 */
    private Long modifierId;
    /** 最近修改时间 */
    private LocalDateTime modifiedTime;

}
