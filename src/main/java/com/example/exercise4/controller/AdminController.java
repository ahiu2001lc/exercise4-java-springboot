package com.example.exercise4.controller;

import com.example.exercise4.dto.request.RoleRequest;
import com.example.exercise4.dto.request.UpdatePasswordRequest;
import com.example.exercise4.dto.request.UpdateUserRequest;
import com.example.exercise4.dto.request.UserRequest;
import com.example.exercise4.dto.response.PageResponse;
import com.example.exercise4.dto.response.RoleResponse;
import com.example.exercise4.dto.response.UserResponse;
import com.example.exercise4.service.role.RoleService;
import com.example.exercise4.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@Valid @RequestBody RoleRequest request){
        return roleService.createRole(request);
    }

    @GetMapping("/roles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponse> getRole(@PathVariable Long id){
        RoleResponse response = roleService.getRole(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN', 'SECURITY')")
    public PageResponse<UserResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username,asc") String sort,
            @RequestParam(required = false) String q
    ){
       return userService.getUsers(page, size, sort, q);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN', 'SECURITY')")
    public UserResponse findUser(@PathVariable Long id){
        return userService.findUser(id);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ){
        return userService.updateUser(id, request);
    }

    @PostMapping("users/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequest request
    ){
        userService.updatePassword(id, request);
    }

    @DeleteMapping("users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
