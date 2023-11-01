package com.daoying.utils;

import java.util.UUID;

/**
 * 文件名称工具方法
 * @author zyp
 */
public class FileNameUtils {

    /**
     * 生成UUID格式的fileName
     * @return String
     */
    public static String generateFileName(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 生成UUID格式的fileName并添加后缀
     * @return String
     */
    public static String generateFileName(String suffix){
        return UUID.randomUUID().toString().replaceAll("-","") + suffix;
    }

}
