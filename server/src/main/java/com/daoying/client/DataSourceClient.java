package com.daoying.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源连接实体
 * @author daoying
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "sys_client")
public class DataSourceClient {

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 连接名称
     */
    private String name;

    /**
     * 连接地址
     */
    private String addr;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 备注
     */
    private String memo;

    /**
     * 连接类型
     */
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    /**
     * 状态 -1：删除 0: 为初始化 1:正常
     */
    private Integer status;

    /**
     * 其他参数
     */
    private String otherParams;
}
