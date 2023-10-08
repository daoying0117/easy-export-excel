package com.daoying.system.token;

import com.daoying.system.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * token 实体
 * @author daoying
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "sys_token")
public class Token {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    /**
     * 是否撤销
     */
    public boolean revoked;

    /**
     * 是否已经失效
     */
    public boolean expired;

    /**
     * 数据库中只有一个外键user_id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
