package kz.kbtu.auth.service.controller;

import kz.kbtu.auth.service.domain.User;
import kz.kbtu.auth.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MeController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal UserDetails principal) {
        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        return ResponseEntity.ok(Map.of(
                "id",    user.getId(),
                "email", user.getEmail(),
                "name",  user.getName(),
                "roles", user.getRoles()
        ));
    }
}