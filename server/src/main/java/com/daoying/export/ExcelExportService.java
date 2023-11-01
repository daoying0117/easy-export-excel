package com.daoying.export;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.daoying.export.config.ExportConfig;
import com.daoying.export.config.ExportFieldConfig;
import com.daoying.utils.FileNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * excel 导出方法
 * @author zyp
 */
@Service
@Slf4j
public class ExcelExportService implements ExportService {

    private static final String SEPARATOR = ".";

    private static final String SUFFIX = ".xlsx";

    @Override
    public String pretreatmentFileName(String path, String fileName) {
        //通过配置文件默认值保证生成文件路径不为null
        assert StringUtils.hasText(path);
        if (StringUtils.hasText(fileName)) {
            //判断文件名中是否存在'.'
            if (fileName.contains(SEPARATOR)) {
                //截取.前的内容
                fileName = fileName.split(SEPARATOR)[0];
            }

            //给文件名称添加后缀
            fileName = fileName + SUFFIX;
        } else {
            fileName = FileNameUtils.generateFileName(SUFFIX);
        }
        return path + "/" + fileName;
    }

    @Override
    public boolean export(
            List<JSONObject> dataList,
            String path,
            String fileName,
            ExportConfig config
    ) {
        String exportName = pretreatmentFileName(path, fileName);
        try {
            EasyExcel.write(exportName)
                    .head(head(config))
                    .sheet("sheet1")
                    .doWrite(dealExportData(dataList,config));
            return true;
        } catch (Exception e) {
            log.error("文件导出错误: {}",exportName,e);
            return false;
        }
    }

    /**
     * 处理需要导出的数据
     * @param dataList 需要导出的原始数据
     * @param config 导出配置
     * @return List
     */
    private List<List<String>> dealExportData(List<JSONObject> dataList,ExportConfig config){
        List<List<String>> result = new ArrayList<>();
        List<ExportFieldConfig> fieldConfigs = config.getFieldConfigs();
        if (Objects.nonNull(fieldConfigs) && !fieldConfigs.isEmpty()) {
            for (JSONObject jsonObject : dataList) {
                List<String> temp = new ArrayList<>();
                for (ExportFieldConfig fieldConfig : fieldConfigs) {
                    temp.add(jsonObject.getString(fieldConfig.field));
                }
                result.add(temp);
            }

        }

        return result;
    }

    /**
     * 构建表头
     *
     * @param config 导出配置
     * @return List
     */
    private List<List<String>> head(ExportConfig config) {
        List<List<String>> head = new ArrayList<>();
        List<ExportFieldConfig> fieldConfigs = config.getFieldConfigs();
        if (Objects.nonNull(fieldConfigs) && !fieldConfigs.isEmpty()) {
            for (ExportFieldConfig fieldConfig : fieldConfigs) {
                var fieldName = StringUtils.hasText(fieldConfig.getFieldName())
                        ? fieldConfig.fieldName : fieldConfig.field;
                head.add(List.of(fieldName));
            }
        }
        return head;
    }
}
