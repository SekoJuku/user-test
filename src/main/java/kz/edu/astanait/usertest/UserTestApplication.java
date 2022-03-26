package kz.edu.astanait.usertest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UserTestApplication {

    /*
    #TODO: [+] Database need to be Normalized (store countries separated)
    #TODO: [+] Fix create method endpoint
    #TODO: [+] Task to define country by ip is not solved
    #TODO: [+] Functions and class members should be named inCamelStyle
    #TODO: [?] Tests have no asserts and mock checks
    #TODO: [+] Logger added but not used
    #TODO: [?] Add unit test for UserService
     */


    public static void main(String[] args) {
        SpringApplication.run(UserTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}