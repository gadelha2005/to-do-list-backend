package com.gadelha.to_do_list.service;

import com.gadelha.to_do_list.dto.request.LoginRequestDTO;
import com.gadelha.to_do_list.dto.request.RegisterRequestDTO;
import com.gadelha.to_do_list.dto.response.AuthResponseDTO;
import com.gadelha.to_do_list.exception.EmailAlreadyExistsException;
import com.gadelha.to_do_list.model.User;
import com.gadelha.to_do_list.repository.UserRepository;
import com.gadelha.to_do_list.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        userRepository.save(user);
        return new AuthResponseDTO(jwtService.generateToken(user));
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        User user = userRepository.findByEmail(dto.email()).orElseThrow();
        return new AuthResponseDTO(jwtService.generateToken(user));
    }
}
