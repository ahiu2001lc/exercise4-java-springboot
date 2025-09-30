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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor
class MockUserServiceTest {
    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper objectMapper;
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
    @Autowired
    private UserDetailsService userDetailsService;

    private String request;
    private String token;
    private String wrongLoginRequest;
    private UserEntity user;

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
    void getProfileWithoutAuthentication_test() throws Exception {
        mock.perform(get("/api/users/profile"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getProfileWithUSER_ROLE_test() throws Exception{
        token = getToken("user", "user123@");

        mock.perform(get("/api/users/profile")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.roles").value("ROLE_USER"));
    }

    @Test
    void getProfileWithWrongPassword_test() throws Exception{
        wrongLoginRequest = """
        {
          "username": "user",
          "password": "user123@4"
        }
        """;

        mock.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongLoginRequest))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getListUsersWithROLE_USER_test() throws Exception{
        token = getToken("user", "user123@");

        mock.perform(get("/api/admin/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getListUsersWithROLE_ADMIN_test() throws Exception{
        token = getToken("admin", "admin123@");

        mock.perform(get("/api/admin/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createUserSuccessWithROLE_ADMIN_test() throws Exception{
        request = """
        {
          "username": "alice",
          "password": "alice123@",
          "fullName": "New User",
          "roles": ["ROLE_USER"]
        }
        """;

        token = getToken("admin", "admin123@");

        mock.perform(post("/api/admin/users")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isCreated())
                .andDo(print());

        user = userRepository.findByUsernameWithRoles("alice")
                .orElseThrow(()->new EntityNotFoundException("User not found: " + "alice"));

        assertTrue(passwordEncoder.matches("alice123@", user.getPassword()));
        System.out.println("Password_hash: alice123@ = " + user.getPassword());
    }

    @Test
    void createUserConflictWithROLE_ADMIN_test() throws Exception{
        request = """
        {
          "username": "user",
          "password": "user123@",
          "fullName": "New User",
          "roles": ["ROLE_USER"]
        }
        """;
        token = getToken("admin", "admin123@");

        mock.perform(post("/api/admin/users")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void updateRoleWithROLE_ADMIN_test() throws Exception{
        request = """
        {
          "fullName": "user update",
          "enabled": true,
          "roles": ["ROLE_ADMIN"]
        }
        """;
        token = getToken("admin", "admin123@");

        mock.perform(put("/api/admin/users/{id}", 2L)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isOk())
                .andDo(print());

        UserDetails userDetails = userDetailsService.loadUserByUsername("user");

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void updatePasswordSuccess_test() throws Exception{
        request = """
        {
          "newPassword": "admin123#"
        }
        """;
        token = getToken("admin", "admin123@");

        mock.perform(post("/api/admin/users/{id}/password", 1L)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))
                .andExpect(status().isNoContent())
                .andDo(print());

        wrongLoginRequest = """
        {
          "username": "admin",
          "password": "admin123@"
        }
        """;
        mock.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongLoginRequest))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        token = getToken("admin", "admin123#");
        mock.perform(get("/api/users/profile")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteUserWithROLE_ADMIN_test() throws Exception{
        token = getToken("admin", "admin123@");

        mock.perform(delete("/api/admin/users/{id}", 2L)
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNoContent());

        wrongLoginRequest = """
        {
          "username": "user",
          "password": "user123@"
        }
        """;
        mock.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongLoginRequest))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
