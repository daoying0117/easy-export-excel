package com.daoying.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 数据连接数据接口
 * @author daoying
 */
@Repository
public interface DataSourceClientRepository
        extends JpaRepository<DataSourceClient,Integer>,
        CrudRepository<DataSourceClient,Integer> {
}
