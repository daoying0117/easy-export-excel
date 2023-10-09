package com.daoying.client;

import com.alibaba.fastjson.JSONObject;
import com.daoying.dsl.CommonSql;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mysql 数据连接接口
 * @author daoying
 */

@RequiredArgsConstructor
@Service
public class MysqlClientService implements ClientService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean testClient(DataSourceConfig client) {
        //构建连接
        Optional<DataSourceClient> dataSourceClient = buildClient(client);
        if (dataSourceClient.isEmpty()) {
            return false;
        }
        DataSourceClient sourceClient = dataSourceClient.get();
        Optional<Set<String>> indexes = getIndexes(sourceClient);
        return indexes.isPresent();
    }

    /**
     * 构建数据源
     * @param dataSourceClient 数据源配置
     * @return DataSource
     */
    public DataSource buildDataSource(DataSourceConfig dataSourceClient){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(buildClientAddr(dataSourceClient));
        dataSource.setUsername(dataSourceClient.getUsername());
        dataSource.setPassword(dataSourceClient.getPassword());
        return dataSource;
    }

    /**
     * 构建数据源
     * @param dataSourceClient 数据源配置
     * @return DataSource
     * @param database 数据库
     */
    public DataSource buildDataSource(DataSourceConfig dataSourceClient,String database){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(buildClientAddr(dataSourceClient,database));
        dataSource.setUsername(dataSourceClient.getUsername());
        dataSource.setPassword(dataSourceClient.getPassword());
        return dataSource;
    }

    @Override
    public Optional<DataSourceClient> buildClient(DataSourceConfig client) {
        DataSource dataSource = buildDataSource(client);
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.setQueryTimeout(5000);
        return Optional.of(new MysqlDataSourceClient(jdbcTemplate));
    }

    @Override
    public Optional<DataSourceClient> buildClient(DataSourceConfig client,String database) {
        DataSource dataSource = buildDataSource(client,database);
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.setQueryTimeout(5000);
        return Optional.of(new MysqlDataSourceClient(jdbcTemplate,database));
    }

    /**
     * jdbc:mysql://127.0.0.1:3306/easy_export_excel
     * @param config 数据源配置
     * @return String
     */
    @Override
    public String buildClientAddr(DataSourceConfig config) {
        return """
                jdbc:mysql://%s:%d?%s""".formatted(
                    config.getAddr(),
                    config.getPort(),
                    config.getOtherParams()
        );
    }

    private String buildClientAddr(DataSourceConfig config, String schema) {
        return """
                jdbc:mysql://%s:%d/%s?%s""".formatted(
                    config.getAddr(),
                    config.getPort(),
                    schema,
                    config.getOtherParams()
        );
    }

    @Override
    public Optional<List<JSONObject>> execute(DataSourceClient client, String dsl) {
        return Optional.empty();
    }

    @Override
    public Optional<Set<String>> getIndexes(DataSourceClient client) {
        if (client instanceof MysqlDataSourceClient){
            JdbcTemplate template = ((MysqlDataSourceClient) client).getJdbcTemplate();
            try {
                List<Map<String, Object>> maps = template.queryForList(CommonSql.SHOW_DATABASE);
                return Optional.of(maps.stream()
                        .map(m -> m.containsKey("database") ? (String) m.get("database") : "")
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toSet()));
            }catch (Exception e){
                log.error("执行SQL错误",e);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
