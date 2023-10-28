package com.daoying.client.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据连接数据接口
 * @author daoying
 */
@Repository
public interface DataSourceDataSourceConfigRepository
        extends JpaRepository<DataSourceConfig,Integer>,
        CrudRepository<DataSourceConfig,Integer> {
}
