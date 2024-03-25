package com.runjian.auth.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * @author Miracle
 * @date 2023/4/17 15:35
 */
public class KeyGeneratorUtils {

    public static KeyPair generateRsaKey(){
        KeyPair keyPair = null;
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keyPair;
    }
}
