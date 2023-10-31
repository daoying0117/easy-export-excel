package com.daoying.dsl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * SQL 解析器
 * @author daoying
 */

@Service
public class SqlAnalysis {

    private static final String END_SYMBOL = ";";

    @Data
    @AllArgsConstructor
    @Builder
    public static class AnalysisResult{
        /**
         * sql 是否符合规范
         */
        boolean qualified;

        /**
         * 处理后的SQL
         */
        String sql;

        /**
         * 错误信息
         */
        String err;
    }

    /**
     * 检查当前语句是否是规范SQL语句
     * @param sql SQL
     * @return bool
     */
    public AnalysisResult checkSql(String sql){
        try {
            CCJSqlParserUtil.parse(sql);
            return AnalysisResult.builder()
                    .qualified(true)
                    .sql(sql)
                    .build();
        } catch (JSQLParserException e) {
            return AnalysisResult.builder()
                    .qualified(false)
                    .sql(sql)
                    .err("sql格式错误!!!")
                    .build();
        }
    }

    /**
     * 检查当前语句是否是规范SQL查询语句
     * @param sql SQL
     * @return bool
     */
    public AnalysisResult checkSqlIsSearch(String sql){
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Select){
                return AnalysisResult.builder()
                        .qualified(true)
                        .sql(sql)
                        .build();
            }else {
                return AnalysisResult.builder()
                        .qualified(false)
                        .sql(sql)
                        .err("当前语句非查询语句")
                        .build();
            }

        } catch (JSQLParserException e) {
            return AnalysisResult.builder()
                    .qualified(false)
                    .sql(sql)
                    .err("sql格式错误!!!")
                    .build();
        }
    }

    /**
     * SQL 预处理
     * @param sql SQL
     * @param isSearch 是否是查询语句
     * @return AnalysisResult
     */
    public AnalysisResult pretreatment(String sql,boolean isSearch){

        if (StringUtils.hasText(sql)){
            //判断sql是否以';'结尾
            if (!sql.endsWith(END_SYMBOL)){
                sql += END_SYMBOL;
            }
            if (isSearch){
                return checkSqlIsSearch(sql);
            }else {
                return checkSql(sql) ;
            }
        }

        return AnalysisResult.builder()
                .qualified(false)
                .err("SQL语句为空")
                .build();
    }
}
