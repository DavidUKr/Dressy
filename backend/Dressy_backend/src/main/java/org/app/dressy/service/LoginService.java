package org.app.dressy.service;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public OAuth2ResourceServerProperties.Jwt getToken(String username, String password) {
        return null;
    }
}
