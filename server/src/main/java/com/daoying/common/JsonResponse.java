package com.daoying.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 响应结果集
 * @author daoying
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonResponse {

    private Integer code;

    private String message;

    private Object data;

    private boolean success;

    public static JsonResponse ok(Object data,String message) {
        return JsonResponse.builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .success(true)
                .build();
    }

    public static JsonResponse ok(Object data) {
        return ok(data,"ok");
    }

    public static JsonResponse ok() {
        return ok(null,"ok");
    }

    public static JsonResponse error(String message) {
        return JsonResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message)
                .success(false)
                .build();
    }

    public JsonResponse code(Integer code){
        this.code = code;
        return this;
    }

    public JsonResponse message(String message){
        this.message = message;
        return this;
    }

    public JsonResponse data(Object data){
        this.data = data;
        return this;
    }
}
