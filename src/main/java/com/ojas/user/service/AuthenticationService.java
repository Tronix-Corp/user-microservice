package com.ojas.user.service;

import com.ojas.user.dto.AuthenticationRequest;
import com.ojas.user.dto.AuthenticationResponse;
import com.ojas.user.dto.ResetPasswordRequest;
import com.ojas.user.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request) throws Exception;

    AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception;

    boolean updatePassword(Integer userID, ResetPasswordRequest request) throws Exception;
}
