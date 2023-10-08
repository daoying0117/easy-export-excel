package com.daoying.system.config;

import com.daoying.system.token.TokenRepository;
import com.daoying.system.token.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT鉴权过滤器
 * @author daoying
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //判断请求是否为登录请求，如果是登录请求则不进行处理
        if (request.getServletPath().contains("/api/system/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        //从请求头中获取鉴权authHeader
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userAccount;

        //如果不存在Token或者Token不已Bearer开头，则不进行处理
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //从authHeader中截取出Token信息
        jwt = authHeader.substring(7);
        //从Token中获取userAccount(账户)
        userAccount = jwtService.extractUsername(jwt);
        //SecurityContextHolder 中的 Authentication 为空时，才进行处理
        if (userAccount != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //获取用户信息,此处有个疑虑是否要用this.的方式来代用userDetailsService
            UserDetails userDetails = null;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(userAccount);
            } catch (Exception e) {
                request.setAttribute("filter.error",e);
                //将异常分发到error处理器
                request.getRequestDispatcher("/internal/error").forward(request,response);
                return;
            }

            //从数据库中查询Token并判断Token状态是否正常
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            //如果Token有效,并且Token状态正常,将用户信息存储到SecurityContextHolder
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
