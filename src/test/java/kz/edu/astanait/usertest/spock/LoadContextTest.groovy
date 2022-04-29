package kz.edu.astanait.usertest.spock

import kz.edu.astanait.usertest.repository.UserRepository
import kz.edu.astanait.usertest.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@SpringBootTest
class LoadContextTest extends Specification {
    @Autowired
    ApplicationContext context

    def "test context loads"() {
        expect:
            context != null
            context.containsBean("userService")
            context.containsBean("userRepository")
    }

}
