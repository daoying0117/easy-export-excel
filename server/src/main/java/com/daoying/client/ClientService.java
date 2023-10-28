package com.daoying.client;

import com.alibaba.fastjson.JSONObject;
import com.daoying.client.datasource.DataSourceClient;
import com.daoying.client.config.DataSourceConfig;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 数据连接接口
 * @author daoying
 */
public interface ClientService {


    /**
     * 测试连接
     * @param client 数据源连接配置
     * @return bool
     */
    boolean testClient(DataSourceConfig client);

    /**
     * 构建数据源连接
     * @param client 数据源连接配置
     * @return Optional
     */
    Optional<DataSourceClient> buildClient(DataSourceConfig client);

    /**
     * 构建数据源连接
     * @param client 数据源连接配置
     * @param schema 需要连接的数据库（索引）
     * @return Optional
     */
    Optional<DataSourceClient> buildClient(DataSourceConfig client,String schema);


    /**
     * 构建 连接地址
     * @param config 数据源配置
     * @return String
     */
    String buildClientAddr(DataSourceConfig config);

    /**
     * 执行DSL
     * @param client 数据源连接配置
     * @param dsl sql语句
     * @return Optional
     */
    Optional<List<JSONObject>> execute(DataSourceClient client, String dsl);

    /**
     * 获取索引（数据库表）
     * @param client 数据源连接
     * @return List
     */
    Optional<Set<String>> getIndexes(DataSourceClient client);
}
