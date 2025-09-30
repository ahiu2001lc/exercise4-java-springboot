package com.example.exercise4.service.user;

import com.example.exercise4.dto.request.UpdatePasswordRequest;
import com.example.exercise4.dto.request.UpdateUserRequest;
import com.example.exercise4.dto.request.UserRequest;
import com.example.exercise4.dto.response.EchoResponse;
import com.example.exercise4.dto.response.PageResponse;
import com.example.exercise4.dto.response.UserResponse;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserResponse getProfile(String username);

    UserResponse findUser(Long id);

    EchoResponse echo(Authentication auth, String msg);

    UserResponse createUser(UserRequest request);

    PageResponse<UserResponse> getUsers(int page, int size, String sort, String q);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void updatePassword(Long id, UpdatePasswordRequest request);

    void deleteUser(Long id);
}
