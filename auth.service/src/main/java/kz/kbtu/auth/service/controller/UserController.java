package kz.kbtu.auth.service.controller;

import kz.kbtu.auth.service.domain.User;
import kz.kbtu.auth.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll() {
        return userRepository.findAll();
    }
}