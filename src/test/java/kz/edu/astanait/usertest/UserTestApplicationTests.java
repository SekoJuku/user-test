package kz.edu.astanait.usertest;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.repository.UserRepository;
import kz.edu.astanait.usertest.service.UserService;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class UserTestApplicationTests {

    @Autowired private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    @Qualifier(value = "userServiceImpl")
    private UserService userService;


    @Test
    void contextLoads() {
        Assertions.assertThat(userRepository).isNotNull();
        Assertions.assertThat(userService).isNotNull();
        Assertions.assertThat(mockMvc).isNotNull();
    }
}