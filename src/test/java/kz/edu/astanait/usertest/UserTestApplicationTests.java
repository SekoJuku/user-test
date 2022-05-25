package kz.edu.astanait.usertest;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.repository.RoleRepository;
import kz.edu.astanait.usertest.repository.UserRepository;
import kz.edu.astanait.usertest.service.UserService;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class UserTestApplicationTests {

    @Autowired private MockMvc mockMvc;

    @Autowired private UserRepository userRepository;

    @Autowired private UserService userService;


    @Test
    void contextLoads() {
        Assertions.assertThat(userRepository).isNotNull();
        Assertions.assertThat(userService).isNotNull();
        Assertions.assertThat(mockMvc).isNotNull();
    }

    @Test
    public void createUserTest() throws Exception {
        UserDtoRequest request = UserFacade.UserToDtoRequest(UserFacade.createTestUser());
        request.setRoleId(userService.getRoleById(1L).getId());
        User newUser = userService.create(request);
        assertEquals(request.getName(),newUser.getName());
        assertEquals(request.getEmail(), newUser.getEmail());
        userRepository.delete(newUser);
    }

}
