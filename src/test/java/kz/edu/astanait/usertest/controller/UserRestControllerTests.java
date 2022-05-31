package kz.edu.astanait.usertest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Role;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.service.UserService;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static kz.edu.astanait.usertest.utils.facade.UserFacade.createTestUser;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        UserDtoRequest request = UserFacade.userToDtoRequest(user);


        given(userService.create(request)).willReturn(user);
        given(userService.getRoleById(1L)).willReturn(new Role(1L,"ROLE_ANONYM"));

        request.setRoleId(userService.getRoleById(1L).getId());
        mvc.perform(post(apiPrefix)
                .with(httpBasic("admin@admin.com", "123"))
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
                .with(httpBasic("admin@admin.com", "123"))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void editUserAPITest() throws Exception {
        User user = createTestUser();
        user.setId(1L);
        UserDtoRequest request = UserFacade.userToDtoRequest(user);

        given(userService.edit(request)).willReturn(user);
        given(userService.getRoleById(1L)).willReturn(new Role(1L,"ROLE_ANONYM"));
        mvc.perform(put(apiPrefix + "/" + user.getId())
                .with(httpBasic("admin@admin.com", "123"))
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
