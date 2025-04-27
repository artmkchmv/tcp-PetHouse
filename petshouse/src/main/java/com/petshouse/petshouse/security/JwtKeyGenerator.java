package com.petshouse.petshouse.security;

import javax.crypto.SecretKey;
import java.util.Base64;

import io.jsonwebtoken.Jwts;

public class JwtKeyGenerator {

    public static void main(String[] args) {
        String accessKey = generateBase64Key();
        String refreshKey = generateBase64Key();

        System.out.println("jwt.secret.access=" + accessKey);
        System.out.println("jwt.secret.refresh=" + refreshKey);
    }

    private static String generateBase64Key() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
