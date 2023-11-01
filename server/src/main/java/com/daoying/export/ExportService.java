package com.daoying.export;

import com.alibaba.fastjson.JSONObject;
import com.daoying.export.config.ExportConfig;

import java.util.List;

/**
 * 导出文件service
 * @author zyp
 */
public interface ExportService {

    /**
     * 预处理导出文件名称
     * @param path 文件路径
     * @param fileName 文件名称
     * @return String
     */
    String pretreatmentFileName(String path,String fileName);

    /**
     * 导出方法
     * @param dataList 数据集合
     * @param path 导出文件路径
     * @param fileName 生成文件名称
     * @return bool
     */
    boolean export(List<JSONObject> dataList, String path, String fileName, ExportConfig config);
}
