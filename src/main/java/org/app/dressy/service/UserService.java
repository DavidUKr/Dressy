package org.app.dressy.service;
import lombok.RequiredArgsConstructor;
import org.app.dressy.model.UserDTO;
import org.app.dressy.repo.UserRepo;
import org.app.dressy.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserUtil userUtil;

    public UserDTO getUserById(String id) {
        return userUtil.patchDTOfromUser(userRepo.findById(id).orElse(null));
    }

    public List<UserDTO> getAllUsers() {
    }

    public void deleteUser(String id) {
    }

    public UserDTO updateUser(String id, UserDTO userDTO) {
    }
}
