package org.app.dressy.repo;
import org.app.dressy.model.Image;
import org.app.dressy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepo extends JpaRepository<Image, String> {

    List<Image> findAllByUser(User user);
    List<Image> findAllByStyle(String style);
}
