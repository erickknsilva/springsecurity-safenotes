package tech.buildrun.safenotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.safenotes.entity.Role;
import tech.buildrun.safenotes.entity.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
