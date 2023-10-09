package com.daoying.client;

import com.daoying.dsl.CommonSql;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class MysqlClientServiceTest {

    @Resource
    private  MysqlClientService mysqlClientService;

    private DataSourceConfig buildTestConfig(){
        return DataSourceConfig.builder()
                .addr("82.156.27.61")
                .port(3306)
                .username("root")
                .password("daoying").build();
    }

    private DataSourceConfig buildBadTestConfig(){
        return DataSourceConfig.builder()
                .addr("82.156.27.231")
                .port(3306)
                .username("root")
                .password("daoying").build();
    }

    @Test
    public void testTestClient(){
        DataSourceConfig sourceClient = buildTestConfig();
        boolean b = mysqlClientService.testClient(sourceClient);
        assert b;
    }

    @Test
    public void test2TestClient(){
        DataSourceConfig sourceClient = buildBadTestConfig();
        boolean b = mysqlClientService.testClient(sourceClient);
        assert !b;
    }

    @Test
    public void testBuildClient(){
        DataSourceConfig sourceClient = buildTestConfig();
        Optional<DataSourceClient> service = mysqlClientService.buildClient(sourceClient);
        if (service.isPresent()){
            DataSourceClient client = service.get();

            if (client instanceof MysqlDataSourceClient){
                JdbcTemplate jdbcTemplate = ((MysqlDataSourceClient) client).getJdbcTemplate();
                List<Map<String, Object>> maps = jdbcTemplate.queryForList(CommonSql.SHOW_DATABASE);
                for (Map<String, Object> map : maps) {
                    System.out.println(map);
                }
            }
        }
    }

    @Test
    public void testGetIndexes(){
        DataSourceConfig sourceClient = buildTestConfig();
        Optional<DataSourceClient> dataSourceClient = mysqlClientService.buildClient(sourceClient);
        if (dataSourceClient.isPresent()){
            Optional<Set<String>> indexes = mysqlClientService.getIndexes(dataSourceClient.get());
            System.out.println(indexes);
        }
    }

}