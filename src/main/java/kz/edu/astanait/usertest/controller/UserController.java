package kz.edu.astanait.usertest.controller;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.service.UserService;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@AllArgsConstructor
@Log
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        log.info("GET:getById(" + id+ ")");
        return userService.getById(id);
    }

    @PostMapping("")
    public User create(@RequestBody UserDtoRequest userDtoRequest) {
        log.info("POST:create( " + userDtoRequest.toString()+ ")");
        return userService.create(UserFacade.UserDtoToUser(userDtoRequest));
    }

    @PutMapping("/{id}")
    public User edit(@PathVariable Long id, @RequestBody UserDtoRequest userDtoRequest) {
        log.info("PUT:edit(" + id+ ", " + userDtoRequest.toString()+ ")");
        userDtoRequest.setId(id);
        return userService.edit(UserFacade.UserDtoToUser(userDtoRequest));
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        log.info("DELETE:delete(" + id+ ")");
        userService.delete(id);
        return "User is deleted!";
    }

    @GetMapping("/ip")
    public String getIp() {
        log.info("GET:getIp()");
        return userService.getIp();
    }

}
