package com.myproject.book_my_show.auth.repository;

import com.myproject.book_my_show.auth.entity.OtpTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpTokenEntity, Long> {

    Optional<OtpTokenEntity> findFirstByEmailAndConsumedFalseOrderByCreatedAtDesc(String email);

    Optional<OtpTokenEntity> findFirstByEmailOrderByCreatedAtDesc(String email);

}
