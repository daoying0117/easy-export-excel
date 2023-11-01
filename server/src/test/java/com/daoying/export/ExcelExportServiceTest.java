package com.daoying.export;

import com.alibaba.fastjson.JSONObject;
import com.daoying.export.config.ExportConfig;
import com.daoying.export.config.ExportFieldConfig;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest
class ExcelExportServiceTest {

    @Resource
    @Qualifier("excelExportService")
    private ExportService exportService;

    private List<JSONObject> mockData(){
        List<JSONObject> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            var obj = new JSONObject();
            obj.put("name","姓名" + i);
            obj.put("age",18 + i);
            obj.put("info","我是UI个" + i);
            result.add(obj);
        }

        return result;
    }

    private ExportConfig mockExportConfig(){
        List<ExportFieldConfig> fieldConfigs = new ArrayList<>();
        fieldConfigs.add(ExportFieldConfig.builder().field("name").fieldName("姓名").build());
        fieldConfigs.add(ExportFieldConfig.builder().field("age").fieldName("年龄").build());
        fieldConfigs.add(ExportFieldConfig.builder().field("info").fieldName("信息").build());
        return ExportConfig.builder().fieldConfigs(fieldConfigs).build();
    }


    @Test
    public void testExport(){
        var dataList = mockData();
        var config = mockExportConfig();
        assert exportService.export(dataList,"E:\\export_test","",config);
    }
}