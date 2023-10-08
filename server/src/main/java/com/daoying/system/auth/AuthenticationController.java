package com.daoying.system.auth;

import com.daoying.common.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 鉴权登录方法
 *
 * @author daoying
 */

@RestController
@RequestMapping("/api/system/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * 注册方法
     *
     * @param request 注册请求
     * @return Token
     */
    @PostMapping("/register")
    public JsonResponse register(
            @RequestBody RegisterRequest request
    ) {
        //校验参数是否合法
        service.checkParam(request.getAccount());
        return JsonResponse.ok(service.register(request));
    }

    /**
     * 鉴权方法
     *
     * @param request 鉴权请求
     * @return Token
     */
    @PostMapping("/authenticate")
    public JsonResponse authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return JsonResponse.ok(service.authenticate(request));
    }


    /**
     * 刷新Token方法
     *
     * @param request 刷新Token请求
     */
    @PostMapping("/refresh-token")
    public JsonResponse refreshToken(
            HttpServletRequest request
    ) {
        return JsonResponse.ok(service.refreshToken(request));
    }
}
