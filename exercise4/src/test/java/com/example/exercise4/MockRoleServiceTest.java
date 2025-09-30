package com.example.exercise4;

import com.example.exercise4.entity.RoleEntity;
import com.example.exercise4.entity.UserEntity;
import com.example.exercise4.entity.UserRoleEntity;
import com.example.exercise4.repository.RoleRepository;
import com.example.exercise4.repository.UserRepository;
import com.example.exercise4.repository.UserRoleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor
class MockRoleServiceTest {
    @Autowired
    private MockMvc mock;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    private String request;
    private String token;

    private String getToken(String username, String password) throws Exception {
        String loginRequest = """
        {
          "username": "%s",
          "password": "%s"
        }
        """.formatted(username, password);

        MvcResult loginResult = mock.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        return node.get("token").asText();
    }

    @BeforeEach
    void setUp(){
        entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE roles ALTER COLUMN id RESTART WITH 1").executeUpdate();

        RoleEntity roleAdmin = roleRepository.save(new RoleEntity(null, "ROLE_ADMIN"));
        RoleEntity roleUser = roleRepository.save(new RoleEntity(null, "ROLE_USER"));

        UserEntity user1 =  userRepository.save(new UserEntity(
                null,
                "admin",
                passwordEncoder.encode("admin123@"),
                "admin test",
                1,
                null,
                null
        ));
        UserEntity user2 = userRepository.save(new UserEntity(
                null,
                "user",
                passwordEncoder.encode("user123@"),
                "user test",
                1,
                null,
                null
        ));

        userRoleRepository.save(new UserRoleEntity(user1, roleAdmin));
        userRoleRepository.save(new UserRoleEntity(user2, roleUser));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findRoleWithROLE_USER_test() throws Exception {
        token = getToken("user", "user123@");

        mock.perform(get("/api/admin/roles/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void findRoleWithROLE_ADMIN_test() throws Exception {
        token = getToken("admin", "admin123@");

        mock.perform(get("/api/admin/roles/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createRoleSuccess_test() throws Exception {
        token = getToken("admin", "admin123@");

        request = """
        {
          "name": "ROLE_HR"
        }
        """;

        mock.perform(post("/api/admin/roles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createRoleFailed_test() throws Exception {
        token = getToken("admin", "admin123@");

        request = """
        {
          "name": "ROLE_ADMIN"
        }
        """;

        mock.perform(post("/api/admin/roles")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
