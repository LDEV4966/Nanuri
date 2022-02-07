package com.example.nanuri.service.user;

import com.example.nanuri.domain.user.User;
import com.example.nanuri.domain.user.UserRepository;
import com.example.nanuri.handler.exception.ErrorCode;
import com.example.nanuri.handler.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
}
