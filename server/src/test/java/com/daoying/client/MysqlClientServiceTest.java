package com.daoying.client;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

@SpringBootTest
class MysqlClientServiceTest {

    @Resource
    private  MysqlClientService mysqlClientService;

    @Test
    public void testBuildClient(){
        DataSourceConfig sourceClient = DataSourceConfig.builder()
                .addr("jdbc:mysql://82.156.27.61:3306/easy_export_excel")
                .username("root")
                .password("daoying").build();

        Optional<DataSourceClient> service = mysqlClientService.buildClient(sourceClient);

        if (service.isPresent()){
            DataSourceClient client = service.get();

            if (client instanceof MysqlDataSourceClient){
                JdbcTemplate jdbcTemplate = ((MysqlDataSourceClient) client).getJdbcTemplate();
                jdbcTemplate.query("select  * from sys_user",r -> {
                    String string = r.getString(1);
                });
            }
        }
    }


}