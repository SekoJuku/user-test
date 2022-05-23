package kz.edu.astanait.usertest.controller;

import kz.edu.astanait.usertest.annotation.Metric;
import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.service.UserService;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@AllArgsConstructor
@Log
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @Metric(name = "method.getById")
    public User getById(@PathVariable Long id) {
        log.info("GET:getById(" + id+ ")");
        return userService.getById(id);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImageByUserId(@PathVariable Long id) {
        Image image = userService.getImageByUserId(id);
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", image.getName()+"."+image.getExtension()))
            .body(image.getData());
    }

    @PostMapping("")
    public User create(@ModelAttribute UserDtoRequest userDtoRequest) {
        log.info("POST:create( " + userDtoRequest+ ")");
        return userService.create(userDtoRequest);
    }

    @PutMapping("/{id}")
    public User edit(@PathVariable Long id, @ModelAttribute UserDtoRequest userDtoRequest) {
        log.info("PUT:edit(" + id+ ", " + userDtoRequest+ ")");
        userDtoRequest.setId(id);
        return userService.edit(userDtoRequest);
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
