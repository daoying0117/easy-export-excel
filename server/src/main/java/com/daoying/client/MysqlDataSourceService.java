package com.daoying.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Mysql 数据源连接
 * @author daoying
 */


@Getter
@AllArgsConstructor
public class MysqlDataSourceService implements DataSourceService {

    private JdbcTemplate jdbcTemplate;
}
