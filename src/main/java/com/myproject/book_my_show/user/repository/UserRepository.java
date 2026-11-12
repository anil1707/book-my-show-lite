package com.myproject.book_my_show.user.repository;

import com.myproject.book_my_show.user.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Boolean existsByEmail(String email);
}
