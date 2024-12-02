package org.app.dressy.control;

import org.app.dressy.security.model.AuthenticationRequest;
import org.app.dressy.security.model.AuthenticationResponse;
import org.app.dressy.security.model.RegisterRequest;
import org.app.dressy.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController{

    private final AuthenticationService authenticationService;

    @GetMapping(value = "/test_connection")
    public ResponseEntity<String> checkConnection() {
        return ResponseEntity.ok("Working connection and credentials! Great!");
    }


    @PostMapping(value = "/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
