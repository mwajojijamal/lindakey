package com.lindakey.users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring automatically writes the SQL to find a user by their exact username or email
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}