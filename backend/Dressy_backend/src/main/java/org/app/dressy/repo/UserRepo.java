package org.app.dressy.repo;

import lombok.RequiredArgsConstructor;
import org.app.dressy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findById(String id);
}
