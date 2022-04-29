package kz.edu.astanait.usertest.spock

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import kz.edu.astanait.usertest.UserTestApplication
import kz.edu.astanait.usertest.dto.request.GetIpDtoRequest
import kz.edu.astanait.usertest.model.Country
import kz.edu.astanait.usertest.model.User
import kz.edu.astanait.usertest.repository.CountryRepository
import kz.edu.astanait.usertest.repository.H2TestProfileJPAConfig
import kz.edu.astanait.usertest.repository.UserRepository
import kz.edu.astanait.usertest.service.CountryService
import kz.edu.astanait.usertest.utils.facade.UserFacade
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate
import spock.lang.Specification


@SpringBootTest(classes = [
    H2TestProfileJPAConfig.class,
    UserTestApplication.class
])
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest extends Specification {
    String apiPrefix = "/api/user"

    @Autowired MockMvc mvc

    @Autowired CountryRepository countryRepository

    @Autowired UserRepository userRepository

    @MockBean RestTemplate restTemplate

    def setup() {
        Country country = countryRepository.save(new Country("Kazakhstan"))
        User user = UserFacade.createTestUser()
        user.setCountry(country)
        userRepository.save(user)
    }

    @Test
    def "create user should send back user and OK status"() {
        given:
            User user = UserFacade.createTestUser()
            restTemplate.getForObject(_, _) >> new GetIpDtoRequest("192.168.1.1", "Kazakhstan")
        expect:
            mvc.perform(post(apiPrefix)
                    .content(getUserInJson(user))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(user.getName()))
    }
    @Test
    def "edit user should send back user and Ok status"() {
        given:
            User user = UserFacade.createTestUser()
            user.setId(1L)
        expect:
            mvc.perform(put(apiPrefix + "/" + user.getId())
                .content(getUserInJson(user))
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
    }

    private static String getUserInJson(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
        return mapper.writeValueAsString(user)
    }
}
