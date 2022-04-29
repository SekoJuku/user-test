package kz.edu.astanait.usertest.spock

import kz.edu.astanait.usertest.dto.request.GetIpDtoRequest
import kz.edu.astanait.usertest.model.Country
import kz.edu.astanait.usertest.model.User
import kz.edu.astanait.usertest.repository.UserRepository
import kz.edu.astanait.usertest.service.CountryService
import kz.edu.astanait.usertest.service.UserService
import kz.edu.astanait.usertest.utils.HttpUtils
import kz.edu.astanait.usertest.utils.facade.UserFacade
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Specification
import spock.lang.Unroll


class UserServiceUnitTest extends Specification {
    UserService userService

    def userRepository = Mock(UserRepository)

    def countryService = Mock(CountryService)

    def restTemplate = Mock(RestTemplate)

    def setup() {
        userService = new UserService(restTemplate, userRepository, countryService)
    }

    def "getById should show whether user with id #id or not user exists"() {
        given:
            User user = UserFacade.createTestUser()
            user.setId(1L)
            userRepository.findById(user.getId()) >> Optional.of(user)
        expect:
            userService.getById(1L).getId() == user.getId()
    }
    def "create user should create user and return new user"() {
        given:
            User user = UserFacade.createTestUser()
            Country country = new Country(1L,"Kazakhstan")
            restTemplate.getForObject(_, _) >> new GetIpDtoRequest("192.168.1.1", country.getName())
            countryService.findByName(country.getName()) >> country
            user.setCountry(country)
            userRepository.save(_ as User) >> user
        expect:
            userService.create(user)
    }
    def "edit user should edit user and return modified user"() {
        given:
            User user = UserFacade.createTestUser()
            user.setId(1L)
            userRepository.findById(user.getId()) >> Optional.of(user)
            userRepository.save(user) >> user
        expect:
            userService.edit(user)
    }
    def "delete user should delete user and do not return anything"() {
        given:
            Long id = 1L
        expect:
            userService.delete(id)
    }
}
