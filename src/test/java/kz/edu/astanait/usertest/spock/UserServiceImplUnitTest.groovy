package kz.edu.astanait.usertest.spock

import kz.edu.astanait.usertest.dto.request.GetIpDtoRequest
import kz.edu.astanait.usertest.dto.request.UserDtoRequest
import kz.edu.astanait.usertest.model.Country
import kz.edu.astanait.usertest.model.User
import kz.edu.astanait.usertest.repository.ImageRepository
import kz.edu.astanait.usertest.repository.RoleRepository
import kz.edu.astanait.usertest.repository.UserRepository
import kz.edu.astanait.usertest.service.CountryService
import kz.edu.astanait.usertest.service.UserService
import kz.edu.astanait.usertest.service.impl.UserServiceImpl
import kz.edu.astanait.usertest.utils.facade.UserFacade
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class UserServiceImplUnitTest extends Specification {
    UserService userService

    @MockBean private RestTemplate restTemplate;
    @MockBean private UserRepository userRepository;
    @MockBean private RoleRepository roleRepository;
    @MockBean private CountryService countryService;
    @MockBean private ImageRepository imageRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    def setup() {
        userService = new UserServiceImpl(restTemplate, userRepository, roleRepository, countryService,imageRepository, passwordEncoder)
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
            def dtoRequest = UserFacade.userToDtoRequest(user)
            Country country = new Country(1L,"Kazakhstan")
            restTemplate.getForObject(_, _) >> new GetIpDtoRequest("192.168.1.1", country.getName())
            countryService.findByName(country.getName()) >> country
            user.setCountry(country)
            userRepository.save(_ as User) >> user
        expect:
            userService.create(dtoRequest)
    }
    def "edit user should edit user and return modified user"() {
        given:
            User user = UserFacade.createTestUser()
            def dtoRequest = UserFacade.userToDtoRequest(user)
            user.setId(1L)
            userRepository.findById(user.getId()) >> Optional.of(user)
            userRepository.save(user) >> user
        expect:
            userService.edit(dtoRequest)
    }
    def "delete user should delete user and do not return anything"() {
        given:
            Long id = 1L
        expect:
            userService.delete(id)
    }
}
