package com.XenoTest.Xeno.repository;

import com.XenoTest.Xeno.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
