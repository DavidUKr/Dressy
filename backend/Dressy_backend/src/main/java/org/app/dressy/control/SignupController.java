package org.app.dressy.control;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.UserDTO;
import org.app.dressy.service.SignupService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/signup")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    // Register a new user
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        signupService.registerUser(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }
}