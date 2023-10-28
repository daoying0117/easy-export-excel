package com.daoying.client.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Mysql 数据源连接
 * @author daoying
 */


@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MysqlDataSourceClient implements DataSourceClient {

    private JdbcTemplate jdbcTemplate;

    private String database;

    public MysqlDataSourceClient(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
