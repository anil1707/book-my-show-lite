package com.myproject.book_my_show.auth.service;

import com.myproject.book_my_show.auth.dto.AuthResponseDto;
import com.myproject.book_my_show.auth.entity.OtpTokenEntity;
import com.myproject.book_my_show.auth.repository.OtpTokenRepository;
import com.myproject.book_my_show.common.exception.InvalidOtpException;
import com.myproject.book_my_show.common.exception.TooManyRequestsException;
import com.myproject.book_my_show.user.dto.UserResponseDto;
import com.myproject.book_my_show.user.entity.AppUser;
import com.myproject.book_my_show.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {

    Logger log = LoggerFactory.getLogger(AuthService.class);

    @Value("${otp.max-attempt:5}")
    private int EXPIRY_MINUTES;

    @Value("${otp.expiry-minutes:5}")
    private  int MAX_ATTEMPTS;

    private final OtpTokenRepository otpTokenRepository;
    private final SecureRandom random = new SecureRandom();

    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(OtpTokenRepository otpTokenRepository, UserService userService, JwtService jwtService){
        this.otpTokenRepository = otpTokenRepository;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Transactional
    public void requestOtp(String rawEmail){
        String email  = rawEmail.trim().toLowerCase();
//        otpTokenRepository.findFirstByEmailOrderByCreatedAtDesc(email).ifPresent(last -> {
//            if (last.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
//                throw new TooManyRequestsException("Please wait before requesting another OTP");
//            }
//        });
        String otp = String.format("%06d", random.nextInt(1_000_000));

        OtpTokenEntity token = new OtpTokenEntity();
        token.setEmail(email);
        token.setOtpHash(otp);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES));
        otpTokenRepository.save(token);
        // Dev only: replace with real email sending later
        log.info("OTP for {} is {}", email, otp);
    }

    @Transactional(noRollbackFor = InvalidOtpException.class)
    public AuthResponseDto verifyOtp(String rawEmail, String otp){
        String email = rawEmail.trim().toLowerCase();
        OtpTokenEntity otpToken = otpTokenRepository.findFirstByEmailAndConsumedFalseOrderByCreatedAtDesc(email)
                .orElseThrow(
                        ()-> new InvalidOtpException("Invalid ro expired otp")
                );
        if(otpToken.getExpiresAt().isBefore(LocalDateTime.now()) || otpToken.getAttempts() >= MAX_ATTEMPTS){
            throw new InvalidOtpException("Invalid or expired OTP");
        }

        if(!otpToken.getOtpHash().equals(otp)){
            otpToken.setAttempts(otpToken.getAttempts()+1);
            throw new InvalidOtpException("Invalid or expired OTP");
        }

        otpToken.setConsumed(true);

        AppUser user = userService.findOrCreateByEmail(email);

        String token = jwtService.generateToken(user);

        UserResponseDto userDto = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );

        return new AuthResponseDto(token, "Bearer", jwtService.getExpirationSeconds(), userDto);
    }

}
