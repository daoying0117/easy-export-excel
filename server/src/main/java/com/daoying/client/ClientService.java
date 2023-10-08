package com.daoying.client;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Optional;

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
    boolean testClient(DataSourceClient client);

    /**
     * 构建数据源连接
     * @param client 数据源连接配置
     * @return Optional
     */
    Optional<DataSourceService> buildClient(DataSourceClient client);

    /**
     * 执行SQL
     * @param client 数据源连接配置
     * @param sql sql语句
     * @return Optional
     */
    Optional<List<JSONObject>> execute(DataSourceService client, String sql);
}
