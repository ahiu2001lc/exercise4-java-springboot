package com.example.exercise4.service.user;

import com.example.exercise4.dto.request.UpdatePasswordRequest;
import com.example.exercise4.dto.request.UpdateUserRequest;
import com.example.exercise4.dto.request.UserRequest;
import com.example.exercise4.dto.response.EchoResponse;
import com.example.exercise4.dto.response.PageResponse;
import com.example.exercise4.dto.response.UserResponse;
import com.example.exercise4.entity.RoleEntity;
import com.example.exercise4.entity.UserEntity;
import com.example.exercise4.entity.UserRoleEntity;
import com.example.exercise4.exception.ExceptionMessages;
import com.example.exercise4.mapper.UserMapper;
import com.example.exercise4.repository.RoleRepository;
import com.example.exercise4.repository.UserRepository;
import com.example.exercise4.service.redis.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public UserResponse getProfile(String username) {
        String redisKey = "name:" + username;
        Object cached = redisService.hashMap("users", redisKey);

        if (cached != null) {
            return objectMapper.convertValue(cached, UserResponse.class);
        }

        UserEntity user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(()-> new EntityNotFoundException(ExceptionMessages.NOT_FOUND +" "+ username));

        UserResponse response = userMapper.toResponse(user);

        redisService.hashSet("users", redisKey, response);
        redisService.setTimeToLive("users", 1);

        return response;
    }

    @Transactional(readOnly = true)
    public UserResponse findUser(Long id){
        String redisKey = "user:" + id;
        Object cached = redisService.hashMap("users", redisKey);

        if(cached != null){
            return objectMapper.convertValue(cached, UserResponse.class);
        }

        UserEntity user = userRepository.findByIdWithRoles(id)
                .orElseThrow(()-> new EntityNotFoundException(ExceptionMessages.NOT_FOUND+" "+id));

        UserResponse response = userMapper.toResponse(user);

        redisService.hashSet("users", redisKey, response);

        return response;
    }

    public EchoResponse echo(Authentication auth, String msg) {
        return new EchoResponse(auth.getName(), msg);
    }

    @Transactional
    public UserResponse createUser(UserRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException(ExceptionMessages.BAD_REQUEST +" "+ request.getUsername());
        }
        UserEntity user = userMapper.toEntity(request);
        user.setEnabled(1);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<UserRoleEntity> userRoles = new HashSet<>();
        if(request.getRoles() != null){
            for (String roleName : request.getRoles()){
                RoleEntity role = roleRepository.findByName(roleName)
                        .orElseThrow(()-> new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + roleName));

                UserRoleEntity ur = new UserRoleEntity();
                ur.setUser(user);
                ur.setRole(role);
                userRoles.add(ur);
            }
        }

        user.setUserRoles(userRoles);
        user = userRepository.save(user);
        UserResponse response = userMapper.toResponse(user);

        String redisKey = "user:" + user.getId();
        redisService.hashSet("users", redisKey, response);

        return response;
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getUsers(int page, int size, String sort, String q) {
        String redisKey = "users:" + page + ":" + size + ":" + sort + ":" + (q == null ? "" : q);

        Object cached = redisService.hashMap("users", redisKey);
        if (cached != null) {
            return objectMapper.convertValue(cached, new TypeReference<PageResponse<UserResponse>>() {});
        }

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 ? Sort.Direction.fromString(sortParams[1]) : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<UserEntity> userPage;
        if (q != null && !q.isEmpty()) {
            userPage = userRepository.findByUsernameContainingIgnoreCase(q, pageable);
            if(userPage.isEmpty()){
                throw new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + q);
            }
        } else {
            userPage = userRepository.findAll(pageable);
        }

        Page<UserResponse> userResponsePage = userPage.map(userMapper::toResponse);

        PageResponse<UserResponse> response = new PageResponse<>();
        response.setContent(userResponsePage.getContent());
        response.setPage(userResponsePage.getNumber());
        response.setSize(userResponsePage.getSize());
        response.setTotalElements(userResponsePage.getTotalElements());
        response.setTotalPages(userResponsePage.getTotalPages());

        redisService.hashSet("users", redisKey, response);

        return response;
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + id));
        if (request.getFullName() != null){
            user.setFullName(request.getFullName());
        }
        if (request.getEnabled() != null){
            user.setEnabled(request.getEnabled() ? 1:0);
        }
        if (request.getRoles() != null){
            Set<RoleEntity> validateRoles = request.getRoles().stream()
                    .map(name -> roleRepository.findByName(name)
                            .orElseThrow(()->new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + name)))
                    .collect(Collectors.toSet());

            user.getUserRoles().clear();
            userRepository.flush();
            for (RoleEntity roleEntity : validateRoles){
                UserRoleEntity ur = new UserRoleEntity();
                ur.setUser(user);
                ur.setRole(roleEntity);
                user.getUserRoles().add(ur);
            }
        }

        UserEntity saved = userRepository.save(user);
        UserResponse response = userMapper.toResponse(saved);

        String redisKey = "users:" + id;
        redisService.delete("users", redisKey);
        redisService.hashSet("users", redisKey, response);

        return response;
    }

    @Transactional
    public void updatePassword(Long id, UpdatePasswordRequest request){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + id));

        String passwordHash = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(passwordHash);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + id));

        user.setEnabled(0);
        UserEntity saved = userRepository.save(user);
        UserResponse response = userMapper.toResponse(saved);

        String redisKey = "user:" + id;
        redisService.delete("users", redisKey);
        redisService.hashSet("users", redisKey, response);
    }
}
