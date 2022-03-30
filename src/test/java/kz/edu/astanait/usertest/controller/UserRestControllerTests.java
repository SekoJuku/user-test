package kz.edu.astanait.usertest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.repository.UserRepository;
import kz.edu.astanait.usertest.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.client.RestTemplate;

import static kz.edu.astanait.usertest.utils.facade.UserFacade.createTestUser;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserRestControllerTests {
    private final String apiPrefix = "/api/user";

    @Autowired MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(mvc);
        Assertions.assertNotNull(userService);
        Assertions.assertNotNull(restTemplate);
    }

    @Test
    public void createUserAPITest() throws Exception {
        User user = createTestUser();

        given(userService.create(user)).willReturn(user);

        mvc.perform(post(apiPrefix)
                .content(getUserInJson(user))
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    public void getUserApiTest() throws Exception {
        User user = createTestUser();
        user.setId(1L);

        given(userService.getById(1L))
                .willReturn(user);

        mvc.perform(get(apiPrefix + "/" + user.getId())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void editUserAPITest() throws Exception {
        User user = createTestUser();
        user.setId(1L);

        given(userService.edit(user)).willReturn(user);

        mvc.perform(put(apiPrefix + "/" + user.getId())
                .content(getUserInJson(user))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    private String getUserInJson(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(user);
    }

}
