package com.daoying.system.auth;

import com.daoying.system.exceptions.BadRequestException;
import com.daoying.system.exceptions.UserAlreadyExistException;
import com.daoying.system.token.JwtService;
import com.daoying.system.token.Token;
import com.daoying.system.token.TokenRepository;
import com.daoying.system.token.TokenType;
import com.daoying.system.user.User;
import com.daoying.system.user.UserInfoDTO;
import com.daoying.system.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 鉴权登录方法
 * @author daoying
 */

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  /**
   * 注册方法
   * @param request 注册请求体
   * @return Token
   */
  public AuthenticationResponse register(RegisterRequest request) {

    //构建用户信息
    var user = User.builder()
        .name(request.getName())
        .account(request.getAccount())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .email(request.getEmail())
        .phone(request.getPhone())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  /**
   * 登录方法
   * @param request 登录请求体
   * @return Token
   */
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getAccount(),
            request.getPassword()
        )
    );
    var user = repository.findByAccount(request.getAccount())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  /**
   * 保存用户Token
   * @param user 用户信息
   * @param jwtToken 用户Token
   */
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  /**
   * 注销用户所有Token
   * @param user 用户信息
   */
  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()){
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  /**
   * 校验参数
   * @param account 账户
   */
  public void checkParam(String account){
    //校验account是否重复
    repository.findByAccount(account)
            .ifPresent(user -> {
              throw new UserAlreadyExistException("当前账户已存在");
            });
  }

  /**
   * 刷新Token
   * @param request 请求体
   */
  public AuthenticationResponse refreshToken(
          HttpServletRequest request) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String account;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      throw new BadRequestException("请求头中未找到Bearer");
    }
    refreshToken = authHeader.substring(7);
    account = jwtService.extractUsername(refreshToken);
    if (account != null) {
      var user = repository.findByAccount(account)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
      }
    }
    return null;
  }

  /**
   * 获取当前用户信息和权限
   * @return 用户信息
   */
  public UserInfoDTO getUserInfo(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.nonNull(authentication)){
      User user = (User) authentication.getPrincipal();
      return UserInfoDTO.builder()
              .name(user.getName())
              .email(user.getEmail())
              .role(user.getRole())
              .authorities(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
              .build();
    }
    return UserInfoDTO.builder().build();
  }
}
