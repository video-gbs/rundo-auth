package com.runjian.auth.utils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Miracle
 * @date 2023/8/17 10:33
 */
public class ValidUrlUtils {

    public static boolean validateUri(String redirectUri) {
        try {
            URI validRedirectUri = new URI(redirectUri);
            return validRedirectUri.getFragment() == null;
        } catch (URISyntaxException ex) {
            return false;
        }
    }
}
