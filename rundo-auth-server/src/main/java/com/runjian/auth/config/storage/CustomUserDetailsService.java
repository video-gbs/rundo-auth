package com.runjian.auth.config.storage;

import com.runjian.auth.entity.RoleAuth;
import com.runjian.auth.entity.UserAuth;
import com.runjian.auth.feign.AuthRbacApi;
import com.runjian.auth.vo.dto.AuthUserDto;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/4/19 15:13
 */
@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final AuthRbacApi authRbacApi;
    /**
     * 用户端配置
     * @return
     */
    //@Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails = User.builder()
                .username("user")
                .password(passwordEncoder.encode("12345678"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CommonResponse<AuthUserDto> authUserRsp = authRbacApi.getAuthUser(username);
        if (authUserRsp.isError()){
            throw new BusinessException(BusinessErrorEnums.FEIGN_REQUEST_BUSINESS_ERROR, authUserRsp.getMsg());
        }
        AuthUserDto authUser = authUserRsp.getData();
        if (Objects.isNull(authUser)){
            return null;
        }
        return new UserAuth(authUser.getUsername(), authUser.getPassword(),
                authUser.isEnabled(), authUser.isAccountNonExpired(),
                authUser.isAccountNonLocked(), authUser.isCredentialsNonExpired(),
                authUser.isUsingMfa(), authUser.getMfaKey(),
                authUser.getAuthorities().stream().map(RoleAuth::new).collect(Collectors.toSet()));
    }
}
