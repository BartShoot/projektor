package top.cinema.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import top.cinema.app.entities.core.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
