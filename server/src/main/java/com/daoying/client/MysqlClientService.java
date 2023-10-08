package com.daoying.client;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * Mysql 数据连接接口
 * @author daoying
 */

@RequiredArgsConstructor
@Service
public class MysqlClientService implements ClientService {

    private final DataSourceConfigService dataSourceClientService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean testClient(DataSourceConfig client) {
        return false;
    }

    @Override
    public Optional<DataSourceClient> buildClient(DataSourceConfig client) {
        DataSource dataSource = dataSourceClientService.buildDataSource(client);
        jdbcTemplate.setDataSource(dataSource);
        return Optional.of(new MysqlDataSourceClient(jdbcTemplate));
    }

    @Override
    public Optional<List<JSONObject>> execute(DataSourceClient client, String sql) {
        return Optional.empty();
    }
}
