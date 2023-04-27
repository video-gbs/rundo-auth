package com.runjian.auth.service;

import com.runjian.auth.domain.vo.response.AuthorizeData;
import org.springframework.security.core.Authentication;

/**
 * @author Miracle
 * @date 2023/4/20 10:04
 */
public interface AuthService {

    AuthorizeData authenticate(Authentication authentication, String reqUrl, String reqMethod, String jsonStr);

}
