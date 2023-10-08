package com.daoying.client;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * 数据源连接 Service
 * @author daoying
 */

@Service
public class DataSourceConfigService {

    public DataSource buildDataSource(DataSourceConfig dataSourceClient){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceClient.getAddr());
        dataSource.setUsername(dataSourceClient.getUsername());
        dataSource.setPassword(dataSourceClient.getPassword());
        return dataSource;
    }

}
