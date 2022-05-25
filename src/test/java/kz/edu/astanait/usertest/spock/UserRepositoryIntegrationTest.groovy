package kz.edu.astanait.usertest.spock

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import kz.edu.astanait.usertest.dto.request.GetIpDtoRequest
import kz.edu.astanait.usertest.model.Country
import kz.edu.astanait.usertest.model.User
import kz.edu.astanait.usertest.repository.CountryRepository
import kz.edu.astanait.usertest.repository.UserRepository
import kz.edu.astanait.usertest.utils.facade.UserFacade
import org.junit.ClassRule
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate
import org.testcontainers.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

import javax.transaction.Transactional

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [
    Initializer])
class UserRepositoryIntegrationTest extends Specification {

    @Autowired
    UserRepository userRepository

    @Autowired
    CountryRepository countryRepository

    @Autowired
    MockMvc mvc

    @MockBean
    RestTemplate restTemplate


    @BeforeEach
    def setup() {
        Country country = countryRepository.save(new Country("Kazakhstan"))
        User user = UserFacade.createTestUser()
        user.setCountry(country)
        userRepository.save(user)
    }

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
    @Transactional
    void "create User should return user"() {
        given:
            User user = UserFacade.createTestUser()
            user.setCountry(countryRepository.getById(1L))
        when:
            User newUser = userRepository.save(user)
        then:
            newUser.getId() != null
    }

    @Test
    void "create user should send back user and OK status"() {
        given:
            User user = UserFacade.createTestUser()
            restTemplate.getForObject(_ as URI, _ as Class<Object>) >> new GetIpDtoRequest("192.168.1.1", "Kazakhstan")
        expect:
            mvc.perform(post("/api/user")
                .with(httpBasic("admin@admin.com", "123"))
                .content(getUserInJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("\$.name").value(user.getName()))
                .andExpect(jsonPath("\$.surname").value(user.getSurname()))
                .andExpect(jsonPath("\$.middlename").value(user.getMiddlename()))
                .andExpect(jsonPath("\$.sex").value(user.getSex()))

    }
    @Test
    void "edit user should send back user and Ok status"() {
        given:
            User user = UserFacade.createTestUser()
            user.setId(1L)
        expect:
            mvc.perform(put("/api/user" + "/" + user.getId())
                .with(httpBasic("admin@admin.com", "123"))
                .content(getUserInJson(user))
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.id").value(user.getId()))
    }

    private static String getUserInJson(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
        return mapper.writeValueAsString(user)
    }
}
