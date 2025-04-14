package com.security.demo.repository;

import com.security.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
   Optional<User> findByUserName(String username);

   Boolean existsByUserName(String username);

   Boolean existsByEmail(String email);
}
