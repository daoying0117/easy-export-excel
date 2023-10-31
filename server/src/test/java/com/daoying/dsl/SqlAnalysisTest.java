package com.daoying.dsl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SqlAnalysisTest {

    @Resource
    private SqlAnalysis analysis;


    @Test
    public void testSearchPretreatment(){
        SqlAnalysis.AnalysisResult pretreatment = analysis
                .pretreatment("select * from sys_user where id = 1", true);
        assert pretreatment.qualified;
    }


    @Test
    public void testPretreatment(){
        SqlAnalysis.AnalysisResult pretreatment = analysis
                .pretreatment("update sys_client set id = 1 where addr = '1123';", false);
        assert pretreatment.qualified;
    }

    @Test
    public void testBadSearchPretreatment(){
        SqlAnalysis.AnalysisResult pretreatment = analysis
                .pretreatment("select sys_user from * where id = 1", true);
        assert !pretreatment.qualified;
    }


    @Test
    public void testBadPretreatment(){
        SqlAnalysis.AnalysisResult pretreatment = analysis
                .pretreatment("update *.user set id = 1 where addr = '1123';", false);
        assert !pretreatment.qualified;
    }
}