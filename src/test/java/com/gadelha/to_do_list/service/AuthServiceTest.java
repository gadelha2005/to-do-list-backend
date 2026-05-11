package com.gadelha.to_do_list.service;

import com.gadelha.to_do_list.dto.request.LoginRequestDTO;
import com.gadelha.to_do_list.dto.request.RegisterRequestDTO;
import com.gadelha.to_do_list.dto.response.AuthResponseDTO;
import com.gadelha.to_do_list.exception.EmailAlreadyExistsException;
import com.gadelha.to_do_list.model.User;
import com.gadelha.to_do_list.repository.UserRepository;
import com.gadelha.to_do_list.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User mockUser() {
        return User.builder().id(1L).name("Pedro").email("pedro@email.com").password("hashed").build();
    }

    @Test
    @DisplayName("Deve registrar usuário e retornar token")
    void register_success_returnsToken() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Pedro", "pedro@email.com", "123456");

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(mockUser());
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponseDTO result = authService.register(dto);

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar EmailAlreadyExistsException quando e-mail já cadastrado")
    void register_duplicateEmail_throwsException() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Pedro", "pedro@email.com", "123456");

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve autenticar usuário e retornar token")
    void login_success_returnsToken() {
        LoginRequestDTO dto = new LoginRequestDTO("pedro@email.com", "123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(mockUser()));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponseDTO result = authService.login(dto);

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Deve delegar autenticação ao AuthenticationManager no login")
    void login_callsAuthenticationManager() {
        LoginRequestDTO dto = new LoginRequestDTO("pedro@email.com", "wrong");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(mockUser()));
        when(jwtService.generateToken(any())).thenReturn("token");

        authService.login(dto);

        verify(authenticationManager).authenticate(
                argThat(auth -> auth instanceof UsernamePasswordAuthenticationToken
                        && auth.getPrincipal().equals("pedro@email.com"))
        );
    }
}
