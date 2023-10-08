package com.daoying;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
class ServerApplicationTests {

    @Test
    void contextLoads() {
    }


    /**
     * 生成一个加密Key
     */
    @Test
    void buildEncoderKey(){
        String uuid = UUID.randomUUID().toString();
        String key = Encoders.BASE64.encode(uuid.getBytes());
        System.out.println(key);
    }

}
