package com.runjian.rbac.util;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/5/25 17:11
 */
@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserInfoMapper userInfoMapper;

    public UserInfo getUserInfo(){
        String userAccount = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserInfo> userInfoOp = userInfoMapper.selectByAccount(userAccount);
        if (userInfoOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND);
        }
        return userInfoOp.get();
    }

    public List<String> getRoleList(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
