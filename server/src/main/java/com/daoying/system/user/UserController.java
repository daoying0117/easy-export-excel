package com.daoying.system.user;

import com.daoying.common.JsonResponse;
import com.daoying.system.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * @author daoying
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService service;

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @GetMapping("/getUserInfo")
    public JsonResponse getUserInfo(){
        return JsonResponse.ok(service.getUserInfo());
    }
}
