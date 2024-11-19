package org.app.dressy.control;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.UserDTO;
import org.app.dressy.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get a list of all users
    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    // Get a user by ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    // Delete a user by ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Update a user by ID
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@PathVariable("id") String id, @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        return ResponseEntity.ok("User updated successfully");
    }
}
