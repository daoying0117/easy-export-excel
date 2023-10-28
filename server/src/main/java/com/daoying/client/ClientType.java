package com.daoying.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据源连接类型
 * @author daoying
 */

@Getter
@RequiredArgsConstructor
public enum ClientType {

    /**
     * ES
     */
    ES("elasticsearch"),

    /**
     * MYSQL
     */
    MYSQL("mysql");

    private final String type;
}
