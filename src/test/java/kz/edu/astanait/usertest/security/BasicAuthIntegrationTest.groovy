package kz.edu.astanait.usertest.security


import com.fasterxml.jackson.databind.ObjectMapper
import kz.edu.astanait.usertest.dto.request.GetIpDtoRequest
import kz.edu.astanait.usertest.dto.request.UserDtoRequest
import kz.edu.astanait.usertest.model.Role
import kz.edu.astanait.usertest.model.User
import kz.edu.astanait.usertest.repository.CountryRepository
import kz.edu.astanait.usertest.repository.UserRepository
import kz.edu.astanait.usertest.utils.facade.UserFacade
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@RunWith(SpringRunner)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [
    Initializer])
class BasicAuthIntegrationTest extends Specification {

    @Autowired MockMvc mvc

    @Autowired UserRepository userRepository

    @Autowired CountryRepository countryRepository

    @Autowired PasswordEncoder passwordEncoder

    @MockBean RestTemplate restTemplate

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
        .withDatabaseName("user-test-testing")
        .withUsername("postgres")
        .withPassword("123")

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(applicationContext.getEnvironment())
        }
    }

    @Test
    def "get user with default ANONYM ROLE should send 200"() {
        given:
            User user = UserFacade.createTestUser()
            User newUser = userRepository.save(user)
        expect:
            mvc.perform(get("/api/user" + "/" + newUser.getId())
                .with(httpBasic("user@user.com","123")))
                .andDo(print())
                .andExpect(status().isOk())
        userRepository.delete(newUser)
    }
    @Test
    def "create user with default ANONYM ROLE shouldn't send 200"() {
        given:
            User testUser = UserFacade.createTestUser()
            testUser.setRole(new Role(1L,"ROLE_ANONYM")) // glupo
            UserDtoRequest request = UserFacade.userToDtoRequest(testUser)
            restTemplate.getForObject(_ as URI, _ as Class<Object>) >> new GetIpDtoRequest("192.168.1.1", "Kazakhstan")
        expect:
            mvc.perform(post("/api/user")
                .with(httpBasic("user@user.com","123"))
                .content(getRequestInJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
    }

    @Test
    def "create user with default ADMIN ROLE should send 200"() {
        given:
            User testUser = UserFacade.createTestUser()
            testUser.setRole(new Role(1L,"ROLE_ANONYM")) // glupo
            UserDtoRequest request = UserFacade.userToDtoRequest(testUser)
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "sadas".getBytes())
            restTemplate.getForObject(_ as URI, _ as Class<Object>) >> new GetIpDtoRequest("192.168.1.1", "Kazakhstan")
        expect:
            mvc.perform(multipart("/api/user")
                .file(file)
                .param("user", getRequestInJson(request))
                .with(httpBasic("admin@admin.com", "123"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("\$.name").value(testUser.getName()))
                .andExpect(jsonPath("\$.surname").value(testUser.getSurname()))
                .andExpect(jsonPath("\$.middlename").value(testUser.getMiddlename()))
                .andExpect(jsonPath("\$.sex").value(testUser.getSex()))
    }

    @Test
    def "create user with default ADMIN ROLE but is given to user should send status OK"() {
        given:
            User testUser = UserFacade.createTestUser()
            testUser.setEmail("ulan@gmail.com")
            testUser.setRole(new Role(2L,"ROLE_ADMIN")) // glupo
            testUser.setPassword(passwordEncoder.encode("123"))
            def savedUser = userRepository.save(testUser)
            println "password"+ " "+ savedUser.getPassword()+ " " + passwordEncoder.matches("123", savedUser.getPassword())
            User user = UserFacade.createTestUser()
            user.setEmail("sultan@gmail.com")
            user.setRole(new Role(1L,"ROLE_ANONYM"))
            UserDtoRequest request = UserFacade.userToDtoRequest(user)
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "sadas".getBytes())
            restTemplate.getForObject(_ as URI, _ as Class<Object>) >> new GetIpDtoRequest("192.168.1.1", "Kazakhstan")
        expect:
            mvc.perform(multipart("/api/user")
//                .file(file)
                .param("user", getRequestInJson(request))
                .with(httpBasic(savedUser.getEmail(), "123"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("\$.name").value(user.getName()))
                .andExpect(jsonPath("\$.surname").value(user.getSurname()))
                .andExpect(jsonPath("\$.middlename").value(user.getMiddlename()))
                .andExpect(jsonPath("\$.sex").value(user.getSex()))
    }

    private static String getRequestInJson(UserDtoRequest request) {
        def string = new ObjectMapper().writeValueAsString(request)
        return string
    }
}
