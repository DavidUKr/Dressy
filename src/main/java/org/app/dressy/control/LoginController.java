package org.app.dressy.control;

import lombok.RequiredArgsConstructor;
import org.app.dressy.service.LoginService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // Get a token for user authentication
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public OAuth2ResourceServerProperties.Jwt getToken(@RequestParam("username") String username, @RequestParam("password") String password) {
        return loginService.getToken(username, password);
    }
}