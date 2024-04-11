package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.enums.Role;
import org.airway.airwaybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findUserByUserRole(Role role);

    User findUserByEmail(String email);
}
