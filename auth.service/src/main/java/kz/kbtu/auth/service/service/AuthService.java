package kz.kbtu.auth.service.service;

import kz.kbtu.auth.service.domain.Role;
import kz.kbtu.auth.service.domain.User;
import kz.kbtu.auth.service.dto.AuthDTO.AuthResponse;
import kz.kbtu.auth.service.dto.AuthDTO.LoginRequest;
import kz.kbtu.auth.service.dto.AuthDTO.RegisterRequest;
import kz.kbtu.auth.service.events.UserRegisteredEvent;
import kz.kbtu.auth.service.producer.UserRegisteredProducer;
import kz.kbtu.auth.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRegisteredProducer userRegisteredProducer;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered: " + request.email());
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .name(request.name())
                .roles(Set.of(Role.ROLE_USER))
                .build();

        user = userRepository.save(user);


        userRegisteredProducer.publish(new UserRegisteredEvent(
                user.getId().toString(),
                user.getEmail(),
                user.getName()
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getEmail(), user.getName(), Role.ROLE_USER.name());
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String primaryRole = user.getRoles().stream()
                .findFirst()
                .map(Enum::name)
                .orElse(Role.ROLE_USER.name());

        return new AuthResponse(token, user.getEmail(), user.getName(), primaryRole);
    }
}