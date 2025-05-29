package com.project.ordersystemapi;

import com.project.ordersystemapi.model.User;
import com.project.ordersystemapi.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndReturnUser() {
        User user = new User();
        user.setName("Pedro");
        user.setEmail("pedro@email.com");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Pedro");
    }

    @Test
    void shouldListUser() {
        User user = new User();
        user.setName("Joao");
        user.setEmail("joao@email.com");
        userRepository.save(user);

        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].getName()).isEqualTo("Joao");
    }
}
