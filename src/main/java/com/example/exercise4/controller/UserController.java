package com.example.exercise4.controller;

import com.example.exercise4.dto.response.UserResponse;
import com.example.exercise4.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserResponse getProfile(Authentication authentication) {
        return userService.getProfile(authentication.getName());
    }

    @GetMapping("/echo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Map<String, String>> echo(@RequestParam("msg") String msg) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Map<String, String> response = new LinkedHashMap<>();
        response.put("who", username);
        response.put("msg", msg);

        return ResponseEntity.ok(response);
    }
}

