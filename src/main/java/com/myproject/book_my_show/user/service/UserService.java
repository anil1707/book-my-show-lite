package com.myproject.book_my_show.user.service;

import com.myproject.book_my_show.common.exception.ResourceNotFoundException;
import com.myproject.book_my_show.user.dto.UpdateProfileRequestDto;
import com.myproject.book_my_show.user.dto.UserResponseDto;
import com.myproject.book_my_show.user.entity.AppUser;
import com.myproject.book_my_show.user.enums.Role;
import com.myproject.book_my_show.user.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    // Used by the OTP login flow: returns the existing user or creates a new one
    @Transactional
    public AppUser findOrCreateByEmail(String rawEmail){
        String email = normalize(rawEmail);
        return userRepository.findByEmail(email)
                .orElseGet(
                        () -> {
                            AppUser user = new AppUser();
                            user.setEmail(email);
                            user.setRole(Role.USER);
                            return userRepository.save(user);
                        }
                );
    }


    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id){
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with given id: " + id));
    }


    @Transactional
    public UserResponseDto updateProfile(Long id, UpdateProfileRequestDto request){
        AppUser user = userRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("user not found of given id: " + id)
                );
        user.setName(request.name().trim());
        return  toResponse(user);
    }



    private @NonNull String normalize(String email) {
        return email.trim().toLowerCase();

    }

    private UserResponseDto toResponse(AppUser user){
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
