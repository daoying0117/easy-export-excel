package com.daoying.client.config;

import com.daoying.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 数据源连接 Service
 * @author daoying
 */

@Service
@RequiredArgsConstructor
public class DataSourceConfigService {

    private final ClientService clientService;

}
