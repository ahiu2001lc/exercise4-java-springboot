package com.example.exercise4.service.role;

import com.example.exercise4.dto.request.RoleRequest;
import com.example.exercise4.dto.response.RoleResponse;
import com.example.exercise4.entity.RoleEntity;
import com.example.exercise4.exception.ExceptionMessages;
import com.example.exercise4.mapper.RoleMapper;
import com.example.exercise4.repository.RoleRepository;
import com.example.exercise4.service.redis.RedisService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RedisService redisService;

    @Transactional(readOnly = true)
    public RoleResponse getRole(Long id){
        String redisKey = "role:" + id;
        Object cached = redisService.hashMap("roles", redisKey);

        if (cached != null) {
            return (RoleResponse) cached; // trả dữ liệu từ Redis
        }
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(ExceptionMessages.NOT_FOUND + " " + id));
        RoleResponse response = roleMapper.toResponse(role);

        redisService.hashSet("roles", redisKey, response);
        redisService.setTimeToLive("roles", 1);

        return response;
    }

    @Transactional
    public RoleResponse createRole(RoleRequest request){
        if (roleRepository.existsByName(request.getName())){
            throw new IllegalArgumentException(ExceptionMessages.BAD_REQUEST + " " + request.getName());
        }

        RoleEntity role = roleMapper.toEntity(request);

        role = roleRepository.save(role);

        RoleResponse response = roleMapper.toResponse(role);

        String redisKey = "role:" + response.getId();
        redisService.delete("roles", redisKey);

        redisService.hashSet("roles", redisKey, response);
        redisService.setTimeToLive("roles", 1);

        return response;
    }
}
