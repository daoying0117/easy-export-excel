package com.daoying.system.user;

import com.daoying.system.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户信息DTO
 * @author daoying
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    private String name;

    private String email;

    private Role role;

    private List<String> authorities;

}
