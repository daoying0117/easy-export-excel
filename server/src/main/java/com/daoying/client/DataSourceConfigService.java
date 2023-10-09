package com.daoying.client;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * 数据源连接 Service
 * @author daoying
 */

@Service
@RequiredArgsConstructor
public class DataSourceConfigService {

    private final ClientService clientService;

}
