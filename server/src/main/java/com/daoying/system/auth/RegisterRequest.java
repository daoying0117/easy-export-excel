package com.daoying.system.auth;

import com.daoying.system.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册请求参数
 * @author daoying
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String name;
  private String account;
  private String email;
  private String password;
  private Role role;
  private String phone;
}
