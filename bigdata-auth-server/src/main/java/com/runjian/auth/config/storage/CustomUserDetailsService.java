package com.runjian.auth.config.storage;

import com.runjian.auth.feign.AuthRbacApi;
import com.runjian.auth.vo.response.AuthUserRsp;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.constant.LogTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/4/19 15:13
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService  {

    private final PasswordEncoder passwordEncoder;

    private final AuthRbacApi authRbacApi;
    /**
     * 用户端配置
     * @return
     */
    //@Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, InternalAuthenticationServiceException {

        AuthUserRsp authUser;
        try{
            CommonResponse<AuthUserRsp> authUserRsp = authRbacApi.getAuthUser(username);
            if (authUserRsp.isError()){
                throw new InternalAuthenticationServiceException("权限RBAC系统访问异常：" + authUserRsp.getMsg());
            }
            authUser = authUserRsp.getData();
            if (Objects.isNull(authUser)){
                throw new UsernameNotFoundException("用户名或密码错误");
            }
            if (authUser.getAuthorities().isEmpty()){
                throw new UsernameNotFoundException("用户没有任何角色，禁止访问");
            }
        } catch (AuthenticationException ex){
            throw ex;
        } catch (Exception ex){
            log.error(LogTemplate.PROCESS_LOG_MSG_TEMPLATE, "授权登录", "登录失败", ex);
            throw new InternalAuthenticationServiceException("权限RBAC系统访问异常");
        }

        return User.builder().username(authUser.getUsername())
                .password(authUser.getPassword())
                .disabled(!authUser.isEnabled())
                .accountExpired(!authUser.isAccountNonExpired())
                .accountLocked(!authUser.isAccountNonLocked())
                .credentialsExpired(!authUser.isCredentialsNonExpired())
                .roles(authUser.getAuthorities().toArray(String[]::new))
                .build();
    }
}
