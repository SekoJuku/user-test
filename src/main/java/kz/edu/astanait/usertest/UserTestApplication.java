package kz.edu.astanait.usertest;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableWebMvc
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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
